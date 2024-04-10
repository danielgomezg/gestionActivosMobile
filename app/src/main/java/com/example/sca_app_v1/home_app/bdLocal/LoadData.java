package com.example.sca_app_v1.home_app.bdLocal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.sca_app_v1.home_app.HomeActivity;
import com.example.sca_app_v1.home_app.article.ArticleFragment;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.sca_app_v1.databinding.ActivityLoadDataBinding;

import com.example.sca_app_v1.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadData extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityLoadDataBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoadDataBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadCompanyData();
    }

    public void loadCompanyData() {
        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
        String token = sharedPreferences.getString("accessToken", null);
        Integer company_id = sharedPreferences.getInt("company_id", 0);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            GetLocalBD.getAllDB(LoadData.this, token, company_id).thenRun(() -> {
                System.out.println("End query 1");
                Intent intent = new Intent(LoadData.this, HomeActivity.class);
                startActivity(intent);
//                ArticleFragment articleFragment = new ArticleFragment();
//                articleFragment.showArticles(LoadData.this);
            });

            runOnUiThread(() -> {
                System.out.println("End query 0");
                // binding.getRoot().removeView(progressView);
            });
        });
    }

}