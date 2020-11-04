package com.sacchon.restapi.common;

import com.sacchon.restapi.resource.util.ResourceUtils;
import com.sacchon.restapi.security.SacchonRole;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;
import java.util.List;

public class Utilities {


    public static void checkRoles(ServerResource serverResource,String[] roles){
        List<String> roleList = new ArrayList<>();
        for(String r : roles){
            roleList.add(r);
        }
        ResourceUtils.checkRole(serverResource, roleList);
    }
}
