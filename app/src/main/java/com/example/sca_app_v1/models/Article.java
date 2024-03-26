package com.example.sca_app_v1.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Article {
    private Integer id;
    private String name;
    private String description;
    private String code;
    private String photo;
    private Integer count_active;
    private String creation_date;
    private Integer removed;
    private Integer category_id;
    private Integer company_id;

    public Article(Integer id, String name, String description, String code, String photo, Integer count_active, String creation_date, Integer removed, Integer category_id, Integer company_id) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.code = code;
        this.photo = photo;
        this.count_active = count_active;
        this.creation_date = creation_date;
        this.removed = removed;
        this.category_id = category_id;
        this.company_id = company_id;
    }

    public Article(JSONObject article) throws JSONException {
        this.id = (int) article.getInt("id");
        this.name = article.getString("name");
        this.description = article.getString("description");
        this.code = article.getString("code");
        this.photo = article.getString("photo");
        this.count_active = (int) article.getInt("count_active");
        this.creation_date = article.getString("creation_date");
        this.removed = (int) article.getInt("removed");
        this.category_id = (int) article.get("category_id");
        this.company_id = (int) article.getInt("company_id");
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public String getPhoto() {
        return photo;
    }

    public Integer getCount_active() {
        return count_active;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public Integer getRemoved() {
        return removed;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public Integer getCompany_id() {
        return company_id;
    }

}
