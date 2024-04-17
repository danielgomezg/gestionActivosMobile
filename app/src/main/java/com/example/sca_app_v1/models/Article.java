package com.example.sca_app_v1.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sca_app_v1.home_app.bdLocal.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    private Integer sync;
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

        int _sync_idIndex = cursor.getColumnIndex("sync");
        if (_sync_idIndex != -1) this.sync = cursor.getInt(_sync_idIndex);

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

    public Integer getSync() { return sync; }

    public void setSync(Integer sync) {
        this.sync = sync;
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

    public String getArticleName(int articleId, List<Article> articleList) {
        for (Article article : articleList) {
            if (article.getId() == articleId) {
                return article.getName();
            }
        }
        return null;
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

    public List<Article> getArticles(Context context, Integer offset) {
        System.out.println("IN GET ALL ARTICLES");
        String sql = "SELECT * FROM articulo WHERE removed = 0 ORDER BY id DESC LIMIT  " + offset + ", 50";

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
            values.put("sync", 1);
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
            if (!this.sync.equals(1)) {
                values.put("sync", 2);
            }
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

    public boolean deleteArticle(Context context) {
        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();

            db.beginTransaction();

            // Crear un ContentValues con los nuevos valores del artículo
            ContentValues values = new ContentValues();
            values.put("removed", 1);
            values.put("sync", 3);

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

    public boolean deleteArticleLocal(Context context) {
        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();

            db.beginTransaction();

            // Definir la condición WHERE para la actualización (basado en el ID del artículo)
            String whereClause = "id = ?";
            String[] whereArgs = {String.valueOf(this.id)}; // El ID del artículo a actualizar

            // Actualizar el registro en la base de datos
            int rowsAffected = db.delete("articulo", whereClause, whereArgs);

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

    public boolean updateArticleSync(Context context){
        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();

            db.beginTransaction();

            // Crear un ContentValues con los nuevos valores del artículo
            ContentValues values = new ContentValues();
            values.put("sync", 0);

            // Definir la condición WHERE para la actualización (basado en el ID del artículo)
            String whereClause = "id = ?";
            String[] whereArgs = {String.valueOf(this.id)};

            // Actualizar el registro en la base de datos
            int rowsAffected = db.update("articulo", values, whereClause, whereArgs);

            db.setTransactionSuccessful();

            return rowsAffected > 0;
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

    public Article getArticleById(Context context, Integer articleId) {
        String sql = "SELECT * FROM articulo WHERE id = ?";

        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Cursor cursor = dbHelper.executeQuery(sql, new String[]{articleId.toString()});

            if (cursor.moveToFirst()) {
                return new Article(cursor);
            }

            cursor.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Metodo que detecta si hay articulos sin sincronizar
    /*
     * Estados de sincronización (async column):
     * 0: sin cambios, no se sincroniza
     * 1: nuevo activo, se sincroniza
     * 2: activo modificado, se sincroniza
     * 3: activo eliminado, se sincroniza
     */
    public static boolean hasUnsyncedArticles(Context context) {
        String sql = "SELECT * FROM articulo WHERE sync <> 0 LIMIT 1";

        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Cursor cursor = dbHelper.executeQuery(sql);

            boolean hasUnsyncedArticles = cursor.moveToFirst();

            cursor.close();
            return hasUnsyncedArticles;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void syncUploadArticles(Context context, String token, Integer companyId) {
        System.out.println("SYNC UPLOAD ARTICLES");
        String sql = "SELECT * FROM articulo WHERE sync <> 0";

        try {

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Cursor cursor = dbHelper.executeQuery(sql);

            List<Article> articles = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    System.out.println(cursor);
                    Article article = new Article(cursor);
                    System.out.println(article.getId());
                    System.out.println(article.printData());

                    switch (article.getSync()) {
                        case 1:
                            // Sincronizar nuevo activo
                            System.out.println("SSSSSSSSSS");
                            System.out.println(article.getSync());
                            article.syncCreate(context, token);
                            break;
                        
                        case 2:
                            // Sincronizar activo modificado
                            System.out.println("SSSSSSSSSS");
                            System.out.println(article.getSync());
                            article.syncUpdate(context, token, companyId);
                            break;

                        case 3:
                            // Sincronizar activo eliminado
                            System.out.println("SSSSSSSSSS");
                            System.out.println(article.getSync());
                            article.syncDelete(context, token, companyId);
                            break;
                        
                    }


                } while (cursor.moveToNext());
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void syncUpdate(Context context, String token, Integer companyId) {
        //PETICION A LA API PARA ACTUALIZAR EL ARTICULO
        String url = "http://10.0.2.2:9000/article/" + this.getId();
        Integer idArt = this.getId();
        RequestQueue queue = Volley.newRequestQueue(context);

        // Crear el objeto JSON para enviar en el cuerpo de la solicitud
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", this.getName());
            jsonBody.put("description", this.getDescription());
            jsonBody.put("code", this.getCode());
            jsonBody.put("photo", this.getPhoto());
            jsonBody.put("category_id", this.getCategory_id());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("RESPONSE UPDATE ARTICLE");
                    System.out.println(response.toString());
                    String code = "0";
                    try {
                        code = response.getString("code");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    if(code.equals("201")){
                        boolean successful = updateArticleSync(context);
                        if (successful){
                            Article art = new Article();
                            art = art.getArticleById(context, idArt);
                            System.out.println("FIN");
                            System.out.println(art.getSync());
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    System.out.println("Error: " + error);
                }
            }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token); // Reemplaza 'token' con tu token de autenticación
                headers.put("companyId", String.valueOf(companyId));
                return headers;
            }
        };
        // Agregar la solicitud a la cola
        queue.add(jsonRequest);
    }

    public void syncCreate(Context context, String token) {
        //PETICION A LA API PARA CREAR EL ARTICULO
        String url = "http://10.0.2.2:9000/article";
        RequestQueue queue = Volley.newRequestQueue(context);
        Integer idArt = this.getId();

        // Crear el objeto JSON para enviar en el cuerpo de la solicitud
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", this.getName());
            jsonBody.put("description", this.getDescription());
            jsonBody.put("code", this.getCode());
            jsonBody.put("photo", this.getPhoto());
            jsonBody.put("category_id", this.getCategory_id());
            jsonBody.put("company_id", this.getCompany_id());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONSE CREATE ARTICLE");
                        System.out.println(response.toString());
                        String code = "0";
                        try {
                            code = response.getString("code");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        if(code.equals("201")){
                            boolean successful = updateArticleSync(context);
                            if (successful){
                                Article art = new Article();
                                art = art.getArticleById(context, idArt);
                                System.out.println("FIN");
                                System.out.println(art.getSync());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("Error: " + error);
            }
        }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                //headers.put("companyId", String.valueOf(companyId));
                return headers;
            }
        };
        // Agregar la solicitud a la cola
        queue.add(jsonRequest);
    }

    public void syncDelete(Context context, String token, Integer companyId) {
        //PETICION A LA API PARA ELIMINAR EL ARTICULO
        String url = "http://10.0.2.2:9000/article/" + this.getId();
        RequestQueue queue = Volley.newRequestQueue(context);
        Integer idArt = this.getId();

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONSE DELETE ARTICLE");
                        System.out.println(response.toString());
                        String code = "0";
                        try {
                            code = response.getString("code");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        if(code.equals("201")){
                            boolean successful = updateArticleSync(context);
                            if (successful){
                                Article art = new Article();
                                art = art.getArticleById(context, idArt);
                                System.out.println("FIN");
                                System.out.println(art.getSync());
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("Error: " + error);
            }
        }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                headers.put("companyId", String.valueOf(companyId));
                return headers;
            }
        };
        // Agregar la solicitud a la cola
        queue.add(jsonRequest);
    }


}
