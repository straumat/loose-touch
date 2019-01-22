package com.oakinvest.lt.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Application status.
 */
@JsonInclude(NON_NULL)
public class Status {

    /**
     * Date the status was issued.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm a z")
    private Date date;

    /**
     * Number of users in the application.
     */
    private long usersCount;

    /**
     * Default constructor.
     */
    public Status() {
        date = new Date();
    }

    /**
     * Getter of date.
     *
     * @return date
     */
    public final Date getDate() {
        return date;
    }

    /**
     * Setter of date.
     *
     * @param newDate the date to set
     */
    public final void setDate(final Date newDate) {
        date = newDate;
    }

    /**
     * Getter of usersCount.
     *
     * @return usersCount
     */
    public final long getUsersCount() {
        return usersCount;
    }

    /**
     * Setter of usersCount.
     *
     * @param newApplicationUsers the usersCount to set
     */
    public final void setUsersCount(final long newApplicationUsers) {
        usersCount = newApplicationUsers;
    }

}
