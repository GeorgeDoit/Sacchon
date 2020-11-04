package com.sacchon.restapi.resource.interfaces.consultations;


import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.representations.consultations.ConsultRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;


public interface ConsultResource {

    @Get("json")
    ConsultRepresentation getConsult() throws NotFoundException;

    @Put
    ConsultRepresentation update(ConsultRepresentation consultRepresentation)
            throws Exception;
}
