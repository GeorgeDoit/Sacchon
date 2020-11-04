package com.sacchon.restapi.resource.impl.consultations;

import com.sacchon.restapi.common.Utilities;
import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.model.Consult;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.repository.ConsultRepository;
import com.sacchon.restapi.repository.patient.PatientRepository;
import com.sacchon.restapi.repository.util.JpaUtil;
import com.sacchon.restapi.representations.consultations.ConsultRepresentation;
import com.sacchon.restapi.resource.interfaces.consultations.ConsultResource;
import com.sacchon.restapi.resource.util.ResourceUtils;
import com.sacchon.restapi.security.SacchonRole;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ConsultResourceImpl extends ServerResource implements ConsultResource {

    private EntityManager em;
    private ConsultRepository consultRepository;
    private PatientRepository patientRepository ;
    private long id;

    @Override
    protected void doInit(){
        try{
            em = JpaUtil.getEntityManager();
            consultRepository = new ConsultRepository(em);
            patientRepository = new PatientRepository(em);
            id = Long.parseLong(getAttribute("id"));
        } catch (Exception e){
            throw new ResourceException(e);
        }

    }

    @Override
    protected void doRelease(){
        em.close();
    }

    /**
     * This method is used to get a single known consultation by id
     * @return
     * @throws NotFoundException
     */
     @Override
     public ConsultRepresentation getConsult() throws NotFoundException {

         String[] roles = {SacchonRole.ROLE_PATIENT.getRoleName(),SacchonRole.ROLE_DOCTOR.getRoleName()};
         Utilities.checkRoles(this,roles);

         Consult consult = getConsultIfExists(consultRepository,id);
         ConsultRepresentation consultRepresentation = ConsultRepresentation.getConsultRepresentation(consult);
         return consultRepresentation;
     }

    /**
     * This method is used to modify an existing consultation
     * @param consultRepresentation
     * @return
     * @throws NotFoundException
     * @throws BadEntityException
     */
    @Override
     public ConsultRepresentation update(ConsultRepresentation consultRepresentation) throws Exception {
         String[] roles = {SacchonRole.ROLE_DOCTOR.getRoleName()};
         Utilities.checkRoles(this,roles);
         Consult consult = getConsultIfExists(consultRepository,id);
         if(!consult.isSeen()){
             consult.setTitle(consultRepresentation.getTitle());
             consult.setDescription(consultRepresentation.getDescription());
             consultRepository.save(consult);

             Patient p = getPatientIfExists(patientRepository,consult.getPatientToConsult().getId());
             p.setWarning(true);
             patientRepository.save(p);

         }else{
             throw new BadRequestException("Consult can't be modified!");
         }

         return ConsultRepresentation.getConsultRepresentation(consult);
     }

    /**
     * This method returns a consultation if exists
      * @param consultRepository
     * @param id
     * @return
     * @throws NotFoundException
     */
    private Consult getConsultIfExists(ConsultRepository consultRepository, long id) throws NotFoundException {
        Optional<Consult> consult = consultRepository.findById(id);
        setExisting(consult.isPresent());
        if (!consult.isPresent()) throw new NotFoundException("Consult  with id:" + id + " not found");
        return  consult.get();
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


}
