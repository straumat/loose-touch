package com.oakinvest.lt.util.error;

import java.util.LinkedList;
import java.util.List;

/**
 * Loost touch exception.
 */
public class LooseTouchException extends RuntimeException {

    /**
     * The error type.
     */
    private final LooseTouchErrorType type;

    /**
     * List of errors with their code.
     */
    private final List<LooseTouchErrorDetail> errors = new LinkedList<>();

    /**
     * Constructor with a simple error message.
     *
     * @param newType error type
     * @param message message
     */
    public LooseTouchException(final LooseTouchErrorType newType, final String message) {
        super(message);
        this.type = newType;
    }

    /**
     * Constructor with a message error and an error.
     *
     * @param newType  error type
     * @param message  message
     * @param newError error
     */
    public LooseTouchException(final LooseTouchErrorType newType, final String message, final LooseTouchErrorDetail newError) {
        super(message);
        this.type = newType;
        this.errors.add(newError);
    }

    /**
     * Constructor with a message error and list of errors.
     *
     * @param newType   error type
     * @param message   message
     * @param newErrors list of errors
     */
    public LooseTouchException(final LooseTouchErrorType newType, final String message, final List<LooseTouchErrorDetail> newErrors) {
        super(message);
        this.type = newType;
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
     * Getter of errors.
     *
     * @return errors
     */
    public final List<LooseTouchErrorDetail> getErrors() {
        return errors;
    }

    @Override
    public final String toString() {
        StringBuilder value = new StringBuilder("CeriseException{type=" + type + ", message=" + getMessage() + "}, errors {");
        for (LooseTouchErrorDetail error : getErrors()) {
            value.append("{code=")
                    .append(error.getCode())
                    .append(", message=")
                    .append(error.getMessage())
                    .append("}, ");
        }
        if (value.toString().endsWith(", ")) {
            value = new StringBuilder(value.substring(0, value.toString().length() - 2));
        }
        value.append("}");
        return value.toString();
    }

}
