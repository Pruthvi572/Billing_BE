package com.billing.Invoizo.security.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.json.simple.JSONArray;

import java.util.Date;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginResponse {
    private String accessToken;
    private String type = "Bearer";
    private String refreshToken;
    private List<String> modules;
    private List<String> permissions;
    private String districtId;
    private String employeeDesignationId;
    private String employeeId;
    private String designationName;
    private int designationId;
    private String name;
    private String policeStation;
    private String email;
    private String message;
    private int categoryId;
    private JSONArray circles;
//    private List<MasterLabelsEntity> masterLabels;
    private String districtName;
    private String address;
    private Date lastLoggedInAt;
}
