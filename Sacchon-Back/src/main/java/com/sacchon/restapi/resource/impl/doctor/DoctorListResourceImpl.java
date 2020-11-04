package com.sacchon.restapi.resource.impl.doctor;

import com.sacchon.restapi.common.Constants;
import com.sacchon.restapi.common.Utilities;
import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.model.Doctor;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.model.UserTable;
import com.sacchon.restapi.repository.DoctorRepository;
import com.sacchon.restapi.repository.UserTableRepository;
import com.sacchon.restapi.repository.util.JpaUtil;
import com.sacchon.restapi.representations.doctor.DoctorRepresentation;
import com.sacchon.restapi.representations.patient.PatientRepresentation;
import com.sacchon.restapi.resource.interfaces.doctor.DoctorListResource;
import com.sacchon.restapi.resource.util.ResourceUtils;
import com.sacchon.restapi.security.SacchonRole;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


public class DoctorListResourceImpl extends ServerResource implements DoctorListResource {
    private DoctorRepository doctorRepository ;
    private UserTableRepository userTableRepository;
    private EntityManager em;


    @Override
    protected void doInit() {
        try {
            em = JpaUtil.getEntityManager();
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
     * This method is used to create a new doctor account
     * @param doctorRepresentation
     * @return
     * @throws BadEntityException
     */
    @Override
    public DoctorRepresentation add(DoctorRepresentation doctorRepresentation) throws BadEntityException {

        String[] roles = {SacchonRole.ROLE_ADMIN.getRoleName()};
        Utilities.checkRoles(this,roles);
        Doctor doctor = DoctorRepresentation.getDoctor(doctorRepresentation);
        checkIfUserExists(doctor,doctorRepresentation);
        return DoctorRepresentation.getDoctorRepresentation(doctor);
    }

    /**
     * This method is used to get doctors(1.by username 2.by a state ,3.all of them)
     * @return
     * @throws NotFoundException
     */
    @Override
    public List<DoctorRepresentation> getDoctors() throws BadRequestException {
        String[] roles = {SacchonRole.ROLE_ADMIN.getRoleName(),SacchonRole.ROLE_DOCTOR.getRoleName()};
        Utilities.checkRoles(this,roles);
        String username = getQueryValue(Constants.QV_USERNAME);
        if(username!=null) {
            return findDoctorByUsername(username);
        }
        String state = getQueryValue(Constants.QV_STATE);
        if(state!=null) {
            return findByState(state);
        }
        return findAll();


    }

    /**
     * This method is used to find a doctor by username
     * @param username
     * @return
     */
    private List<DoctorRepresentation> findDoctorByUsername(String username) {
        List<Doctor> doctors;
        List<DoctorRepresentation> doctorRepresentationList = new ArrayList<>();
        doctors = doctorRepository.findDoctorByUsername(username);
        doctors.forEach(doctor -> doctorRepresentationList.add(DoctorRepresentation.getDoctorRepresentation(doctor)));
        return doctorRepresentationList;
    }

    /**
     * This method is used to find all doctors
     * @return
     */
    private List<DoctorRepresentation> findAll() {
        List<Doctor> doctors;
        List<DoctorRepresentation> doctorRepresentationList = new ArrayList<>();
        doctors= doctorRepository.findAll();
        doctors.forEach(doctor -> doctorRepresentationList.add(DoctorRepresentation.getDoctorRepresentation(doctor)));
        return doctorRepresentationList;
    }

    /**
     * This method is used to find doctors by a state
     * @param state
     * @return
     * @throws BadRequestException
     */
    private List<DoctorRepresentation> findByState(String state) throws BadRequestException {
        switch (state){
            case Constants.QV_INACTIVE:
                String start= getQueryValue(Constants.QV_FROM);
                String end = getQueryValue(Constants.QV_TO);
                if(start != null && end !=null){
                    long from = Long.parseLong(start);
                    long to = Long.parseLong(end);
                    return  findInactiveDoctors(from,to);
                }else{
                    throw new BadRequestException("You didnt provide time period in request");
                }
            default:
                throw new BadRequestException("BadRequestException");
        }


    }

    /**
     * This method is used to update user table after doctor creation
     * @param doctor
     * @param doctorRepresentation
     */
    public void updateUser(Doctor doctor,DoctorRepresentation doctorRepresentation){
        UserTable user = new UserTable();
        user.setUsername(doctorRepresentation.getUsername());
        user.setPassword(doctorRepresentation.getPassword());
        user.setDoctor(doctor);
        user.setActive(true);
        user.setRole(SacchonRole.ROLE_DOCTOR.getRoleName());
        userTableRepository.save(user);
    }

    /**
     * This method checks if a user exists
     * @param doctor
     * @param doctorRepresentation
     * @throws BadEntityException
     */
    private void checkIfUserExists(Doctor doctor,DoctorRepresentation doctorRepresentation) throws BadEntityException {
        if(!userTableRepository.findUser(doctor.getUsername())){
            doctorRepository.save(doctor);
            updateUser(doctor,doctorRepresentation);
        }else{
            throw new BadEntityException("User with username " + doctor.getUsername() + " already exists!");
        }
    }

    /**
     * This method is used to the doctors that are inactive
     * @param from
     * @param to
     * @return
     * @throws BadRequestException
     */
    private List<DoctorRepresentation> findInactiveDoctors(long from,long to) throws BadRequestException {
        List<Doctor> doctors = new ArrayList<>();
        List<DoctorRepresentation> doctorRepresentationList = new ArrayList<>();
        doctors = doctorRepository.findInactiveDoctors(from,to);
        doctors.forEach(doctor -> doctorRepresentationList.add(DoctorRepresentation.getDoctorRepresentation(doctor)));
        return doctorRepresentationList;
    }
}
