package com.wendersonp.account.infrastructure.exception;

import com.wendersonp.account.util.ExceptionMessageEnum;

public class AWSException extends RuntimeException {

    public AWSException(ExceptionMessageEnum exceptionMessageEnum) {
        super(exceptionMessageEnum.getMessage());
    }
}
