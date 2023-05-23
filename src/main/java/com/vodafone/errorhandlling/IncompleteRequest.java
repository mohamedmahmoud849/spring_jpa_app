package com.vodafone.errorhandlling;

import org.springframework.http.HttpStatus;

public class IncompleteRequest extends APIException{
    public IncompleteRequest(String message) {
        super(message);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
