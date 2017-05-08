package com.mad2man.sbweb.common.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FieldErrorViewModel implements Serializable {

    private static final long serialVersionUID = 2948142613298932393L;

    private String objectName;
    private String field;
    private String message;
}
