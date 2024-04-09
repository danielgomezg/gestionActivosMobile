package com.example.sca_app_v1.models;

import android.content.Context;
import android.database.Cursor;

import com.example.sca_app_v1.home_app.bdLocal.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Office {

    private Integer id;
    private String description;
    private Integer floor;
    private String name_in_charge;
    private Integer removed;
    private Integer sucursal_id;

    public Office() {
    }

    public Office(Cursor cursor) {

        int _idIndex = cursor.getColumnIndex("id");
        if (_idIndex != -1) this.id = cursor.getInt(_idIndex);

        int _descriptionIndex = cursor.getColumnIndex("description");
        if (_descriptionIndex != -1) this.description = cursor.getString(_descriptionIndex);

        int _floorIndex = cursor.getColumnIndex("floor");
        if (_floorIndex != -1) this.floor = cursor.getInt(_floorIndex);

        int _name_in_chargeIndex = cursor.getColumnIndex("name_in_charge");
        if (_name_in_chargeIndex != -1) this.name_in_charge = cursor.getString(_name_in_chargeIndex);

        int _removedIndex = cursor.getColumnIndex("removed");
        if (_removedIndex != -1) this.removed = cursor.getInt(_removedIndex);

        int _sucursal_idIndex = cursor.getColumnIndex("sucursal_id");
        if (_sucursal_idIndex != -1) this.sucursal_id = cursor.getInt(_sucursal_idIndex);

    }

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

    public String getFullName() {
        return  this.floor + " - " + this.description;
    }

    public List<Office> getOfficeStore(Context context, Integer sucursal_id) {

        List<Office> offices = new ArrayList<>();
        String sql = "SELECT * FROM oficina WHERE sucursal_id = ? AND removed = 0";

        try {

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Cursor cursor = dbHelper.executeQuery(sql, new String[]{sucursal_id.toString()});
            if (cursor.moveToFirst()) {
                do {
                    offices.add(new Office(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();
            return offices;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
