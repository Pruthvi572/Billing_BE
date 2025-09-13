package com.billing.Invoizo.user.entity;


import com.billing.Invoizo.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "employee_bank_Details")
public class EmployeeBankDetailsEntity extends BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id", length = 255, nullable = false)
    private String id;

    @Column(name = "employeeid", length = 255)
    private String employeeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeid", referencedColumnName = "employeeid", insertable = false, updatable = false, nullable = false)
    private EmployeeEntity employeeEntity;

    @Column(name = "accountnumber", length = 50)
    private String accountNumber;

    @Column(name = "ifsccode", length = 50)
    private String ifscCode;

    @Column(name = "branchname", length = 200)
    private String branchName;

    @Column(name = "bankname", length = 200)
    private String bankName;

    @Column(name = "bankbranchcode", length = 200)
    private String bankBranchCode;

    @Column(name = "bankcode", length = 100)
    private String bankCode;

    @Column(name = "accountopendate")
    private Date accountOpenDate;
}
