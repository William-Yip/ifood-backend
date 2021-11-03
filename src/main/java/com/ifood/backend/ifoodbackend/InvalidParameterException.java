package com.ifood.backend.ifoodbackend;


import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

public class InvalidParameterException extends RuntimeException {

    private List<String> fields;

    public InvalidParameterException(Errors errors) {
        fields = new ArrayList<>();

        List<ObjectError> allErrors = errors.getAllErrors();

        allErrors.forEach(error -> fields.add(error.getDefaultMessage()) );
    }

    public List<String> getFields() {
        return fields;
    }

}