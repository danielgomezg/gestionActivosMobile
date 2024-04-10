package com.example.sca_app_v1.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sca_app_v1.home_app.bdLocal.DatabaseHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Active {

    private Integer id;
    private String bar_code;
    private String comment;
    private String acquisition_date;
    private String accounting_document;
    private String accounting_record_number;
    private String name_in_charge_active;
    private String rut_in_charge_active;
    private String serie;
    private String model;
    private String state;
    private String creation_date;
    private Integer removed;
    private Integer office_id;
    private Integer article_id;

    public Active(){}

    public Active(Cursor cursor) {

        int _idIndex = cursor.getColumnIndex("id");
        if (_idIndex != -1) this.id = cursor.getInt(_idIndex);

        int _bar_codeIndex = cursor.getColumnIndex("bar_code");
        if (_bar_codeIndex != -1) this.bar_code = cursor.getString(_bar_codeIndex);

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

        int _creation_dateIndex = cursor.getColumnIndex("creation_date");
        if (_creation_dateIndex != -1) this.creation_date = cursor.getString(_creation_dateIndex);

        int _removedIndex = cursor.getColumnIndex("removed");
        if (_removedIndex != -1) this.removed = cursor.getInt(_removedIndex);

        int _office_idIndex = cursor.getColumnIndex("office_id");
        if (_office_idIndex != -1) this.office_id = cursor.getInt(_office_idIndex);

        int _article_idIndex = cursor.getColumnIndex("article_id");
        if (_article_idIndex != -1) this.article_id = cursor.getInt(_article_idIndex);

    }

    public Active(Integer id, String bar_code, String comment, String acquisition_date, String accounting_document, String accounting_record_number, String name_in_charge_active, String rut_in_charge_active, String serie, String model, String state, String creation_date, Integer removed, Integer office_id, Integer article_id) {
        this.id = id;
        this.bar_code = bar_code;
        this.comment = comment;
        this.acquisition_date = acquisition_date;
        this.accounting_document = accounting_document;
        this.accounting_record_number = accounting_record_number;
        this.name_in_charge_active = name_in_charge_active;
        this.rut_in_charge_active = rut_in_charge_active;
        this.serie = serie;
        this.model = model;
        this.state = state;
        this.creation_date = creation_date;
        this.removed = removed;
        this.office_id = office_id;
        this.article_id = article_id;
    }

    public Active(JSONObject active) throws JSONException {
        this.id = (int) active.getInt("id");
        this.bar_code = active.getString("bar_code");
        this.comment = active.getString("comment");
        this.acquisition_date = active.getString("acquisition_date");
        this.accounting_document = active.getString("accounting_document");
        this.accounting_record_number = active.getString("accounting_record_number");
        this.name_in_charge_active = active.getString("name_in_charge_active");
        this.rut_in_charge_active = active.getString("rut_in_charge_active");
        this.serie = active.getString("serie");
        this.model = active.getString("model");
        this.state = active.getString("state");
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
}
