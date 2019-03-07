package com.oakinvest.lt.util.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Loose touch error detail.
 */
@SuppressWarnings("magicnumber")
@JsonInclude(NON_NULL)
public class LooseTouchErrorDetail {

    /**
     * Error code.
     */
    @ApiModelProperty(value = "Error code",
            example = "email_required",
            required = true,
            position = 1)
    private final LooseTouchErrorCode code;

    /**
     * Error message.
     */
    @ApiModelProperty(value = "A human-readable message providing more details about the error",
            example = "The contact email is required",
            required = true,
            position = 2)
    private final String message;

    /**
     * Constructor.
     *
     * @param newErrorCode error code
     * @param newMessage   error message
     */
    public LooseTouchErrorDetail(final LooseTouchErrorCode newErrorCode, final String newMessage) {
        this.code = newErrorCode;
        this.message = newMessage;
    }

    /**
     * Getter of code.
     *
     * @return code
     */
    public final LooseTouchErrorCode getCode() {
        return code;
    }

    /**
     * Getter of message.
     *
     * @return message
     */
    public final String getMessage() {
        return message;
    }

}
