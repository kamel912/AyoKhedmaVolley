package com.ayokhedma.ayokhedma.Models;

/**
 * Created by MK on 01/06/2017.
 */

public class CategoryModel {
    private String id,name;

    public CategoryModel(String cat_id, String cat_name) {
        this.id = cat_id;
        this.name = cat_name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
