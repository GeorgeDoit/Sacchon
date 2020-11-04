package com.sacchon.restapi.resource.impl.patient;


import com.sacchon.restapi.common.Utilities;
import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.model.Doctor;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.model.PatientMeasurement;
import com.sacchon.restapi.model.UserTable;
import com.sacchon.restapi.repository.DoctorRepository;
import com.sacchon.restapi.repository.patient.PatientRepository;
import com.sacchon.restapi.repository.UserTableRepository;
import com.sacchon.restapi.repository.util.JpaUtil;
import com.sacchon.restapi.representations.patient.PatientMeasurementRepresentation;
import com.sacchon.restapi.representations.patient.PatientRepresentation;
import com.sacchon.restapi.resource.interfaces.patient.PatientResource;
import com.sacchon.restapi.resource.util.ResourceUtils;
import com.sacchon.restapi.security.SacchonRole;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class  PatientResourceImpl extends ServerResource implements PatientResource {

    private PatientRepository patientRepository ;
    private UserTableRepository userTableRepository ;
    private EntityManager em;
    private long id;

    @Override
    protected void doInit() {
        try {
            em = JpaUtil.getEntityManager();
            patientRepository = new PatientRepository(em);
            userTableRepository = new UserTableRepository(em);
            id = Long.parseLong(getAttribute("id"));
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
     * This method is used to get a single patient according to id
     */
    @Override
    public PatientRepresentation getPatient() throws NotFoundException, ResourceException {
        String[] roles = {SacchonRole.ROLE_PATIENT.getRoleName(),SacchonRole.ROLE_ADMIN.getRoleName()};
        Utilities.checkRoles(this,roles);
        Patient patient = getPatientIfExists(patientRepository,id);
        PatientRepresentation patientRepresentation = PatientRepresentation.getPatientRepresentation(patient);
        return patientRepresentation;
    }

    /**
     * This method is used to remove a patient according to id
     * @throws NotFoundException
     */
    @Override
    public void remove() throws NotFoundException {
        String[] roles = {SacchonRole.ROLE_PATIENT.getRoleName(),SacchonRole.ROLE_ADMIN.getRoleName()};
        Utilities.checkRoles(this,roles);
        Patient p = getPatientIfExists(patientRepository,id);
        UserTable user = getUserIfExists(userTableRepository,p);
        updateUser(user);
    }

    /**
     * This method is used to update a patient
     * @param pmRepr
     * @return
     * @throws NotFoundException
     * @throws BadEntityException
     */
    @Override
    public PatientRepresentation update(PatientRepresentation pmRepr) throws NotFoundException, BadEntityException {
        String[] roles = {SacchonRole.ROLE_PATIENT.getRoleName()};
        Utilities.checkRoles(this,roles);
        Patient pm = getPatientIfExists(patientRepository,id);
        pm.setAmka(pmRepr.getAmka());
        pm.setGender(pmRepr.getGender());
        pm.setName(pmRepr.getName());
        pm.setSurname(pmRepr.getSurname());
        pm.setDob(pmRepr.getDob());
        patientRepository.save(pm);
        

        return PatientRepresentation.getPatientRepresentation(pm);

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
        setExisting(patient.isPresent());
        if (!patient.isPresent())  throw new NotFoundException("Patient with id: " + id + " not found");
        return patient.get();
    }

    /**
     * This method is used to get a user if exists
     * @param userTableRepository
     * @param p
     * @return
     * @throws NotFoundException
     */
    public UserTable getUserIfExists(UserTableRepository userTableRepository,Patient p) throws NotFoundException {
        Optional<UserTable> userTable = userTableRepository .findUser(p.getId(), SacchonRole.ROLE_PATIENT.getRoleName());
        if (!userTable.isPresent())  throw new NotFoundException("User is not found");
        UserTable user = userTable.get();
        return user;
    }

    /**
     * This method is used to update user table after remove(soft remove that works with an active variable)
     * @param user
     */
    public void updateUser(UserTable user){
        user.setActive(false);
        userTableRepository.save(user);
    }

}
