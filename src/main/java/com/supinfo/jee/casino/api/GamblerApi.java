package com.supinfo.jee.casino.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "gameblerApi", url = "http://localhost:8081/")
public interface GamblerApi {
    @PostMapping("/register")
    LaunchOutputDto register(@RequestBody GamberInputDto newGamebler);

}
