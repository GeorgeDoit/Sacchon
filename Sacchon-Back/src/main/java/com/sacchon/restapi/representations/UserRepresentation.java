package com.sacchon.restapi.representations;

import com.sacchon.restapi.model.Admin;
import com.sacchon.restapi.model.Doctor;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.model.UserTable;
import lombok.Data;

@Data
public class UserRepresentation {

    private String username;
    private String password;
    private String role;
    private boolean active;
    private long doctorId;
    private long patientId;
    private long adminId;

    //pairnei customerRepresentation gyrnaei Customer
    static public UserTable getUser(UserRepresentation userRepresentation) {
        UserTable user = new UserTable();

        user.setUsername(userRepresentation.getUsername());
        user.setPassword(userRepresentation.getPassword());
        user.setRole(userRepresentation.getRole());
        user.setActive(userRepresentation.isActive());

        return user;
    }


    static public UserRepresentation getUserRepresentation(UserTable user) {
        UserRepresentation userRepresentation = new UserRepresentation();

        userRepresentation.setUsername(user.getUsername());
        userRepresentation.setPassword(user.getPassword());
        userRepresentation.setRole(user.getRole());
        userRepresentation.setActive(user.isActive());
        if(user.getDoctor()!=null){
            userRepresentation.setDoctorId(user.getDoctor().getId());
        }
        if(user.getAdmin()!=null){
            userRepresentation.setAdminId(user.getAdmin().getId());
        }
        if(user.getPatient()!=null){
            userRepresentation.setPatientId(user.getPatient().getId());
        }

        return userRepresentation;
    }
}