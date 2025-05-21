package com.wendersonp.account.infrastructure.exception;

import com.wendersonp.account.util.ExceptionMessageEnum;

public class InfrastructureException extends RuntimeException{

    public InfrastructureException(ExceptionMessageEnum exceptionMessageEnum, Throwable cause) {
        super(exceptionMessageEnum.getMessage(), cause);
    }

}
