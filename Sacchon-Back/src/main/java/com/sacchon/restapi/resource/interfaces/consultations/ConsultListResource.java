package com.sacchon.restapi.resource.interfaces.consultations;

import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.representations.consultations.ConsultRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.List;

public interface ConsultListResource {

    @Post("json")
    ConsultRepresentation add(ConsultRepresentation consultRepresentation) throws BadEntityException, NotFoundException, BadRequestException;

    @Get("json")
    List<ConsultRepresentation> getConsults() throws NotFoundException, BadRequestException;
}
