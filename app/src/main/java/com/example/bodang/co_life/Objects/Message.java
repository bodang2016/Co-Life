package com.example.bodang.co_life.Objects;

import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    Timestamp time;
    String sender;
    String receiver;
    String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Message(String receiver, String sender, String content, int type, Timestamp time) {
        super();
        this.time = time;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.type = type;
    }

    int type;

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
