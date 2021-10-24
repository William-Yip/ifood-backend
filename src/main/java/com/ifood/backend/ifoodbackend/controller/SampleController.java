package com.ifood.backend.ifoodbackend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("ifood")
public class SampleController {

    @GetMapping
    public ResponseEntity test() {
        return  ResponseEntity.ok().build();
    }

}
