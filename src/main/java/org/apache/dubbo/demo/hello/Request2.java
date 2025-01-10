package org.apache.dubbo.demo.hello;

import java.io.Serializable;

public class Request2 implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;
    private Integer count;
    private String sessionId;

    public Request2() {
    }

    public Request2(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }


    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
