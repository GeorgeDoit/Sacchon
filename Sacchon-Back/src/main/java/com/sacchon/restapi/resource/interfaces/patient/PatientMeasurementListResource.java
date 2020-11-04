package com.sacchon.restapi.resource.interfaces.patient;

import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.representations.patient.PatientMeasurementRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.List;

public interface PatientMeasurementListResource {

    @Post("json")
    public PatientMeasurementRepresentation add(PatientMeasurementRepresentation patientRepresentation)
            throws BadEntityException, NotFoundException;

    @Get("json")
    public List<PatientMeasurementRepresentation> getPatientMeasurements() throws NotFoundException, BadRequestException;
}
