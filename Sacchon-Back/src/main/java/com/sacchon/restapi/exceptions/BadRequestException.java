package com.sacchon.restapi.exceptions;


import org.restlet.resource.Status;

import java.util.logging.Level;
import java.util.logging.Logger;

@Status(value = 500, serialize = true)
public class BadRequestException extends Exception{

    Logger LOGGER = Logger.getLogger(BadRequestException.class.getName());
    public BadRequestException(String message) {
        super(message);
        LOGGER.log(Level.SEVERE,message);
    }
}
