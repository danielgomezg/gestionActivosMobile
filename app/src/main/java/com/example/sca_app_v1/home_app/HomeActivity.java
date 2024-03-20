package com.example.sca_app_v1.home_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.sca_app_v1.R;
import com.example.sca_app_v1.databinding.ActivityMainBinding;
import com.example.sca_app_v1.home_app.company.Company;
import com.example.sca_app_v1.home_app.company.CompanyAdapter;
import com.example.sca_app_v1.home_app.company.CompanyItem;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    //String[] items = { "Funa", "Savory", "Trendy", "Bresler" };
    AutoCompleteTextView companySelect;
    CompanyAdapter adapterItems;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        String token = sharedPreferences.getString("accessToken", null);

        companySelect = findViewById(R.id.company_select);
        Company.getCompanyList(this, token, new Company.CompanyListCallback() {
            public void onSuccess(List<CompanyItem> companies) {
                System.out.println("response in on success: " + companies);
                adapterItems = new CompanyAdapter(HomeActivity.this, companies);
                companySelect.setAdapter(adapterItems);
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
            }
        });

        companySelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CompanyItem item = (CompanyItem) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(), "ID: "+item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Reemplaza with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_active, R.id.nav_article)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    /*private void getCompanyList() {
        System.out.println("get company List");
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        String token = sharedPreferences.getString("accessToken", null);
        String url = "http://10.0.2.2:9000/companiesIdName";
        System.out.println("token: " + token);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,

            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response.toString());

                    try {
                        String code = response.getString("code");
                        if (code.equals("200")) {
                            JSONArray result = response.getJSONArray("result");
                            System.out.println(result);
                            companySelect = findViewById(R.id.company_select);
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

    }*/

    /*private TextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        SharedPreferences prefs = getSharedPreferences("prefs_name", Context.MODE_PRIVATE);
        String user_name_Json = prefs.getString("user","");
        System.out.println(user_name_Json);

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(user_name_Json);

            // Acceder al valor del nombre de usuario dentro del objeto JSON
            String userNameValue = jsonObject.getString("firstName");

            // Mostrar el nombre de usuario en el TextView
            userName = findViewById(R.id.user_name);
            userName.setText(userNameValue);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }*/

}
