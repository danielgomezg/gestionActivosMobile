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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

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
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }

    public String getPhoto2() {
        return photo2;
    }

    public void setPhoto2(String photo2) {
        this.photo2 = photo2;
    }

    public String getPhoto3() {
        return photo3;
    }

    public void setPhoto3(String photo3) {
        this.photo3 = photo3;
    }

    public String getPhoto4() {
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
            values.put("sync", 2);
            values.put("office_id", this.office_id);
            values.put("article_id", this.article_id);

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

    public static void syncUploadActives(Context context, String token, Integer companyId) {
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

                    switch (active.getSync()) {
                        case 1:
                            // Sincronizar nuevo activo
                            active.syncCreate(context, token, companyId);
                            break;

                        case 2:
                            // Sincronizar activo modificado
                            active.syncUpdate(context, token, companyId);
                            break;

                        case 3:
                            // Sincronizar activo eliminado
                            active.syncDelete(context, token, companyId);
                            break;

                    }


                } while (cursor.moveToNext());
            }

            cursor.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void syncUpdate(Context context, String token, Integer companyId){
        //PETICION A LA API PARA ACTUALIZAR EL ACTIVO
        String url = "http://10.0.2.2:9000/active/" + this.getId();
        RequestQueue queue = Volley.newRequestQueue(context);

        // Crear el objeto JSON para enviar en el cuerpo de la solicitud
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("bar_code", this.getBar_code());
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
            jsonBody.put("office_id", this.getOffice_id());
            jsonBody.put("article_id", this.getArticle_id());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONSE UPDATE ACTIVE");
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
                                System.out.println("UPDATED ACTIVE");
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

    public void syncCreate(Context context, String token, Integer companyId){
        //PETICION A LA API PARA CREAR EL ACTIVE
        String url = "http://10.0.2.2:9000/active";
        RequestQueue queue = Volley.newRequestQueue(context);

        // Crear el objeto JSON para enviar en el cuerpo de la solicitud
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("bar_code", this.getBar_code());
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
            jsonBody.put("office_id", this.getOffice_id());
            jsonBody.put("article_id", this.getArticle_id());
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
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        if(code.equals("201")){
                            boolean successful = updateActiveSync(context);
                            if (successful){
                                System.out.println("CREATED ACTIVE");
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

    public void syncDelete(Context context, String token, Integer companyId){
        //PETICION A LA API PARA ELIMINAR EL ARTICULO
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
}
