package com.example.models.admin;

public class Brand {
    private String name;
    private String description;
    public Brand(String name , String des)
    {
        this.name=name;
        this.description=des;
    }
    // get set 
    public String getName()
    {
        return name;
    }
    public String getDescription()
    {
        return description;
    }
    // set we dont necessary
    
}
