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
    private Date currentDate;

    /**
     * Number of users in the application.
     */
    private long numberOfUsers;

    /**
     * Default constructor.
     */
    public Status() {
        currentDate = new Date();
    }

    /**
     * Getter of currentDate.
     *
     * @return currentDate
     */
    public final Date getCurrentDate() {
        return currentDate;
    }

    /**
     * Setter of currentDate.
     *
     * @param newDate the currentDate to set
     */
    public final void setCurrentDate(final Date newDate) {
        currentDate = newDate;
    }

    /**
     * Getter of numberOfUsers.
     *
     * @return numberOfUsers
     */
    public final long getNumberOfUsers() {
        return numberOfUsers;
    }

    /**
     * Setter of numberOfUsers.
     *
     * @param newApplicationUsers the numberOfUsers to set
     */
    public final void setNumberOfUsers(final long newApplicationUsers) {
        numberOfUsers = newApplicationUsers;
    }

}
