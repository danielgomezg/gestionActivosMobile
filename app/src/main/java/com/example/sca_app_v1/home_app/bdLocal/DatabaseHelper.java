package com.example.sca_app_v1.home_app.bdLocal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.sqlite.SQLiteStatement;

import com.example.sca_app_v1.models.Article;
import com.example.sca_app_v1.models.Company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "sca_app.db";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public List<Map<String, String>> executeSqlQuery(String sql) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
    
        List<Map<String, String>> results = new ArrayList<>();
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
    }

    // Método para crear la tabla
    @Override
    public void onCreate(SQLiteDatabase db) {
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
                "description TEXT NOT NULL," +
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
                "FOREIGN KEY (company_id) REFERENCES compania(id)," +
                "FOREIGN KEY (category_id) REFERENCES categoria(id)" +
                ")";
        db.execSQL(CREATE_TABLE_Article);

        String CREATE_TABLE_Active = "CREATE TABLE activo (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "bar_code TEXT NOT NULL," +
                "comment TEXT," +
                "acquisition_date DATE NOT NULL," +
                "accounting_document TEXT NOT NULL," +
                "accounting_record_number TEXT NOT NULL," +
                "name_in_charge_active TEXT NOT NULL," +
                "rut_in_charge_active TEXT NOT NULL," +
                "serie TEXT NOT NULL," +
                "model TEXT NOT NULL," +
                "state TEXT NOT NULL," +
                "creation_date DATE NOT NULL," +
                "removed INTEGER DEFAULT 0 NOT NULL," +
                "office_id INTEGER," +
                "article_id INTEGER," +
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

    public void insertCompany(Company company) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {

            

        } catch (Exception e) {
            System.out.println("Error al insertar datos: " + e.getMessage());

        } finally {
            db.close();
        }
    }

    // Método para insertar datos de compañía
    public boolean insertCompanyData(int id, String name, String rut, String country, String contactName, String contactEmail, String contactPhone, int removed, String nameDb) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {

            // Verificar si ya existe una compañía con ese ID
            Cursor cursor = db.rawQuery(
                "SELECT * FROM compania WHERE id = ?",
                new String[] {String.valueOf(id)}
            );
            boolean exists = (cursor.getCount() > 0);
            cursor.close();

            // Si la compañía ya existe, no insertarla
            if (exists) {
                return false;
            }

            values.put("id", id);
            values.put("name", name);
            values.put("rut", rut);
            values.put("country", country);
            values.put("contact_name", contactName);
            values.put("contact_email", contactEmail);
            values.put("contact_phone", contactPhone);
            values.put("removed", removed);
            values.put("name_db", nameDb);
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
    public boolean insertSucursalData(int id, String description, String number, String address, String region, String city, String commune, int removed, int companyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {

            String sql = "INSERT INTO sucursal (id, description, number, address, region, city, commune, removed, company_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            SQLiteStatement statement = db.compileStatement(sql);
            statement.bindLong(1, id);
            statement.bindString(2, description);
            statement.bindString(3, number);
            statement.bindString(4, address);
            statement.bindString(5, region);
            statement.bindString(6, city);
            statement.bindString(7, commune);
            statement.bindLong(8, removed);
            statement.bindLong(9, companyId);

            long result = statement.executeInsert();

            if (result == -1) {
                System.out.println("Error al insertar datos en la tabla sucursal");
                return false;
            } else {
                // La inserción fue exitosa
                System.out.println("Datos insertados correctamente en la tabla sucursal");
                db.setTransactionSuccessful();
                return true;
            }

        } catch (Exception e) {
            System.out.println("Error al insertar datos: " + e.getMessage());
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }

    }

    // Método para insertar datos de oficina
    public boolean insertOfficeData(int id, String description, int floor, String nameInCharge, int removed, int sucursalId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {

            String sql = "INSERT INTO oficina (id, description, floor, name_in_charge, removed, sucursal_id) VALUES (?, ?, ?, ?, ?, ?)";

            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindLong(1, id);
            statement.bindString(2, description);
            statement.bindLong(3, floor);
            statement.bindString(4, nameInCharge);
            statement.bindLong(5, removed);
            statement.bindLong(6, sucursalId);

            long result = statement.executeInsert();

            // Verifica si la inserción fue exitosa
            if (result == -1) {
                System.out.println("Error al insertar datos en la tabla de oficina");
                return false;
            } else {
                // La inserción fue exitosa
                System.out.println("Datos insertados correctamente en la tabla de oficina");
                db.setTransactionSuccessful();
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error al insertar datos: " + e.getMessage());
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
        
    }

    public boolean insertCategoryData(int id, String description, int parentId, int removed) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();

        try {

            String sql = "INSERT INTO categoria (id, description, parent_id, removed) VALUES (?, ?, ?, ?)";

            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindLong(1, id);
            statement.bindString(2, description);
            statement.bindLong(3, parentId);
            statement.bindLong(4, removed);

            long result = statement.executeInsert();

            // Verifica si la inserción fue exitosa
            if (result == -1) {
                System.out.println("Error al insertar datos en la tabla de categoría");
                return false;
            } else {
                System.out.println("Datos insertados correctamente en la tabla de categoría");
                db.setTransactionSuccessful();
                return true;
            }

        } catch (Exception e) {
            System.out.println("Error al insertar datos: " + e.getMessage());
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
        
    }

    public void insertArticleTransaction(List<Article> articles) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {

            for (Article article : articles) {
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

    public boolean insertArticleData(int id, String name, String description, String code, String photo, int countActive, String creationDate, int removed,
                                  int companyId, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {

            String sql = "INSERT INTO articulo (id, name, description, code, photo, count_active, creation_date, removed, company_id, category_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindLong(1, id);
            statement.bindString(2, name);
            statement.bindString(3, description);
            statement.bindString(4, code);
            statement.bindString(5, photo);
            statement.bindLong(6, countActive);
            statement.bindString(7, creationDate);
            statement.bindLong(8, removed);
            statement.bindLong(9, companyId);
            statement.bindLong(10, categoryId);

            long result = statement.executeInsert();

            // Verifica si la inserción fue exitosa
            if (result == -1) {
                System.out.println("Error al insertar datos en la tabla de artículo");
                return false;
            } else {
                System.out.println("Datos insertados correctamente en la tabla de artículo");
                db.setTransactionSuccessful();
                return true;
            }

        } catch (Exception e) {
            System.out.println("Error al insertar datos: " + e.getMessage());
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }

    }

    public boolean insertActiveData(String barCode, String comment, String acquisitionDate, String accountingDocument, String accountingRecordNumber,
                                 String nameInChargeActive, String rutInChargeActive, String serie, String model, String state, String creationDate,
                                 int removed, int officeId, int articleId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {

            String sql = "INSERT INTO activo (bar_code, comment, acquisition_date, accounting_document, accounting_record_number, name_in_charge_active, rut_in_charge_active, serie, model, state, creation_date, removed, office_id, article_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            SQLiteStatement statement = db.compileStatement(sql);

            statement.bindString(1, barCode);
            statement.bindString(2, comment);
            statement.bindString(3, acquisitionDate);
            statement.bindString(4, accountingDocument);
            statement.bindString(5, accountingRecordNumber);
            statement.bindString(6, nameInChargeActive);
            statement.bindString(7, rutInChargeActive);
            statement.bindString(8, serie);
            statement.bindString(9, model);
            statement.bindString(10, state);
            statement.bindString(11, creationDate);
            statement.bindLong(12, removed);
            statement.bindLong(13, officeId);
            statement.bindLong(14, articleId);

            long result = statement.executeInsert();

            // Verifica si la inserción fue exitosa
            if (result == -1) {
                System.out.println("Error al insertar datos en la tabla de activo");
                return false;
            } else {
                System.out.println("Datos insertados correctamente en la tabla de activo");
                db.setTransactionSuccessful();
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error al insertar datos: " + e.getMessage());
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }

    }


}
