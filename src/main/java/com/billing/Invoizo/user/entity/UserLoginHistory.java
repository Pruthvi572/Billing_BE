package com.billing.Invoizo.user.entity;


import com.billing.Invoizo.constants.EnumConstants;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "user_login_history")
public class UserLoginHistory {
    @Id
    @Column
    private Date loginTime;

    @Column(length = 255, nullable = false)
    private String employeeId;

    @Column(length = 255)
    private String ipAddress;

    @Enumerated(EnumType.STRING)
    @Column(length = 255)
    private EnumConstants.FromType isFrom;

    @Column(length = 255)
    private String mobileDeviceId;

    @Column(length = 255)
    private String mobileDeviceModelName;

}
