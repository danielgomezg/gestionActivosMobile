package com.example.sca_app_v1.models;

public class Store {

    private Integer id;
    private String description;
    private String number;
    private String address;
    private String region;
    private String city;
    private String commune;
    private Integer removed;
    private Integer company_id;

    public Store(Integer id, String description, String number, String address, String region, String city, String commune, Integer removed, Integer company_id) {
        this.id = id;
        this.description = description;
        this.number = number;
        this.address = address;
        this.region = region;
        this.city = city;
        this.commune = commune;
        this.removed = removed;
        this.company_id = company_id;
    }

    public Store(JSONObject store) {
        this.id = (int) store.getInt("id");
        this.description = store.getString("description");
        this.number = store.getString("number");
        this.address = store.getString("address");
        this.region = store.getString("region");
        this.city = store.getString("city");
        this.commune = store.getString("commune");
        this.removed = (int) store.getInt("removed");
        this.company_id = (int) store.getInt("company_id");
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getNumber() {
        return number;
    }

    public String getAddress() {
        return address;
    }

    public String getRegion() {
        return region;
    }

    public String getCity() {
        return city;
    }

    public String getCommune() {
        return commune;
    }

    public Integer getRemoved() {
        return removed;
    }

    public Integer getCompany_id() {
        return company_id;
    }

}
