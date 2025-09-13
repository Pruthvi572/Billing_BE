package com.billing.Invoizo.constants;

import org.springframework.util.StringUtils;

import java.util.Calendar;

public class EnumConstants {


    public enum FileExtensions {
        JPG("jpg"), PNG("png");

        private final String value;

        FileExtensions(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }


    public enum ULB_COLUMN_INDEX {
        SERIALNO(0), AREA_CODE(1), AREA_NAME(2), SECTOR(3), GSTNUMBER(4),
        DISTRICT_NAME(5), LATITUDE(6), LONGITUDE(7),
        NORTHLATLNG(8), EASTLATLNG(9), SOUTHLATLNG(10), WESTLATLNG(11);

        private final int value;

        ULB_COLUMN_INDEX(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum EMP_COLUMN_INDEX {
        SERIALNO(0), EMP_ID(1), NAME(2), DESIGNATION(3), MOBILE(4), GENDER(5),
        EMAIL(6), AGENCYCODE(7), ADDRESS(8), ULB_CODE(9);

        private final int value;

        EMP_COLUMN_INDEX(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum PERMISSION_TYPE {
        WEB(1), MOBILE(0);

        private final int type;

        PERMISSION_TYPE(final int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }

    public enum SMS_TYPE {
        LEAD_WINNER_SMS_COUNTRY(1), SMS_COUNTRY(2);

        private final int value;

        SMS_TYPE(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum MobileStatus {
        NO_WORKS((byte) -3), ACCOUNT_LOCKED((byte) -2), INVALID_USER((byte) -1), LOGIN_SUCCESS((byte) 1),
        LOGIN_FAILURE((byte) 0), LOGIN_LOGGEDIN((byte) 2), TRANSFERRED((byte) 3), DELETED((byte) 4),
        ACCESS_DENIED((byte) 5), PAYMENT_PENDING((byte) 6), SUBSCRIPTION_INPROGRESS((byte) 7),
        SUBSCRIPTION_EXPIRED((byte) 8), REGISTRATION_PENDING((byte) 9), NO_ROLES((byte) 10);

        private final byte value;

        MobileStatus(final byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }
    }


    public static String getFinancialYear() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        if (month < 4) {
            return (year - 1) + "-" + (year % 100);
        } else {
            return year + "-" + ((year + 1) % 100);
        }
    }

    public enum Designations {
        SUPER_ADMIN(0), ADMIN(1), MANAGING_DIRECTOR(2), GENERAL_CREDIT_MANAGER(3),
        DISTRICT_MANAGER(4), ASSISTANT_MANAGER(5), CLUSTER_COORDINATOR(6), BOOK_KEEPER(7),
        SHG_HEAD(8), SHG_MEMBER(9);

        private final int value;

        Designations(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum FromType {
        CLOUD, MOBILE;

        @Override
        public String toString() {
            return StringUtils.capitalize(name());
        }
    }
}