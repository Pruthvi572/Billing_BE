package com.billing.Invoizo.masters.ulb.entity;

import com.billing.Invoizo.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "master_ulb")
public class ULBEntity extends BaseEntity {

    @Id
    @Column(name = "ulbcode", length = 255, nullable = false)
    private String ulbCode;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "districtname", length = 50)
    private String districtName;

    @Column(name = "latitude", length = 50)
    private String latitude;

    @Column(name = "longitude", length = 50)
    private String longitude;

    @Column(name = "magnitude", length = 50)
    private String magnitude;

    @Column(name = "gradeid", nullable = false, columnDefinition = "int default 0")
    private int gradeId;

    @Column(name = "gradename", length = 50)
    private String gradeName;

    @Column(name = "licenseid", length = 50)
    private String licenseId;

    @Column(name = "subclientid", length = 50)
    private String subClientId;

    @Column(name = "type", nullable = false, columnDefinition = "smallint default 0")
    private byte type;

    @Column(name = "departmentgstnumber", length = 255)
    private String departmentGSTNumber;

    @Column(name = "boundarylatlng", length = 255)
    private String boundaryLatLng;

    @Column(name = "schemecode", length = 255)
    private String schemeCode;

    @Column(name = "agencycode", length = 255)
    private String agencyCode;

    @Column(name = "agencyname", length = 255)
    private String agencyName;

    @Column(name = "parentulbcode", length = 255)
    private String parentUlbCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentulbcode", referencedColumnName = "ulbcode", insertable = false, updatable = false)
    private ULBEntity parentUlbEntity;

    @Column(name = "schemehierarchy", length = 255)
    private String schemeHierarchy;

    @Column(name = "schemehierarchylevelorder", length = 255)
    private String schemeHierarchyLevelOrder;

    @Column(name = "schemehierarchylocation", length = 255)
    private String schemeHierarchyLocation;

    @Column(name = "usernumber", length = 18)
    private String userNumber;

    @Column(name = "bankname", length = 255)
    private String bankName;

    @Column(name = "branchname", length = 255)
    private String branchName;

    @Column(name = "bankaccountnumber", length = 255)
    private String bankAccountNumber;

    @Column(name = "ifsccode", length = 255)
    private String ifscCode;

    @Column(name = "holdingaccountnumber", length = 255)
    private String holdingAccountNumber;

    @Column(name = "holdingaccountifsccode", length = 255)
    private String holdingAccountIfscCode;


}
