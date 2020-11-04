package com.sacchon.restapi.representations.patient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sacchon.restapi.model.Patient;
import com.sacchon.restapi.model.PatientMeasurement;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PatientMeasurementRepresentation {

    private long id;
    private double glucoseLevel;
    private int carbIntake;
    private Date dateStored;

    static public PatientMeasurement getPatientMeasurement(PatientMeasurementRepresentation patientMeasurementRepresentation,Patient patient){
        PatientMeasurement patientMeasurement = new PatientMeasurement();
        patientMeasurement.setCarbIntake(patientMeasurementRepresentation.getCarbIntake());
        patientMeasurement.setGlucoseLevel(patientMeasurementRepresentation.getGlucoseLevel());
        patientMeasurement.setPatient(patient);
        Date date = new Date();
        patientMeasurement.setDateStored(date);

        return patientMeasurement;
    }

    static public PatientMeasurementRepresentation getPatientMeasurementRepresentation(PatientMeasurement pm){
        PatientMeasurementRepresentation patientMeasurementRepresentation = new PatientMeasurementRepresentation();

        patientMeasurementRepresentation.setGlucoseLevel(pm.getGlucoseLevel());
        patientMeasurementRepresentation.setCarbIntake(pm.getCarbIntake());
        patientMeasurementRepresentation.setDateStored(pm.getDateStored());
        patientMeasurementRepresentation.setId(pm.getId());

        return patientMeasurementRepresentation;
    }
}
