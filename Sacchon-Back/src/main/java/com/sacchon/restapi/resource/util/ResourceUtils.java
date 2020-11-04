package com.sacchon.restapi.resource.util;

import org.restlet.data.Status;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.List;

public class ResourceUtils {

    /**
     * Indicates if the authenticated client user associated to the current
     * request is in the given role name.
     *
     * @param serverResource    *   The current server resource.
     * @param roles     *            The role to check.
     * @throws ResourceException
     *             In case the current authenticated user has not sufficient
     *             permission.
     */
    public static void checkRole(ServerResource serverResource, List<String> roles)
            throws ResourceException {

                boolean flag = false;

                for (String role: roles){
                    if (serverResource.isInRole(role)) {
                        flag = true;
                    }
                }
                if(!flag) {
                    throw new ResourceException(
                            Status.CLIENT_ERROR_FORBIDDEN.getCode(),
                            "You're not authorized to send this call.");
                }
    }

}
