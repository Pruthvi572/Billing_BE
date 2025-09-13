package com.billing.Invoizo.security.payload.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class MobileLoginRequest extends LoginRequest implements Serializable {


    private String deviceModel;
    private String mobileSessionId;
    private String mobileVersion;
    private String mobileIpAddress;
    private String ipAddress;
    private String mobileLastLocation;
}