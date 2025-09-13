package com.billing.Invoizo.user.entity;


import com.billing.Invoizo.masters.designation_permission.entity.DesignationPermissionEntity;
import com.billing.Invoizo.util.ArrayToStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.billing.Invoizo.masters.designations.entity.DesignationsEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@Entity
@Data
@Table(name = "user_employee")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeEntity implements Serializable, UserDetails {

    private static final long serialVersionUID = 1L;

    @Transient
    List<EmployeeULBDesignationEntity> employeeULBDesignationEntities;

    @Transient
    private String designation;

    @Transient
    private String permissions;

    @Transient
    private String profilePicture;

    @Id
    @Column(name = "employeeid", nullable = false, length = 255)
    private String employeeId;

    @Column(name = "designationid", nullable = false, columnDefinition = "int default 0")
    private int designationId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designationid", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private DesignationsEntity designationsEntity;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "designationid", referencedColumnName = "designationid", insertable = false, updatable = false)
    private Set<DesignationPermissionEntity> designationPermissionEntities;

    @Column(name = "name", length = 255)
    private String name;

    @JsonIgnoreProperties
    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "mobilenumber", length = 15)
    private String mobileNumber;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "pan", length = 50)
    private String pan;

    @Column(name = "photopath", length = 255)
    private String photoPath;

    @Column(name = "createdby", length = 255)
    private String createdBy;

    @Column(name = "createdat", nullable = true)
    private Date createdAt;

    @Column(name = "modifiedby", length = 255)
    private String modifiedBy;

    @Column(name = "modifiedat", nullable = true)
    private Date modifiedAt;

    @JsonProperty
    @Column(name = "enabled", nullable = false, columnDefinition = "boolean default false")
    private boolean enabled;

    @JsonProperty
    @Column(name = "isdeleted", nullable = false, columnDefinition = "boolean default false")
    private boolean isDeleted;

    @JsonProperty
    @Column(name = "istransferred", nullable = false, columnDefinition = "boolean default false")
    private boolean isTransferred;

    @JsonProperty
    @Column(name = "haswritepermission", nullable = false, columnDefinition = "boolean default false")
    private boolean hasWritePermission;

    @Column(name = "ipaddress", length = 255)
    private String ipAddress;

    @Column(name = "cloudlastlog", nullable = true)
    private Date cloudLastLog;

    @Column(name = "mobilelastlog", nullable = true)
    private Date mobileLastLog;

    // Spring security properties start
    @Column(name = "accountnonexpired", nullable = false, columnDefinition = "boolean default false")
    private boolean accountNonExpired;

    @Column(name = "accountnonlocked", nullable = false, columnDefinition = "boolean default false")
    private boolean accountNonLocked;

    @Column(name = "credentialsnonexpired", nullable = false, columnDefinition = "boolean default false")
    private boolean credentialsNonExpired;
    // Spring security properties end

    @Column(name = "otp", length = 15)
    private String otp;

    @Column(name = "otpmobilenumber", nullable = true)
    private String otpMobileNumber;

    @Column(name = "otpverifiedat", nullable = true)
    private Date otpVerifiedAt;

    @Column(name = "otpexpiresat", nullable = true)
    private Date otpExpiresAt;

    @JsonProperty
    @Column(name = "hasagreedterms", nullable = false, columnDefinition = "boolean default false")
    private boolean hasAgreedTerms;

    @Column(name = "regid", length = 255)
    private String regId;

    @Column(name = "devicemodel", length = 255)
    private String deviceModel;

    @Column(name = "mobileversion", length = 50)
    private String mobileVersion;

    @Column(name = "mobilesessionid", length = 255)
    private String mobileSessionId;

    @Column(name = "mobileloginstatus", nullable = false)
    private byte mobileLoginStatus;

    @Column(name = "aadharnumber", length = 15)
    private String aadharNumber;

    @Column(name = "address", length = 255)
    private String address;

    // for mobile setting to mobile version
    @Transient
    private String versionName;
    // for mobile setting to mobileSessionId
    @Transient
    private String deviceId;

    // being used for initialSetip
    @Column(name = "ulbcode")
    private String ulbCode;

    @Transient
    private String ulbName;

    @Transient
    private List<Map<String, Object>> employeeULBDesignations;

    @Transient
    private String oldPhotoPath;

    // Below 2 properties for Import excel
    @Transient
    private String designationName;
    @Transient
    private String departmentName;

    @Transient
    private String token;

    @Column(name = "mobileipaddress", length = 255)
    private String mobileIpAddress;

    @Column(name = "mobilelastlocation", length = 255)
    private String mobileLastLocation;

    @Column(name = "lastsynctime", nullable = true)
    private Date lastSyncTime;

    @Column(name = "transferotp", length = 20)
    private String transferOtp;

    @Column(name = "transferotpexpiresat", nullable = true)
    private Date transferOtpExpiresAt;

    public EmployeeEntity() {
        super();
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Column(name = "lockedtime")
    private Date lockedTime;

    @Column(name = "lastfailed", nullable = true)
    private Date lastFailed;

    @Column(name = "failedattempts", nullable = true)
    private Integer failedAttempts;

    @Column(name = "isactive", columnDefinition = "boolean default false")
    private boolean isActive;

    @Column(name = "membercode")
    private String memberCode;

    @Column(name = "shgcode")
    @Convert(converter = ArrayToStringConverter.class)
    private List<String> shgCode;

    @Column(name = "hierarchy", nullable = false, columnDefinition = "int default 0")
    private int hierarchy;

    @Column(name = "iseligibleforincentives", columnDefinition = "boolean default false")
    private boolean isEligibleForIncentives;


    @OneToOne(mappedBy = "employeeEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EmployeeBankDetailsEntity employeeBankDetailsEntity;

    @Override
    public String getUsername() {
        return getEmployeeId();
    }
}
