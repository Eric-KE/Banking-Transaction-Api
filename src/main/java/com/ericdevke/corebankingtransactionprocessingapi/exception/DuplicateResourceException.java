package com.ericdevke.corebankingtransactionprocessingapi.exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException (String message){
        super(message);
    }
}
