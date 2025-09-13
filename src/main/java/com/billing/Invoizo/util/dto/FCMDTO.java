package com.billing.Invoizo.util.dto;

import lombok.Data;

import java.util.List;

@Data
public class FCMDTO {
    private String title;
    private String body;
    private String imageUrl;
    private List<String> regIds;
    private String moduleName;
    private String action;
    private String winCode;
    private String referenceNo;
    private String nextApproverEmployeeDesignationId;

    public FCMDTO(String title, String body, List<String> regIds, String moduleName, String action, String winCode,
                  String referenceNo, String nextApproverEmployeeDesignationId) {
        super();
        this.title = title;
        this.body = body;
        this.regIds = regIds;
        this.moduleName = moduleName;
        this.action = action;
        this.winCode = winCode;
        this.referenceNo = referenceNo;
        this.nextApproverEmployeeDesignationId = nextApproverEmployeeDesignationId;
    }


}
