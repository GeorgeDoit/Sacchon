package com.sacchon.restapi.representations.patient;



import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PatientMeasurementsAvgRepresentation {

    private double avrgCarb;
    private double avrgGlucoseLevel;

}
