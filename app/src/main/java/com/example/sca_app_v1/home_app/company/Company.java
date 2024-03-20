package com.example.sca_app_v1.home_app.company;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.AutoCompleteTextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sca_app_v1.R;
import com.example.sca_app_v1.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Company {

    private ActivityMainBinding binding;
    AutoCompleteTextView companySelect;
    public static void getCompanyList(Context context, String token, final CompanyListCallback callback) {
        System.out.println("get company List");
//        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
//        String token = sharedPreferences.getString("accessToken", null);
        String url = "http://10.0.2.2:9000/companiesIdName";
        System.out.println("token: " + token);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("RESPONSE GET");
                        System.out.println(response.toString());

                        try {
                            String code = response.getString("code");
                            if (code.equals("200")) {
                                JSONArray result = response.getJSONArray("result");
//                                List<String> companies = new ArrayList<>();
//                                System.out.println(result);
//                                for (int i = 0; i < result.length(); i++) {
//                                    JSONObject company = result.getJSONObject(i);
//                                    String name = company.getString("name");
//                                    companies.add(name);
//                                }
                                List<CompanyItem> companies = new ArrayList<>();
                                for (int i = 0; i < result.length(); i++) {

                                    JSONObject company = result.getJSONObject(i);
                                    String name = company.getString("name");
                                    String id = company.getString("id");
                                    companies.add(new CompanyItem(id, name));
                                }

                                callback.onSuccess(companies);
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error: "+error);
            }
        }


        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token); // Reemplaza 'token' con tu token de autenticación
                return headers;
            }
        };
        queue.add(jsonRequest);

    }

    public interface CompanyListCallback {
        void onSuccess(List<CompanyItem> companies);
        void onError(String error);

    }


}
