package com.example.mny.Model;

public class Customer extends User{
    String nickname = "";

    public Customer() {

    }

    public Customer(String email, String number, int banCount, boolean isBanned, String nickname) {
        super(email, number, banCount, isBanned);
        this.nickname = nickname;
    }

    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }

}
