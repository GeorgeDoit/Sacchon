package com.sacchon.restapi.exceptions;

import org.restlet.resource.Status;

import java.util.logging.Level;
import java.util.logging.Logger;

@Status(value = 500, serialize = true)
public class NotFoundException extends Exception{
    Logger LOGGER = Logger.getLogger(NotFoundException.class.getName());
    public NotFoundException(String message) {
        super(message);
        LOGGER.log(Level.SEVERE,message);
    }
}
