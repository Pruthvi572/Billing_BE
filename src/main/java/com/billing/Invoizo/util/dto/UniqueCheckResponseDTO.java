package com.billing.Invoizo.util.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UniqueCheckResponseDTO implements Serializable {
    @JsonProperty("isPresentInDB")
    private boolean isPresentInDB;
}
