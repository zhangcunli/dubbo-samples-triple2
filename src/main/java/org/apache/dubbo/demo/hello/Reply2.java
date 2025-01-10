package org.apache.dubbo.demo.hello;

import java.io.Serializable;
import java.math.BigDecimal;

public class Reply2 implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;
    private BigDecimal lnt;

    public Reply2() {
    }

    public Reply2(String message, BigDecimal lnt) {
        this.message = message;
        this.lnt = lnt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getLnt() {
        return lnt;
    }

    public void setLnt(BigDecimal lnt) {
        this.lnt = lnt;
    }
}
