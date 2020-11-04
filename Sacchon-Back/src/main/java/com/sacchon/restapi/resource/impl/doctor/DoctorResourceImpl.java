package com.sacchon.restapi.resource.impl.doctor;

import com.sacchon.restapi.common.Utilities;
import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.model.Doctor;
import com.sacchon.restapi.model.Patient;

import com.sacchon.restapi.model.UserTable;
import com.sacchon.restapi.repository.DoctorRepository;
import com.sacchon.restapi.repository.UserTableRepository;
import com.sacchon.restapi.repository.patient.PatientRepository;
import com.sacchon.restapi.repository.util.JpaUtil;
import com.sacchon.restapi.representations.doctor.DoctorRepresentation;
import com.sacchon.restapi.representations.patient.PatientRepresentation;
import com.sacchon.restapi.resource.interfaces.doctor.DoctorResource;
import com.sacchon.restapi.resource.util.ResourceUtils;
import com.sacchon.restapi.security.SacchonRole;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorResourceImpl extends ServerResource implements DoctorResource {


    private DoctorRepository doctorRepository;
    private UserTableRepository userTableRepository ;
    private PatientRepository patientRepository;
    private EntityManager em;
    private long id;

    @Override
    protected void doInit() {
        try {
            em = JpaUtil.getEntityManager();
            doctorRepository = new DoctorRepository(em);
            userTableRepository = new UserTableRepository(em);
            patientRepository = new PatientRepository(em);
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
     * This method is used to get a single doctor by id
     * @return
     * @throws NotFoundException
     */
    @Override
    public DoctorRepresentation getDoctor() throws NotFoundException {
        String[] roles = {SacchonRole.ROLE_ADMIN.getRoleName(),SacchonRole.ROLE_DOCTOR.getRoleName()};
        Utilities.checkRoles(this,roles);
        Doctor doctor = getDoctorIfExists(doctorRepository,id);
        DoctorRepresentation doctorRepresentation = DoctorRepresentation.getDoctorRepresentation(doctor);
        return doctorRepresentation;
    }

    /**
     * This method is used to remove a single doctor by id
     * @throws NotFoundException
     */
    @Override
    public void remove() throws NotFoundException {

        String[] roles = {SacchonRole.ROLE_ADMIN.getRoleName(),SacchonRole.ROLE_DOCTOR.getRoleName()};
        Utilities.checkRoles(this,roles);

        Doctor d = getDoctorIfExists(doctorRepository,id);
        //search for patients
        List<Patient> patients = patientRepository.findPatientsByDocId(d.getId());
        for (Patient p: patients) {
            p.setDoctor(null);
            p.setAvailableConsulted(true);
        }
        UserTable user = getUserIfExists(userTableRepository,d);
        updateUser(user);
    }

    /**
     * This method is used to update a doctor
     * @param doctorRepresentation
     * @return
     * @throws NotFoundException
     * @throws BadEntityException
     */
    @Override
    public DoctorRepresentation update(DoctorRepresentation doctorRepresentation) throws NotFoundException {
        String[] roles = {SacchonRole.ROLE_DOCTOR.getRoleName(),SacchonRole.ROLE_ADMIN.getRoleName()};
        Utilities.checkRoles(this,roles);
        Doctor doc = getDoctorIfExists(doctorRepository,id);
        doc.setName(doctorRepresentation.getName());
        doc.setSurname(doctorRepresentation.getSurname());
        doctorRepository.save(doc);
        return DoctorRepresentation.getDoctorRepresentation(doc);
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
        if (!doctor.isPresent())  throw new NotFoundException("Doctor with id: " + id + " not found");
        setExisting(doctor.isPresent());
        Doctor d = doctor.get();
        return  d;
     }

    /**
     * This method returns a user if exists
      * @param userTableRepository
     * @param d
     * @return
     * @throws NotFoundException
     */
    public UserTable getUserIfExists(UserTableRepository userTableRepository,Doctor d) throws NotFoundException {
        Optional<UserTable> userTable = userTableRepository .findUser(d.getId(), SacchonRole.ROLE_DOCTOR.getRoleName());
        if (!userTable.isPresent())  throw new NotFoundException("User is not found");
        UserTable user = userTable.get();
        return user;
    }

    public void updateUser(UserTable user){
        user.setActive(false);
        userTableRepository.save(user);
    }
}
