package com.billing.Invoizo.user.entity;


import com.billing.Invoizo.masters.designations.entity.DesignationsEntity;
import com.billing.Invoizo.masters.ulb.entity.ULBEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "user_employee_ulb_designation")
public class EmployeeULBDesignationEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    // Id of the Employee - Foreign Key employeeid+designationId+ulbCode
    @Id
    @Column(name = "employeedesignationid", length = 255, nullable = false)
    private String employeeDesignationId;

    @Column(name = "employeeid", length = 255)
    private String employeeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeid", referencedColumnName = "employeeid", insertable = false, updatable = false, nullable = false)
    private EmployeeEntity employeeEntity;

    // ULB the employee belongs to
    @Column(name = "ulbcode", length = 255)
    private String ulbCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ulbcode", referencedColumnName = "ulbcode", insertable = false, updatable = false, nullable = false)
    private ULBEntity ulbEntity;

    // Designation of the employee
    @Column(name = "designationid", nullable = false, columnDefinition = "int default 0")
    private int designationId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "designationid", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private DesignationsEntity designationsEntity;

    // Whether the employee is active or inactive
    @Column(name = "activestatus", nullable = false, columnDefinition = "boolean default false")
    private boolean activeStatus;

    // From date which the designation is assigned
    @Column(name = "fromdate")
    private Date fromDate;

    // To date until when he has the designation before becoming inactive
    @Column(name = "todate")
    private Date toDate;

    // flag to delete
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted;

    // Whether it is base designation or not
    @Column(name = "isbasedesignation", nullable = false, columnDefinition = "boolean default false")
    private boolean isBaseDesignation;

    // Order of the Designations which the employee is assigned
    @Column(name = "designationsorder", nullable = false, columnDefinition = "int default 0")
    private int designationsOrder;

}
