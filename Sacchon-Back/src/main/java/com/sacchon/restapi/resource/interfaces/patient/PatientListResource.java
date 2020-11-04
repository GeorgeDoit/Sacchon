package com.sacchon.restapi.resource.interfaces.patient;

import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.representations.patient.PatientRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.List;

public interface PatientListResource {

    @Get("json")
    public List<PatientRepresentation> getPatients()
            throws NotFoundException, BadRequestException;


}
