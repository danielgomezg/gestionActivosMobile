package com.example.sca_app_v1.home_app.bdLocal;

import android.content.Context;
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

public class GetLocalBD {

    private ActivityMainBinding binding;
    AutoCompleteTextView companySelect;
    private static List<Integer> failsOffsetStore = new ArrayList<>();
    private static List<Integer> failsOffsetOffice = new ArrayList<>();
    private static List<Integer> failsOffsetActive = new ArrayList<>();
    private static List<Integer> failsOffsetArticle = new ArrayList<>();
    private static List<Integer> failsOffsetCategory = new ArrayList<>();

    public static void deleteLocalTables(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        dbHelper.deleteAllTables();
    }



    private static CompletableFuture<Void> fetchOficinas(Context context, String token, Integer companyId) {
        return CompletableFuture.runAsync(() -> {
            int limit = 5;
            String url = "";
            int count = 0;
            int offset = 0;
            DatabaseHelper dbHelper = null;
            List<Integer> failsOffset = new ArrayList<>();
            List<Office> officeList = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(context);

            if (failsOffsetOffice.size() > 0) {

                System.out.println("SEARCH OFFSET FAIL");
                for (Integer off : failsOffsetOffice) {
                    url = "http://10.0.2.2:9000/offices?limit=" + limit + "&offset=" + offset;
                    try {
    
                        CompletableFuture<JSONObject> futureOffice = LoadData.requestGet(context, url, token, companyId);
                        JSONObject response = null;
                        response = futureOffice.get();
                
                        if (response == null) {
                            System.out.println("Error en la respuesta de oficinas A");
                            failsOffset.add(offset);
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
                                failsOffset.add(offset);
                                failsOffsetOffice.add(offset);
                            }
                        }

                    } catch (JSONException | InterruptedException | ExecutionException e) {
                        throw new RuntimeException(e);
                    }

                }

            }
            else {

                System.out.println("SEARCH OFFSET NORMAL");
                try {
                    dbHelper = new DatabaseHelper(context);
                    do {
    
                        url = "http://10.0.2.2:9000/offices?limit=" + limit + "&offset=" + offset;
                        CompletableFuture<JSONObject> futureOffice = LoadData.requestGet(context, url, token, companyId);
                        JSONObject response = futureOffice.get();
    
                        try {
                            if (response == null) {
                                System.out.println("Error en la respuesta de oficinas A");
                                failsOffset.add(offset);
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
                                    failsOffset.add(offset);
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

            System.out.println("FAIL OFFSET OFFICES size " + failsOffset.size());
            System.out.println("FAIL OFFSET OFFICES" + failsOffset);

            dbHelper.insertOfficeTransaction(officeList);
            String selectSql = "SELECT * FROM oficina";
            List<Map<String, String>> results_cat = dbHelper.executeSqlQuery(selectSql);
            System.out.println(results_cat.size());
            for (Map<String, String> row : results_cat) {
                System.out.println("---");
                System.out.println(row);
            }

        });
    }

    private static CompletableFuture<Void> fetchSucursales(Context context, String token, Integer companyId) {
        return CompletableFuture.runAsync(() -> {
            int limit = 5;
            String url = "";
            int[] count = {0};
            int[] offset = {0};
            DatabaseHelper dbHelper = null;
            List<Integer> failsOffset = new ArrayList<>();
            List<Store> sucursalesList = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(context);

            try {
                dbHelper = new DatabaseHelper(context);
                do {
                    CountDownLatch latch = new CountDownLatch(1);

                    url = "http://10.0.2.2:9000/sucursales?limit=" + limit + "&offset=" + offset[0];
                    System.out.println("url --> " + url);
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    System.out.println("RESPONSE GET SUCURSALES");
                                    System.out.println(response.toString());
                                    try {
                                        int status = response.getInt("code");
                                        if (status == 200) {

                                            count[0] = response.getInt("count");

                                            // GET SUCURSALES
                                            JSONArray sucursales = response.getJSONArray("result");

                                            for (int i = 0; i < sucursales.length(); i++){
                                                Store sucursal = new Store(sucursales.getJSONObject(i));
                                                sucursalesList.add(sucursal);
                                            }

                                        }
                                        else {

                                        }

                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

                                    latch.countDown(); // Decrementa el contador del latch

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            System.out.println("Error: "+error);
                            failsOffset.add(offset[0]);

                            latch.countDown(); // Decrementa el contador del latch
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

                    queue.add(jsonRequest);

                    latch.await();

                    offset[0] += limit;
                    System.out.println("offset: " + offset);


                } while (offset[0] < count[0]);

                dbHelper.insertSucursalTransaction(sucursalesList);
                String selectSql = "SELECT * FROM sucursal";
                List<Map<String, String>> results_cat = dbHelper.executeSqlQuery(selectSql);
                System.out.println(results_cat.size());
                for (Map<String, String> row : results_cat) {
                    System.out.println("---");
                    System.out.println(row);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static CompletableFuture<Void> fetchActivos(Context context, String token, Integer companyId) {
        return CompletableFuture.runAsync(() -> {
            int limit = 5;
            String url = "";
            int[] count = {0};
            int[] offset = {0};
            DatabaseHelper dbHelper = null;
            List<Integer> failsOffset = new ArrayList<>();
            List<Active> activeList = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(context);

            try {
                dbHelper = new DatabaseHelper(context);
                do {
                    CountDownLatch latch = new CountDownLatch(1);

                    url = "http://10.0.2.2:9000/actives?limit=" + limit + "&offset=" + offset[0];
                    System.out.println("url --> " + url);
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    System.out.println("RESPONSE GET ACTIVES");
                                    System.out.println(response.toString());
                                    try {
                                        int status = response.getInt("code");
                                        if (status == 200) {

                                            count[0] = response.getInt("count");

                                            // GET CATEGORIAS
                                            JSONArray actives = response.getJSONArray("result");

                                            for (int i = 0; i < actives.length(); i++){
                                                Active active = new Active(actives.getJSONObject(i));
                                                activeList.add(active);
                                            }

                                        }
                                        else {

                                        }

                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

                                    latch.countDown(); // Decrementa el contador del latch

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            System.out.println("Error: "+error);
                            failsOffset.add(offset[0]);

                            latch.countDown(); // Decrementa el contador del latch
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

                    queue.add(jsonRequest);

                    latch.await();

                    offset[0] += limit;
                    System.out.println("offset: " + offset);


                } while (offset[0] < count[0]);

                dbHelper.insertActiveTransaction(activeList);
                String selectSql = "SELECT * FROM activo";
                List<Map<String, String>> results_cat = dbHelper.executeSqlQuery(selectSql);
                System.out.println(results_cat.size());
                for (Map<String, String> row : results_cat) {
                    System.out.println("---");
                    System.out.println(row);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private static CompletableFuture<Void> fetchArticulos(Context context, String token, Integer companyId) {
        return CompletableFuture.runAsync(() -> {

            int limit = 5;
            String url = "";
            int[] count = {0};
            int[] offset = {0};
            DatabaseHelper dbHelper = null;
            List<Integer> failsOffset = new ArrayList<>();
            List<Article> articlesList = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(context);

            try {
                dbHelper = new DatabaseHelper(context);
                do {
                    CountDownLatch latch = new CountDownLatch(1);
    
                    url = "http://10.0.2.2:9000/articles?limit=" + limit + "&offset=" + offset[0];
                    System.out.println("url --> " + url);
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
    
                                System.out.println("RESPONSE GET ARTICLES");
                                System.out.println(response.toString());
                                try {
                                    int status = response.getInt("code");
                                    if (status == 200) {
                                        
                                        count[0] = response.getInt("count");

                                        // GET ARTICULOS
                                        JSONArray articles = response.getJSONArray("result");
                                        for (int i = 0; i < articles.length(); i++) {
                                            Article article = new Article(articles.getJSONObject(i));
                                            articlesList.add(article);
                                        }

                                    }
                                    else {
                                        
                                    }

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                                latch.countDown(); // Decrementa el contador del latch
    
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                System.out.println("Error: "+error);
                                failsOffset.add(offset[0]);
    
                                latch.countDown(); // Decrementa el contador del latch
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
    
                    queue.add(jsonRequest);
    
                    latch.await();
    
                    offset[0] += limit;
                    System.out.println("offset: " + offset);
    
    
                } while (offset[0] < count[0]);

                dbHelper.insertArticleTransaction(articlesList);
                String selectSql = "SELECT * FROM articulo";
                List<Map<String, String>> results_art = dbHelper.executeSqlQuery(selectSql);
                System.out.println(results_art.size());
                for (Map<String, String> row : results_art) {
                    System.out.println("---");
                    System.out.println(row);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
    }

    private static CompletableFuture<Void> fetchCategories(Context context, String token, Integer companyId) {
        return CompletableFuture.runAsync(() -> {

            int limit = 5;
            String url = "";
            int[] count = {0};
            int[] offset = {0};
            DatabaseHelper dbHelper = null;
            List<Integer> failsOffset = new ArrayList<>();
            List<Category> categoryList = new ArrayList<>();
            RequestQueue queue = Volley.newRequestQueue(context);

            try {
                dbHelper = new DatabaseHelper(context);
                do {
                    CountDownLatch latch = new CountDownLatch(1);
    
                    url = "http://10.0.2.2:9000/categories?limit=" + limit + "&offset=" + offset[0];
                    System.out.println("url --> " + url);
                    JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
    
                                System.out.println("RESPONSE GET CATEGORIES");
                                System.out.println(response.toString());
                                try {
                                    int status = response.getInt("code");
                                    if (status == 200) {
                                        
                                        count[0] = response.getInt("count");

                                        // GET CATEGORIAS
                                        JSONArray categories = response.getJSONArray("result");
                                        
                                        for (int i = 0; i < categories.length(); i++){
                                            Category category = new Category(categories.getJSONObject(i));
                                            categoryList.add(category);
                                        }

                                    }
                                    else {
                                        
                                    }

                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }

                                latch.countDown(); // Decrementa el contador del latch
    
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                System.out.println("Error: "+error);
                                failsOffset.add(offset[0]);
    
                                latch.countDown(); // Decrementa el contador del latch
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
    
                    queue.add(jsonRequest);
    
                    latch.await();
    
                    offset[0] += limit;
                    System.out.println("offset: " + offset);
    
    
                } while (offset[0] < count[0]);

                dbHelper.insertCategoryTransaction(categoryList);
                String selectSql = "SELECT * FROM categoria";
                List<Map<String, String>> results_cat = dbHelper.executeSqlQuery(selectSql);
                System.out.println(results_cat.size());
                for (Map<String, String> row : results_cat) {
                    System.out.println("---");
                    System.out.println(row);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        });
    }


    public static void syncProductionDB(Context context, String token, Integer companyId, final GetLocalBDCallback callback) {

        // Borrar todas las tablas de la base de datos local
        DatabaseHelper dbHelper = null;
        try {

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
                System.out.println("FAIL OFFSET STORE size " + failsOffsetOffice.size());
                boolean allSuccess = failsOffsetStore.size() == 0 && failsOffsetOffice.size() == 0 && failsOffsetActive.size() == 0 && failsOffsetArticle.size() == 0 && failsOffsetCategory.size() == 0;
                
                if (allSuccess) {
                    callback.onSuccess("Datos descargados con éxito");
                } else {
                    callback.onError("Error durante la sincronización");
                }
                // callback.onSuccess("Datos descargados con éxito");



            }).exceptionally(ex -> {
                System.out.println("Error durante la sincronización: " + ex.getMessage());
                callback.onError("Error durante la sincronización: " + ex.getMessage());
                return null;
            });

        } finally {
            System.out.println("FINALLY");
            if (dbHelper != null) {
                dbHelper.close();
            }
        }


        //
        // Funcion actual para sync de la base de datos
        //fetchAll(context, token, companyId, callback);

        
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
