package com.example.sca_app_v1.home_app.bdLocal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.AutoCompleteTextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sca_app_v1.databinding.ActivityMainBinding;
import com.example.sca_app_v1.home_app.company.Company;
import com.example.sca_app_v1.home_app.company.CompanyItem;

import com.example.sca_app_v1.home_app.bdLocal.DatabaseHelper;
import com.example.sca_app_v1.models.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetLocalBD {

    private ActivityMainBinding binding;
    AutoCompleteTextView companySelect;
    public static void getAllDB(Context context, String token, Integer companyId) {
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

                                
                                String selectSql = "SELECT * FROM compania";
                                List<Map<String, String>> results = dbHelper.executeSqlQuery(selectSql);
                                System.out.println(results.size());
                                for (Map<String, String> row : results) {
                                    System.out.println("---");
                                    System.out.println(row);
                                }
                                
                                String dbPath = db.getPath();
                                System.out.println("La base de datos se almacena en: " + dbPath);


                                JSONObject company = result.getJSONObject("company");
                                System.out.println("company: " + company);
                                boolean companyInsetStatus = dbHelper.insertCompanyData((int) company.get("id"), (String) company.get("name"), company.get("rut").toString(), company.get("country").toString(), company.get("contact_name").toString(), company.get("contact_email").toString(), company.get("contact_phone").toString(), (int) company.get("removed"), company.get("name_db").toString());
                                System.out.println("companyInsetStatus > " + companyInsetStatus);
                            
                                JSONArray sucursales = result.getJSONArray("sucursales");
                                System.out.println("sucursales: " + sucursales);
                                List<Store> storeList = new ArrayList<>();
                                for (int i = 0; i < sucursales.length(); i++){
                                    JSONObject sucursal = sucursales.getJSONObject(i);
                                    Store storeItem = new Store(
                                            (int) sucursal.get("id"),
                                            sucursal.get("description").toString(),
                                            sucursal.get("number").toString(),
                                            sucursal.get("address").toString(),
                                            sucursal.get("region").toString(),
                                            sucursal.get("city").toString(),
                                            sucursal.get("commune").toString(),
                                            (int) sucursal.get("removed"),
                                            (int) sucursal.get("company_id")
                                    );
                                    storeList.add(storeItem);
                                    dbHelper.insertSucursalTransaction(storeList);
                                }
                                selectSql = "SELECT * FROM sucursal";
                                List<Map<String, String>> results_suc = dbHelper.executeSqlQuery(selectSql);
                                System.out.println(results_suc.size());
                                for (Map<String, String> row : results_suc) {
                                    System.out.println("---");
                                    System.out.println(row);
                                }

                                JSONArray offices = result.getJSONArray("offices");
                                System.out.println("offices: " + offices);
                                List<Office> officeList = new ArrayList<>();
                                for (int i = 0; i < offices.length(); i++){
                                    JSONObject office = offices.getJSONObject(i);
                                    Office officeItem = new Office(
                                            (int) office.get("id"),
                                            office.get("description").toString(),
                                            (int) office.get("floor"),
                                            office.get("name_in_charge").toString(),
                                            (int) office.get("removed"),
                                            (int) office.get("sucursal_id")
                                    );
                                    officeList.add(officeItem);
                                    dbHelper.insertOfficeTransaction(officeList);
                                }
                                selectSql = "SELECT * FROM oficina";
                                List<Map<String, String>> results_off = dbHelper.executeSqlQuery(selectSql);
                                System.out.println(results_off.size());
                                for (Map<String, String> row : results_off) {
                                    System.out.println("---");
                                    System.out.println(row);
                                }

                                JSONArray articles = result.getJSONArray("articles");
                                System.out.println("articles: " + articles);
                                List<Article> articlesList = new ArrayList<>();
                                for (int i = 0; i < articles.length(); i++) {
                                    JSONObject article = articles.getJSONObject(i);

                                    int categoryId = 0;


                                    Article articleItem = new Article(
                                            (int) article.get("id"),
                                            article.get("name").toString(),
                                            article.get("description").toString(),
                                            article.get("code").toString(),
                                            article.get("photo").toString(),
                                            (int) article.get("count_active"),
                                            article.get("creation_date").toString(),
                                            (int) article.get("removed"),
                                            categoryId,
                                            (int) article.get("company_id")
                                    );
                                    articlesList.add(articleItem);
                                }
                                dbHelper.insertArticleTransaction(articlesList);

                                selectSql = "SELECT * FROM articulo";
                                List<Map<String, String>> results_art = dbHelper.executeSqlQuery(selectSql);
                                System.out.println(results_art.size());
                                for (Map<String, String> row : results_art) {
                                    System.out.println("---");
                                    System.out.println(row);
                                }

                                JSONArray actives = result.getJSONArray("actives");
                                System.out.println("actives: " + actives);
                                List<Active> activeList = new ArrayList<>();
                                for (int i = 0; i < actives.length(); i++){
                                    JSONObject active = actives.getJSONObject(i);
                                    Active activeItem = new Active(
                                            (int) active.get("id"),
                                            active.get("bar_code").toString(),
                                            active.get("comment").toString(),
                                            active.get("acquisition_date").toString(),
                                            active.get("accounting_document").toString(),
                                            active.get("accounting_record_number").toString(),
                                            active.get("name_in_charge_active").toString(),
                                            active.get("rut_in_charge_active").toString(),
                                            active.get("serie").toString(),
                                            active.get("model").toString(),
                                            active.get("state").toString(),
                                            active.get("creation_date").toString(),
                                            (int) active.get("removed"),
                                            (int) active.get("office_id"),
                                            (int) active.get("article_id")
                                    );
                                    activeList.add(activeItem);
                                    dbHelper.insertActiveTransaction(activeList);
                                }
                                selectSql = "SELECT * FROM activo";
                                List<Map<String, String>> results_act = dbHelper.executeSqlQuery(selectSql);
                                System.out.println(results_act.size());
                                for (Map<String, String> row : results_act) {
                                    System.out.println("---");
                                    System.out.println(row);
                                }

                                JSONArray categories = result.getJSONArray("categories");
                                System.out.println("categories: " + categories);
                                List<Category> categoryList = new ArrayList<>();
                                for (int i = 0; i < categories.length(); i++){
                                    JSONObject category = categories.getJSONObject(i);
                                    Category categoryItem = new Category(
                                            (int) category.get("id"),
                                            category.get("description").toString(),
                                            (int) category.get("parent_id"),
                                            (int) category.get("removed")
                                    );
                                    categoryList.add(categoryItem);
                                    dbHelper.insertCategoryTransaction(categoryList);
                                }
                                selectSql = "SELECT * FROM categoria";
                                List<Map<String, String>> results_cat = dbHelper.executeSqlQuery(selectSql);
                                System.out.println(results_cat.size());
                                for (Map<String, String> row : results_cat) {
                                    System.out.println("---");
                                    System.out.println(row);
                                }


                                

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

    }

}
