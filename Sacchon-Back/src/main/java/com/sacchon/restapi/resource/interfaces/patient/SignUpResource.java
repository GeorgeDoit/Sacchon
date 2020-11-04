package com.sacchon.restapi.resource.interfaces.patient;

import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.representations.patient.PatientRepresentation;
import org.restlet.resource.Post;

public interface SignUpResource {
    @Post("json")
    public PatientRepresentation add(PatientRepresentation patientRepresentation)
            throws BadEntityException;
}
