package com.billing.Invoizo.initialsetup.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@AllArgsConstructor
@NoArgsConstructor
public class SetupFlagDTO implements Serializable {

    @JsonProperty
    private boolean setupFlag;

}
