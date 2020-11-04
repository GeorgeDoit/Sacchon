package com.sacchon.restapi.resource.interfaces.patient;

import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.representations.patient.PatientRepresentation;
import org.restlet.resource.Get;

import java.util.List;

public interface PatientReportResource {

    @Get("json")
    public List<PatientRepresentation> getPatients()
            throws NotFoundException, BadRequestException;

}
