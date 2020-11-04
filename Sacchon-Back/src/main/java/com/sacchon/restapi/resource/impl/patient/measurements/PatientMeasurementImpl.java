package com.sacchon.restapi.resource.impl.patient.measurements;

import com.sacchon.restapi.common.Utilities;
import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.model.PatientMeasurement;
import com.sacchon.restapi.repository.patient.PatientMeasurementRepository;
import com.sacchon.restapi.repository.patient.PatientRepository;
import com.sacchon.restapi.repository.util.JpaUtil;
import com.sacchon.restapi.representations.patient.PatientMeasurementRepresentation;
import com.sacchon.restapi.resource.interfaces.patient.PatientMeasurementResource;
import com.sacchon.restapi.resource.util.ResourceUtils;
import com.sacchon.restapi.security.SacchonRole;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientMeasurementImpl extends ServerResource implements PatientMeasurementResource {

    private PatientMeasurementRepository patientMeasurementRepository ;
    private EntityManager em;
    private long id;

    @Override
    protected void doInit() {
        try {
            em = JpaUtil.getEntityManager();
            patientMeasurementRepository = new PatientMeasurementRepository(em);
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
     * This method is used to get a patient measurement according to id
     * @return
     * @throws NotFoundException
     * @throws ResourceException
     */
       @Override
    public PatientMeasurementRepresentation getPatientMeasurement() throws NotFoundException, ResourceException {
        String[] roles = {SacchonRole.ROLE_PATIENT.getRoleName(),SacchonRole.ROLE_DOCTOR.getRoleName()};
        Utilities.checkRoles(this,roles);
        PatientMeasurement pm = getPatientMeasurementIfExists(patientMeasurementRepository,id);
        PatientMeasurementRepresentation patientMeasurementRepresentation = PatientMeasurementRepresentation.getPatientMeasurementRepresentation(pm);
        return patientMeasurementRepresentation;
    }


    /**
     * This method is used to remove patient measurement according to id
     * @throws NotFoundException
     */
    @Override
    public void remove() throws NotFoundException {
        String[] roles = {SacchonRole.ROLE_PATIENT.getRoleName()};
        Utilities.checkRoles(this,roles);
        PatientMeasurement pm = getPatientMeasurementIfExists(patientMeasurementRepository,id);
        patientMeasurementRepository.deleteById(id);
    }

    /**
     * This method is used to update a patient measurement according to id
     * @param pmRepr
     * @return
     * @throws NotFoundException
     * @throws BadEntityException
     */
    @Override
    public PatientMeasurementRepresentation update(PatientMeasurementRepresentation pmRepr) throws NotFoundException, BadEntityException {
       String[] roles = {SacchonRole.ROLE_PATIENT.getRoleName()};
       Utilities.checkRoles(this,roles);
       PatientMeasurement pm = getPatientMeasurementIfExists(patientMeasurementRepository,id);
       pm.setCarbIntake(pmRepr.getCarbIntake());
       pm.setGlucoseLevel(pmRepr.getGlucoseLevel());
       patientMeasurementRepository.save(pm);
       return PatientMeasurementRepresentation.getPatientMeasurementRepresentation(pm);

    }


    /**
     * This method is used to a get a patient measurement if exists
     * @param pmRepository
     * @param id
     * @return
     * @throws NotFoundException
     */
    public PatientMeasurement getPatientMeasurementIfExists(PatientMeasurementRepository pmRepository, long id) throws NotFoundException {
        Optional<PatientMeasurement> patientMeasurement = patientMeasurementRepository.findById(id);
        setExisting(patientMeasurement.isPresent());
        if (!patientMeasurement.isPresent())  throw new NotFoundException("Patient Measurement with id: " + id + " not found");
        return patientMeasurement.get();
    }

}
