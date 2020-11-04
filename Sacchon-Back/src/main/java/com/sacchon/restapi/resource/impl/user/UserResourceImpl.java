package com.sacchon.restapi.resource.impl.user;


import com.sacchon.restapi.common.Constants;
import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;

import com.sacchon.restapi.model.UserTable;
import com.sacchon.restapi.repository.UserTableRepository;
import com.sacchon.restapi.repository.util.JpaUtil;
import com.sacchon.restapi.representations.UserRepresentation;
import com.sacchon.restapi.resource.interfaces.user.UserResource;
import com.sacchon.restapi.security.SacchonRole;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserResourceImpl extends ServerResource implements UserResource {

    private UserTableRepository userTableRepository ;
    private EntityManager em;
    private long id;

    @Override
    protected void doInit() {
        try {
            em = JpaUtil.getEntityManager();
            userTableRepository = new UserTableRepository(em);
            //id = Long.parseLong(getAttribute("id"));
        }
        catch(Exception ex){

            throw new ResourceException(ex);

        }
    }

    @Override
    protected void doRelease() {
        em.close();
    }

    @Override
    public UserRepresentation getUser() throws NotFoundException, ResourceException, BadRequestException {
        /*List<String> roleList = new ArrayList<>();
        roleList.add(SacchonRole.ROLE_ADMIN.getRoleName());*/

        String username = getQueryValue(Constants.QV_USERNAME);
        if(username==null) throw new BadRequestException("The request you provided is wrong");
        String password = getQueryValue(Constants.QV_PASSWORD);
        if(password==null) throw new BadRequestException("The request you provided is wrong");

        Optional<UserTable> user = userTableRepository.findUserInstance(username,password);
        if (!user.isPresent())  throw new NotFoundException("User not exists");

        UserRepresentation userRepresentation = UserRepresentation.getUserRepresentation(user.get());
        return userRepresentation;
    }

}