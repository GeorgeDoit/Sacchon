package com.sacchon.restapi.resource.impl.consultations;

import com.sacchon.restapi.common.Constants;
import com.sacchon.restapi.common.Utilities;
import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.model.Consult;
import com.sacchon.restapi.model.Doctor;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.repository.ConsultRepository;
import com.sacchon.restapi.repository.DoctorRepository;
import com.sacchon.restapi.repository.patient.PatientRepository;
import com.sacchon.restapi.repository.util.JpaUtil;
import com.sacchon.restapi.representations.consultations.ConsultRepresentation;
import com.sacchon.restapi.resource.interfaces.consultations.ConsultListResource;
import com.sacchon.restapi.security.SacchonRole;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ConsultListResourceImpl extends ServerResource implements ConsultListResource {

    private EntityManager em;
    private ConsultRepository consultRepository;
    private PatientRepository patientRepository ;
    private DoctorRepository doctorRepository ;
    private boolean doctorExists;
    private boolean patientExists;

    @Override
    protected void doInit() {
        try {
            em = JpaUtil.getEntityManager();
            consultRepository = new ConsultRepository(em);
            patientRepository = new PatientRepository(em);
            doctorRepository = new DoctorRepository(em);
        } catch (Exception e) {
            throw new ResourceException(e);
        }
    }

    @Override
    protected void doRelease() {
        em.close();
    }


    /**
     * This method is used to provide advice to a patient
     */
    @Override
    public ConsultRepresentation add(ConsultRepresentation consultRepresentation) throws BadEntityException, NotFoundException, BadRequestException {

        String[] roles = {SacchonRole.ROLE_DOCTOR.getRoleName()};
        Utilities.checkRoles(this,roles);
        String doctor_id = getQueryValue(Constants.QV_DOCTOR_ID);
        String patient_id = getQueryValue(Constants.QV_PATIENT_ID);
        if(doctor_id == null) throw new BadRequestException("Required doctor id when added consult" );
        if(patient_id == null )throw new BadRequestException("Required patient id when added consult" );
        long did = Long.parseLong(doctor_id);
        long pid = Long.parseLong(patient_id);
        Doctor d = getDoctorIfExists(doctorRepository,did);
        Patient p = getPatientIfExists(patientRepository,pid);
        if (consultRepresentation == null) throw new BadEntityException("Bad Entity exception");
        Consult consult = ConsultRepresentation.getConsult(consultRepresentation,p,d);
        consult(p,d,consult);
        return ConsultRepresentation.getConsultRepresentation(consult);

        /* if(!p.isAvailableConsulted()){
            if(consult.getDateStored().getTime() - p.getDateMonthStarted().getTime() >=(30*86400000L)){
                p.setAvailableConsulted(true);
            }
        }*/
    }

    private void consult(Patient p, Doctor d, Consult consult) throws NotFoundException {
        if(p.isAvailableConsulted()){
            consultRepository.save(consult);
            updateDoctor(d,consult);
            updatePatient(p,d,consult);
        }else{
            throw new NotFoundException("Doctor with id : " + d.getId() +" can't consult" + p.getId());
        }
    }

    /**
     * This method is used to get consultations of doctor or a patient
     * @return
     */
    @Override
    public List<ConsultRepresentation> getConsults() throws NotFoundException, BadRequestException {
        long pid=0;
        long did=0;

        String[] roles = {SacchonRole.ROLE_PATIENT.getRoleName(),SacchonRole.ROLE_DOCTOR.getRoleName()};
        Utilities.checkRoles(this,roles);
        String doctor_id = getQueryValue(Constants.QV_DOCTOR_ID);
        String patient_id = getQueryValue(Constants.QV_PATIENT_ID);
        if(doctor_id!=null){
            did = Long.parseLong(doctor_id);
            Doctor d = getDoctorIfExists(doctorRepository,did);
            doctorExists = true;
        }
        if(patient_id!=null){
            pid = Long.parseLong(patient_id);
            Patient p = getPatientIfExists(patientRepository,pid);
            patientExists = true;

            updateWarning(p);

        }
        return getConsultations(pid,did);
    }

    /**
     * This method takes from the repository the appropriate method for getting consultations. It updates consult if seen by a patient,so to can't be modified in future.
     * @param pid
     * @param did
     * @return
     * @throws BadRequestException
     */
    private List<ConsultRepresentation> getConsultations(long pid,long did) throws BadRequestException {
        List<Consult> consults = new ArrayList<>();
        List<ConsultRepresentation> consultRepresentationList = new ArrayList<>();
        if(patientExists && doctorExists){
            consults= consultRepository.findConsults(pid,did);
            consults.forEach(consult -> consultRepresentationList.add(ConsultRepresentation.getConsultRepresentation(consult)));
        }else{
            if(patientExists){

                consults= consultRepository.findPatientConsults(pid);
                consults.forEach(consult -> updateConsult(consult));
                consults.forEach(consult -> consultRepresentationList.add(ConsultRepresentation.getConsultRepresentation(consult)));
            }else if(doctorExists){
                consults= consultRepository.findDoctorConsults(did);
                consults.forEach(consult -> consultRepresentationList.add(ConsultRepresentation.getConsultRepresentation(consult)));
            }else{
                throw new BadRequestException("Required doctor or patient id when getting consultations" );
            }
        }
        return  consultRepresentationList;
    }

    private void updateWarning(Patient p) {
        p.setWarning(false);
        patientRepository.save(p);
    }

    private void updateConsult(Consult consult) {
        if(!consult.isSeen()){
            consult.setSeen(true);
            consultRepository.save(consult);
        }
    }

    /**
     * This method is used to return a Patient if exists
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
     * This method is used to return a Doctor if exists
     * @param doctorRepository
     * @param id
     * @return
     * @throws NotFoundException
     */
    public Doctor getDoctorIfExists(DoctorRepository doctorRepository,long id) throws NotFoundException {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (!doctor.isPresent())  throw new NotFoundException("Doctor with id: " + id + " not found");
        setExisting(doctor.isPresent());
        Doctor d = doctor.get();
        return  d;
    }

    /**
     * This method is used to update Patient after consultation
     * @param p
     * @param d
     * @param consult
     */
    public void updatePatient(Patient p,Doctor d,Consult consult){
        p.setDoctor(d);
        p.setDateMonthStarted(consult.getDateStored());
        p.setAvailableConsulted(false);
        p.setDateLastConsulted(consult.getDateStored());
        patientRepository.save(p);
    }

    /**
     * This method is used to update doctor after consultation
     * @param d
     * @param consult
     */
    public void updateDoctor(Doctor d,Consult consult){
        d.setDateLastConsulted(consult.getDateStored());
        doctorRepository.save(d);
    }


}
