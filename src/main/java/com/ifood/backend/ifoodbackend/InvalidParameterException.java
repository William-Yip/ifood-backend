package com.ifood.backend.ifoodbackend;


import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class InvalidParameterException extends RuntimeException {

    private List<String> fields = new ArrayList<>();
    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public InvalidParameterException(String simpleError) {
        fields.add(simpleError);
    }

    public InvalidParameterException(Errors errors) {

        List<ObjectError> allErrors = errors.getAllErrors();

        allErrors.forEach(error -> fields.add(error.getDefaultMessage()) );
    }

    public List<String> getFields() {
        return fields;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

}