package com.sacchon.restapi.resource.impl.patient.measurements;

import com.sacchon.restapi.common.Constants;
import com.sacchon.restapi.common.Utilities;
import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.model.Doctor;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.model.PatientMeasurement;
import com.sacchon.restapi.repository.DoctorRepository;
import com.sacchon.restapi.repository.patient.PatientMeasurementRepository;
import com.sacchon.restapi.repository.patient.PatientRepository;
import com.sacchon.restapi.repository.util.JpaUtil;
import com.sacchon.restapi.representations.patient.PatientMeasurementRepresentation;
import com.sacchon.restapi.resource.interfaces.patient.PatientMeasurementListResource;
import com.sacchon.restapi.resource.util.ResourceUtils;
import com.sacchon.restapi.security.SacchonRole;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.resource.Status;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientMeasurementListResourceImpl extends ServerResource implements PatientMeasurementListResource {
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
     * This method is used to add a patient measurement for a patient.
     * @param patientMeasurementRepresentation
     * @return
     * @throws BadEntityException
     * @throws NotFoundException
     */
    @Override
    public PatientMeasurementRepresentation add(PatientMeasurementRepresentation patientMeasurementRepresentation) throws BadEntityException, NotFoundException {
        String[] roles = {SacchonRole.ROLE_PATIENT.getRoleName()};
        Utilities.checkRoles(this,roles);
        String patient_id = getQueryValue(Constants.QV_PATIENT_ID);
        if(patient_id == null) throw new NotFoundException("Bad request");
        long pid = Long.parseLong(patient_id);
        Patient p = getPatientIfExists(patientRepository,pid);
        if(patientMeasurementRepresentation == null) throw  new BadEntityException("Bad entity");
        PatientMeasurement pm = PatientMeasurementRepresentation.getPatientMeasurement(patientMeasurementRepresentation,p);
        patientMeasurementRepository.save(pm);
        updatePatient(p,pm);
        return PatientMeasurementRepresentation.getPatientMeasurementRepresentation(pm);
    }

    /**
     * This method is used to get measurements of a patient over a user specified period
     * @return
     * @throws NotFoundException
     */

    @Override
    public List<PatientMeasurementRepresentation> getPatientMeasurements() throws NotFoundException, BadRequestException {
        String[] roles = {SacchonRole.ROLE_PATIENT.getRoleName(),SacchonRole.ROLE_DOCTOR.getRoleName()};
        Utilities.checkRoles(this,roles);
        String patient_id = getQueryValue(Constants.QV_PATIENT_ID);
        String from = getQueryValue(Constants.QV_FROM);
        String to = getQueryValue(Constants.QV_TO);
        if(patient_id == null) throw new BadRequestException("Required patient id when getting measurements");
        long pid = Long.parseLong(patient_id);
        Patient patient = getPatientIfExists(patientRepository,pid);
        setTimeRange(from,to);
        return findPatientMeasurements(pid,dateFrom,dateTo);
    }

    private List<PatientMeasurementRepresentation> findPatientMeasurements(long pid, long dateFrom, long dateTo) {
        List<PatientMeasurementRepresentation> pmRepresentationList = new ArrayList<>();
        List<PatientMeasurement> patientMeasurements= patientMeasurementRepository.findPatientMeasurements(pid,dateFrom,dateTo);
        patientMeasurements.forEach(pm -> pmRepresentationList.add(PatientMeasurementRepresentation.getPatientMeasurementRepresentation(pm)));
        return pmRepresentationList;
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
     * This method is used to update a patient after measurement stored.It checks if a month has passed so the patient to be available to be consulted
     * @param p
     * @param pm
     */
    public void updatePatient(Patient p,PatientMeasurement pm){
        if(p.getDateMonthStarted()==null){
            p.setDateMonthStarted(pm.getDateStored());
        }else{
            if(pm.getDateStored().getTime() - p.getDateMonthStarted().getTime() >=(Constants.MONTH_CALENDAR*Constants.DAY_MILLIS)){
                p.setAvailableConsulted(true);
                p.setDateAvailableConsulted(pm.getDateStored());
            }
        }
        p.setDateLastStored(pm.getDateStored());
        patientRepository.save(p);
    }


}
