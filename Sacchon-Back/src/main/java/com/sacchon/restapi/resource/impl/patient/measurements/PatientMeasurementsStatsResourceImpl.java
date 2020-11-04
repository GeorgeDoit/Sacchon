package com.sacchon.restapi.resource.impl.patient.measurements;

import com.sacchon.restapi.common.Constants;
import com.sacchon.restapi.common.Utilities;
import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.model.PatientMeasurement;
import com.sacchon.restapi.repository.patient.PatientMeasurementRepository;
import com.sacchon.restapi.repository.patient.PatientRepository;
import com.sacchon.restapi.repository.util.JpaUtil;
import com.sacchon.restapi.representations.patient.PatientMeasurementRepresentation;
import com.sacchon.restapi.representations.patient.PatientMeasurementsAvgRepresentation;
import com.sacchon.restapi.resource.interfaces.patient.PatientMeasurementsStatsResource;
import com.sacchon.restapi.resource.util.ResourceUtils;
import com.sacchon.restapi.security.SacchonRole;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.*;

public class PatientMeasurementsStatsResourceImpl extends ServerResource implements PatientMeasurementsStatsResource {
    private PatientMeasurementRepository patientMeasurementRepository ;
    private PatientRepository patientRepository ;
    private EntityManager em;
    private long dateFrom =0;
    private long dateTo =0;


    @Override
    protected void doInit() {
        try {
            em = JpaUtil.getEntityManager();
            patientMeasurementRepository = new PatientMeasurementRepository(em);
            patientRepository = new PatientRepository(em);
        }
        catch(Exception ex){
            throw new ResourceException(ex);
        }
    }

    @Override
    protected void doRelease() {
        em.close();
    }

    /**
     * This method is used to get average carb and glucose of a patient over a user specified period
     * @return
     * @throws NotFoundException
     */
    @Override
    public Map<String,PatientMeasurementsAvgRepresentation> getPatientMeasurementsStatistics() throws NotFoundException, BadRequestException {
        String[] roles = {SacchonRole.ROLE_PATIENT.getRoleName()};
        Utilities.checkRoles(this,roles);
        String patient_id = getQueryValue(Constants.QV_PATIENT_ID);
        String from = getQueryValue(Constants.QV_FROM);
        String to = getQueryValue(Constants.QV_TO);
        if(patient_id == null) throw new BadRequestException("Required patient id when getting measurements statistics");
        long pid = Long.parseLong(patient_id);
        Patient patient = getPatientIfExists(patientRepository,pid);
        setTimeRange(from,to);
        return findAverage(pid);
    }

    private Map<String,PatientMeasurementsAvgRepresentation> findAverage(long pid) {
        List<PatientMeasurement> patientMeasurements= new ArrayList<>();
        patientMeasurements= patientMeasurementRepository.findPatientMeasurements(pid,dateFrom,dateTo);
        Map<String,PatientMeasurementsAvgRepresentation> pmRep = calculateAverage(patientMeasurements);
        return pmRep;
    }

    /**
     * This method is used to set the Date From and Date To according to the query values
     * @param from
     * @param to
     */
    private void setTimeRange(String from, String to) {
        if(from==null || to==null){
            dateFrom =0;
            dateTo =0 ;
        }else{
            dateFrom = Long.parseLong(from);
            dateTo =Long.parseLong(to);
        }
    }

    /**
     * This method is used to get a patient if exists
     * @param patientRepository
     * @param id
     * @return
     * @throws NotFoundException
     */
    public Patient getPatientIfExists(PatientRepository patientRepository, long id) throws NotFoundException {
        Optional<Patient> patient = patientRepository.findById(id);
        if (!patient.isPresent())  throw new NotFoundException("Patient with id: " + id + " not found");
        setExisting(patient.isPresent());
        return patient.get();
    }

    /**
     * This method is used to calculate glucose level average and sum of carbIntake
     * @param patientMeasurements
     */
    public Map<String,PatientMeasurementsAvgRepresentation> calculateAverage(List<PatientMeasurement> patientMeasurements){

        Map<String, List<PatientMeasurement>> pmMap = new HashMap<String, List<PatientMeasurement>>();
        for(PatientMeasurement pm :patientMeasurements){
            if (!pmMap.containsKey(pm.getDateStored().toString())) {
                pmMap.put(pm.getDateStored().toString(), new ArrayList<PatientMeasurement>());
            }
            pmMap.get(pm.getDateStored().toString()).add(pm);
        }

        Map<String,PatientMeasurementsAvgRepresentation> pmAvg = new HashMap<String,PatientMeasurementsAvgRepresentation>();
        for (Map.Entry<String,List<PatientMeasurement>> entry : pmMap.entrySet()){
             double totalCarbIntake=0.0;
             double totalGlucoseLevel=0.0;
             double avgCarb=0.0;
             double avgGlucose=0.0;
            List<PatientMeasurement> pmList = entry.getValue();
            for(PatientMeasurement pm :pmList){
                totalCarbIntake = totalCarbIntake + pm.getCarbIntake();
                totalGlucoseLevel =  totalGlucoseLevel + pm.getGlucoseLevel();
            }
            avgCarb = totalCarbIntake;
            avgGlucose = totalGlucoseLevel/pmList.size();
            PatientMeasurementsAvgRepresentation pmAvgRep = getPmAverageRepresentation(avgCarb,avgGlucose);
            pmAvg.put(entry.getKey(),pmAvgRep);
         }
         return pmAvg;


    }

    /**
     * This method returns the representation of results
     * @param avgCarb
     * @param avgGlucose
     * @return
     */
    public PatientMeasurementsAvgRepresentation getPmAverageRepresentation(double avgCarb,double avgGlucose){
        PatientMeasurementsAvgRepresentation pmAvrg = new PatientMeasurementsAvgRepresentation();
        pmAvrg.setAvrgCarb(avgCarb);
        pmAvrg.setAvrgGlucoseLevel(avgGlucose);
        return pmAvrg;
    }


}

