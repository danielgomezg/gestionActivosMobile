package com.example.sca_app_v1.models;

import android.content.Context;
import android.database.Cursor;

import com.example.sca_app_v1.home_app.bdLocal.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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

    public Store() {

    }

    public Store(Cursor cursor) {
        
        int _idIndex = cursor.getColumnIndex("id");
        if (_idIndex != -1) this.id = cursor.getInt(_idIndex);

        int _descriptionIndex = cursor.getColumnIndex("description");
        if (_descriptionIndex != -1) this.description = cursor.getString(_descriptionIndex);

        int _numberIndex = cursor.getColumnIndex("number");
        if (_numberIndex != -1) this.number = cursor.getString(_numberIndex);

        int _addressIndex = cursor.getColumnIndex("address");
        if (_addressIndex != -1) this.address = cursor.getString(_addressIndex);

        int _regionIndex = cursor.getColumnIndex("region");
        if (_regionIndex != -1) this.region = cursor.getString(_regionIndex);

        int _cityIndex = cursor.getColumnIndex("city");
        if (_cityIndex != -1) this.city = cursor.getString(_cityIndex);

        int _communeIndex = cursor.getColumnIndex("commune");
        if (_communeIndex != -1) this.commune = cursor.getString(_communeIndex);

        int _removedIndex = cursor.getColumnIndex("removed");
        if (_removedIndex != -1) this.removed = cursor.getInt(_removedIndex);

        int _company_idIndex = cursor.getColumnIndex("company_id");
        if (_company_idIndex != -1) this.company_id = cursor.getInt(_company_idIndex);

    }
    
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

    public Store(JSONObject store) throws JSONException {
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

    public String getFullName() {
        return number + " - " + description;
    }

    public List<Store> getStores(Context context) {

        List<Store> stores = new ArrayList<>();
        String sql = "SELECT * FROM sucursal WHERE removed = 0";

        try {

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Cursor cursor = dbHelper.executeQuery(sql);
            if (cursor.moveToFirst()) {
                do {
                    stores.add(new Store(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();
            return stores;

        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    public String getStoreInfo(int storeId, List<Store> storeList) {
        for (Store store : storeList) {
            if (store.getId() == storeId) {
                return store.getFullName();
            }
        }
        return null;
    }

}
