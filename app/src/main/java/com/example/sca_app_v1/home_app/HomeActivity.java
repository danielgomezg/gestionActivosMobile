package com.example.sca_app_v1.home_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.sca_app_v1.home_app.article.ArticleFragment;
import com.example.sca_app_v1.home_app.article.FormCreateArticle;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        //Barra
        ProgressBar loadingSpinner = findViewById(R.id.loading_spinner);
        loadingSpinner.setVisibility(View.VISIBLE);

        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        String token = sharedPreferences.getString("accessToken", null);
        Integer company_id = sharedPreferences.getInt("company_id", 0);

        View progressView = getLayoutInflater().inflate(R.layout.circular_progress, null);
        //binding.getRoot().addView(progressView);

        // GetLocalBD.getAllDB(this, token, company_id);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            GetLocalBD.getAllDB(HomeActivity.this, token, company_id).thenRun(() -> {
                System.out.println("End query 1");
                ArticleFragment articleFragment = new ArticleFragment();
                articleFragment.showArticles(HomeActivity.this);
            });

            runOnUiThread(() -> {
                System.out.println("End query 0");
                // binding.getRoot().removeView(progressView);
            });
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingSpinner.setVisibility(View.GONE);
            }
        }, 3000);
        //loadingSpinner.setVisibility(View.GONE);

        //boton add
        setSupportActionBar(binding.appBarMain.toolbar);
       /* binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Reemplaza with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                FormCreateArticle bottomSheet = new FormCreateArticle();
                bottomSheet.show(getSupportFragmentManager(), "formCreateArticle");
            }
        });*/

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
