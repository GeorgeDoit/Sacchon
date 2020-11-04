package com.sacchon.restapi.representations.patient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sacchon.restapi.model.Gender;
import com.sacchon.restapi.model.Patient;
import lombok.Data;
import java.sql.Timestamp;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PatientRepresentation {
    private long id;
    private String name;
    private String surname;
    private Date dob;
    private long amka;
    private Gender gender;
    private boolean availableConsulted;
    private String username;
    private String password;
    private Date dateLastConsulted;
    private Date dateMonthStarted;
    private Date dateLastStored;
    private Date dateAvailableConsulted;
    private boolean warning;

    static public Patient getPatient(PatientRepresentation patientRepresentation){
        Patient patient = new Patient();
        patient.setName(patientRepresentation.getName());
        patient.setSurname(patientRepresentation.getSurname());
        patient.setDob(patientRepresentation.getDob());
        patient.setUsername(patientRepresentation.getUsername());
        patient.setGender(patientRepresentation.getGender());
        patient.setAmka(patientRepresentation.getAmka());
        Date date = new Date();
        patient.setDateAccountCreated(new Timestamp(date.getTime()));
        return patient;
    }

    static public PatientRepresentation getPatientRepresentation(Patient patient){
        PatientRepresentation patientRepresentation = new PatientRepresentation();
        patientRepresentation.setName(patient.getName());
        patientRepresentation.setSurname(patient.getSurname());
        patientRepresentation.setDob(patient.getDob());
        patientRepresentation.setUsername(patient.getUsername());
        patientRepresentation.setAmka(patient.getAmka());
        patientRepresentation.setGender(patient.getGender());
        patientRepresentation.setId(patient.getId());
        patientRepresentation.setAvailableConsulted(patient.isAvailableConsulted());
        patientRepresentation.setDateLastConsulted(patient.getDateLastConsulted());
        patientRepresentation.setDateLastStored(patient.getDateLastStored());
        patientRepresentation.setDateAvailableConsulted(patient.getDateAvailableConsulted());
        patientRepresentation.setWarning(patient.isWarning());
        return patientRepresentation;
    }
}
