package com.oakinvest.lt.util.error;

import io.swagger.annotations.ApiModelProperty;

import java.util.LinkedList;
import java.util.List;

/**
 * Loose touch error.
 */
@SuppressWarnings({"magicnumber", "unused"})
public class LooseTouchError {

    /**
     * The error type.
     */
    @ApiModelProperty(value = "The type of error returned",
            example = "resource_not_found",
            required = true,
            position = 1)
    private final LooseTouchErrorType type;

    /**
     * A human-readable message providing more details about the error.
     */
    @ApiModelProperty(value = "A human-readable message providing more details about the error",
            example = "The contact was not found",
            required = true,
            position = 2)
    private final String message;

    /**
     * List of errors with their code.
     */
    @ApiModelProperty(value = "List of errors",
            position = 3)
    private final List<LooseTouchErrorDetail> errors = new LinkedList<>();

    /**
     * Constructor (single error).
     *
     * @param newType    error type
     * @param newMessage The error message associated with exception
     */
    public LooseTouchError(final LooseTouchErrorType newType, final String newMessage) {
        super();
        this.type = newType;
        this.message = newMessage;
    }

    /**
     * Constructor (several errors).
     *
     * @param newType    error type
     * @param newMessage Error message
     * @param newErrors  List of errors
     */
    public LooseTouchError(final LooseTouchErrorType newType, final String newMessage, final List<LooseTouchErrorDetail> newErrors) {
        super();
        this.type = newType;
        this.message = newMessage;
        this.errors.addAll(newErrors);
    }

    /**
     * Getter of type.
     *
     * @return type
     */
    public final LooseTouchErrorType getType() {
        return type;
    }

    /**
     * Getter of message.
     *
     * @return message
     */
    public final String getMessage() {
        return message;
    }

    /**
     * Getter of errors.
     *
     * @return errors
     */
    public final List<LooseTouchErrorDetail> getErrors() {
        return errors;
    }

}
