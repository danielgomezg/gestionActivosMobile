package com.example.sca_app_v1.models;

import android.content.Context;
import android.database.Cursor;

import com.example.sca_app_v1.home_app.bdLocal.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private DatabaseHelper dbHelper;

    public Article() {
    }

    public Article(Cursor cursor) {

        int _idIndex = cursor.getColumnIndex("id");
        if (_idIndex != -1) this.id = cursor.getInt(_idIndex);

        int _nameIndex = cursor.getColumnIndex("name");
        if (_nameIndex != -1) this.name = cursor.getString(_nameIndex);

        int _descriptionIndex = cursor.getColumnIndex("description");
        if (_descriptionIndex != -1) this.description = cursor.getString(_descriptionIndex);

        int _codeIndex = cursor.getColumnIndex("code");
        if (_codeIndex != -1) this.code = cursor.getString(_codeIndex);

        int _photoIndex = cursor.getColumnIndex("photo");
        if (_photoIndex != -1) this.photo = cursor.getString(_photoIndex);

        int _count_activeIndex = cursor.getColumnIndex("count_active");
        if (_count_activeIndex != -1) this.count_active = cursor.getInt(_count_activeIndex);

        int _creation_dateIndex = cursor.getColumnIndex("creation_date");
        if (_creation_dateIndex != -1) this.creation_date = cursor.getString(_creation_dateIndex);

        int _removedIndex = cursor.getColumnIndex("removed");
        if (_removedIndex != -1) this.removed = cursor.getInt(_removedIndex);

        int _category_idIndex = cursor.getColumnIndex("category_id");
        if (_category_idIndex != -1) this.category_id = cursor.getInt(_category_idIndex);

        int _company_idIndex = cursor.getColumnIndex("company_id");
        if (_company_idIndex != -1) this.company_id = cursor.getInt(_company_idIndex);

    }

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

    public String printData() {
        return "Article{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", code='" + code + '\'' +
                ", photo='" + photo + '\'' +
                ", count_active=" + count_active +
                ", creation_date='" + creation_date + '\'' +
                ", removed=" + removed +
                ", category_id=" + category_id +
                ", company_id=" + company_id +
                '}';
    }


    public void getAllArticles(Context context) {
        System.out.println("IN GET ALL ARTICLES");
        String sql = "SELECT * FROM articulo";

        try {

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            List<Map<String, String>> articles = dbHelper.executeSqlQuery(sql);
            
        
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public List<Article> getArticles(Context context) {
        System.out.println("IN GET ALL ARTICLES");
        String sql = "SELECT * FROM articulo";

        try {

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Cursor cursor = dbHelper.executeQuery(sql);

            List<Article> articles = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    articles.add(new Article(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();
            return articles;
        
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }

    }



}
