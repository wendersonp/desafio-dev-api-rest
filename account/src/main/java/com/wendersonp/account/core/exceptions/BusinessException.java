package com.wendersonp.account.core.exceptions;

import com.wendersonp.account.util.ExceptionMessageEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{


    public BusinessException(ExceptionMessageEnum exceptionMessageEnum) {
        super(exceptionMessageEnum.getMessage());
    }
}
