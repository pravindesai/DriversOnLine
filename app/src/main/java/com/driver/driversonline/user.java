package com.driver.driversonline;

import java.io.Serializable;

public class user implements Serializable {
    public String num;
    public String name;
    public String city;
    public String type;
    public String lno;

    public user() {
    }

    public user(String num, String name, String city, String type, String lno) {
        this.num = num;
        this.name = name;
        this.city = city;
        this.type = type;
        this.lno = lno;
    }
}