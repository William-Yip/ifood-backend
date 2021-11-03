package com.ifood.backend.ifoodbackend.validator;

import com.ifood.backend.ifoodbackend.dto.CityQuery;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class CityQueryValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(CityQuery.class);
    }

    private static final String INVALID_PARAMETER = "invalid parameter";

    @Override
    public void validate(Object target, Errors errors) {

        CityQuery cityQuery = (CityQuery) target;

        String name = cityQuery.getName();

        Boolean validName = Objects.nonNull(name) && name.trim().isEmpty();
//        Boolean validName = Objects.nonNull(name) && !name.trim().isEmpty();
        Double lat = cityQuery.getLat();
        Double lon = cityQuery.getLon();


        if ( !validName && Objects.isNull(lat) && Objects.isNull(lon) ) {
            errors.reject(INVALID_PARAMETER, "must provide city's name or its coordinates(lat and lon)");
            return;
        }

        // if name is not provided check for lat and lon parameters
        if (!validName) {
            if ( Objects.isNull(lat) || Objects.isNull(lon) ) {
                errors.reject(INVALID_PARAMETER, "both values must be provided (lat and lon)");
            }
        }

    }

}