package com.sacchon.restapi.resource.interfaces.patient;

import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.representations.patient.PatientStatsSubsRepresentation;
import org.restlet.resource.Get;

public interface PatientStatsSubsResource {

    @Get("json")
    public PatientStatsSubsRepresentation getPatientStatsSubs() throws NotFoundException, BadRequestException;
}
