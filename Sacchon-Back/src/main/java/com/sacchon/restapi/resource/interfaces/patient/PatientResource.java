package com.sacchon.restapi.resource.interfaces.patient;


import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.representations.patient.PatientMeasurementRepresentation;
import com.sacchon.restapi.representations.patient.PatientRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface PatientResource {

    @Get("json")
    public PatientRepresentation getPatient() throws NotFoundException;

    @Delete
    public void remove() throws NotFoundException;

    @Put("json")
    public PatientRepresentation update(PatientRepresentation patientRepresentation)
            throws NotFoundException, BadEntityException;


}
