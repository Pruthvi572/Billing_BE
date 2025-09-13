package com.billing.Invoizo.masters.designations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DesignationDTO implements Serializable {
    private int id;
    private String name;
    private String abbreviation;
    private int hierarchy;
    private int designationOrder;
}
