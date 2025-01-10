package org.apache.dubbo.demo.hello;

import java.io.Serializable;
import java.math.BigDecimal;

public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;

    public Request() {
    }

    public Request(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
