package com.example.sca_app_v1.home_app.bdLocal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.sca_app_v1.models.*;
//import com.example.sca_app_v1.models.Company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sca_gestion_activos_12.db";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void deleteAllTables() {
        SQLiteDatabase db = getWritableDatabase();
    
        try {
            // Obtén una lista de todas las tablas
            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
            List<String> tables = new ArrayList<>();
    
            // Añade cada tabla a la lista
            while (cursor.moveToNext()) {
                tables.add(cursor.getString(0));
            }
    
            // Cierra el cursor
            cursor.close();
    
            // Borra cada tabla
            for (String table : tables) {
                if (!table.equals("android_metadata") && !table.equals("sqlite_sequence")) {
                    db.execSQL("DROP TABLE IF EXISTS " + table);
                }
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error al borrar todas las tablas", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public List<Map<String, String>> executeSqlQuery(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        List<Map<String, String>> results = new ArrayList<>();

        try {

            Cursor cursor = db.rawQuery(sql, null);
    
            if (cursor.moveToFirst()) {
                do {
                    Map<String, String> row = new HashMap<>();
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        row.put(cursor.getColumnName(i), cursor.getString(i));
                    }
                    results.add(row);
                } while (cursor.moveToNext());
            }
        
            cursor.close();
            db.close();

            return results;
             
        } catch (Exception e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (db != null) {
                db.close();
            }
        }
        
        
    }

    // Método para crear la tabla
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("CREANDO TABLES");
        System.out.println("CREANDO TABLES");
        System.out.println("CREANDO TABLES");
        System.out.println("CREANDO TABLES");

        String CREATE_TABLE_Company = "CREATE TABLE compania (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT UNIQUE NOT NULL," +
                "rut TEXT UNIQUE NOT NULL," +
                "country TEXT NOT NULL," +
                "contact_name TEXT NOT NULL," +
                "contact_email TEXT NOT NULL," +
                "contact_phone TEXT NOT NULL," +
                "removed INTEGER DEFAULT 0 NOT NULL," +
                "name_db TEXT NOT NULL" +
                ")";
        db.execSQL(CREATE_TABLE_Company);

        String CREATE_TABLE_Sucursal = "CREATE TABLE sucursal (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "description TEXT NOT NULL," +
                "number TEXT UNIQUE NOT NULL," +
                "address TEXT NOT NULL," +
                "region TEXT NOT NULL," +
                "city TEXT," +
                "commune TEXT NOT NULL," +
                "removed INTEGER DEFAULT 0 NOT NULL," +
                "company_id INTEGER," +
                "FOREIGN KEY (company_id) REFERENCES compania(id)" +
                ")";
        db.execSQL(CREATE_TABLE_Sucursal);

        String CREATE_TABLE_Office = "CREATE TABLE oficina (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "description TEXT," +
                "floor INTEGER NOT NULL," +
                "name_in_charge TEXT NOT NULL," +
                "removed INTEGER DEFAULT 0 NOT NULL," +
                "sucursal_id INTEGER," +
                "FOREIGN KEY (sucursal_id) REFERENCES sucursal(id)" +
                ")";
        db.execSQL(CREATE_TABLE_Office);

        String CREATE_TABLE_Category = "CREATE TABLE categoria (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "description TEXT," +
                "code TEXT NOT NULL," +
                "parent_id INTEGER NOT NULL," +
                "removed INTEGER DEFAULT 0 NOT NULL" +
                ")";
        db.execSQL(CREATE_TABLE_Category);

        String CREATE_TABLE_Article = "CREATE TABLE articulo (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "code TEXT NOT NULL," +
                "photo TEXT," +
                "count_active INTEGER DEFAULT 0 NOT NULL," +
                "creation_date DATE NOT NULL," +
                "removed INTEGER DEFAULT 0 NOT NULL," +
                "company_id INTEGER," +
                "category_id INTEGER," +
                "sync INTEGER DEFAULT 0 NOT NULL," +
                "FOREIGN KEY (company_id) REFERENCES compania(id)," +
                "FOREIGN KEY (category_id) REFERENCES categoria(id)" +
                ")";
        db.execSQL(CREATE_TABLE_Article);

        String CREATE_TABLE_Active = "CREATE TABLE activo (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "bar_code TEXT NOT NULL," +
                "virtual_code TEXT," +
                "comment TEXT," +
                "acquisition_date DATE NOT NULL," +
                "accounting_document TEXT NOT NULL," +
                "accounting_record_number TEXT NOT NULL," +
                "name_in_charge_active TEXT NOT NULL," +
                "rut_in_charge_active TEXT NOT NULL," +
                "serie TEXT NOT NULL," +
                "model TEXT NOT NULL," +
                "state TEXT NOT NULL," +
                "brand TEXT NOT NULL," +
                "photo1 TEXT," +
                "photo2 TEXT," +
                "photo3 TEXT," +
                "photo4 TEXT," +
                "creation_date DATE NOT NULL," +
                "removed INTEGER DEFAULT 0 NOT NULL," +
                "office_id INTEGER," +
                "article_id INTEGER," +
                "sync INTEGER DEFAULT 0 NOT NULL," +
                "FOREIGN KEY (office_id) REFERENCES oficina(id)," +
                "FOREIGN KEY (article_id) REFERENCES articulo(id)" +
                ")";
        db.execSQL(CREATE_TABLE_Active);
    }

    // Método para actualizar la base de datos (si es necesario)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aquí puedes realizar operaciones de actualización si es necesario
    }

    // Método para insertar datos de compañía
    public boolean insertCompanyData(Company company) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {

            // Verificar si ya existe una compañía con ese ID
            Cursor cursor = db.rawQuery(
                "SELECT * FROM compania WHERE id = ?",
                new String[] {String.valueOf(company.getId())}
            );
            boolean exists = (cursor.getCount() > 0);
            cursor.close();

            // Si la compañía ya existe, no insertarla
            if (exists) {
                return false;
            }

            values.put("id", company.getId());
            values.put("name", company.getName());
            values.put("rut", company.getRut());
            values.put("country", company.getCountry());
            values.put("contact_name", company.getContact_name());
            values.put("contact_email", company.getContact_email());
            values.put("contact_phone", company.getContact_phone());
            values.put("removed", company.getRemoved());
            values.put("name_db", company.getName_db());
            // Aquí puedes continuar insertando otros campos de la compañía si es necesario
            long result = db.insert("compania", null, values);
            if (result == -1) {
                //Toast.makeText(context, "Error al insertar datos en la tabla de compañía", Toast.LENGTH_SHORT).show();
                System.out.println("Error al insertar datos en la tabla compania");
                return false;
            }else {
                // La inserción fue exitosa
                System.out.println("Datos insertados correctamente en la tabla compania");
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error al insertar datos: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    // Método para insertar datos de sucursal
    public void insertSucursalTransaction(List<Store> stores) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {

            for (Store store : stores){
                // Verificar si ya existe un articulo con ese ID
                Cursor cursor = db.rawQuery(
                        "SELECT * FROM sucursal WHERE id = ?",
                        new String[] {String.valueOf(store.getId())}
                );
                boolean exists = (cursor.getCount() > 0);
                cursor.close();

                // Si la sucursal ya existe, no insertar
                if (exists) {
                    continue;
                }

                ContentValues values = new ContentValues();
                values.put("id", store.getId());
                values.put("description", store.getDescription());
                values.put("number", store.getNumber());
                values.put("address", store.getAddress());
                values.put("region", store.getRegion());
                values.put("city", store.getCity());
                values.put("commune", store.getCommune());
                values.put("removed", store.getRemoved());
                values.put("company_id", store.getCompany_id());

                db.insert("sucursal", null, values);
            }

            db.setTransactionSuccessful();

        } catch (Exception e) {
            System.out.println("Error al insertar datos: " + e.getMessage());
            //return false;
        } finally {
            db.endTransaction();
            //db.close();
        }

    }

    public void insertOfficeTransaction(List<Office> offices) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {

            for (Office office : offices){
                // Verificar si ya existe un articulo con ese ID
                Cursor cursor = db.rawQuery(
                        "SELECT * FROM oficina WHERE id = ?",
                        new String[] {String.valueOf(office.getId())}
                );
                boolean exists = (cursor.getCount() > 0);
                cursor.close();

                // Si la oficina ya existe, no insertar
                if (exists) {
                    continue;
                }

                ContentValues values = new ContentValues();
                values.put("id", office.getId());
                values.put("description", office.getDescription());
                values.put("floor", office.getFloor());
                values.put("name_in_charge", office.getName_in_charge());
                values.put("removed", office.getRemoved());
                values.put("sucursal_id", office.getSucursal_id());

                db.insert("oficina", null, values);
            }

            db.setTransactionSuccessful();

        } catch (Exception e) {
            System.out.println("Error al insertar datos: " + e.getMessage());
        } finally {
            db.endTransaction();
        }

    }

    public void insertCategoryTransaction(List<Category> categories) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {

            for (Category category : categories){
                // Verificar si ya existe un articulo con ese ID
                Cursor cursor = db.rawQuery(
                        "SELECT * FROM categoria WHERE id = ?",
                        new String[] {String.valueOf(category.getId())}
                );
                boolean exists = (cursor.getCount() > 0);
                cursor.close();

                // Si la oficina ya existe, no insertar
                if (exists) {
                    continue;
                }

                ContentValues values = new ContentValues();
                values.put("id", category.getId());
                values.put("description", category.getDescription());
                values.put("parent_id", category.getParent_id());
                values.put("removed", category.getRemoved());
                values.put("code", category.getCode());

                db.insert("categoria", null, values);
            }

            db.setTransactionSuccessful();

        } catch (Exception e) {
            System.out.println("Error al insertar datos: " + e.getMessage());
        } finally {
            db.endTransaction();
        }

    }

    public void insertArticleTransaction(List<Article> articles) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {

            for (Article article : articles) {

                Cursor cursor = db.rawQuery(
                        "SELECT * FROM articulo WHERE id = ?",
                        new String[] {String.valueOf(article.getId())}
                );
                boolean exists = (cursor.getCount() > 0);
                cursor.close();

                // Si el articulo ya existe, no insertar
                if (exists) {
                    continue;
                }

                ContentValues values = new ContentValues();
                values.put("id", article.getId());
                values.put("name", article.getName());
                values.put("description", article.getDescription());
                values.put("code", article.getCode());
                values.put("photo", article.getPhoto());
                values.put("count_active", article.getCount_active());
                values.put("creation_date", article.getCreation_date());
                values.put("removed", article.getRemoved());
                values.put("company_id", article.getCompany_id());
                values.put("category_id", article.getCategory_id());

                db.insert("articulo", null, values);

            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

    }

    public void insertActiveTransaction(List<Active> actives) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {

            for (Active active : actives){
                // Verificar si ya existe un articulo con ese ID
                Cursor cursor = db.rawQuery(
                        "SELECT * FROM activo WHERE id = ?",
                        new String[] {String.valueOf(active.getId())}
                );
                boolean exists = (cursor.getCount() > 0);
                cursor.close();

                // Si la oficina ya existe, no insertar
                if (exists) {
                    continue;
                }

                ContentValues values = new ContentValues();
                values.put("id", active.getId());
                values.put("bar_code", active.getBar_code());
                values.put("comment", active.getComment());
                values.put("acquisition_date", active.getAcquisition_date());
                values.put("accounting_document", active.getAccounting_document());
                values.put("accounting_record_number", active.getAccounting_record_number());
                values.put("name_in_charge_active", active.getName_in_charge_active());
                values.put("rut_in_charge_active", active.getRut_in_charge_active());
                values.put("serie", active.getSerie());
                values.put("model", active.getModel());
                values.put("state", active.getState());
                values.put("brand", active.getBrand());
                values.put("creation_date", active.getCreation_date());
                values.put("removed", active.getRemoved());
                values.put("office_id", active.getOffice_id());
                values.put("article_id", active.getArticle_id());

                db.insert("activo", null, values);
            }

            db.setTransactionSuccessful();

        } catch (Exception e) {
            System.out.println("Error al insertar datos: " + e.getMessage());
        } finally {
            db.endTransaction();
        }

    }

    public void deleteAllDataFromDatabase() {
        SQLiteDatabase db = null;
        try {
            // Obtener una instancia de la base de datos en modo escritura
            db = this.getWritableDatabase();

            // Eliminar los datos de las tablas
            db.delete("compania", null, null);
            db.delete("sucursal", null, null);
            db.delete("oficina", null, null);
            db.delete("categoria", null, null);
            db.delete("articulo", null, null);
            db.delete("activo", null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public Cursor executeQuery(String query) {
        return executeQuery(query, null);
    }

    public Cursor executeQuery(String query, String[] selectionArgs) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            // Obtener una instancia de la base de datos en modo lectura
            db = this.getReadableDatabase();
            String dbPath = db.getPath();
            System.out.println("La base de datos se almacena en: " + dbPath);
    
            cursor = db.rawQuery(query, selectionArgs);
        } catch (Exception e) {
            System.out.println("Error al ejecutar la consulta: " + e.getMessage());
            e.printStackTrace();
        }
    
        // Devolver el Cursor. Nota: no cierres el Cursor aquí, 
        // deberías cerrarlo después de usarlo en otra parte de tu código
        return cursor;
    }

}