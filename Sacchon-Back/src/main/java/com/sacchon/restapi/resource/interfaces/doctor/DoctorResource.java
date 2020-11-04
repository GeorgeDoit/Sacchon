package com.sacchon.restapi.resource.interfaces.doctor;

import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.representations.doctor.DoctorRepresentation;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Put;

public interface DoctorResource {

    @Get("json")
    public DoctorRepresentation getDoctor() throws NotFoundException;

    @Delete
    public void remove() throws NotFoundException;

    @Put
    DoctorRepresentation update(DoctorRepresentation doctorRepresentation) throws NotFoundException;

}
