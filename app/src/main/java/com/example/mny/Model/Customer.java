package com.example.mny.Model;

import java.io.Serializable;

public class Customer extends User implements Serializable {
    private String nickname = "";
    private String email;
    private String number;
    private boolean isBanned;
    private int banCount;

    public Customer() {

    }

    public Customer(String email, String number, int banCount, boolean isBanned, String nickname) {
        super(email, number, banCount, isBanned);
        this.nickname = nickname;
    }

    @Override
    public String getEmail() { return email; }
    @Override
    public void setEmail(String email) { this.email = email; }

    @Override
    public String getNumber() { return number; }
    @Override
    public void setNumber(String number) { this.number = number; }

    public boolean isBanned() { return isBanned; }
    public void setBanned(boolean banned) { isBanned = banned; }

    @Override
    public int getBanCount() { return banCount; }
    @Override
    public void setBanCount(int banCount) { this.banCount = banCount; }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

}
