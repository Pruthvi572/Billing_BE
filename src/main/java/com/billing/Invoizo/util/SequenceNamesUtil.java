package com.billing.Invoizo.util;

public class SequenceNamesUtil {
    public enum Sequences {

        MASTER_AREAS_SEQUENCE("master_areas_sequence"),
        MASTER_DESIGNATION_SEQUENCE("master_designation_sequence"),
        NOTIFICATION_HISTORY_SEQUENCE("notification_history_sequence");

        private final String value;

        Sequences(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
