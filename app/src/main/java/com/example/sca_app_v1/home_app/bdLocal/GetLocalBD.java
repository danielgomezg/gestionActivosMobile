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
