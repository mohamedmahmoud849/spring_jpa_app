package com.vodafone.errorhandlling;

import org.springframework.http.HttpStatus;

public class DuplicateEntity extends APIException{
    public DuplicateEntity(String message){
        super(message);
    }
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
