package com.example.sca_app_v1.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sca_app_v1.home_app.bdLocal.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Article implements Serializable {
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

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public void setCategory_id(Integer idCategory) {
        this.category_id = idCategory;
    }

    public Integer getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Integer idCompany) {
        this.company_id = idCompany;
    }

    //Se obtiene el id del articulo
    public int getArticleID(String code, List<Article> articleList) {
        for (Article article : articleList) {
            if (article.getCode().equals(code)) {
                return article.getId();
            }
        }
        return 0;
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

    public List<Article> getArticles(Context context) {
        System.out.println("IN GET ALL ARTICLES");
        String sql = "SELECT * FROM articulo WHERE removed = 0";

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

    public boolean createArticle(Context context) {
        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();

            db.beginTransaction();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = dateFormat.format(new Date());

            // Crear un ContentValues con los valores del nuevo artículo
            ContentValues values = new ContentValues();
            values.put("name", this.name);
            values.put("description", this.description);
            values.put("code", this.code);
            values.put("photo", this.photo);
            values.put("creation_date", currentDate);
            values.put("category_id", this.category_id);
            values.put("company_id", this.company_id);

            // Insertar el nuevo registro en la base de datos
            long newRowId = db.insert("articulo", null, values);

            db.setTransactionSuccessful();

            return newRowId != -1; // Devolver true si se insertó el nuevo registro correctamente
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }


    public boolean updateArticle(Context context) {
        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();

            db.beginTransaction();

            // Crear un ContentValues con los nuevos valores del artículo
            ContentValues values = new ContentValues();
            values.put("name", this.name);
            values.put("description", this.description);
            values.put("code", this.code);
            values.put("photo", this.photo);
            values.put("category_id", this.category_id);

            // Definir la condición WHERE para la actualización (basado en el ID del artículo)
            String whereClause = "id = ?";
            String[] whereArgs = {String.valueOf(this.id)}; // El ID del artículo a actualizar

            // Actualizar el registro en la base de datos
            int rowsAffected = db.update("articulo", values, whereClause, whereArgs);

            db.setTransactionSuccessful();

            return rowsAffected > 0; // Devolver true si se actualizó al menos un registro
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
        }
    }



}
