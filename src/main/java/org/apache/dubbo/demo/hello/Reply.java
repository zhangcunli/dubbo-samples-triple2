package org.apache.dubbo.demo.hello;

import java.io.Serializable;

public class Reply implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;

    public Reply() {
    }

    public Reply(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
