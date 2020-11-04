package com.sacchon.restapi.resource.interfaces.user;

import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.representations.UserRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.List;

public interface UserListResource {
    @Post("json")
    public UserRepresentation add(UserRepresentation userRepresentation)
            throws BadEntityException;

    @Get("json")
    public List<UserRepresentation> getUsers()
            throws NotFoundException;

}