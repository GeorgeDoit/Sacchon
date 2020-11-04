package com.sacchon.restapi.resource.impl.patient;

import com.sacchon.restapi.common.Constants;
import com.sacchon.restapi.common.Utilities;
import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.repository.patient.PatientMeasurementRepository;
import com.sacchon.restapi.repository.patient.PatientRepository;
import com.sacchon.restapi.repository.util.JpaUtil;
import com.sacchon.restapi.representations.patient.PatientStatsSubsRepresentation;
import com.sacchon.restapi.resource.interfaces.patient.PatientStatsSubsResource;
import com.sacchon.restapi.resource.util.ResourceUtils;
import com.sacchon.restapi.security.SacchonRole;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientStatsSubsResourceImpl extends ServerResource implements PatientStatsSubsResource {
    private PatientMeasurementRepository patientMeasurementRepository ;
    private PatientRepository patientRepository ;
    private EntityManager em;
    private long dateFrom=0;
    private long dateTo=0;
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
     * This method is used to get the information submissions(stored data) of a patient over a time range
     * @return
     * @throws NotFoundException
     * @throws BadRequestException
     */
    @Override
    public PatientStatsSubsRepresentation getPatientStatsSubs() throws NotFoundException, BadRequestException {
        String[] roles = {SacchonRole.ROLE_ADMIN.getRoleName()};
        //Utilities.checkRoles(this,roles);
        String patient_id = getQueryValue(Constants.QV_PATIENT_ID);
        String from = getQueryValue(Constants.QV_FROM);
        String to = getQueryValue(Constants.QV_TO);
        if(from != null && to !=null){
            dateFrom = Long.parseLong(from);
            dateTo = Long.parseLong(to);
        }
        if(patient_id==null) throw new BadRequestException("Required patient id when getting patient submissions stats");
        long pid = Long.parseLong(patient_id);
        Patient patient = getPatientIfExists(patientRepository,pid);
        long subs = patientMeasurementRepository.findPatientStatsSubs(pid,dateFrom,dateTo);
        PatientStatsSubsRepresentation patientStatsSubsRepresentation = PatientStatsSubsRepresentation.getPatientSubsRepresentation(subs);
        return patientStatsSubsRepresentation;
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
        if (!patient.isPresent())  throw new NotFoundException("Patient with id: " + id + "not found");
        setExisting(patient.isPresent());
        return patient.get();
    }

}

