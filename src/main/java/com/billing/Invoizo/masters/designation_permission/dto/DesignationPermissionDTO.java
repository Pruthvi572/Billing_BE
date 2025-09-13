package com.billing.Invoizo.masters.designation_permission.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesignationPermissionDTO implements Serializable {

    private String name;
    private String permissions;
    private int designationId;
    private int type;
    private String title;
    private String module;
    private String moduleName;
    @JsonProperty
    private boolean isAssigned;
    private String description;
    @JsonProperty
    private boolean isEnabled;
    private int permissionOrder;

}
