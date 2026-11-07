package com.example.models.admin;

public class NewProduct {

    private String name;
    private String slug;
    private String description;
    private double price;
    private double salePrice;
    public String color;
    public String brand;
    public String category;
    private String img;

    // Constructor
    public NewProduct(String name, String slug, String description, double price, double salePrice, String color,
            String brand, String category,String img) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.price = price;
        this.salePrice = salePrice;
        this.color = color;
        this.brand = brand;
        this.category = category;
        this.img=img;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
    public double getSalePrice() {
        return salePrice;
    }

    public String getColor() {
        return color;
    }

    public String getBrand() {
        return brand;
    }
    public String getCategory(){
        return category;
    }
    public String getImg(){
        return img;
    }
}
