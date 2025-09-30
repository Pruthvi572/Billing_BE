package com.billing.Invoizo.constants;

public class EndpointConstants {

    /**
     * API Prefix for Both WEB and MOBILE
     */
    public static final String API_VERSION_1 = "v1";
    public static final String WEB_API_PREFIX = "/web/api/" + API_VERSION_1;
    public static final String MOBILE_API_PREFIX = "/mobile/api/" + API_VERSION_1;
    public static final String MASTER = "/master";

    /**
     * COMMON PATHS
     */
    public static final String VIEW = "/view";
    public static final String UPDATE = "/update";
    private static final String UNIQUE_CHECK = "/uniquecheck";
    public static final String DROPDOWN = "/dropdown";
    public static final String DELETE = "/{id}";
    public static final String IMPORT = "/import";
    public static final String EXCEL = "/excel";
    public static final String PAYMENT = "/payment";
    public static final String AXIS = "/axis";


    /**
     * Initial Setup API Paths
     */
    public static final String SETUP_PREFIX = "/initial/setup";
    public static final String INITIAL_SETUP = WEB_API_PREFIX + SETUP_PREFIX + "/save";
    public static final String SETUP_FLAG = INITIAL_SETUP + "/setupflag";
    public static final String LICENSE_CHECK = INITIAL_SETUP + "/check/license";
    public static final String DESIGNATION_LIST = INITIAL_SETUP + "/designation";

    /**
     * Employee Related APIs
     */
    public static final String EMPLOYEES_API = "/employees";
    public static final String LICENSE_ID = "/licenseid";
    public static final String REG_ID = "/registrationid";
    public static final String MOBILE_NUMBER = "/mobilenumber";
    public static final String EMAIL_ID = "/email";
    public static final String BANK_DETAILS = "/bank/details";
    public static final String MASTER_EMPLOYEES_API = WEB_API_PREFIX + EMPLOYEES_API;
    public static final String MOBILE_SHG_CODE_VERIFY = MOBILE_API_PREFIX + "/shg/code/verify";
    public static final String MOBILE_MASTER_EMPLOYEES_API = MOBILE_API_PREFIX + EMPLOYEES_API;
    public static final String USER_EMPLOYEES_API = WEB_API_PREFIX + EMPLOYEES_API;
    public static final String EMPLOYEE_UNIQUECHECK = USER_EMPLOYEES_API + UNIQUE_CHECK;
    public static final String MOBILE_EMPLOYEE_UNIQUECHECK = EMPLOYEES_API + UNIQUE_CHECK;
    public static final String EMPLOYEE_VIEW = MASTER_EMPLOYEES_API + VIEW;
    public static final String EMPLOYEE_BY_DESIGNATIONID = MASTER_EMPLOYEES_API + "/designationid";
    public static final String MOBILE_EMPLOYEE_VIEW = MOBILE_MASTER_EMPLOYEES_API + VIEW;
    /**
     *
     */
    public static final String BUSINESS_DETAILS_API = WEB_API_PREFIX + "/business/details";

}
