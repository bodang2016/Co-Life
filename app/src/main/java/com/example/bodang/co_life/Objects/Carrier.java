package com.example.bodang.co_life.Objects;

import java.io.Serializable;


public class Carrier implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Object object;
    private ContentType carrierType;
    private String sender;

    public Carrier() {

    }

    public Carrier(Object object, ContentType carrierType, String sender) {
        this.object = object;
        this.carrierType = carrierType;
        this.sender = sender;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public ContentType getCarrierType() {
        return carrierType;
    }

    public void setCarrierType(ContentType carrierType) {
        this.carrierType = carrierType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
