package com.billing.Invoizo.masters.ulb.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ULBDTO implements Serializable {
    private String ulbCode;

    private String name;

    private String districtName;

    private String latitude;

    private String longitude;

    private String magnitude;

    private int gradeId;

    private String gradeName;

    private String licenseId;

    private String subClientId;

    private byte type;

    private String departmentGSTNumber;

    private String boundaryLatLng;

    private String schemeCode;

    private String agencyCode;

    private String agencyName;

    private String parentUlbCode;

    private String schemeHierarchy;

    private String schemeHierarchyLevelOrder;

    private String schemeHierarchyLocation;

    private String userNumber;

    private String bankName;

    private String branchName;

    private String bankAccountNumber;

    private String ifscCode;

    private String holdingAccountNumber;

    private String holdingAccountIfscCode;
}
