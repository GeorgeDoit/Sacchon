package com.sacchon.restapi.resource.impl.user;

import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.model.UserTable;
import com.sacchon.restapi.repository.UserTableRepository;
import com.sacchon.restapi.repository.util.JpaUtil;
import com.sacchon.restapi.representations.UserRepresentation;
import com.sacchon.restapi.resource.interfaces.user.UserListResource;
import com.sacchon.restapi.security.SacchonRole;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

public class UserListResourceImpl extends ServerResource implements UserListResource {

    private UserTableRepository userRepository ;
    private EntityManager em;

    @Override
    protected void doInit() {
        try {
            em = JpaUtil.getEntityManager();
            userRepository = new UserTableRepository(em);
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
     * This method used to create a new user
     * @param userRepresentation
     * @return
     * @throws BadEntityException
     */
    @Override
    public UserRepresentation add(UserRepresentation userRepresentation) throws BadEntityException {
        if (userRepresentation == null) throw new BadEntityException("Null consult representation error");
        UserTable user = UserRepresentation.getUser(userRepresentation);
        userRepository.save(user);
        return UserRepresentation.getUserRepresentation(user);
    }

    /**
     * This method is used to get all users
     * @return
     * @throws NotFoundException
     */
    @Override
    public List<UserRepresentation> getUsers() throws NotFoundException {

       /* List<String> roleList = new ArrayList<>();
        roleList.add(SacchonRole.ROLE_DOCTOR.getRoleName());*/

        List<UserTable> users = userRepository.findAll();
        List<UserRepresentation> userRepresentationList = new ArrayList<>();
        users.forEach(user -> userRepresentationList.add(UserRepresentation.getUserRepresentation(user)));

        return userRepresentationList;

    }
}