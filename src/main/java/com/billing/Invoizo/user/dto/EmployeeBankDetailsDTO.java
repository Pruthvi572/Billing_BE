package com.billing.Invoizo.user.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeBankDetailsDTO implements Serializable {

    private String bankDetailsId;
    private String bankName;
    private String ifscCode;
    private String branchName;
    private String accountNumber;
    private String bankBranchCode;
    private String bankCode;
    private Date accountOpenDate;

}
