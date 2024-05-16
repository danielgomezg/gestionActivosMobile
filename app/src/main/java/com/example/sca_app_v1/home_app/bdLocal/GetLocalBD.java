package com.example.sca_app_v1.home_app.bdLocal;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;
import android.widget.AutoCompleteTextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sca_app_v1.databinding.ActivityMainBinding;

import com.example.sca_app_v1.home_app.bdLocal.DatabaseHelper;
import com.example.sca_app_v1.models.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class GetLocalBD {

    private ActivityMainBinding binding;
    AutoCompleteTextView companySelect;
    private static Set<Integer> failsOffsetStore = new HashSet<>();
    private static Set<Integer> failsOffsetOffice = new HashSet<>();
    private static Set<Integer> failsOffsetActive = new HashSet<>();
    private static Set<Integer> failsOffsetArticle = new HashSet<>();
    private static Set<Integer> failsOffsetCategory = new HashSet<>();
    private static int company_id = -1;

    public static void deleteLocalTables(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.deleteAllTables();
    }

    private static void clearFailsOffsets() {
        failsOffsetStore.clear();
        failsOffsetOffice.clear();
        failsOffsetActive.clear();
        failsOffsetArticle.clear();
        failsOffsetCategory.clear();
    }

    private static CompletableFuture<Void> fetchOficinas(Context context, String token, Integer companyId) {
        return CompletableFuture.runAsync(() -> {
            int limit = 5;
            int count = 0;
            int offset = 0;
            String url = "";
            List<Office> officeList = new ArrayList<>();
            DatabaseHelper dbHelper = new DatabaseHelper(context);

            if (failsOffsetOffice.size() > 0 && !failsOffsetOffice.contains(0)) {

                System.out.println("SEARCH OFFSET FAIL");
                for (Integer off : failsOffsetOffice) {
                    url = "http://10.0.2.2:9000/offices?limit=" + limit + "&offset=" + off;
                    try {
    
                        CompletableFuture<JSONObject> futureOffice = LoadData.requestGet(context, url, token, companyId);
                        JSONObject response = null;
                        response = futureOffice.get();
                
                        if (response == null) {
                            System.out.println("Error en la respuesta de oficinas A");
                        }
                        else {
                            int status = response.getInt("code");
                            if (status == 200) {
                                failsOffsetOffice.remove(off);
                                count = response.getInt("count");
                                // GET CATEGORIAS
                                JSONArray offices = response.getJSONArray("result");
                                for (int i = 0; i < offices.length(); i++){
                                    Office office = new Office(offices.getJSONObject(i));
                                    officeList.add(office);
                                }
                            }
                            else {
                                System.out.println("Error en la respuesta de oficinas B");
                            }
                        }

                    } catch (JSONException | InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
            else {
                failsOffsetOffice.remove(0);
                System.out.println("SEARCH OFFSET NORMAL");
                try {
                    //dbHelper = new DatabaseHelper(context);
                    do {
    
                        url = "http://10.0.2.2:9000/offices?limit=" + limit + "&offset=" + offset;
                        CompletableFuture<JSONObject> futureOffice = LoadData.requestGet(context, url, token, companyId);
                        JSONObject response = futureOffice.get();
    
                        try {
                            if (response == null) {
                                System.out.println("Error en la respuesta de oficinas A");
                                failsOffsetOffice.add(offset);
                            }
                            else {
                                int status = response.getInt("code");
                                if (status == 200) {
                                    count = response.getInt("count");
                                    
                                    // GET CATEGORIAS
                                    JSONArray offices = response.getJSONArray("result");
                                    for (int i = 0; i < offices.length(); i++){
                                        Office office = new Office(offices.getJSONObject(i));
                                        officeList.add(office);
                                    }
                                }
                                else {
                                    System.out.println("Error en la respuesta de oficinas B");
                                    failsOffsetOffice.add(offset);
                                }
                            }
    
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
    
                        offset += limit;
                        System.out.println("offset offices: " + offset);
    
    
                    } while (offset < count);
    
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }

            }

            System.out.println("FAIL OFFSET OFFICES size " + failsOffsetOffice.size());
            System.out.println("FAIL OFFSET OFFICES" + failsOffsetOffice);

            if (officeList.size() > 0){
                dbHelper.insertOfficeTransaction(officeList);
                String selectSql = "SELECT * FROM oficina";
                List<Map<String, String>> results_cat = dbHelper.executeSqlQuery(selectSql);
                System.out.println(results_cat.size());
                for (Map<String, String> row : results_cat) {
                    System.out.println("---");
                    System.out.println(row);
                }
            }
        });
    }

    private static CompletableFuture<Void> fetchSucursales(Context context, String token, Integer companyId) {
        return CompletableFuture.runAsync(() -> {
            int limit = 5;
            String url = "";
            int count = 0;
            int offset = 0;
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            List<Store> sucursalList = new ArrayList<>();

            if (failsOffsetStore.size() > 0 && !failsOffsetStore.contains(0)) {

                System.out.println("SEARCH OFFSET FAIL SUCURSAL");
                for (Integer off : failsOffsetStore) {
                    url = "http://10.0.2.2:9000/sucursales?limit=" + limit + "&offset=" + off;
                    try {

                        CompletableFuture<JSONObject> futureSucursal = LoadData.requestGet(context, url, token, companyId);
                        JSONObject response = null;
                        response = futureSucursal.get();

                        if (response == null) {
                            System.out.println("Error en la respuesta de Sucursales con offset " + offset);
                        }
                        else {
                            int status = response.getInt("code");
                            if (status == 200) {
                                count = response.getInt("count");
                                failsOffsetStore.remove(off);
                                // GET SUCURSALES
                                JSONArray sucursales = response.getJSONArray("result");
                                for (int i = 0; i < sucursales.length(); i++){
                                    Store sucursal = new Store(sucursales.getJSONObject(i));
                                    sucursalList.add(sucursal);
                                }
                            }
                            else {
                                System.out.println("Error en la respuesta de Sucursales con status" + status + " con offset " + offset);
                            }
                        }

                    } catch (JSONException | InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
            else {
                failsOffsetStore.remove(0);
                System.out.println("SEARCH OFFSET NORMAL");
                try {
                    //dbHelper = new DatabaseHelper(context);
                    do {

                        url = "http://10.0.2.2:9000/sucursales?limit=" + limit + "&offset=" + offset;
                        CompletableFuture<JSONObject> futureSucursal = LoadData.requestGet(context, url, token, companyId);
                        JSONObject response = futureSucursal.get();

                        try {
                            if (response == null) {
                                System.out.println("Error en la respuesta de sucursales con offset " + offset);
                                failsOffsetStore.add(offset);
                            }
                            else {
                                int status = response.getInt("code");
                                if (status == 200) {
                                    count = response.getInt("count");
                                    // GET CATEGORIAS
                                    JSONArray sucursales = response.getJSONArray("result");
                                    for (int i = 0; i < sucursales.length(); i++){
                                        Store sucursal = new Store(sucursales.getJSONObject(i));
                                        sucursalList.add(sucursal);
                                    }
                                }
                                else {
                                    System.out.println("Error en la respuesta de sucursal con status " + status + "con offset " + offset);
                                    failsOffsetStore.add(offset);
                                }
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        offset += limit;
                        System.out.println("offset sucursales: " + offset);


                    } while (offset < count);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }

            }

            System.out.println("FAIL OFFSET SUCURSALES size " + failsOffsetStore.size());
            System.out.println("FAIL OFFSET SUCURSALES " + failsOffsetStore);

            if (sucursalList.size() > 0){
                dbHelper.insertSucursalTransaction(sucursalList);
                String selectSql = "SELECT * FROM sucursal";
                List<Map<String, String>> results_cat = dbHelper.executeSqlQuery(selectSql);
                System.out.println(results_cat.size());
                for (Map<String, String> row : results_cat) {
                    System.out.println("---");
                    System.out.println(row);
                }
            }

        });
    }

    private static CompletableFuture<Void> fetchActivos(Context context, String token, Integer companyId) {
        return CompletableFuture.runAsync(() -> {
            int limit = 5;
            String url = "";
            int count = 0;
            int offset = 0;
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            List<Active> activeList = new ArrayList<>();

            if (failsOffsetActive.size() > 0 && !failsOffsetActive.contains(0)) {

                System.out.println("SEARCH OFFSET FAIL ACTIVE");
                for (Integer off : failsOffsetActive) {
                    url = "http://10.0.2.2:9000/actives?limit=" + limit + "&offset=" + off;
                    try {

                        CompletableFuture<JSONObject> futureActive = LoadData.requestGet(context, url, token, companyId);
                        JSONObject response = null;
                        response = futureActive.get();

                        if (response == null) {
                            System.out.println("Error en la respuesta de Activos con offset " + offset);
                        }
                        else {
                            int status = response.getInt("code");
                            if (status == 200) {
                                count = response.getInt("count");
                                failsOffsetActive.remove(off);
                                // GET ACTIVOS
                                JSONArray actives = response.getJSONArray("result");
                                for (int i = 0; i < actives.length(); i++){
                                    Active active = new Active(actives.getJSONObject(i));
                                    activeList.add(active);
                                }
                            }
                            else {
                                System.out.println("Error en la respuesta de Actives con status" + status + " con offset " + offset);
                            }
                        }

                    } catch (JSONException | InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
            else {
                failsOffsetActive.remove(0);
                System.out.println("SEARCH OFFSET NORMAL");
                try {
                    //dbHelper = new DatabaseHelper(context);
                    do {

                        url = "http://10.0.2.2:9000/actives?limit=" + limit + "&offset=" + offset;
                        CompletableFuture<JSONObject> futureActive= LoadData.requestGet(context, url, token, companyId);
                        JSONObject response = futureActive.get();

                        try {
                            if (response == null) {
                                System.out.println("Error en la respuesta de actives con offset " + offset);
                                failsOffsetActive.add(offset);
                            }
                            else {
                                int status = response.getInt("code");
                                if (status == 200) {
                                    count = response.getInt("count");
                                    // GET ACTIVES
                                    JSONArray actives = response.getJSONArray("result");
                                    for (int i = 0; i < actives.length(); i++){
                                        Active active = new Active(actives.getJSONObject(i));
                                        activeList.add(active);
                                    }
                                }
                                else {
                                    System.out.println("Error en la respuesta de Activo con status " + status + "con offset " + offset);
                                    failsOffsetActive.add(offset);
                                }
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        offset += limit;
                        System.out.println("offset actives: " + offset);


                    } while (offset < count);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }

            }

            System.out.println("FAIL OFFSET ACTIVES size " + failsOffsetActive.size());
            System.out.println("FAIL OFFSET ACTIVES " + failsOffsetActive);
            System.out.println("FAIL ACTIVES " + activeList);

            if (activeList.size() > 0){
                dbHelper.insertActiveTransaction(activeList);
                String selectSql = "SELECT * FROM activo";
                List<Map<String, String>> results_cat = dbHelper.executeSqlQuery(selectSql);
                System.out.println(results_cat.size());
                for (Map<String, String> row : results_cat) {
                    System.out.println("---");
                    System.out.println(row);
                }
            }

        });
    }

    private static CompletableFuture<Void> fetchArticulos(Context context, String token, Integer companyId) {
        return CompletableFuture.runAsync(() -> {
            int limit = 5;
            String url = "";
            int count = 0;
            int offset = 0;
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            List<Article> articleList = new ArrayList<>();

            if (failsOffsetArticle.size() > 0 && !failsOffsetArticle.contains(0)) {

                System.out.println("SEARCH OFFSET FAIL ARTICULOS");
                for (Integer off : failsOffsetArticle) {
                    url = "http://10.0.2.2:9000/articles?limit=" + limit + "&offset=" + off;
                    try {

                        CompletableFuture<JSONObject> futureArticle = LoadData.requestGet(context, url, token, companyId);
                        JSONObject response = null;
                        response = futureArticle.get();

                        if (response == null) {
                            System.out.println("Error en la respuesta de Articles con offset " + offset);
                        }
                        else {
                            int status = response.getInt("code");
                            if (status == 200) {
                                count = response.getInt("count");
                                failsOffsetArticle.remove(off);
                                // GET ARTICULOS
                                JSONArray articles = response.getJSONArray("result");
                                for (int i = 0; i < articles.length(); i++){
                                    Article article = new Article(articles.getJSONObject(i));
                                    articleList.add(article);
                                }
                            }
                            else {
                                System.out.println("Error en la respuesta de Articulos con status" + status + " con offset " + offset);
                            }
                        }

                    } catch (JSONException | InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
            else {
                failsOffsetArticle.remove(0);
                System.out.println("SEARCH OFFSET NORMAL");
                try {
                    //dbHelper = new DatabaseHelper(context);
                    do {

                        url = "http://10.0.2.2:9000/articles?limit=" + limit + "&offset=" + offset;
                        CompletableFuture<JSONObject> futureArticle = LoadData.requestGet(context, url, token, companyId);
                        JSONObject response = futureArticle.get();

                        try {
                            if (response == null) {
                                System.out.println("Error en la respuesta de articulos con offset " + offset);
                                failsOffsetArticle.add(offset);
                            }
                            else {
                                int status = response.getInt("code");
                                if (status == 200) {
                                    count = response.getInt("count");
                                    // GET CATEGORIAS
                                    JSONArray articles = response.getJSONArray("result");
                                    for (int i = 0; i < articles.length(); i++){
                                        Article article = new Article(articles.getJSONObject(i));
                                        articleList.add(article);
                                    }
                                }
                                else {
                                    System.out.println("Error en la respuesta de articulos con status " + status + "con offset " + offset);
                                    failsOffsetArticle.add(offset);
                                }
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        offset += limit;
                        System.out.println("offset articulos: " + offset);


                    } while (offset < count);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }

            }

            System.out.println("FAIL OFFSET ARTICULOS size " + failsOffsetArticle.size());
            System.out.println("FAIL OFFSET ARTICULOS " + failsOffsetArticle);

            if (articleList.size() > 0){
                dbHelper.insertArticleTransaction(articleList);
                String selectSql = "SELECT * FROM articulo";
                List<Map<String, String>> results_cat = dbHelper.executeSqlQuery(selectSql);
                System.out.println(results_cat.size());
                for (Map<String, String> row : results_cat) {
                    System.out.println("---");
                    System.out.println(row);
                }
            }

        });
    }

    private static CompletableFuture<Void> fetchCategories(Context context, String token, Integer companyId) {
        return CompletableFuture.runAsync(() -> {
            int limit = 5;
            String url = "";
            int count = 0;
            int offset = 0;
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            List<Category> categoryList = new ArrayList<>();

            if (failsOffsetCategory.size() > 0 && !failsOffsetCategory.contains(0)) {

                System.out.println("SEARCH OFFSET FAIL CATEGORIA");
                for (Integer off : failsOffsetCategory) {
                    url = "http://10.0.2.2:9000/categories?limit=" + limit + "&offset=" + offset;
                    try {

                        CompletableFuture<JSONObject> futureCategory = LoadData.requestGet(context, url, token, companyId);
                        JSONObject response = null;
                        response = futureCategory.get();

                        if (response == null) {
                            System.out.println("Error en la respuesta de categorias con offset " + off);
                        }
                        else {
                            int status = response.getInt("code");
                            if (status == 200) {
                                count = response.getInt("count");
                                failsOffsetCategory.remove(off);
                                // GET CATEGORIAS
                                JSONArray categories = response.getJSONArray("result");
                                for (int i = 0; i < categories.length(); i++){
                                    Category category = new Category(categories.getJSONObject(i));
                                    categoryList.add(category);
                                }
                            }
                            else {
                                System.out.println("Error en la respuesta de categorias con status" + status + " con offset " + offset);
                            }
                        }

                    } catch (JSONException | InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
            else {
                failsOffsetCategory.remove(0);
                System.out.println("SEARCH OFFSET NORMAL");
                try {
                    //dbHelper = new DatabaseHelper(context);
                    do {

                        url = "http://10.0.2.2:9000/categories?limit=" + limit + "&offset=" + offset;
                        CompletableFuture<JSONObject> futureCategory = LoadData.requestGet(context, url, token, companyId);
                        JSONObject response = futureCategory.get();

                        try {
                            if (response == null) {
                                System.out.println("Error en la respuesta de catgories con offset " + offset);
                                failsOffsetCategory.add(offset);
                            }
                            else {
                                int status = response.getInt("code");
                                if (status == 200) {
                                    count = response.getInt("count");
                                    // GET CATEGORIES
                                    JSONArray categories = response.getJSONArray("result");
                                    for (int i = 0; i < categories.length(); i++){
                                        Category category = new Category(categories.getJSONObject(i));
                                        categoryList.add(category);
                                    }
                                }
                                else {
                                    System.out.println("Error en la respuesta de categorias con status " + status + "con offset " + offset);
                                    failsOffsetCategory.add(offset);
                                }
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        offset += limit;
                        System.out.println("offset categorias: " + offset);


                    } while (offset < count);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                }

            }

            System.out.println("FAIL OFFSET CATEGORIAS size " + failsOffsetCategory.size());
            System.out.println("FAIL OFFSET CATEGORIAS " + failsOffsetCategory);

            if (categoryList.size() > 0){
                dbHelper.insertCategoryTransaction(categoryList);
                String selectSql = "SELECT * FROM categoria";
                List<Map<String, String>> results_cat = dbHelper.executeSqlQuery(selectSql);
                System.out.println(results_cat.size());
                for (Map<String, String> row : results_cat) {
                    System.out.println("---");
                    System.out.println(row);
                }
            }

        });
    }


    public static void syncProductionDB(Context context, String token, Integer companyId, final GetLocalBDCallback callback) {

        // Borrar todas las tablas de la base de datos local
        DatabaseHelper dbHelper = null;

        try {
            System.out.println("IDC GETLOCALDB " + company_id);
            System.out.println("PARAMETRO " + companyId);
            if (companyId != company_id){
                clearFailsOffsets();
                company_id = companyId;
            }

            dbHelper = new DatabaseHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.deleteAllDataFromDatabase();

            CompletableFuture<Void> futureActivos = fetchActivos(context, token, companyId);
            CompletableFuture<Void> futureOficinas = fetchOficinas(context, token, companyId);
            CompletableFuture<Void> futureArticulos = fetchArticulos(context, token, companyId);
            CompletableFuture<Void> futureCategories = fetchCategories(context, token, companyId);
            CompletableFuture<Void> futureSucursales = fetchSucursales(context, token, companyId);

            // Espera a que todas las futuras operaciones se completen
            CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                    futureActivos, futureOficinas, futureArticulos, futureCategories, futureSucursales
            );

            // Encadena el callback después de que todas las operaciones se completen
            allFutures.thenRun(() -> {
                System.out.println("Todas las operaciones se han completado");
                System.out.println("FAIL OFFSET ARTICLE size " + failsOffsetArticle.size());
                System.out.println("FAIL OFFSET ARTICLE size " + failsOffsetArticle);
                System.out.println("FAIL OFFSET CATEGORY size " + failsOffsetCategory);
                System.out.println("FAIL OFFSET ACTVE size " + failsOffsetActive);
                System.out.println("FAIL OFFSET STORE size " + failsOffsetStore);
                System.out.println("FAIL OFFSET OFFICE size " + failsOffsetOffice);
                boolean allSuccess = failsOffsetStore.size() == 0 && failsOffsetOffice.size() == 0 && failsOffsetActive.size() == 0 && failsOffsetArticle.size() == 0 && failsOffsetCategory.size() == 0;
                
                if (allSuccess) {
                    callback.onSuccess("Datos descargados con éxito");
                } else {
                    callback.onError("Error durante la sincronización");
                    System.out.println("Error durante la sincronización " + allSuccess);
                }

            }).exceptionally(ex -> {
                System.out.println("Error durante la sincronización::: " + ex.getMessage());
                callback.onError("Error durante la sincronización: " + ex.getMessage());
                return null;
            });

        } finally {
            System.out.println("FINALLY");
            if (dbHelper != null) {
                dbHelper.close();
            }
        }
    }

    private static void fetchAll(Context context, String token, Integer companyId, GetLocalBDCallback callback) {

        //String url = "http://192.168.100.8:9000/all/data";
        String url = "http://10.0.2.2:9000/all/data";
        RequestQueue queue = Volley.newRequestQueue(context);

        // Crear el objeto JSON para enviar en el cuerpo de la solicitud
        JSONObject jsonBody = new JSONObject();
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,

            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("RESPONSE GET");
                    System.out.println(response.toString());
                    DatabaseHelper dbHelper = null;
                    try {
                        String code = response.getString("code");
                        if (code.equals("200")) {

                            JSONObject result = response.getJSONObject("result");
                            System.out.println("result all bd" + result);

                            dbHelper = new DatabaseHelper(context);
                            SQLiteDatabase db = dbHelper.getWritableDatabase();

                            dbHelper.deleteAllDataFromDatabase();
                            
                            String selectSql = "SELECT * FROM compania";
                            List<Map<String, String>> results = dbHelper.executeSqlQuery(selectSql);
                            System.out.println(results.size());
                            for (Map<String, String> row : results) {
                                System.out.println("---");
                                System.out.println(row);
                            }
                            
                            String dbPath = db.getPath();
                            System.out.println("La base de datos se almacena en: " + dbPath);

                            // GET COMPANY
                            Company companyItem = new Company(result.getJSONObject("company"));
                            boolean companyInsetStatus = dbHelper.insertCompanyData(companyItem);
                            
                            // GET SUCURSALES
                            JSONArray sucursales = result.getJSONArray("sucursales");
                            List<Store> storeList = new ArrayList<>();
                            for (int i = 0; i < sucursales.length(); i++){
                                Store store = new Store(sucursales.getJSONObject(i));
                                storeList.add(store);
                            }
                            dbHelper.insertSucursalTransaction(storeList);
                            selectSql = "SELECT * FROM sucursal";
                            List<Map<String, String>> results_suc = dbHelper.executeSqlQuery(selectSql);
                            System.out.println(results_suc.size());
                            for (Map<String, String> row : results_suc) {
                                System.out.println("---");
                                System.out.println(row);
                            }

                            // GET OFICINAS
                            JSONArray offices = result.getJSONArray("offices");
                            List<Office> officeList = new ArrayList<>();
                            for (int i = 0; i < offices.length(); i++){
                                Office office = new Office(offices.getJSONObject(i));
                                officeList.add(office);
                            }
                            dbHelper.insertOfficeTransaction(officeList);

                            selectSql = "SELECT * FROM oficina";
                            List<Map<String, String>> results_off = dbHelper.executeSqlQuery(selectSql);
                            System.out.println(results_off.size());
                            for (Map<String, String> row : results_off) {
                                System.out.println("---");
                                System.out.println(row);
                            }

                            // GET ARTICULOS
                            JSONArray articles = result.getJSONArray("articles");
                            List<Article> articlesList = new ArrayList<>();
                            for (int i = 0; i < articles.length(); i++) {
                                Article article = new Article(articles.getJSONObject(i));
                                articlesList.add(article);
                            }
                            dbHelper.insertArticleTransaction(articlesList);

                            selectSql = "SELECT * FROM articulo";
                            List<Map<String, String>> results_art = dbHelper.executeSqlQuery(selectSql);
                            System.out.println(results_art.size());
                            for (Map<String, String> row : results_art) {
                                System.out.println("---");
                                System.out.println(row);
                            }

                            // GET ACTIVOS
                            JSONArray actives = result.getJSONArray("actives");
                            List<Active> activeList = new ArrayList<>();
                            for (int i = 0; i < actives.length(); i++){
                                Active active = new Active(actives.getJSONObject(i));
                                activeList.add(active);
                            }
                            dbHelper.insertActiveTransaction(activeList);

                            selectSql = "SELECT * FROM activo";
                            List<Map<String, String>> results_act = dbHelper.executeSqlQuery(selectSql);
                            System.out.println(results_act.size());
                            for (Map<String, String> row : results_act) {
                                System.out.println("---");
                                System.out.println(row);
                            }

                            // GET CATEGORIAS
                            JSONArray categories = result.getJSONArray("categories");
                            List<Category> categoryList = new ArrayList<>();
                            for (int i = 0; i < categories.length(); i++){
                                Category category = new Category(categories.getJSONObject(i));
                                categoryList.add(category);
                            }
                            dbHelper.insertCategoryTransaction(categoryList);

                            selectSql = "SELECT * FROM categoria";
                            List<Map<String, String>> results_cat = dbHelper.executeSqlQuery(selectSql);
                            System.out.println(results_cat.size());
                            for (Map<String, String> row : results_cat) {
                                System.out.println("---");
                                System.out.println(row);
                            }

                            callback.onSuccess(response.toString()); 

                        }
                        else {
                            callback.onError("Error durante el inicio de sesión: ");
                        }

                    } catch (JSONException e) {
                        System.out.println("In exceptions!!!");
                        throw new RuntimeException(e);
                        
                    } finally {
                        System.out.println("FINALLY");
                        if (dbHelper != null) {
                            dbHelper.close();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    System.out.println("Error: "+error);
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

    public interface GetLocalBDCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public static CompletableFuture<Void> getAllDB(Context context, String token, Integer companyId) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        if (Looper.myLooper() == Looper.getMainLooper()) {
            // Estás en el hilo principal
            System.out.println("Estás en el hilo principal");
        } else {
            // No estás en el hilo principal
            System.out.println("No estás en el hilo principal");
        }

        System.out.println("get local bd");
//        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
//        String token = sharedPreferences.getString("accessToken", null);
        //String url = "http://192.168.100.8:9000/all/data";
        String url = "http://10.0.2.2:9000/all/data";
        System.out.println("token: " + token);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONSE GET");
                        System.out.println(response.toString());
                        DatabaseHelper dbHelper = null;
                        try {
                            String code = response.getString("code");
                            if (code.equals("200")) {

                                JSONObject result = response.getJSONObject("result");
                                System.out.println("result all bd" + result);

                                dbHelper = new DatabaseHelper(context);
                                SQLiteDatabase db = dbHelper.getWritableDatabase();

//                                dbHelper.deleteAllDataFromDatabase();
                                
                                String selectSql = "SELECT * FROM compania";
                                List<Map<String, String>> results = dbHelper.executeSqlQuery(selectSql);
                                System.out.println(results.size());
                                for (Map<String, String> row : results) {
                                    System.out.println("---");
                                    System.out.println(row);
                                }
                                
                                String dbPath = db.getPath();
                                System.out.println("La base de datos se almacena en: " + dbPath);

                                // GET COMPANY
                                Company companyItem = new Company(result.getJSONObject("company"));
                                boolean companyInsetStatus = dbHelper.insertCompanyData(companyItem);
                                
                                // GET SUCURSALES
                                JSONArray sucursales = result.getJSONArray("sucursales");
                                List<Store> storeList = new ArrayList<>();
                                for (int i = 0; i < sucursales.length(); i++){
                                    Store store = new Store(sucursales.getJSONObject(i));
                                    storeList.add(store);
                                }
                                dbHelper.insertSucursalTransaction(storeList);
                                
                                selectSql = "SELECT * FROM sucursal";
                                List<Map<String, String>> results_suc = dbHelper.executeSqlQuery(selectSql);
                                System.out.println(results_suc.size());
                                for (Map<String, String> row : results_suc) {
                                    System.out.println("---");
                                    System.out.println(row);
                                }

                                // GET OFICINAS
                                JSONArray offices = result.getJSONArray("offices");
                                List<Office> officeList = new ArrayList<>();
                                for (int i = 0; i < offices.length(); i++){
                                    Office office = new Office(offices.getJSONObject(i));
                                    officeList.add(office);
                                }
                                dbHelper.insertOfficeTransaction(officeList);

                                selectSql = "SELECT * FROM oficina";
                                List<Map<String, String>> results_off = dbHelper.executeSqlQuery(selectSql);
                                System.out.println(results_off.size());
                                for (Map<String, String> row : results_off) {
                                    System.out.println("---");
                                    System.out.println(row);
                                }

                                // GET ARTICULOS
                                JSONArray articles = result.getJSONArray("articles");
                                List<Article> articlesList = new ArrayList<>();
                                for (int i = 0; i < articles.length(); i++) {
                                    Article article = new Article(articles.getJSONObject(i));
                                    articlesList.add(article);
                                }
                                dbHelper.insertArticleTransaction(articlesList);

                                selectSql = "SELECT * FROM articulo";
                                List<Map<String, String>> results_art = dbHelper.executeSqlQuery(selectSql);
                                System.out.println(results_art.size());
                                for (Map<String, String> row : results_art) {
                                    System.out.println("---");
                                    System.out.println(row);
                                }

                                // GET ACTIVOS
                                JSONArray actives = result.getJSONArray("actives");
                                List<Active> activeList = new ArrayList<>();
                                for (int i = 0; i < actives.length(); i++){
                                    Active active = new Active(actives.getJSONObject(i));
                                    activeList.add(active);
                                }
                                dbHelper.insertActiveTransaction(activeList);

                                selectSql = "SELECT * FROM activo";
                                List<Map<String, String>> results_act = dbHelper.executeSqlQuery(selectSql);
                                System.out.println(results_act.size());
                                for (Map<String, String> row : results_act) {
                                    System.out.println("---");
                                    System.out.println(row);
                                }

                                // GET CATEGORIAS
                                JSONArray categories = result.getJSONArray("categories");
                                List<Category> categoryList = new ArrayList<>();
                                for (int i = 0; i < categories.length(); i++){
                                    Category category = new Category(categories.getJSONObject(i));
                                    categoryList.add(category);
                                }
                                dbHelper.insertCategoryTransaction(categoryList);

                                selectSql = "SELECT * FROM categoria";
                                List<Map<String, String>> results_cat = dbHelper.executeSqlQuery(selectSql);
                                System.out.println(results_cat.size());
                                for (Map<String, String> row : results_cat) {
                                    System.out.println("---");
                                    System.out.println(row);
                                }

                                future.complete(null);
                                return;

                            }
                            else {

                            }

                        } catch (JSONException e) {
                            System.out.println("In exceptions!!!");
                            throw new RuntimeException(e);
                            
                        } finally {
                            System.out.println("FINALLY");
                             if (dbHelper != null) {
                                 dbHelper.close();
                             }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                System.out.println("Error: "+error);
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token); // Reemplaza 'token' con tu token de autenticación
                headers.put("companyId", String.valueOf(companyId));
                return headers;
            }
        };
        queue.add(jsonRequest);
        return future;
    }

}
