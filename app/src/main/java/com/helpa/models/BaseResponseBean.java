package com.helpa.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by app-server on 5/4/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponseBean {

    public int CODE;
    public String MESSAGE;
}
