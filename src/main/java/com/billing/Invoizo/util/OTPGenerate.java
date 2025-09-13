package com.billing.Invoizo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component("otpGenerate")
public class OTPGenerate {

    //    @Value("${isProduction}")
    boolean isProduction;
    @Autowired
    @Qualifier("randomNumberGenerator")
    private RandomNumberGenerator randomNumberGenerator;

    public String generate() {
        String otp = "";
        if (isProduction) {
            otp = randomNumberGenerator.randomNumber3Digits() + "" + randomNumberGenerator.randomNumber3Digits();
        } else {
            otp = "123456";
        }
        return otp;
    }
}
