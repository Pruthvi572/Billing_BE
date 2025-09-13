package com.billing.Invoizo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
@Data
public class InvoizoProperties {

    private boolean runDefaultScripts;

    private String securityCheckURL;

    private String imagesPathGet;

    private String imagesPathSave;

    private String imagesPathDownload;

    private boolean citizenLicenseCheck;

    private boolean citizenCountCheck;

    private Integer citizenCount;

    private boolean isProduction;

    private String[] corsURLs;

    //In Milliseconds
    private long accountLockTime;

    private String supportMail;

    private String websiteUrl;

    private int smsServiceType;

    private String drNotifyUrl;

    private String leadWinnerSMSCountryAuthKey;

    private String leadWinnerSMSCountryAuthToken;

    private String leadWinnerSMSCountrySenderId;

    private boolean smsServiceEnabled;

    private boolean isSmsCountryEnabled;

    private String smsCountryAuthKey;

    private String smsCountryAuthToken;

    private String smsCountrySenderId;

    private String smsCountrySendURL;

    private String excelPathDownload;

    private String excelImportTempPath;
}
