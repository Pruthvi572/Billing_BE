package com.billing.Invoizo.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeListDTO implements Serializable {
    private String employeeId;
    private String name;
    private String mobileNumber;
    private String gender;
    private String designationName;
}
