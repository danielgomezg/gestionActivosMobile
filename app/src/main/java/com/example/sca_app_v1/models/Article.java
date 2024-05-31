package com.example.sca_app_v1.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sca_app_v1.R;
import com.example.sca_app_v1.home_app.bdLocal.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.*;


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
        if (photo == null || "null".equals(photo)) {
            return "";
        }
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

    public List<Article> getArticles(Context context, Integer offset, Integer limit) {
        System.out.println("IN GET ALL ARTICLES");
        String sql = "SELECT * FROM articulo WHERE removed = 0 ORDER BY id DESC LIMIT  " + offset + ", " + limit;

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

    public static Integer getArticlesCount(Context context) {
        System.out.println("IN GET ALL ARTICLES COUNT");
        String sql = "SELECT COUNT(*) FROM articulo WHERE removed = 0";

        try {

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Cursor cursor = dbHelper.executeQuery(sql);

            Integer count = 0;

            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }

            cursor.close();
            return count;
        
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    public List<Article> getArticlesName(Context context) {
        System.out.println("IN GET ALL ARTICLES Name");
        String sql = "SELECT id, name FROM articulo WHERE removed = 0";

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

    public boolean deleteArticleLocal(Context context, String photo) {
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
            System.out.println("Número de filas afectadas: " + rowsAffected);

            if (rowsAffected > 0 && photo != null && !photo.isEmpty()) {
                // Dividir la cadena de fotos en rutas individuales
                String[] photoPaths = photo.split(",");

                // Eliminar cada archivo de foto
                for (String photoPath : photoPaths) {
                    File photoFile = new File(photoPath);
                    if (photoFile.exists()) {
                        boolean deleted = photoFile.delete();
                        System.out.println("Foto en " + photoPath + " eliminada: " + deleted);
                    } else {
                        System.out.println("Foto en " + photoPath + " no encontrada.");
                    }
                }
            }

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

    public static void syncUploadArticles(Context context, String token, Integer companyId, SyncCallback callback) {
        System.out.println("SYNC UPLOAD ARTICLES");
        String sql = "SELECT * FROM articulo WHERE sync <> 0";

        try {

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Cursor cursor = dbHelper.executeQuery(sql);

            AtomicInteger operationCounter = new AtomicInteger(cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    System.out.println(cursor);
                    Article article = new Article(cursor);
                    System.out.println(article.getId());
                    System.out.println(article.printData());

                    switch (article.getSync()) {
                        case 1:
                            // Sincronizar nuevo activo
                            System.out.println("1111111111");
                            System.out.println(article.getSync());
                            article.syncCreate(context, token, operationCounter, callback);
                            break;
                        
                        case 2:
                            // Sincronizar activo modificado
                            System.out.println("222222222222");
                            System.out.println(article.getSync());
                            article.syncUpdate(context, token, companyId, operationCounter, callback);
                            break;

                        case 3:
                            // Sincronizar activo eliminado
                            System.out.println("33333333333333");
                            System.out.println(article.getSync());
                            article.syncDelete(context, token, companyId, operationCounter, callback);
                            break;
                        
                    }
                } while (cursor.moveToNext());
            }

            cursor.close();

            //callback.onSuccess();

        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(e);
        }
    }

    public void syncUpdate(Context context, String token, Integer companyId, AtomicInteger operationCounter, SyncCallback callback) {
        // URL para actualizar el artículo
        //String url = "http://192.168.100.8:9000/article/" + this.getId();
        String url = context.getString(R.string.BASE_URL) + "/article/" + this.getId();
        RequestQueue queue = Volley.newRequestQueue(context);

        // Lista para almacenar las URLs de las imágenes subidas correctamente
        List<String> photoUrls = new ArrayList<>();

        // `StringBuilder` para concatenar las imágenes subidas
        StringBuilder photoUrlsString = new StringBuilder();

        // Dividir la cadena de fotos del artículo en un array
        String[] photosArticle = this.getPhoto().split(",");

        // Contador para llevar un registro de cuántas imágenes se han procesado
        final AtomicInteger handledCount = new AtomicInteger(0);
        final int totalPhotos = photosArticle.length;

        // Lista para mantener un registro de las imágenes subidas
        Set<String> uploadedPhotos = new HashSet<>();

        // Definir el callback para manejar la subida de imágenes
        UploadImageCallback uploadCallback = new UploadImageCallback() {
            @Override
            public void onSuccess(String photoUrl) {
                // Almacenar la URL de la imagen subida y marcarla como subida
                System.out.println("Imagen subida: " + photoUrl);
                photoUrls.add(photoUrl);
                handledCount.incrementAndGet();

                // Verificar si se ha terminado de procesar todas las imágenes
                checkCompletion();
            }

            @Override
            public void onError(String errorMessage) {
                // Manejar el error de `uploadImage()`
                System.out.println("Error al subir la imagen: " + errorMessage);
                handledCount.incrementAndGet();

                // Verificar si se ha terminado de procesar todas las imágenes
                checkCompletion();
            }

            // Método para verificar si todas las imágenes se han procesado
            private void checkCompletion() {
                System.out.println("Verificando si todas las imágenes se han procesado");
                if (handledCount.get() == totalPhotos) {
                    System.out.println("Todas las imágenes procesadas");
                    // Combinar las URLs de imágenes subidas
                    for (String url : photoUrls) {
                        if (photoUrlsString.length() > 0) {
                            photoUrlsString.append(",");
                        }
                        photoUrlsString.append(url);
                    }
                    System.out.println("URLS combinadas: " + photoUrlsString.toString());

                    handledCount.incrementAndGet();
                    // Actualizar el artículo
                    updateArticleAPI(context, token, companyId, queue, url, photoUrlsString.toString(), operationCounter, callback);
                }
            }
        };

        /*if (this.getPhoto().equals("")){
            updateArticleAPI(context, token, companyId, queue, url, "", operationCounter, callback);
        }*/

        // Subir solo las imágenes que contienen "mobile_local"
        for (String photo : photosArticle) {
            System.out.println("Procesando imagen: " + photo);
            if (photo.contains("mobile_local")) {
                // Solo subir imágenes nuevas (no duplicadas)
                if (!uploadedPhotos.contains(photo)) {
                    uploadedPhotos.add(photo);
                    System.out.println("Subiendo imagen: " + photo);
                    uploadImage(token, companyId, photo, uploadCallback, context);
                }
            } else {
                // Si la imagen ya está subida, agregarla a la lista de URLs
                System.out.println("Imagen ya subida o no requiere subida: " + photo);
                uploadedPhotos.add(photo);
                photoUrls.add(photo);
                handledCount.incrementAndGet();
            }
        }

        // Llamar a updateArticleAPI() si ninguna imagen requirió subida
        if (handledCount.get() == totalPhotos) {
            System.out.println("Ninguna imagen requería subida. Actualizando el artículo.");
            handledCount.incrementAndGet();
            updateArticleAPI(context, token, companyId, queue, url, this.getPhoto(), operationCounter, callback);
        }
    }

    // ACTUALIZA EL ARTICULO DE LA API
    private void updateArticleAPI(Context context, String token, Integer companyId, RequestQueue queue, String url, String photoName, AtomicInteger operationCounter, SyncCallback callback) {
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", getName());
            jsonBody.put("description", getDescription());
            jsonBody.put("code", getCode());
            jsonBody.put("photo", photoName);
            jsonBody.put("category_id", getCategory_id());

            System.out.println("JSONBODYYYY " + jsonBody);

            // Crear una solicitud JSON a la API para actualizar el artículo
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Manejar la respuesta exitosa
                            System.out.println("RESPONSE UPDATE ARTICLE");
                            System.out.println(response.toString());

                            try {
                                // Acceder al campo "code" en la respuesta JSON
                                String code = response.getString("code");
                                if (code.equals("201")) {
                                    boolean successful = updateArticleSync(context);
                                    System.out.println("SUCCESSFUL " + successful);
                                    operationCompleted(operationCounter, callback);
                                }
                            } catch (JSONException e) {
                                System.out.println("Error al acceder al JSON de respuesta: " + e.getMessage());
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Manejar el error
                            error.printStackTrace();
                            System.out.println("Error: " + error);
                        }
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    // Agregar los encabezados personalizados a la solicitud
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + token); // Añadir token de autenticación
                    headers.put("companyId", String.valueOf(companyId)); // Añadir el ID de la empresa
                    return headers;
                }
            };

            // Agregar la solicitud a la cola
            queue.add(jsonRequest);
        } catch (JSONException e) {
            System.out.println("Error al crear el cuerpo JSON: " + e.getMessage());
        }
    }

    public void syncCreate(Context context, String token, AtomicInteger operationCounter, SyncCallback callbackSync) {
        //PETICION A LA API PARA CREAR EL ARTICULO
        //String url = "http://192.168.100.8:9000/article";
        String url = context.getString(R.string.BASE_URL) + "/article";
        RequestQueue queue = Volley.newRequestQueue(context);

        // Lista para almacenar las URLs de las imágenes subidas correctamente
        List<String> photoUrls = new ArrayList<>();

        // `StringBuilder` para concatenar las imágenes subidas
        StringBuilder photoUrlsString = new StringBuilder();

        // Dividir la cadena de fotos del artículo en un array
        String[] photosArticle = this.getPhoto().split(",");

        // Contador para llevar un registro de cuántas imágenes se han procesado
        final AtomicInteger handledCount = new AtomicInteger(0);
        final int totalPhotos = photosArticle.length;

        // Lista para mantener un registro de las imágenes subidas
        Set<String> uploadedPhotos = new HashSet<>();

        // Definir un callback para manejar el resultado de `uploadImage()`
        UploadImageCallback callback = new UploadImageCallback() {
            @Override
            public void onSuccess(String photoUrl) {
                // Almacenar la URL de la imagen subida y marcarla como subida
                System.out.println("Imagen subida: " + photoUrl);

                photoUrls.add(photoUrl);
                handledCount.incrementAndGet();

                // Verificar si se ha terminado de procesar todas las imágenes
                checkCompletion();
            }

            @Override
            public void onError(String errorMessage) {
                // Manejar el error de `uploadImage()`
                System.out.println("Error al subir la imagen: " + errorMessage);
                handledCount.incrementAndGet();

                // Verificar si se ha terminado de procesar todas las imágenes
                checkCompletion();
            }

            // Método para verificar si todas las imágenes se han procesado
            private void checkCompletion() {
                System.out.println("Verificando si todas las imágenes se han procesado");
                if (handledCount.get() == totalPhotos) {
                    System.out.println("Todas las imágenes procesadas");
                    // Combinar las URLs de imágenes subidas
                    for (String url : photoUrls) {
                        if (photoUrlsString.length() > 0) {
                            photoUrlsString.append(",");
                        }
                        photoUrlsString.append(url);
                    }
                    System.out.println("URLS combinadas: " + photoUrlsString.toString());

                    // Crear el artículo
                    createArticleAPI(context, token, queue, url, photoUrlsString.toString(), operationCounter, callbackSync);
                }
            }

        };

        if (this.getPhoto().equals("")){
            createArticleAPI(context, token, queue, url, "", operationCounter, callbackSync);
        }

        // Subir solo las imágenes que contienen "mobile_local"
        for (String photo : photosArticle) {
            System.out.println("Procesando imagen: " + photo);
            if (photo.contains("mobile_local")) {
                // Solo subir imágenes nuevas (no duplicadas)
                if (!uploadedPhotos.contains(photo)) {
                    uploadedPhotos.add(photo);
                    System.out.println("Subiendo imagen: " + photo);
                    uploadImage(token, this.getCompany_id(), photo, callback, context);
                }
            } else {
                // Si la imagen ya está subida, agregarla a la lista de URLs
                System.out.println("Imagen ya subida o no requiere subida: " + photo);
                //if (!uploadedPhotos.contains(photo)) {
                uploadedPhotos.add(photo);
                photoUrls.add(photo);
                handledCount.incrementAndGet();
                //}
            }
        }

    }

    public void createArticleAPI(Context context, String token, RequestQueue queue, String url, String photoName, AtomicInteger operationCounter, SyncCallback callback) {

        // Crear el objeto JSON para enviar en el cuerpo de la solicitud
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", this.getName());
            jsonBody.put("description", this.getDescription());
            jsonBody.put("code", this.getCode());
            jsonBody.put("photo", photoName);
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
                            if(code.equals("201")){
                                JSONObject result = response.getJSONObject("result");
                                Active.updateArticleIdServer(context, id, result.getInt("id"));
                                boolean successful = updateArticleSync(context);
                                operationCompleted(operationCounter, callback);

                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
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

    public void syncDelete(Context context, String token, Integer companyId, AtomicInteger operationCounter, SyncCallback callback) {
        //PETICION A LA API PARA ELIMINAR EL ARTICULO
        //String url = "http://192.168.100.8:9000/article/" + this.getId();
        String url = context.getString(R.string.BASE_URL) + "/article/" + this.getId();
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
                                operationCompleted(operationCounter, callback);
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

    public void uploadImage(String token, Integer companyId, String imagePath, UploadImageCallback callback, Context context) {
        // URL del endpoint
        //String url = "http://192.168.100.8:9000/image_article";
        String url = context.getString(R.string.BASE_URL) + "/image_article";

        // Crear un objeto File para la imagen
        File imageFile = new File(imagePath);

        // Crear un cliente OkHttp
        OkHttpClient client = new OkHttpClient();

        // Crear el cuerpo del archivo con el tipo de medio "image/jpeg"
        RequestBody fileBody = RequestBody.create(imageFile, MediaType.get("image/jpeg"));

        // Obtener el nombre completo del archivo
        String fullFileName = imageFile.getName();

        // Extraer solo el nombre del archivo después del último guion
        String NameImageArt = fullFileName.substring(fullFileName.lastIndexOf('-') + 1);

        // Crear el cuerpo de la solicitud con el archivo y otros datos
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", NameImageArt, fileBody)
                .addFormDataPart("companyId", String.valueOf(companyId));

        // Crear la solicitud
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(multipartBodyBuilder.build())
                .addHeader("Authorization", "Bearer " + token) // Añadir el token en la cabecera
                .build();

        // Enviar la solicitud de forma asíncrona
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // Llamar al callback con un mensaje de error
                e.printStackTrace();
                callback.onError("Error al subir la imagen: " + e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                // Manejar la respuesta exitosa
                if (response.isSuccessful()) {
                    // Analizar el cuerpo de la respuesta como un objeto JSON
                    String responseBody = response.body().string();
                    try {
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        // Verificar el código en la respuesta JSON
                        String code = jsonResponse.getString("code");
                        if (code.equals("201")) {
                            String photoUrl = jsonResponse.getString("result");
                            // Llamar al callback con el URL de la imagen
                            callback.onSuccess(photoUrl);
                        } else {
                            callback.onError("Error en la respuesta: " + jsonResponse.getString("message"));
                        }
                    } catch (JSONException e) {
                        callback.onError("Error al analizar la respuesta JSON: " + e.getMessage());
                    }
                } else {
                    callback.onError("Error al subir la imagen: " + response.message());
                }
            }
        });
    }

    public static String savePhoto(Context context, Object imageSource, String nameArt) {
        try {
            InputStream inputStream = null;

            // Determinar el tipo de source (Bitmap o Uri)
            if (imageSource instanceof Uri) {
                // Si es un Uri, obtener el InputStream desde el Uri
                inputStream = context.getContentResolver().openInputStream((Uri) imageSource);
            } else if (imageSource instanceof Bitmap) {
                // Si es un Bitmap, convertirlo en un ByteArrayInputStream
                Bitmap bitmap = (Bitmap) imageSource;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] bitmapData = byteArrayOutputStream.toByteArray();
                inputStream = new ByteArrayInputStream(bitmapData);
            }

            // Crear la carpeta imagesArticles en el almacenamiento interno si no existe
            File imagesDir = new File(context.getFilesDir(), "imagesArticles");
            if (!imagesDir.exists()) {
                imagesDir.mkdirs();
            }

            // Generar un UUID para el nombre del archivo
            String uuid = UUID.randomUUID().toString();

            // Crear el nombre del archivo con el UUID y "article_photo.jpg"
            String fileName = uuid + "-" + "mobile_local" + "-" + nameArt + ".jpg";

            // Crear un archivo para guardar la imagen en la carpeta imagesArticles
            File imageFile = new File(imagesDir, fileName);
            OutputStream outputStream = new FileOutputStream(imageFile);

            // Copiar la imagen del InputStream al OutputStream
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Cerrar los flujos de entrada y salida
            inputStream.close();
            outputStream.close();

            // Mostrar un mensaje indicando que la imagen ha sido guardada
            //Toast.makeText(context, "Imagen guardada exitosamente", Toast.LENGTH_SHORT).show();

            // Devolver la ruta absoluta del archivo
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            //Toast.makeText(context, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    interface UploadImageCallback {
        void onSuccess(String photoUrl);
        void onError(String errorMessage);
    }

    public interface SyncCallback {
        void onSuccess();
        void onError(Exception e);
    }

    private static void operationCompleted(AtomicInteger operationCounter, SyncCallback callback) {
        // Decrementar el contador de operaciones y verificar si todas las operaciones han finalizado
        if (operationCounter.decrementAndGet() == 0) {
            // Todas las operaciones han finalizado, llamar a onSuccess()
            callback.onSuccess();
        }
    }
}
