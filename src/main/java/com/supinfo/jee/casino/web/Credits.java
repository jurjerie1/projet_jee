package com.supinfo.jee.casino.web;

import com.supinfo.jee.casino.api.CreditsInputDto;
import com.supinfo.jee.casino.api.CreditsOutputDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Credits {
    private int amount;
    private String pseudo;

}
