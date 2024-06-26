package com.example.sca_app_v1.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Company {

    private Integer id;
    private String name;
    private String rut;
    private String country;
    private String contact_name;
    private String contact_email;
    private String contact_phone;
    private Integer removed;
    private String name_db;

    public Company(Integer id, String name, String rut, String country, String contact_name, String contact_email, String contact_phone, Integer removed, String name_db) {
        this.id = id;
        this.name = name;
        this.rut = rut;
        this.country = country;
        this.contact_name = contact_name;
        this.contact_email = contact_email;
        this.contact_phone = contact_phone;
        this.removed = removed;
        this.name_db = name_db;
    }

    public Company(){

    }

    public Company(JSONObject company) throws JSONException {
        this.id = (int) company.getInt("id");
        this.name = company.getString("name");
        this.rut = company.getString("rut");
        this.country = company.getString("country");
        this.contact_name = company.getString("contact_name");
        this.contact_email = company.getString("contact_email");
        this.contact_phone = company.getString("contact_phone");
        this.removed = (int) company.getInt("removed");
        this.name_db = company.getString("name_db");
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRut() {
        return rut;
    }

    public String getCountry() {
        return country;
    }

    public String getContact_name() {
        return contact_name;
    }

    public String getContact_email() {
        return contact_email;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public Integer getRemoved() {
        return removed;
    }

    public String getName_db() {
        return name_db;
    }

}
