package com.billing.Invoizo.mobile.service;

import com.billing.Invoizo.mobile.dto.MobileLoginResponseEntity;
import com.billing.Invoizo.security.payload.request.MobileLoginRequest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface MobileCredentialsService {

    MobileLoginResponseEntity isAuthorized(MobileLoginRequest loginRequest)
            throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException,
            NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException;

    void logout(String employeeId);

}
