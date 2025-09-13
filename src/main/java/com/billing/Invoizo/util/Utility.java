package com.billing.Invoizo.util;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component("utility")
public class Utility {
    /**
     * @return base64 Pattern
     */
    public static Pattern getBase64Pattern() {
        return Pattern.compile("^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$");
    }

    public static Long getRandomNumber(int a) {
        return Math.round(Math.random() * Math.pow(10, a));
    }

    // Rounding float value to 2 decimals
    public static float roundFloatValue(Double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    // Rounding float value to 2 decimals
    public static float roundFloatValue(Float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    // Rounding float value to 2 decimals
    public static String roundFloatValue(Double d) {
        NumberFormat nf = NumberFormat.getInstance();
        // It means that 3.125 should be rounded to 3.13, and 3.123 to 3.12
        nf.setRoundingMode(RoundingMode.HALF_UP);
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(false);
        return nf.format(d);
    }

    // Rounding float value to 2 decimals
    public static String roundFloatValue(float d) {
        NumberFormat nf = NumberFormat.getInstance();
        // It means that 3.125 should be rounded to 3.13, and 3.123 to 3.12
        nf.setRoundingMode(RoundingMode.HALF_UP);
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(false);
        return nf.format(d);
    }

    // Calculating percentage value
    public static float calculatePercentage(float x, float y) {
        float f = (x / y) * 100;
        BigDecimal percentage = new BigDecimal(Float.toString(f));
        percentage = percentage.setScale(2, RoundingMode.HALF_UP);
        return percentage.floatValue();
    }

    // Calculating tender percentage amount
    public static float calculatePercentageAmount(float percentage, double amount) {
        float f = (float) ((percentage * amount) / 100);
        BigDecimal finalAmount = new BigDecimal(Float.toString(f));
        finalAmount = finalAmount.setScale(2, RoundingMode.HALF_UP);
        return finalAmount.floatValue();
    }

    // Generating UUID
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * Generating financial year
     *
     * @return Financial year in string format
     */
    public static String getFinancialYear() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        if (month < 4) {
            return (year - 1) + "-" + (year % 100);
        } else {
            return year + "-" + ((year + 1) % 100);
        }
    }

    public static String getCurrentFinancialYear() {
        String financialYear = "";
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        if (month < 3) {
            financialYear = String.valueOf(year);
            System.out.println("Financial Year : " + (year - 1) + "-" + year);
        } else {
            financialYear = String.valueOf(year + 1);
            System.out.println("Financial Year : " + year + "-" + (year + 1));
        }
        return financialYear;
    }

    public static boolean validatePanNumber(String pan) {
        String regx = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(pan);
        return matcher.matches();
    }

    public static boolean validateRegNo(String reg) {
        String regre = "^[a-zA-z0-9-]+$";
        Pattern pattern = Pattern.compile(regre);
        Matcher matcher = pattern.matcher(reg);
        return matcher.matches();
    }

    public static String parameterNullCheck(String parameter) {
        return !StringUtil.isNullOrEmpty(parameter) ? parameter : "";
    }


    public static boolean validateNames(String txt) {
        String regx = "^[a-zA-Z )]*$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(txt);
        return matcher.matches();
    }

    public static boolean validateNumbers(String mobile) {
        String regx = "^[0-9]{10}$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    public static boolean validateBankNumber(String mobile) {
        String regx = "^[0-9]*$";
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(mobile);
        return matcher.matches();
    }

    public static boolean validateIfsc(String ifsc) {
        String reg = "^[A-Z]{4}[0-9]{7}$";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(ifsc);
        return matcher.matches();
    }

    public static String snorkelFieldValidator(String parameter) {
        if (!StringUtil.isNullOrEmpty(parameter)) {
            return parameter.length() > 40 ? parameter.substring(0, 40) : parameter;
        } else {
            return "";
        }
    }

    public static String encodeCheckSumWithSHA256(String data) {
        MessageDigest md;
        StringBuilder sb = new StringBuilder();
        String response = null;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes(StandardCharsets.UTF_8));
            // Get the hashbytes
            byte[] hashBytes = md.digest();
            // Convert hash bytes to hex format
            for (byte
                    b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            response = sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Internal server error");
        }
        return response;
    }

