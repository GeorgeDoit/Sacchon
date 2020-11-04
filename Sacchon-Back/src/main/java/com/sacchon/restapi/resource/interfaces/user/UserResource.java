package com.sacchon.restapi.resource.interfaces.user;

import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.representations.UserRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface UserResource {

    @Get("json")
    public UserRepresentation getUser() throws NotFoundException, BadRequestException;

}