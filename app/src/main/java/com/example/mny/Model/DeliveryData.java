package com.example.mny.Model;

import com.example.mny.Controller.Control;

import java.util.ArrayList;

public class DeliveryData {

    private String time;
    private String nickname = "";

    public DeliveryData() {

    }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
}
