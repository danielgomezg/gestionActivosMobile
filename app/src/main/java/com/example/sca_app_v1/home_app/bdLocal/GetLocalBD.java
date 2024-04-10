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

public class GetLocalBD {

    private ActivityMainBinding binding;
    AutoCompleteTextView companySelect;
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

                        } catch (JSONException e) {
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
