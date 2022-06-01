package com.example.mny.Model;

import java.io.Serializable;

public class CustomerGoods extends Goods implements Serializable {

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
