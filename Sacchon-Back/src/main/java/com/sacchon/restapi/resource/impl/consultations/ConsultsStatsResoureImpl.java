package com.sacchon.restapi.resource.impl.consultations;

import com.sacchon.restapi.common.Constants;
import com.sacchon.restapi.common.Utilities;
import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.model.Doctor;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.repository.ConsultRepository;
import com.sacchon.restapi.repository.DoctorRepository;
import com.sacchon.restapi.repository.patient.PatientRepository;
import com.sacchon.restapi.repository.util.JpaUtil;
import com.sacchon.restapi.representations.consultations.ConsultsStatsRepresentation;
import com.sacchon.restapi.resource.interfaces.consultations.ConsultsStatsResource;
import com.sacchon.restapi.resource.util.ResourceUtils;
import com.sacchon.restapi.security.SacchonRole;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsultsStatsResoureImpl extends ServerResource implements ConsultsStatsResource {

    private ConsultRepository consultRepository ;
    private DoctorRepository doctorRepository ;
    private PatientRepository patientRepository ;
    private EntityManager em;
    private long dateFrom = 0 ;
    private long dateTo = 0 ;


    @Override
    protected void doInit() {
        try {
            em = JpaUtil.getEntityManager();
            consultRepository = new ConsultRepository(em);
            doctorRepository = new DoctorRepository(em);
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
     * This method is used to get the submissions(consultations) of a doctor or patient over a time range
     * @return
     * @throws NotFoundException
     */
    @Override
    public ConsultsStatsRepresentation getStats() throws NotFoundException,BadRequestException {

        String[] roles = {SacchonRole.ROLE_ADMIN.getRoleName()};
        Utilities.checkRoles(this,roles);
        String patient_id = getQueryValue(Constants.QV_PATIENT_ID);
        String doctor_id = getQueryValue(Constants.QV_DOCTOR_ID);
        String from = getQueryValue(Constants.QV_FROM);
        String to = getQueryValue(Constants.QV_TO);
        setTimeRange(from,to);
        if(patient_id != null && doctor_id !=null){
            return getPatientDoctorConsultsStats(patient_id,doctor_id);
        }else if(patient_id != null){
            return getPatientConsultsStats(patient_id);
        }else if(doctor_id != null){
            return getDoctorConsultsStats(doctor_id);
        }
        throw new BadRequestException("Bad request");

    }


    /**
     * This method is used if we want to return the number of consultations that a certain doctor made to a certain patient
     * @param patient_id
     * @param doctor_id
     * @return
     * @throws NotFoundException
     */
    private ConsultsStatsRepresentation getPatientDoctorConsultsStats(String patient_id,String doctor_id) throws NotFoundException{
        long pid =  Long.parseLong(patient_id);
        Patient p = getPatientIfExists(patientRepository,pid);
        long did =  Long.parseLong(doctor_id);
        Doctor d = getDoctorIfExists(doctorRepository,did);
        long subs = consultRepository.numConsults(pid,did,dateFrom,dateTo);
        ConsultsStatsRepresentation consultsStatsRepresentation = ConsultsStatsRepresentation.getConsultsStatsRepresentation(subs);
        return consultsStatsRepresentation;

    }

    /**
     * This method is used if we want to return the number of consultations that a certain patient received
     * @param patient_id
     * @return
     * @throws NotFoundException
     */
    private ConsultsStatsRepresentation getPatientConsultsStats(String patient_id) throws NotFoundException{
        long pid =  Long.parseLong(patient_id);
        Patient p = getPatientIfExists(patientRepository,pid);
        long subs = consultRepository.numPatientConsults(pid,dateFrom,dateTo);
        ConsultsStatsRepresentation consultsStatsRepresentation = ConsultsStatsRepresentation.getConsultsStatsRepresentation(subs);
        return consultsStatsRepresentation;
    }

    /**
     * This method is used if we want to return the number of consultations made by a certain doctor
     * @param doctor_id
     * @return
     * @throws NotFoundException
     */
    private ConsultsStatsRepresentation getDoctorConsultsStats(String doctor_id) throws NotFoundException{
        long did =  Long.parseLong(doctor_id);
        Doctor d = getDoctorIfExists(doctorRepository,did);
        long subs = consultRepository.numDoctorConsults(did,dateFrom,dateTo);
        ConsultsStatsRepresentation consultsStatsRepresentation = ConsultsStatsRepresentation.getConsultsStatsRepresentation(subs);
        return consultsStatsRepresentation;

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
     * This method returns a patient if exists
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
     * This method returns a doctor if exists
     * @param doctorRepository
     * @param id
     * @return
     * @throws NotFoundException
     */
    public Doctor getDoctorIfExists(DoctorRepository doctorRepository,long id) throws NotFoundException {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (!doctor.isPresent())  throw new NotFoundException(this.getClass().getName() + "," + "Doctor with id: " + id + " not found");
        setExisting(doctor.isPresent());
        Doctor d = doctor.get();
        return  d;
    }
}
