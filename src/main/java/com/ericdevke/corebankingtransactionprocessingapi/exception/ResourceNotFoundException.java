package com.ericdevke.corebankingtransactionprocessingapi.exception;

public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message){
        super(message);
    }
}
