package com.sacchon.restapi.resource.interfaces.doctor;

import com.sacchon.restapi.exceptions.BadEntityException;
import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.representations.doctor.DoctorRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;

import java.util.List;

public interface DoctorListResource {


    @Post("json")
    public DoctorRepresentation add(DoctorRepresentation doctorRepresentation)
            throws BadEntityException;
    @Get("json")
    public List<DoctorRepresentation> getDoctors() throws NotFoundException, BadRequestException;

}
