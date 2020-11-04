package com.sacchon.restapi.resource.interfaces.patient;

import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.representations.patient.PatientMeasurementRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface PatientMeasurementResource {

    @Get("json")
    public PatientMeasurementRepresentation getPatientMeasurement() throws NotFoundException;

    @Delete
    public void remove() throws NotFoundException;

    @Put("json")
    public PatientMeasurementRepresentation update(PatientMeasurementRepresentation pmRepr)
            throws NotFoundException, BadEntityException;

}
