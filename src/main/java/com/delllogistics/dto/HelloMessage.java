package com.delllogistics.dto;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: jiajie<br/>
 * Date: 12/02/2018<br/>
 * Time: 4:44 PM<br/>
 */
public class HelloMessage {

    private String name;

    public HelloMessage() {
    }

    public HelloMessage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
