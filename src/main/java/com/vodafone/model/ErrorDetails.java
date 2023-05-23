package com.vodafone.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDetails {
    private String code;
    private String message;
    private String url;

}