    static String validateInfo(String value) {
        return StringUtils.isNotEmpty(value) && !"null".equals(value) ? value : "";
    }

    public static String generateCheckSum(Map<String, Object> requestMap) throws Exception {
        StringBuilder finalChkSum = new StringBuilder();
        StringBuilder keys = new StringBuilder();
        try {
            if (null == requestMap) {
                return null;
            }

            for (Map.Entry<String, Object> entry : requestMap.entrySet()) {
                if (!entry.getKey().equals("checksum")) {
                    if (entry.getValue()
                            instanceof List) {
                        List<Object> tempLst = ((List) entry.getValue());
                        if (!CollectionUtils.isEmpty(tempLst) && (tempLst.get(0) instanceof Map)) {
                            List<? extends Map<String, Object>> innerObjectMap
                                    = (List<? extends Map<String, Object>>) entry.getValue();
                            for (Map<String, Object> innerMap : innerObjectMap) {
                                for (Map.Entry<? extends String, ? extends Object> entryInn :
                                        innerMap.entrySet()) {
                                    keys.append(entryInn.getKey());
                                    finalChkSum.append(
                                            getInnerLevel2Map(
                                                    entryInn.getValue(), finalChkSum));
                                }
                            }
                        } else if (!CollectionUtils.isEmpty(tempLst)) {
                            for (Object strValues : tempLst) {
                                finalChkSum.append(
                                        validateInfo(
                                                String.valueOf(strValues)));
                            }
                        }
                    } else if (entry.getValue() instanceof Map) {
                        Map<? extends String, ? extends Object> innerObjectMap2
                                = (Map<? extends String, ? extends Object>) entry.getValue();
                        for (Map.Entry<? extends String, ? extends Object> entryInn :
                                innerObjectMap2.entrySet()) {
                            keys.append(entryInn.getKey());
                            finalChkSum.append(
                                    validateInfo(
                                            String.valueOf(entryInn.getValue())));
                        }
                    } else {
                        finalChkSum.append(
                                validateInfo(
                                        String.valueOf(entry.getValue())));
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return String.valueOf(
                encodeCheckSumWithSHA256(
                        finalChkSum.toString().trim()));
    }

    private static String getInnerLevel2Map(Object entryInnLvl2, StringBuilder finalChkSum123) {
        StringBuilder finalChkSum = new StringBuilder();
        StringBuilder keys = new StringBuilder();
        if (entryInnLvl2 instanceof List) {
            List<Object> tempLst = ((List) entryInnLvl2);
            if (!CollectionUtils.isEmpty(tempLst) && (tempLst.get(0) instanceof Map)) {
                List<? extends Map<String, Object>> innerObjectMap =
                        (List<? extends Map<String, Object>>) entryInnLvl2;
                for (Map<String, Object> innerMap : innerObjectMap) {
                    for (Map.Entry<? extends String, ? extends Object> entryInn : innerMap.entrySet()) {
                        keys.append(entryInn.getKey());
                        finalChkSum.append(
                                validateInfo(String.valueOf(entryInn.getValue())));
                    }
                }
            } else if (!CollectionUtils.isEmpty(tempLst)) {
                for (Object strValues : tempLst) {
                    finalChkSum.append(
                            validateInfo(
                                    String.valueOf(strValues)));
                }
            }
        } else if (entryInnLvl2 instanceof Map) {
            Map<? extends String, ? extends Object> innerObjectMap2 =
                    (Map<? extends String, ? extends Object>) entryInnLvl2;
            for (Map.Entry<? extends
                    String, ? extends Object> entryInn : innerObjectMap2.entrySet()) {
                keys.append(entryInn.getKey());
                finalChkSum.append(
                        validateInfo(
                                String.valueOf(entryInn.getValue())
                        ));
            }
        } else {
            finalChkSum.append(
                    validateInfo(
                            String.valueOf(entryInnLvl2)));
        }
        return finalChkSum.toString();
    }
}
