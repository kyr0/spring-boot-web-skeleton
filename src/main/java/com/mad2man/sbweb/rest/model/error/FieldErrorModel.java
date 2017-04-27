package com.mad2man.sbweb.rest.model.error;

import java.io.Serializable;

public class FieldErrorModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String objectName;

    private final String field;

    private final String message;

    public FieldErrorModel(String dto, String field, String message) {
        this.objectName = dto;
        this.field = field;
        this.message = message;
    }

    public String getObjectName() {
        return objectName;
    }

    public String getField() {
        return field;
    }

    public String getMessage() {
        return message;
    }

}
