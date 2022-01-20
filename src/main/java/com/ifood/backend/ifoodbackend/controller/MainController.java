package com.ifood.backend.ifoodbackend.controller;

import com.ifood.backend.ifoodbackend.InvalidParameterException;
import com.ifood.backend.ifoodbackend.dto.CityQuery;
import com.ifood.backend.ifoodbackend.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ifood")
public class MainController {

    @Autowired
    MainService mainService;

    @GetMapping
    public ResponseEntity findTracksByCity(
                               @RequestParam(required = false, name = "city_name") String cityName,
                               @RequestParam(required = false) Double lat,
                               @RequestParam(required = false) Double lon) throws Exception {

        CityQuery city = new CityQuery();

        city.setName(cityName);
        city.setLat(lat);
        city.setLon(lon);

        List<String> tracks = mainService.findTrackByCity(city);

        return ResponseEntity.ok(tracks);
    }

    @ExceptionHandler
    public ResponseEntity handleException (Exception ex) {

        if (ex instanceof InvalidParameterException) {
            InvalidParameterException invalidParameter = (InvalidParameterException) ex;

            return ResponseEntity.status(invalidParameter.getStatus()).body(invalidParameter.getFields());
        }

        return ResponseEntity.internalServerError().body("Unknow error");
    }

}
