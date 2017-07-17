package com.ayokhedma.ayokhedma.models;

import java.util.List;

/**
 * Created by MK on 01/06/2017.
 */

public class ObjectModel {
    private String id,category,catId,name,region,street,beside,description,start1,end1,start2,end2,weekend,color, count;
    private float rate;
    private List<String> phone;

    public ObjectModel(String id, String category, String catId, String name, String region, String street, String beside, String start2, String weekend, float rate, String description, String start1, String end1, String end2, String color, String count, List<String> phone) {
        this.id = id;
        this.category = category;
        this.catId = catId;
        this.name = name;
        this.region = region;
        this.street = street;
        this.beside = beside;
        this.start2 = start2;
        this.weekend = weekend;
        this.rate = rate;
        this.description = description;
        this.start1 = start1;
        this.end1 = end1;
        this.end2 = end2;
        this.color = color;
        this.count = count;
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

    public String getDescription() {
        return description;
    }

    public String getStart1() {
        return start1;
    }

    public String getEnd1() {
        return end1;
    }

    public String getStart2() {
        return start2;
    }

    public String getEnd2() {
        return end2;
    }

    public String getWeekend() {
        return weekend;
    }

    public String getColor() {
        return color;
    }

    public List<String> getPhone() {
        return phone;
    }

    public String getCount() {
        return count;
    }
}
