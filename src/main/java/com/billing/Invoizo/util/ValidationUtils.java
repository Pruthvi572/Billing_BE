package com.billing.Invoizo.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component("validationUtils")
public class ValidationUtils {

    public static boolean validateNames(String txt) {
        String regx = "^[a-zA-Z0-9 .]*$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(txt);
        return matcher.matches();
    }

    public static boolean validateCode(String empId) {
        String regx = "^[a-zA-Z0-9-/]*$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(empId);
        return matcher.matches();
    }

    public static boolean validateNumbers(String number) {
        String regx = "^[0-9]*$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(number);
        return matcher.matches();
    }

    public static boolean validateEmail(String mail) {
        String regx = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches();
    }

    public static boolean validatePanNumber(String pan) {
        String regx = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(pan);
        return matcher.matches();
    }

    public static boolean validateAmount(String amount) {
        String regx = "[0-9]*\\.?[0-9]*";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(amount);
        return matcher.matches();
    }

    public static boolean validateLatitude(String latitude) {
        String regx = "^(\\+|-)?((\\d((\\.)|\\.\\d{1,6})?)|(0*?[0-8]\\d((\\.)|\\.\\d{1,6})?)|(0*?90((\\.)|\\.0{1,6})?))$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(latitude);
        return matcher.matches();
    }

    public static boolean validateLongitude(String latitude) {
        String regx = "^(\\+|-)?((\\d((\\.)|\\.\\d{1,6})?)|(0*?\\d\\d((\\.)|\\.\\d{1,6})?)|(0*?1[0-7]\\d((\\.)|\\.\\d{1,6})?)|(0*?180((\\.)|\\.0{1,6})?))$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(latitude);
        return matcher.matches();
    }
}
