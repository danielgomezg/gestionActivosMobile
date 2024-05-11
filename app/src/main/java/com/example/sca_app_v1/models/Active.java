package com.example.sca_app_v1.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;

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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class Active implements Serializable {

    private Integer id;
    private String bar_code;
    private String virtual_code;
    private String comment;
    private String acquisition_date;
    private String accounting_document;
    private String accounting_record_number;
    private String name_in_charge_active;
    private String rut_in_charge_active;
    private String serie;
    private String model;
    private String state;
    private String brand;
    private String photo1;
    private String photo2;
    private String photo3;
    private String photo4;
    private String creation_date;
    private Integer removed;
    private Integer office_id;
    private Integer article_id;
    private Integer article_id_server;
    private Integer sync;

    public Active(){}

    public Active(Cursor cursor) {

        int _idIndex = cursor.getColumnIndex("id");
        if (_idIndex != -1) this.id = cursor.getInt(_idIndex);

        int _bar_codeIndex = cursor.getColumnIndex("bar_code");
        if (_bar_codeIndex != -1) this.bar_code = cursor.getString(_bar_codeIndex);

        int _virtual_codeIndex = cursor.getColumnIndex("virtual_code");
        if (_virtual_codeIndex != -1) this.virtual_code = cursor.getString(_virtual_codeIndex);

        int _commentIndex = cursor.getColumnIndex("comment");
        if (_commentIndex != -1) this.comment = cursor.getString(_commentIndex);

        int _acquisition_dateIndex = cursor.getColumnIndex("acquisition_date");
        if (_acquisition_dateIndex != -1) this.acquisition_date = cursor.getString(_acquisition_dateIndex);

        int _accounting_documentIndex = cursor.getColumnIndex("accounting_document");
        if (_accounting_documentIndex != -1) this.accounting_document = cursor.getString(_accounting_documentIndex);

        int _accounting_record_numberIndex = cursor.getColumnIndex("accounting_record_number");
        if (_accounting_record_numberIndex != -1) this.accounting_record_number = cursor.getString(_accounting_record_numberIndex);

        int _name_in_charge_activeIndex = cursor.getColumnIndex("name_in_charge_active");
        if (_name_in_charge_activeIndex != -1) this.name_in_charge_active = cursor.getString(_name_in_charge_activeIndex);

        int _rut_in_charge_activeIndex = cursor.getColumnIndex("rut_in_charge_active");
        if (_rut_in_charge_activeIndex != -1) this.rut_in_charge_active = cursor.getString(_rut_in_charge_activeIndex);

        int _serieIndex = cursor.getColumnIndex("serie");
        if (_serieIndex != -1) this.serie = cursor.getString(_serieIndex);

        int _modelIndex = cursor.getColumnIndex("model");
        if (_modelIndex != -1) this.model = cursor.getString(_modelIndex);

        int _stateIndex = cursor.getColumnIndex("state");
        if (_stateIndex != -1) this.state = cursor.getString(_stateIndex);

        int _brandIndex = cursor.getColumnIndex("brand");
        if (_brandIndex != -1) this.brand = cursor.getString(_brandIndex);

        int _photo1Index = cursor.getColumnIndex("photo1");
        if (_photo1Index != -1) this.photo1 = cursor.getString(_photo1Index);

        int _photo2Index = cursor.getColumnIndex("photo2");
        if (_photo2Index != -1) this.photo2 = cursor.getString(_photo2Index);

        int _photo3Index = cursor.getColumnIndex("photo3");
        if (_photo3Index != -1) this.photo3 = cursor.getString(_photo3Index);

        int _photo4Index = cursor.getColumnIndex("photo4");
        if (_photo4Index != -1) this.photo4 = cursor.getString(_photo4Index);

        int _creation_dateIndex = cursor.getColumnIndex("creation_date");
        if (_creation_dateIndex != -1) this.creation_date = cursor.getString(_creation_dateIndex);

        int _removedIndex = cursor.getColumnIndex("removed");
        if (_removedIndex != -1) this.removed = cursor.getInt(_removedIndex);

        int _office_idIndex = cursor.getColumnIndex("office_id");
        if (_office_idIndex != -1) this.office_id = cursor.getInt(_office_idIndex);

        int _article_idIndex = cursor.getColumnIndex("article_id");
        if (_article_idIndex != -1) this.article_id = cursor.getInt(_article_idIndex);

        int _sync_idIndex = cursor.getColumnIndex("sync");
        if (_sync_idIndex != -1) this.sync = cursor.getInt(_sync_idIndex);

        int _articleIdServerIndex = cursor.getColumnIndex("article_id_server");
        if (_articleIdServerIndex != -1) this.article_id_server = cursor.getInt(_articleIdServerIndex);

    }

    public Active(Integer id, String bar_code, String virtual_code, String comment, String acquisition_date, String accounting_document, String accounting_record_number, String name_in_charge_active, String rut_in_charge_active, String serie, String model, String state, String brand, String photo1, String photo2, String photo3, String photo4, String creation_date, Integer removed, Integer office_id, Integer article_id) {
        this.id = id;
        this.bar_code = bar_code;
        this.virtual_code = virtual_code;
        this.comment = comment;
        this.acquisition_date = acquisition_date;
        this.accounting_document = accounting_document;
        this.accounting_record_number = accounting_record_number;
        this.name_in_charge_active = name_in_charge_active;
        this.rut_in_charge_active = rut_in_charge_active;
        this.serie = serie;
        this.model = model;
        this.state = state;
        this.brand = brand;
        this.photo1 = photo1;
        this.photo2 = photo2;
        this.photo3 = photo3;
        this.photo4 = photo4;
        this.creation_date = creation_date;
        this.removed = removed;
        this.office_id = office_id;
        this.article_id = article_id;
    }

    public Active(JSONObject active) throws JSONException {
        this.id = (int) active.getInt("id");
        this.bar_code = active.getString("bar_code");
        this.virtual_code = active.getString("virtual_code");
        this.comment = active.getString("comment");
        this.acquisition_date = active.getString("acquisition_date");
        this.accounting_document = active.getString("accounting_document");
        this.accounting_record_number = active.getString("accounting_record_number");
        this.name_in_charge_active = active.getString("name_in_charge_active");
        this.rut_in_charge_active = active.getString("rut_in_charge_active");
        this.serie = active.getString("serie");
        this.model = active.getString("model");
        this.state = active.getString("state");
        this.brand = active.getString("brand");
        this.photo1 = active.getString("photo1");
        this.photo2 = active.getString("photo2");
        this.photo3 = active.getString("photo3");
        this.photo4 = active.getString("photo4");
        this.creation_date = active.getString("creation_date");
        this.removed = (int) active.getInt("removed");
        this.office_id = (int) active.getInt("office_id");
        this.article_id = (int) active.getInt("article_id");
    }
    public Integer getArticle_id_server() {
        return article_id_server;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBar_code() {
        return bar_code;
    }

    public void setBar_code(String bar_code) {
        this.bar_code = bar_code;
    }

    public String getVirtual_code() {
        if (virtual_code == null) return "";
        return virtual_code;
    }

    public void setVirtual_code(String virtual_code) {
        this.virtual_code = virtual_code;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAcquisition_date() {
        return acquisition_date;
    }

    public void setAcquisition_date(String acquisition_date) {
        this.acquisition_date = acquisition_date;
    }

    public String getAccounting_document() {
        return accounting_document;
    }

    public void setAccounting_document(String accounting_document) {
        this.accounting_document = accounting_document;
    }

    public String getAccounting_record_number() {
        return accounting_record_number;
    }

    public void setAccounting_record_number(String accounting_record_number) {
        this.accounting_record_number = accounting_record_number;
    }

    public String getName_in_charge_active() {
        return name_in_charge_active;
    }

    public void setName_in_charge_active(String nameInChargeActive) {
        this.name_in_charge_active = nameInChargeActive;
    }

    public String getRut_in_charge_active() {
        return rut_in_charge_active;
    }

    public void setRut_in_charge_active(String rutInChargeActive) {
        this.rut_in_charge_active = rutInChargeActive;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPhoto1() {
        if (photo1 == null || "null".equals(photo1)) {
            return "";
        }
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        if (photo2 == null || "null".equals(photo2)) {
            return "";
        }
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        if (photo3 == null || "null".equals(photo3)) {
            return "";
        }
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
        if (photo4 == null || "null".equals(photo4)) {
            return "";
        }
        return photo4;
    }

    public void setPhoto4(String photo4) {
        this.photo4 = photo4;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public Integer getRemoved() {
        return removed;
    }

    public void setRemoved(int removed) {
        this.removed = removed;
    }

    public Integer getOffice_id() {
        return office_id;
    }

    public void setOffice_id(int office_id) {
        this.office_id = office_id;
    }

    public Integer getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public String printData() {
        return "Active{" +
                "id=" + id +
                ", BarCode='" + bar_code + '\'' +
                ", virtual_code='" + virtual_code + '\'' +
                ", model='" + model + '\'' +
                ", serie='" + serie + '\'' +
                ", brand=" + brand +
                ", creation_date='" + creation_date + '\'' +
                ", removed=" + removed +
                ", article_id=" + article_id +
                ", office_id=" + office_id +
                '}';
    }

    public List<Active> getActives(Context context) {
        System.out.println("IN GET ALL ACTIVES");
        String sql = "SELECT * FROM activo WHERE removed = 0 ORDER BY id DESC";

        try {

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Cursor cursor = dbHelper.executeQuery(sql);

            List<Active> actives = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    actives.add(new Active(cursor));
                } while (cursor.moveToNext());
            }

            cursor.close();
            return actives;

        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    public Integer getSync() { return sync; }

    public void setSync(Integer sync) {
        this.sync = sync;
    }

    public boolean createActive(Context context) {
        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();

            db.beginTransaction();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            String currentDate = dateFormat.format(new Date());

            // Crear un ContentValues con los valores del nuevo artículo
            ContentValues values = new ContentValues();
            values.put("bar_code", this.bar_code);
            values.put("virtual_code", this.virtual_code);
            values.put("comment", this.comment);
            values.put("acquisition_date", this.acquisition_date);
            values.put("accounting_document", this.accounting_document);
            values.put("accounting_record_number", this.accounting_record_number);
            values.put("name_in_charge_active", this.name_in_charge_active);
            values.put("rut_in_charge_active", this.rut_in_charge_active);
            values.put("serie", this.serie);
            values.put("model", this.model);
            values.put("state", this.state);
            values.put("brand", this.brand);
            values.put("photo1", this.photo1);
            values.put("photo2", this.photo2);
            values.put("photo3", this.photo3);
            values.put("photo4", this.photo4);
            values.put("sync", 1);
            values.put("creation_date", currentDate);
            //values.put("creation_date", this.removed);
            values.put("office_id", this.office_id);
            values.put("article_id", this.article_id);
            values.put("article_id_server", this.article_id);

            // Insertar el nuevo registro en la base de datos
            long newRowId = db.insert("activo", null, values);

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

    public boolean updateActive (Context context) {

        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();

            db.beginTransaction();

            // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            // Crear un ContentValues con los valores del nuevo artículo
            ContentValues values = new ContentValues();
            values.put("bar_code", this.bar_code);
            values.put("comment", this.comment);
            values.put("acquisition_date", this.acquisition_date);
            values.put("accounting_document", this.accounting_document);
            values.put("accounting_record_number", this.accounting_record_number);
            values.put("name_in_charge_active", this.name_in_charge_active);
            values.put("rut_in_charge_active", this.rut_in_charge_active);
            values.put("serie", this.serie);
            values.put("model", this.model);
            values.put("state", this.state);
            values.put("brand", this.brand);
            values.put("photo1", this.photo1);
            values.put("photo2", this.photo2);
            values.put("photo3", this.photo3);
            values.put("photo4", this.photo4);
            if (this.sync == 0) values.put("sync", 2);
            values.put("office_id", this.office_id);
            values.put("article_id", this.article_id);
            values.put("article_id_server", this.article_id);

            // Insertar el nuevo registro en la base de datos
            long newRowId = db.update("activo", values, "id = ?", new String[]{String.valueOf(this.id)});

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

    public boolean updateActiveSync(Context context){
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
            int rowsAffected = db.update("activo", values, whereClause, whereArgs);

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

    public boolean deleteActive (Context context) {

        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();

            db.beginTransaction();

            // Crear un ContentValues con los valores del nuevo artículo
            ContentValues values = new ContentValues();
            values.put("removed", 1);
            values.put("sync", 3);

            // Insertar el nuevo registro en la base de datos
            long newRowId = db.update("activo", values, "id = ?", new String[]{String.valueOf(this.id)});

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

    // Metodo que detecta si hay activos sin sincronizar
    /*
     * Estados de sincronización (async column):
     * 0: sin cambios, no se sincroniza
     * 1: nuevo activo, se sincroniza
     * 2: activo modificado, se sincroniza
     * 3: activo eliminado, se sincroniza
     */
    public static boolean hasUnsyncedActive(Context context) {
        String sql = "SELECT * FROM activo WHERE sync <> 0 LIMIT 1";

        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Cursor cursor = dbHelper.executeQuery(sql);

            boolean hasUnsyncedActive = cursor.moveToFirst();

            cursor.close();
            return hasUnsyncedActive;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void syncUploadActives(Context context, String token, Integer companyId, syncCallback callback) {
        System.out.println("SYNC UPLOAD ACTIVES");
        String sql = "SELECT * FROM activo WHERE sync <> 0";

        try {

            DatabaseHelper dbHelper = new DatabaseHelper(context);
            Cursor cursor = dbHelper.executeQuery(sql);

            List<Active> actives = new ArrayList<>();

            if (cursor.moveToFirst()) {
                do {
                    System.out.println(cursor);
                    Active active = new Active(cursor);
                    System.out.println(active.getId());
                    System.out.println("DATOS");
                    System.out.println(active.printData());

                    switch (active.getSync()) {
                        case 1:
                            // Sincronizar nuevo activo
                            System.out.println("1111111111");
                            System.out.println(active.getSync());
                            active.syncCreate(context, token, companyId);
                            break;

                        case 2:
                            // Sincronizar activo modificado
                            System.out.println("222222222222");
                            System.out.println(active.getSync());
                            active.syncUpdate(context, token, companyId);
                            break;

                        case 3:
                            // Sincronizar activo eliminado
                            System.out.println("33333333333333");
                            System.out.println(active.getSync());
                            active.syncDelete(context, token, companyId);
                            break;

                    }


                } while (cursor.moveToNext());
            }

            cursor.close();
            callback.onSuccess();

        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(e);
        }
    }

    public void syncUpdate(Context context, String token, Integer companyId) {
        //PETICION A LA API PARA ACTUALIZAR EL ACTIVO
        //String url = "http://192.168.100.8:9000/active/" + this.getId();
        String url = "http://10.0.2.2:9000/active/" + this.getId();
        RequestQueue queue = Volley.newRequestQueue(context);

        // Lista para almacenar las URLs de las imágenes subidas correctamente
        List<String> photoUrls = new ArrayList<>(Arrays.asList("", "", "", ""));

        // Dividir la cadena de fotos del activo en un array
        String[] photosActive = new String[4];
        photosActive[0] = this.getPhoto1();
        photosActive[1] = this.getPhoto2();
        photosActive[2] = this.getPhoto3();
        photosActive[3] = this.getPhoto4();

        // Contador para llevar un registro de cuántas imágenes se han procesado
        final AtomicInteger handledCount = new AtomicInteger(0);
        final int totalPhotos = 4;

        // Lista para mantener un registro de las imágenes subidas
        Set<String> uploadedPhotos = new HashSet<>();

        // Definir el callback para manejar la subida de imágenes
        Active.UploadImageCallback uploadCallback = new Active.UploadImageCallback() {

            @Override
            public void onSuccess(String photoUrl, int index) {
                // Almacenar la URL de la imagen subida y marcarla como subida
                System.out.println("Imagen subida: " + photoUrl);

                photoUrls.set(index, photoUrl);
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

                    System.out.println("photoUrls " +photoUrls);

                    // Actualizar el artículo
                    updateActiveAPI(context, token, companyId, queue, url, photoUrls);
                }
            }
        };

        if (this.getPhoto1().equals("") && this.getPhoto2().equals("") && this.getPhoto3().equals("") && this.getPhoto4().equals("")) {
            updateActiveAPI(context, token, companyId, queue, url, photoUrls);
        }

        // Subir solo las imágenes que contienen "mobile_local"
        for (int index = 0; index < photosActive.length; index++) {
            String photo = photosActive[index];
            System.out.println("Procesando imagen: " + photo);
            if (photo.contains("mobile_local")) {
                // Solo subir imágenes nuevas (no duplicadas)
                if (!uploadedPhotos.contains(photo)) {
                    uploadedPhotos.add(photo);
                    System.out.println("Subiendo imagen: " + photo);
                    uploadImage(token, companyId, photo, index, uploadCallback);
                }
            } else {
                // Si la imagen ya está subida, agregarla a la lista de URLs
                System.out.println("Imagen ya subida o no requiere subida: " + photo);

                uploadedPhotos.add(photo);
                photoUrls.set(index,photo);
                handledCount.incrementAndGet();

            }
        }
    }

    // ACTUALIZA EL ACTIVO DE LA API
    private void updateActiveAPI(Context context, String token, Integer companyId, RequestQueue queue, String url, List<String> photosUrl) {
        // Crear el objeto JSON para enviar en el cuerpo de la solicitud
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("bar_code", this.getBar_code());
            jsonBody.put("virtual_code", this.getVirtual_code());
            jsonBody.put("comment", this.getComment());
            jsonBody.put("acquisition_date", this.getAcquisition_date());
            jsonBody.put("accounting_document", this.getAccounting_document());
            jsonBody.put("accounting_record_number", this.getAccounting_record_number());
            jsonBody.put("name_in_charge_active", this.getName_in_charge_active());
            jsonBody.put("rut_in_charge_active", this.getRut_in_charge_active());
            jsonBody.put("serie", this.getSerie());
            jsonBody.put("model", this.getModel());
            jsonBody.put("state", this.getState());
            jsonBody.put("brand", this.getBrand());
            jsonBody.put("photo1", photosUrl.get(0));
            jsonBody.put("photo2", photosUrl.get(1));
            jsonBody.put("photo3", photosUrl.get(2));
            jsonBody.put("photo4", photosUrl.get(3));
            jsonBody.put("office_id", this.getOffice_id());
            jsonBody.put("article_id", this.getArticle_id_server());

            System.out.println("JSONBODYYYY " + jsonBody);

            // Crear una solicitud JSON a la API para actualizar el artículo
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // Manejar la respuesta exitosa
                            System.out.println("RESPONSE UPDATE ACTIVE");
                            System.out.println(response.toString());

                            try {
                                // Acceder al campo "code" en la respuesta JSON
                                String code = response.getString("code");
                                if (code.equals("201")) {
                                    boolean successful = updateActiveSync(context);
                                    System.out.println("SUCCESSFUL " + successful);
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

    public void syncCreate(Context context, String token, Integer companyId){
        //PETICION A LA API PARA CREAR EL ACTIVE
        //String url = "http://192.168.100.8:9000/active";
        String url = "http://10.0.2.2:9000/active";
        RequestQueue queue = Volley.newRequestQueue(context);

        // Lista para almacenar las URLs de las imágenes subidas correctamente
        List<String> photoUrls = new ArrayList<>(Arrays.asList("", "", "", ""));

        // Dividir la cadena de fotos del activo en un array
        String[] photosActive = new String[4];
        photosActive[0] = this.getPhoto1();
        photosActive[1] = this.getPhoto2();
        photosActive[2] = this.getPhoto3();
        photosActive[3] = this.getPhoto4();


        // Contador para llevar un registro de cuántas imágenes se han procesado
        final AtomicInteger handledCount = new AtomicInteger(0);
        final int totalPhotos = 4;

        // Lista para mantener un registro de las imágenes subidas
        Set<String> uploadedPhotos = new HashSet<>();

        // Definir un callback para manejar el resultado de `uploadImage()`
        Active.UploadImageCallback callback = new Active.UploadImageCallback() {
            @Override
            public void onSuccess(String photoUrl, int index) {
                // Almacenar la URL de la imagen subida y marcarla como subida
                System.out.println("Imagen subida: " + photoUrl);

                photoUrls.set(index, photoUrl);
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
                    //
                    System.out.println("photoUrls " +photoUrls);

                    // Crear el artículo
                    createActiveAPI(context, token, queue, companyId, url, photoUrls);
                }
            }

        };
        if (this.getPhoto1().equals("") && this.getPhoto2().equals("") && this.getPhoto3().equals("") && this.getPhoto4().equals("")) {
            createActiveAPI(context, token, queue, companyId, url, photoUrls);

        }

        // Subir solo las imágenes que contienen "mobile_local"
        for (int index = 0; index < photosActive.length; index++) {
            String photo = photosActive[index];
            System.out.println("Procesando imagen: " + photo);
            if (photo.contains("mobile_local")) {
                // Solo subir imágenes nuevas (no duplicadas)
                if (!uploadedPhotos.contains(photo)) {
                    uploadedPhotos.add(photo);
                    System.out.println("Subiendo imagen: " + photo);
                    uploadImage(token, companyId, photo, index, callback);
                }
            } else {
                // Si la imagen ya está subida, agregarla a la lista de URLs
                System.out.println("Imagen ya subida o no requiere subida: " + photo);
                uploadedPhotos.add(photo);
                photoUrls.set(index,photo);
                handledCount.incrementAndGet();
            }
        }
    }

    public void createActiveAPI(Context context, String token, RequestQueue queue, int companyId, String url, List<String> photosUrl) {

        // Crear el objeto JSON para enviar en el cuerpo de la solicitud
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("bar_code", this.getBar_code());
            jsonBody.put("virtual_code", this.getVirtual_code());
            jsonBody.put("comment", this.getComment());
            jsonBody.put("acquisition_date", this.getAcquisition_date());
            jsonBody.put("accounting_document", this.getAccounting_document());
            jsonBody.put("accounting_record_number", this.getAccounting_record_number());
            jsonBody.put("name_in_charge_active", this.getName_in_charge_active());
            jsonBody.put("rut_in_charge_active", this.getRut_in_charge_active());
            jsonBody.put("serie", this.getSerie());
            jsonBody.put("model", this.getModel());
            jsonBody.put("state", this.getState());
            jsonBody.put("brand", this.getBrand());
            jsonBody.put("photo1", photosUrl.get(0));
            jsonBody.put("photo2", photosUrl.get(1));
            jsonBody.put("photo3", photosUrl.get(2));
            jsonBody.put("photo4", photosUrl.get(3));
            jsonBody.put("office_id", this.getOffice_id());
            jsonBody.put("article_id", this.getArticle_id_server());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONSE CREATE ACTIVE");
                        System.out.println(response.toString());
                        String code = "0";
                        try {
                            code = response.getString("code");
                            System.out.println("CODE " + code);
                            JSONObject result = response.getJSONObject("result");
                            if(code.equals("201")){
                                System.out.println("RESULT " + result);
                                String virtual_code = result.getString("virtual_code");
                                System.out.println("VC " + virtual_code);
                                boolean successful = updateActiveSync(context);
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
        })
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

    public void syncDelete(Context context, String token, Integer companyId){
        //PETICION A LA API PARA ELIMINAR EL ARTICULO
        //String url = "http://192.168.100.8:9000/active/" + this.getId();
        String url = "http://10.0.2.2:9000/active/" + this.getId();
        RequestQueue queue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONSE DELETE ACTIVE");
                        System.out.println(response.toString());
                        String code = "0";
                        try {
                            code = response.getString("code");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        if(code.equals("201")){
                            boolean successful = updateActiveSync(context);
                            if (successful){
                                System.out.println("DELETED ACTIVE");
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

    public static String savePhoto(Context context, Object imageSource, String nameAct) {
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
            File imagesDir = new File(context.getFilesDir(), "imagesActives");
            if (!imagesDir.exists()) {
                imagesDir.mkdirs();
            }

            // Generar un UUID para el nombre del archivo
            String uuid = UUID.randomUUID().toString();

            // Crear el nombre del archivo con el UUID y "article_photo.jpg"
            String fileName = uuid + "-" + "mobile_local" + "-" + nameAct + ".jpg";

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
            Toast.makeText(context, "Imagen guardada exitosamente", Toast.LENGTH_SHORT).show();

            // Devolver la ruta absoluta del archivo
            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void uploadImage(String token, Integer companyId, String imagePath, int index, Active.UploadImageCallback callback) {
        // URL del endpoint
        //String url = "http://192.168.100.8:9000/image_active";
        String url = "http://10.0.2.2:9000/image_active";

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
                .addHeader("Authorization", "Bearer " + token)
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
                            callback.onSuccess(photoUrl, index);
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

    interface UploadImageCallback {
        void onSuccess(String photoUrl, int index);
        void onError(String errorMessage);
    }

    public interface syncCallback {
        void onSuccess();
        void onError(Exception e);
    }

    public static void updateArticleIdServer(Context context, Integer localArticleId, Integer serverArticleId) {
        System.out.println("DATOS");
        System.out.println("local " + localArticleId);
        System.out.println("SERVER " + serverArticleId);

        SQLiteDatabase db = null;
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();

            // db.beginTransaction();

            // Crear un ContentValues con los nuevos valores del artículo
            ContentValues values = new ContentValues();
            values.put("article_id_server", serverArticleId);

            // Definir la condición WHERE para la actualización (basado en el ID del artículo)
            String whereClause = "article_id = ?";
            String[] whereArgs = {String.valueOf(localArticleId)};

            // Actualizar el registro en la base de datos
            int rowsAffected = db.update("activo", values, whereClause, whereArgs);

            // db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
