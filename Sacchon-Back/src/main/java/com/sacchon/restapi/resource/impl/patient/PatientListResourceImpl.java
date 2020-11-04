package com.sacchon.restapi.resource.impl.patient;

import com.sacchon.restapi.common.Constants;
import com.sacchon.restapi.common.Utilities;
import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.model.Doctor;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.model.UserTable;
import com.sacchon.restapi.repository.DoctorRepository;
import com.sacchon.restapi.repository.patient.PatientRepository;
import com.sacchon.restapi.repository.UserTableRepository;
import com.sacchon.restapi.repository.util.JpaUtil;
import com.sacchon.restapi.representations.patient.PatientRepresentation;
import com.sacchon.restapi.resource.interfaces.patient.PatientListResource;
import com.sacchon.restapi.security.SacchonRole;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class PatientListResourceImpl extends ServerResource implements PatientListResource {
    private PatientRepository patientRepository ;
    private DoctorRepository doctorRepository;
    private UserTableRepository userTableRepository;
    private EntityManager em;
    @Override
    protected void doInit() {
        try {
            em = JpaUtil.getEntityManager();
            patientRepository = new PatientRepository(em);
            doctorRepository = new DoctorRepository(em);
            userTableRepository = new UserTableRepository(em);
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
     * This method is used to get patients(1.by username 2.by doctor id 3.by a state)
     * @return
     * @throws NotFoundException
     */
    @Override
    public List<PatientRepresentation> getPatients() throws NotFoundException, BadRequestException {
        String[] roles = {SacchonRole.ROLE_PATIENT.getRoleName(),SacchonRole.ROLE_DOCTOR.getRoleName(),SacchonRole.ROLE_ADMIN.getRoleName()};
        Utilities.checkRoles(this,roles);
        String username = getQueryValue(Constants.QV_USERNAME);
        if(username!=null) {
            return findPatientsByUserName(username);
        }
        String doctor_id = getQueryValue(Constants.QV_DOCTOR_ID);
        if(doctor_id!=null) {
            return  findPatientByDoctorId(doctor_id);
        }
        String state = getQueryValue(Constants.QV_STATE);
        if(state!=null) {
            return findByState(state);
        }
        return findAll();
    }

    /**
     * This method returns all patients
     * @return
     */
    private List<PatientRepresentation>findAll()
    {
        List<Patient> patients = new ArrayList<>();
        List<PatientRepresentation> patientRepresentationList = new ArrayList<>();
        patients = patientRepository.findAll();
        patients.forEach(patient -> patientRepresentationList.add(PatientRepresentation.getPatientRepresentation(patient)));
        return patientRepresentationList;
    }

    private List<PatientRepresentation> findByState(String state) throws BadRequestException {
        switch (state){
            case Constants.QV_FREE:
                return findFreePatients(state);
            case Constants.QV_INACTIVE:
                String start= getQueryValue(Constants.QV_FROM);
                String end = getQueryValue(Constants.QV_TO);
                if(start != null && end !=null){
                    long from = Long.parseLong(start);
                    long to = Long.parseLong(end);
                    return findInactivePatients(from,to);
                }else{
                    throw new BadRequestException("You didnt provide time period in request");
                }
            case Constants.QV_AVAILABLE_CONSULTED:
                return findAvailableConsultedPatients();
            default:
                throw new BadRequestException("BadRequestException");
        }
    }

    /**
     * This method returns the patients that are not binded with a doctor and are available to be consulted
     * @param free
     * @return
     * @throws BadRequestException
     */
    private List<PatientRepresentation> findFreePatients(String free) throws BadRequestException {
        if(free.equals(Constants.QV_FREE)){
            List<Patient> patients = new ArrayList<>();
            List<PatientRepresentation> patientRepresentationList = new ArrayList<>();
            patients = patientRepository.findFreePatients();
            patients.forEach(patient -> patientRepresentationList.add(PatientRepresentation.getPatientRepresentation(patient)));
            return patientRepresentationList;
        }else{
            throw new BadRequestException("Wrong argument value");
        }
    }

    /**
     * This method returns the patients that are inactive over a time range(not stored data)
     * @param from
     * @param to
     * @return
     * @throws BadRequestException
     */
    private List<PatientRepresentation> findInactivePatients(long from,long to) throws BadRequestException {
        List<Patient> patients = new ArrayList<>();
        List<PatientRepresentation> patientRepresentationList = new ArrayList<>();
        patients = patientRepository.findInactivePatients(from,to);
        patients.forEach(patient -> patientRepresentationList.add(PatientRepresentation.getPatientRepresentation(patient)));
        return patientRepresentationList;
    }

    /**
     * This method returns patients that are binded to doctor
     * @param doctor_id
     * @return
     * @throws NotFoundException
     */
    private List<PatientRepresentation> findPatientByDoctorId(String doctor_id) throws NotFoundException {
        List<Patient> patients = new ArrayList<>();
        List<PatientRepresentation> patientRepresentationList = new ArrayList<>();
        long did = Long.parseLong(doctor_id);
        checkIfDoctorExists(doctorRepository,did);
        patients = patientRepository.findPatientsByDocId(did);
        patients.forEach(patient -> patientRepresentationList.add(PatientRepresentation.getPatientRepresentation(patient)));
        return patientRepresentationList;
    }

    /**
     * This method returns the patients that are available to be consulted
     * @return
     */
    private List<PatientRepresentation> findAvailableConsultedPatients() {
        List<Patient> patients = new ArrayList<>();
        List<PatientRepresentation> patientRepresentationList = new ArrayList<>();
        patients = patientRepository.findAvailablePatients();
        patients.forEach(patient -> patientRepresentationList.add(PatientRepresentation.getPatientRepresentation(patient)));
        return patientRepresentationList;
    }

    /**
     * This method returns the patients by a username
     * @param username
     * @return
     */
    private List<PatientRepresentation> findPatientsByUserName(String username) {
        List<Patient> patients = new ArrayList<>();
        List<PatientRepresentation> patientRepresentationList = new ArrayList<>();
        patients = patientRepository.findPatientsByUsername(username);
        patients.forEach(patient -> patientRepresentationList.add(PatientRepresentation.getPatientRepresentation(patient)));
        return  patientRepresentationList;
    }

    /**
     * This method checks if a doctor exists
     * @param doctorRepository
     * @param did
     * @throws NotFoundException
     */
    private void checkIfDoctorExists(DoctorRepository doctorRepository, long did) throws NotFoundException {
        Optional<Doctor> doctor = doctorRepository.findById(did);
        if(!doctor.isPresent()) throw new NotFoundException("Doctor with id: " + did + " not found");
    }

    /**
     * This method updates user table after patient creation
     * @param patient
     * @param patientRepresentation
     */
    public void updateUser(Patient patient,PatientRepresentation patientRepresentation){
        UserTable user = new UserTable();
        user.setUsername(patientRepresentation.getUsername());
        user.setPassword(patientRepresentation.getPassword());
        user.setPatient(patient);
        user.setActive(true);
        user.setRole(SacchonRole.ROLE_PATIENT.getRoleName());
        userTableRepository.save(user);
    }

    /**
     * This method checks if a user exists
     * @param patient
     * @param patientRepresentation
     * @throws BadEntityException
     */
    private void checkIfUserExists(Patient patient,PatientRepresentation patientRepresentation) throws BadEntityException {
        if(!userTableRepository.findUser(patient.getUsername())){
            patientRepository.save(patient);
            updateUser(patient,patientRepresentation);
        }else{
            throw new BadEntityException("User with username " + patient.getUsername() + " already exists!");
        }
    }


}
