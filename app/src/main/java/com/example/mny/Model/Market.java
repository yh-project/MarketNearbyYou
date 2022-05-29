package com.example.mny.Model;

public class Market extends User {
    String marketname;
    int marketType= 0;
    String address;
    String address_detail;
    String open;
    String close;
    String start;
    String finish;
    String term;

    public Market() {

    }

    public Market(String email, String number, int banCount, boolean isBanned, String marketname) {
        super(email, number, banCount, isBanned);
        this.marketname = marketname;
    }

    public String getMarketname() {
        return marketname;
    }
    public void setMarketname(String marketname) {
        this.marketname = marketname;
    }

    public int getMarketType() {
        return marketType;
    }
    public void setMarketType(int marketType) {
        this.marketType = marketType;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress_detail() {
        return address_detail;
    }

    public void setAddress_detail(String address_detail) {
        this.address_detail = address_detail;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
}
