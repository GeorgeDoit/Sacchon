package com.sacchon.restapi.representations.doctor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sacchon.restapi.model.Doctor;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class DoctorRepresentation {

    private String name;
    private String surname;
    private String password;
    private String username;
    private long id;
    private String uri;

    static public Doctor getDoctor(DoctorRepresentation doctorRepresentation){
        Doctor doctor = new Doctor();
        doctor.setName(doctorRepresentation.getName());
        doctor.setSurname(doctorRepresentation.getSurname());
        doctor.setUsername(doctorRepresentation.getUsername());
        return doctor;
    }

    static public DoctorRepresentation getDoctorRepresentation(Doctor doctor){
        DoctorRepresentation doctorRepresentation = new DoctorRepresentation();
        doctorRepresentation.setId(doctor.getId());
        doctorRepresentation.setName(doctor.getName());
        doctorRepresentation.setSurname(doctor.getSurname());
        doctorRepresentation.setUsername(doctor.getUsername());
        return doctorRepresentation;
    }


}
