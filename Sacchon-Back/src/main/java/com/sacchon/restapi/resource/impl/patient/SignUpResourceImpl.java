package com.sacchon.restapi.resource.impl.patient;

import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.model.UserTable;
import com.sacchon.restapi.repository.UserTableRepository;
import com.sacchon.restapi.repository.patient.PatientRepository;
import com.sacchon.restapi.repository.util.JpaUtil;
import com.sacchon.restapi.representations.patient.PatientRepresentation;
import com.sacchon.restapi.resource.interfaces.patient.SignUpResource;
import com.sacchon.restapi.security.SacchonRole;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;

public class SignUpResourceImpl extends ServerResource implements SignUpResource {
    private PatientRepository patientRepository ;
    private UserTableRepository userTableRepository;
    private EntityManager em;
    @Override
    protected void doInit() {
        try {
            em = JpaUtil.getEntityManager();
            patientRepository = new PatientRepository(em);
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
     * This method is used to create a new patient account
     * @param patientRepresentation
     * @return
     * @throws BadEntityException
     */
    @Override
    public PatientRepresentation add(PatientRepresentation patientRepresentation) throws BadEntityException {
        Patient patient = PatientRepresentation.getPatient(patientRepresentation);
        checkIfUserExists(patient,patientRepresentation);
        return PatientRepresentation.getPatientRepresentation(patient);
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
