package com.sacchon.restapi.resource.interfaces.patient;

import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.representations.patient.PatientMeasurementsAvgRepresentation;
import org.restlet.resource.Get;

import java.util.Map;

public interface PatientMeasurementsStatsResource {

    @Get("json")
    public Map<String,PatientMeasurementsAvgRepresentation> getPatientMeasurementsStatistics() throws NotFoundException, BadRequestException;
}
