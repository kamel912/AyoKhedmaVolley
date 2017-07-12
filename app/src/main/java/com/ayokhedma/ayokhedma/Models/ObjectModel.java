package com.ayokhedma.ayokhedma.Models;

import java.util.List;

/**
 * Created by MK on 01/06/2017.
 */

public class ObjectModel {
    private String id,category,catId,name,region,street,beside,description,color,count;
    private float rate;
    private List<String> phone;

    public ObjectModel(String id, String category, String catId, String name, String region, String street, String beside, String count, float rate, String description, String color, List<String> phone) {
        this.id = id;
        this.category = category;
        this.catId = catId;
        this.name = name;
        this.region = region;
        this.street = street;
        this.beside = beside;
        this.rate = rate;
        this.count = count;
        this.description = description;
        this.color = color;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getCatId() {
        return catId;
    }

    public String getName() {
        return name;
    }

    public String getRegion() {
        return region;
    }

    public String getStreet() {
        return street;
    }

    public String getBeside() {
        return beside;
    }

    public float getRate() {
        return rate;
    }

    public String getCount() {
        return count;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public List<String> getPhone() {
        return phone;
    }
}
