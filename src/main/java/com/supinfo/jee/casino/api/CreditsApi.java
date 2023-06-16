package com.supinfo.jee.casino.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@FeignClient(value = "credits", url = "http://localhost:8081/")
public interface CreditsApi {
    @PostMapping("/credits")
    @ResponseStatus(HttpStatus.CREATED)
    CreditsOutputDto payToWin(@RequestBody CreditsInputDto newCredits);
    
    }
