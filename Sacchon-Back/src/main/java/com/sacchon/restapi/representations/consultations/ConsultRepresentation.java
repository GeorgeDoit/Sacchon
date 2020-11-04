package com.sacchon.restapi.representations.consultations;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sacchon.restapi.model.Consult;
import com.sacchon.restapi.model.Doctor;
import com.sacchon.restapi.model.Patient;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ConsultRepresentation {

    private long id;
    private String title;
    private String description;
    private Date dateStored;
    private boolean seen;

    static public Consult getConsult(ConsultRepresentation consultRepresentation, Patient patient, Doctor doctor){
        Consult consult = new Consult();
        consult.setTitle(consultRepresentation.getTitle());
        consult.setDescription(consultRepresentation.getDescription());
        Date date = new Date();
        consult.setDateStored(date);
        consult.setDoctor_consults(doctor);
        consult.setPatientToConsult(patient);
        consult.setSeen(consultRepresentation.isSeen());

        return consult;
    }

    static public ConsultRepresentation getConsultRepresentation(Consult consult){
        ConsultRepresentation consultRepresentation = new ConsultRepresentation();
        consultRepresentation.setTitle(consult.getTitle());
        consultRepresentation.setDescription(consult.getDescription());
        consultRepresentation.setId(consult.getId());
        consultRepresentation.setSeen(consult.isSeen());
        consultRepresentation.setDateStored(consult.getDateStored());
        return consultRepresentation;
    }

}
