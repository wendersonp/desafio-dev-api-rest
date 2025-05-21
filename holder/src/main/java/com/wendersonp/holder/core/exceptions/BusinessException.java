package com.wendersonp.holder.core.exceptions;

import com.wendersonp.holder.util.ExceptionMessageEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{


    public BusinessException(ExceptionMessageEnum exceptionMessageEnum) {
        super(exceptionMessageEnum.getMessage());
    }
}
