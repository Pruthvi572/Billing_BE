package com.billing.Invoizo.masters.designations.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesignationViewDTO implements Serializable {

    private Map<String, Object> designations;

    private List<Map<String, Object>> cloudPermissions;

    private List<Map<String, Object>> mobilePermissions;


}
