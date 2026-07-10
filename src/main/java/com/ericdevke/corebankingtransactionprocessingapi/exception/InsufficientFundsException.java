package com.ericdevke.corebankingtransactionprocessingapi.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message){
        super(message);
    }
}
