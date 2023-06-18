package com.supinfo.jee.casino.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GamblerInputDto {
    private String pseudo;
    private String password;
}
