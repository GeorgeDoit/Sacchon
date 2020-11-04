package com.sacchon.restapi.resource.interfaces.consultations;

import com.sacchon.restapi.exceptions.BadRequestException;
import com.sacchon.restapi.exceptions.NotFoundException;
import com.sacchon.restapi.representations.consultations.ConsultsStatsRepresentation;
import org.restlet.resource.Get;

public interface ConsultsStatsResource {

    @Get("json")
    public ConsultsStatsRepresentation getStats() throws NotFoundException,BadRequestException;
}
