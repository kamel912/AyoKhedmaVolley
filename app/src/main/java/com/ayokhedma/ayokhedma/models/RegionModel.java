package com.ayokhedma.ayokhedma.models;

/**
 * Created by MK on 02/06/2017.
 */

public class RegionModel {
    private String id,name;

    public RegionModel(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
