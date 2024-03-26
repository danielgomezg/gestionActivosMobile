package com.example.sca_app_v1.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Office {

    private Integer id;
    private String description;
    private Integer floor;
    private String name_in_charge;
    private Integer removed;
    private Integer sucursal_id;

    public Office(Integer id, String description, Integer floor, String name_in_charge, Integer removed, Integer sucursal_id) {
        this.id = id;
        this.description = description;
        this.floor = floor;
        this.name_in_charge = name_in_charge;
        this.removed = removed;
        this.sucursal_id = sucursal_id;
    }

    public Office(JSONObject office) throws JSONException {
        this.id = (int) office.get("id");
        this.description = office.get("description").toString();
        this.floor = (int) office.get("floor");
        this.name_in_charge = office.get("name_in_charge").toString();
        this.removed = (int) office.get("removed");
        this.sucursal_id = (int) office.get("sucursal_id");
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Integer getFloor() {
        return floor;
    }

    public String getName_in_charge() {
        return name_in_charge;
    }

    public Integer getRemoved() {
        return removed;
    }

    public Integer getSucursal_id() {
        return sucursal_id;
    }

}
