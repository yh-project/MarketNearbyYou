package com.example.mny;

public class Customer_Info {
    private String email;
    private String nickname;
    private String number;

    public Customer_Info(String email, String nickname ,String number) {
        this.email = email;
        this.nickname = nickname;
        this.number = number;
    }

    public String getEmail() { return email; }
    public String getNickname() { return nickname; }
    public String getNumber() { return number; }

    public void setEmail(String email) { this.email = email; }
    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setNumber(String number) { this.number = number; }
}
