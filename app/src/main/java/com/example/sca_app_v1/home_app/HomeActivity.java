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

import com.example.sca_app_v1.home_app.bdLocal.GetLocalBD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    //AutoCompleteTextView companySelect;
    //CompanyAdapter adapterItems;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        String token = sharedPreferences.getString("accessToken", null);
        Integer company_id = sharedPreferences.getInt("company_id", 0);

        GetLocalBD.getAllDB(this, token, company_id);



        /*companySelect = findViewById(R.id.company_select);
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

         */

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

}
