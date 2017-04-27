package com.mad2man.sbweb.rest.model.error;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * View Model for transferring error message with a list of field errors.
 */
public class ErrorModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String message;
    private final String description;

    private List<FieldErrorModel> fieldErrors;

    public ErrorModel(String message) {
        this(message, null);
    }

    public ErrorModel(String message, String description) {
        this.message = message;
        this.description = description;
    }

    public ErrorModel(String message, String description, List<FieldErrorModel> fieldErrors) {
        this.message = message;
        this.description = description;
        this.fieldErrors = fieldErrors;
    }

    public void add(String objectName, String field, String message) {
        if (fieldErrors == null) {
            fieldErrors = new ArrayList<>();
        }
        fieldErrors.add(new FieldErrorModel(objectName, field, message));
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }

    public List<FieldErrorModel> getFieldErrors() {
        return fieldErrors;
    }
}
