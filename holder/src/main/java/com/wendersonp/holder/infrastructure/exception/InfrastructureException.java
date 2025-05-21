package com.wendersonp.holder.infrastructure.exception;

import com.wendersonp.holder.util.ExceptionMessageEnum;

public class InfrastructureException extends RuntimeException{

    public InfrastructureException(ExceptionMessageEnum exceptionMessageEnum, Throwable cause) {
        super(exceptionMessageEnum.getMessage(), cause);
    }

}
