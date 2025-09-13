package com.billing.Invoizo.mobile.dto;

import lombok.Data;
import org.json.simple.JSONArray;

import java.io.Serializable;
import java.util.List;
@Data
public class MobileLoginResponseEntity implements Serializable {
    private String employeeName;
    private String email;
    private String mobileNumber;
    private String address;
    private String photoPath;
    private List<String> permissions;
    private int status;
//    private List<EmployeeDesignationProjections> ulbList;
    private String employeeId;
    private List<String> employeeDesignationIds;
//    private List<MasterLabelsEntity> labels;
    private int designationId;
    private int categoryid;
    private String categoryName;
    private String districtId;
    private String password;
    private String token;
    private List<String> modules;
    private String policeStation;
    private JSONArray circles;
}