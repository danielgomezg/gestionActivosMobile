package com.example.sca_app_v1.home_app.bdLocal;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sca_app_v1.home_app.HomeActivity;
import com.example.sca_app_v1.home_app.article.ArticleFragment;
import com.example.sca_app_v1.models.Company;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Layout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.sca_app_v1.databinding.ActivityLoadDataBinding;

import com.example.sca_app_v1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadData extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityLoadDataBinding binding;
    private AutoCompleteTextView companySelect;
    private ArrayAdapter<String> adapterItems;
    private int companyId;
    private Button buttonSync;
    private LinearLayout layoutLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoadDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buttonSync = findViewById(R.id.button_sync);
        layoutLoading = findViewById(R.id.lyLoading);

        getCompanyList();

        buttonSync.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                layoutLoading.setVisibility(View.VISIBLE);
                loadCompanyData();
            }
        });
    }

    public void loadCompanyData() {
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        String token = sharedPreferences.getString("accessToken", null);
        //Integer company_id = sharedPreferences.getInt("company_id", 0);
        System.out.println("Company_id " + companyId);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("company_id", companyId);
        editor.apply();

        GetLocalBD.syncProductionDB(LoadData.this, token, companyId, new GetLocalBD.GetLocalBDCallback(){
            @Override
            public void onSuccess(String response) {
                System.out.println("SUCCESS SYNC");
                layoutLoading.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(LoadData.this, HomeActivity.class);
                startActivity(intent);
            }

            @Override
            public void onError(String error) {
                System.out.println("ERROR SYNC");
                layoutLoading.setVisibility(View.INVISIBLE);
                // Toast.makeText(LoadData.this, "Error al sincronizar.", Toast.LENGTH_SHORT).show();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LoadData.this, "Error al sincronizar.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void getCompanyList() {
        System.out.println("get company List");
        RequestQueue queue = Volley.newRequestQueue(this);
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        String token = sharedPreferences.getString("accessToken", null);
        //String url = "http://192.168.100.8:9000/companiesIdName?limit=200";
        String url = "http://10.0.2.2:9000/companiesIdName?limit=200";

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,

            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("response company");
                    System.out.println(response.toString());
                    try {
                        initializeCompanies(response.getJSONArray("result"));
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("Error RESPONSE!!: " + error);
                    layoutLoading.setVisibility(View.INVISIBLE);
                    Toast.makeText(LoadData.this, "Un error ha ocurrido.", Toast.LENGTH_SHORT).show();
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

        // Agregar la solicitud a la cola
        queue.add(jsonRequest);

    }

    public void initializeCompanies(JSONArray companies) throws JSONException {


        TextInputLayout textInputLayoutCompany = findViewById(R.id.tilCompanies);
        AutoCompleteTextView autoCompleteTextViewCompany = textInputLayoutCompany.findViewById(R.id.company_select);

        List<String> items = new ArrayList<>();
        for (int i = 0; i < companies.length(); i++) {
            JSONObject company = companies.getJSONObject(i);
            items.add(company.getString("name"));
        }

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
        autoCompleteTextViewCompany.setAdapter(adapterItems);

        autoCompleteTextViewCompany.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View _view, int position, long id) {
                try {
                    System.out.println("click opt " + position);
                    System.out.println("long id " + id);
                    String companySelected = (String) parent.getItemAtPosition(position);
                    for (int i = 0; i < companies.length(); i++) {
                        JSONObject jsonObject = companies.getJSONObject(i);
                        if (jsonObject.getString("name").equals(companySelected)) {
                            
                            companyId = jsonObject.getInt("id");
                            break;
                            
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);

                }
            }
        });

    }

    public static CompletableFuture<JSONObject> requesGetOffices(Context context, String token, Integer companyId, Integer limit, Integer offset) {

        CompletableFuture<JSONObject> futureResponse = new CompletableFuture<>();
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://10.0.2.2:9000/offices?limit=" + limit + "&offset=" + offset;
        System.out.println("URL GET OFFICE " + url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    System.out.println(response.toString());
                    try {
                        int status = response.getInt("code");
                        if (status == 200) {

                            futureResponse.complete(response);

                        }
                        else {
                            futureResponse.complete(null);
                        }

                    } catch (JSONException e) {
                        // throw new RuntimeException(e);
                        futureResponse.complete(null);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    futureResponse.complete(null);
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
        return futureResponse;

    }

    public static CompletableFuture<JSONObject> requestGet(Context context, String url, String token, Integer companyId) {

        CompletableFuture<JSONObject> futureResponse = new CompletableFuture<>();
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    System.out.println(response.toString());
                    try {
                        int status = response.getInt("code");
                        if (status == 200) {
                            // response.put("code", "400");
                            futureResponse.complete(response);
                        }
                        else {
                            futureResponse.complete(null);
                        }

                    } catch (JSONException e) {
                        // throw new RuntimeException(e);
                        futureResponse.complete(null);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    futureResponse.complete(null);
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
        return futureResponse;
    
    }

}