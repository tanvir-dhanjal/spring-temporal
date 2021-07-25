package com.codejam.error;

import com.google.common.base.Strings;

public class ServiceException extends RuntimeException {
    private final ServiceErrorsEnum serviceErrorsEnum;

    public ServiceException(final ServiceErrorsEnum serviceErrorsEnum, String message) {
        super(message);
        this.serviceErrorsEnum = serviceErrorsEnum;
        if(!Strings.isNullOrEmpty(message)) {
            serviceErrorsEnum.message = message;
        }
    }

    public ServiceException(final ServiceErrorsEnum serviceErrorsEnum) {
        super(serviceErrorsEnum.getMessage());
        this.serviceErrorsEnum = serviceErrorsEnum;
    }

    public ServiceErrorsEnum getServiceErrorsEnum() {
        return serviceErrorsEnum;
    }
}
