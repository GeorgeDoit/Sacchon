package com.sacchon.restapi.representations.patient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sacchon.restapi.model.Gender;
import com.sacchon.restapi.model.Patient;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PatientStatsSubsRepresentation  {
        private long subs;

        static public PatientStatsSubsRepresentation getPatientSubsRepresentation(long subs){
            PatientStatsSubsRepresentation patientRepresentation = new PatientStatsSubsRepresentation();
            patientRepresentation.setSubs(subs);
            return patientRepresentation;
        }


}
