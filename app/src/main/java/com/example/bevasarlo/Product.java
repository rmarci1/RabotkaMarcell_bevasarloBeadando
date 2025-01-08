package com.example.bevasarlo;

public class Product {
    private String name;
    private double unitPrice;
    private double quantity;
    private String unit;

    public Product(String name, double unitPrice, double quantity, String unit) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public double getGrossPrice() {
        return Math.round(unitPrice * quantity * 100.0) / 100.0;
    }
}

