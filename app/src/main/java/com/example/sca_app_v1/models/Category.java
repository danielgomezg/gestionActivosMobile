package com.example.sca_app_v1.models;

import android.content.Context;
import android.database.Cursor;

import com.example.sca_app_v1.home_app.bdLocal.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Category {

    private Integer id;
    private String description;
    private Integer parent_id;
    private Integer removed;
    private String code;

    public Category() {

    }
    public Category(Cursor cursor) {
        int _idIndex = cursor.getColumnIndex("id");
        if (_idIndex != -1) this.id = cursor.getInt(_idIndex);

        int _descriptionIndex = cursor.getColumnIndex("description");
        if (_descriptionIndex != -1) this.description = cursor.getString(_descriptionIndex);

        int _parent_idIndex = cursor.getColumnIndex("parent_id");
        if (_parent_idIndex != -1) this.parent_id = cursor.getInt(_parent_idIndex);

        int _removedIndex = cursor.getColumnIndex("removed");
        if (_removedIndex != -1) this.removed = cursor.getInt(_removedIndex);
    }

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
        this.code = category.get("code").toString();
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    //Se obtiene la description de la categoria
    public String getCategoryDescription(int categoryId, List<Category> categoryList) {
        for (Category category : categoryList) {
            if (category.getId() == categoryId) {
                return category.getDescription();
            }
        }
        return null;
    }

    //Se obtiene el id de la categoria
    public int getCategoryID(String categoryDescription, List<Category> categoryList) {
        for (Category category : categoryList) {
            if (category.getDescription().equals(categoryDescription)) {
                return category.getId();
            }
        }
        return 0;
    }

    public List<Category> getCategories(Context context) {
        System.out.println("IN GET ALL ARTICLES");
        String sql = "SELECT * FROM categoria";

        try {

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Cursor cursor = dbHelper.executeQuery(sql);

            List<Category> categories = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    categories.add(new Category(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();
            return categories;

        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    public Category getCategoryById(Context context, Integer categoryId) {
        String sql = "SELECT * FROM categoria WHERE id = ?";

        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Cursor cursor = dbHelper.executeQuery(sql, new String[]{categoryId.toString()});

            if (cursor.moveToFirst()) {
                return new Category(cursor);
            }

            cursor.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
