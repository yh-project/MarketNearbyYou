package com.example.mny.Model;

public class CustomerGoods extends Goods {

    private boolean isReserved;

    public CustomerGoods() {

    }

    public CustomerGoods(String name, int price, String currentStock, String category, int max) {
        super(name, price, currentStock, category, max);
    }

    public boolean getStatus() {
        return isReserved;
    }
    public void setStatus(boolean reserved) {
        isReserved = reserved;
    }
}
