package com.example.mny.Model;

public class User {
    String email = "";
    String number = "";
    int banCount = 0;
    boolean isBanned = false;

    public User() {

    }

    public User(String email, String number, int banCount, boolean isBanned) {
        this.email = email;
        this.number = number;
        this.banCount = banCount;
        this.isBanned = isBanned;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public int getBanCount() { return banCount; }
    public void setBanCount(int banCount) { this.banCount = banCount; }

    public boolean getisBanned() { return isBanned; }
    public void setisBanned(boolean isBanned) { this.isBanned = isBanned; }
}
