package com.example.bodang.co_life.Objects;

import java.io.Serializable;

/**
 * This is the Class for sending at each time which could take different objects and sending information with it.
 */
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

    //constructor
    public Carrier(Object object, ContentType carrierType, String sender) {
        this.object = object;
        this.carrierType = carrierType;
        this.sender = sender;
    }

    //get and set methods
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
