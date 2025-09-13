package com.billing.Invoizo.constants;

public class SetupConstants {

    public enum Designations {
        SADMIN("Super Admin"), Admin("Admin");

        private final String value;

        Designations(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
