package com.example.sca_app_v1.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Category {

    private Integer id;
    private String description;
    private Integer parent_id;
    private Integer removed;

    public Category(Integer id, String description, Integer parent_id, Integer removed) {
        this.id = id;
        this.description = description;
        this.parent_id = parent_id;
        this.removed = removed;
    }

    public Category(JSONObject category) throws JSONException {
        this.id =  (int) category.get("id");
        this.description = category.get("description").toString();
        this.parent_id = (int) category.get("parent_id");
        this.removed = (int) category.get("removed");
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public Integer getRemoved() {
        return removed;
    }

}
