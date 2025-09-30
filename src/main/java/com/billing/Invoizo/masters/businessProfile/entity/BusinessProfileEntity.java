package com.billing.Invoizo.masters.businessProfile.entity;

import com.billing.Invoizo.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@Table(name = "business_profile_details")
public class BusinessProfileEntity extends BaseEntity {


    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", length = 255, nullable = false)
    private String id;

    @Column(name = "businessname", nullable = false, length = 150)
    private String businessName;

    @Column(name = "ownername", length = 100)
    private String ownerName;

    @Column(name = "email", length = 120)
    private String email;

    @Column(name = "phonenumber", length = 20)
    private String phoneNumber;

    @Column(name = "alternatephone", length = 20)
    private String alternatePhone;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "pincode", length = 15)
    private String pinCode;

    @Column(name = "gstNumber", length = 20)
    private String gstNumber;

    @Column(name = "panNumber", length = 20)
    private String panNumber;

    @Column(name = "bankname", length = 100)
    private String bankName;

    @Column(name = "accountnumber", length = 50)
    private String accountNumber;

    @Column(name = "ifsccode", length = 20)
    private String ifscCode;

    @Column(name = "branchname", length = 100)
    private String branchName;
}
