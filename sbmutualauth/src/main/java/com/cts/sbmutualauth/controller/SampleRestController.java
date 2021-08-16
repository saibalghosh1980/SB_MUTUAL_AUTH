package com.cts.sbmutualauth.controller;

import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class SampleRestController {

    @PreAuthorize("hasAuthority('ROLE_USER')")
    @GetMapping(value = "/hello",produces = MediaType.APPLICATION_JSON_VALUE)
    public String sayHello(){
        log.debug("Inside say hello!!!!");
        return "{message='Hello World!!!!'}";
    }
    
}
