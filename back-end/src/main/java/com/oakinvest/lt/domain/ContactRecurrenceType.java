package com.oakinvest.lt.domain;

/**
 * Contact recurrence type.
 */
public enum ContactRecurrenceType {

    /**
     * Day.
     */
    DAY("DAY"),

    /**
     * Month.
     */
    MONTH("MONTH"),

    /**
     * Year.
     */
    YEAR("YEAR");

    /**
     * Value.
     */
    private final String value;

    /**
     * Constructor.
     *
     * @param newValue value
     */
    ContactRecurrenceType(final String newValue) {
        this.value = newValue;
    }

    /**
     * Get value.
     *
     * @return value
     */
    public final String getValue() {
        return value;
    }

}
