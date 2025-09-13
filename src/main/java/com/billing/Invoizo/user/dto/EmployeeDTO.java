package com.billing.Invoizo.user.dto;

import com.billing.Invoizo.masters.designation_permission.entity.DesignationPermissionEntity;
import com.billing.Invoizo.user.entity.EmployeeULBDesignationEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.billing.Invoizo.masters.designations.entity.DesignationsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO implements Serializable {
    private List<EmployeeULBDesignationEntity> employeeULBDesignationEntities;
    private String designation;
    private String permissions;
    private String profilePicture;
    private String employeeId;
    private int designationId;
    private DesignationsEntity designationsEntity;
    private Set<DesignationPermissionEntity> designationPermissionEntities;
    private String name;
    private String password;
    private String mobileNumber;
    private String gender;
    private String email;
    private String pan;
    private String photoPath;
    private String createdBy;
    private Date createdAt;
    private String modifiedBy;
    private Date modifiedAt;
    private boolean enabled;
    private boolean isDeleted;
    private boolean isTransferred;
    private boolean hasWritePermission;
    private String ipAddress;
    private Date cloudLastLog;
    private Date mobileLastLog;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private String otp;
    private String otpMobileNumber;
    private Date otpVerifiedAt;
    private Date otpExpiresAt;
    private boolean hasAgreedTerms;
    private String regId;
    private String deviceModel;
    private String mobileVersion;
    private String mobileSessionId;
    private byte mobileLoginStatus;
    private String aadharNumber;
    private String address;
    private String versionName;
    private String deviceId;
    private String ulbCode;
    private String ulbName;
    private List<Map<String, Object>> employeeULBDesignations;
    private String oldPhotoPath;
    private String designationName;
    private String departmentName;
    private String token;
    private String mobileIpAddress;
    private String mobileLastLocation;
    private Date lastSyncTime;
    private String transferOtp;
    private Date transferOtpExpiresAt;
    private Date lockedTime;
    private Date lastFailed;
    private Integer failedAttempts;
    private boolean isActive;
    private String memberCode;
    @JsonProperty
    private boolean isFromMobile;
    private Integer hierarchy;
    private boolean isEligibleForIncentives;
    private List<Integer> areaIds;
    private String designationAbbreviation;
    private String accountNumber;
    private String ifscCode;
    private String branchName;
    private String bankName;
    @JsonProperty
    private boolean isShgHead;
    private List<String> shgCode;
    private String bankBranchCode;
    private String bankCode;
    private Date accountOpenDate;



    public EmployeeDTO(
            String employeeId,
            int designationId,
            String name,
            String password,
            String mobileNumber,
            String gender,
            String email,
            String pan,
            String photoPath,
            String createdBy,
            Date createdAt,
            String modifiedBy,
            Date modifiedAt,
            boolean enabled,
            boolean isDeleted,
            boolean isTransferred,
            boolean hasWritePermission,
            String ipAddress,
            Date cloudLastLog,
            Date mobileLastLog,
            boolean accountNonExpired,
            boolean accountNonLocked,
            boolean credentialsNonExpired,
            String otp,
            String otpMobileNumber,
            Date otpVerifiedAt,
            Date otpExpiresAt,
            boolean hasAgreedTerms,
            String regId,
            String deviceModel,
            String mobileVersion,
            String mobileSessionId,
            byte mobileLoginStatus,
            String aadharNumber,
            String address,
            String ulbCode,
            String mobileIpAddress,
            String mobileLastLocation,
            Date lastSyncTime,
            String transferOtp,
            Date transferOtpExpiresAt,
            Date lockedTime,
            Date lastFailed,
            Integer failedAttempts,
            boolean isActive,
            String memberCode,
            Integer hierarchy,
            boolean isEligibleForIncentives,
            List<String> shgCode,
            String designationName,
            String designationAbbreviation,
            String accountNumber,
            String ifscCode,
            String branchName,
            String bankName
    ) {
        this.employeeId = employeeId;
        this.designationId = designationId;
        this.name = name;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.gender = gender;
        this.email = email;
        this.pan = pan;
        this.photoPath = photoPath;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.modifiedBy = modifiedBy;
        this.modifiedAt = modifiedAt;
        this.enabled = enabled;
        this.isDeleted = isDeleted;
        this.isTransferred = isTransferred;
        this.hasWritePermission = hasWritePermission;
        this.ipAddress = ipAddress;
        this.cloudLastLog = cloudLastLog;
        this.mobileLastLog = mobileLastLog;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.otp = otp;
        this.otpMobileNumber = otpMobileNumber;
        this.otpVerifiedAt = otpVerifiedAt;
        this.otpExpiresAt = otpExpiresAt;
        this.hasAgreedTerms = hasAgreedTerms;
        this.regId = regId;
        this.deviceModel = deviceModel;
        this.mobileVersion = mobileVersion;
        this.mobileSessionId = mobileSessionId;
        this.mobileLoginStatus = mobileLoginStatus;
        this.aadharNumber = aadharNumber;
        this.address = address;
        this.ulbCode = ulbCode;
        this.mobileIpAddress = mobileIpAddress;
        this.mobileLastLocation = mobileLastLocation;
        this.lastSyncTime = lastSyncTime;
        this.transferOtp = transferOtp;
        this.transferOtpExpiresAt = transferOtpExpiresAt;
        this.lockedTime = lockedTime;
        this.lastFailed = lastFailed;
        this.failedAttempts = failedAttempts;
        this.isActive = isActive;
        this.memberCode = memberCode;
        this.hierarchy = hierarchy;
        this.isEligibleForIncentives = isEligibleForIncentives;
        this.shgCode = shgCode;
        this.designationName = designationName;
        this.designationAbbreviation = designationAbbreviation;
        this.accountNumber = accountNumber;
        this.ifscCode = ifscCode;
        this.branchName = branchName;
        this.bankName = bankName;
    }
}
