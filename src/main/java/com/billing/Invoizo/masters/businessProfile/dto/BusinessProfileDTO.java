package com.billing.Invoizo.masters.businessProfile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessProfileDTO implements Serializable {

    private String id;
    private String businessName;
    private String ownerName;
    private String email;
    private String phoneNumber;
    private String alternatePhone;
    private String address;
    private String city;
    private String state;
    private String pinCode;
    private String gstNumber;
    private String panNumber;
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String branchName;

}
