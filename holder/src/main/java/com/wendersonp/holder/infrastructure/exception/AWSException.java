package com.wendersonp.holder.infrastructure.exception;

import com.wendersonp.holder.util.ExceptionMessageEnum;

public class AWSException extends RuntimeException {

    public AWSException(ExceptionMessageEnum exceptionMessageEnum) {
        super(exceptionMessageEnum.getMessage());
    }
}
