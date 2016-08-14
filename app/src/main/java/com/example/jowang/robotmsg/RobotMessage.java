package com.example.jowang.robotmsg;

import java.util.Date;

/**
 * Created by jowang on 16/8/12.
 */
public class RobotMessage {
    private String name;
    private String msg;
    private Type type;
    private Date date;
    public enum Type{INPUT,OUTPUT};

    public RobotMessage(String msg, Type type, Date date) {
        this.msg = msg;
        this.type = type;
        this.date = date;
    }

    public RobotMessage() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
