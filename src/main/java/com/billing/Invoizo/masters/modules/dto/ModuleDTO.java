package com.billing.Invoizo.masters.modules.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleDTO implements Serializable {

    private String constant;

    private String moduleName;
}
