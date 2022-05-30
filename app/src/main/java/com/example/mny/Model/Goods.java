package com.example.mny.Model;

public class Goods {
    private String name;
    private int price;
    private String currentStock;
    private String category;
    private int max;

    public Goods() {

    }

    public Goods(String name, int price, String currentStock, String category, int max) {
        this.name = name;
        this.price = price;
        this.currentStock = currentStock;
        this.category = category;
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(String currentStock) {
        this.currentStock = currentStock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
}
