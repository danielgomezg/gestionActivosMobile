package com.example.sca_app_v1.home_app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.sca_app_v1.MainActivity;
import com.example.sca_app_v1.R;
import com.example.sca_app_v1.databinding.ActivityMainBinding;

import com.example.sca_app_v1.models.Active;
import com.example.sca_app_v1.models.Article;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private TextView nombreTextView;
    private TextView correoTextView;

    private String emailUser;
    private String nameUser;
    private String lastNameUser;
    private Button btnLogout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //boton add
        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        View headerView = navigationView.getHeaderView(0);
        nombreTextView = headerView.findViewById(R.id.nameUser);
        correoTextView = headerView.findViewById(R.id.mailUser);

        //obtener la datos user
        SharedPreferences sharedPreferences = this.getSharedPreferences("session", Context.MODE_PRIVATE);
        String infoUserString = sharedPreferences.getString("user", "");

        try {
            JSONObject userInfoJson = new JSONObject(infoUserString);
            emailUser = userInfoJson.getString("email");
            nameUser = userInfoJson.getString("firstName");
            lastNameUser = userInfoJson.getString("lastName");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        nombreTextView.setText(nameUser + " " + lastNameUser);
        correoTextView.setText(emailUser);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_active, R.id.nav_article, R.id.nav_sync)
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_logout) {
            System.out.println("IN LOGOUT ACTION");

            boolean UnsyncedArticles = Article.hasUnsyncedArticles(HomeActivity.this);
            System.out.println("UnsyncedArticles: " + UnsyncedArticles);
            boolean UnsyncedActives = Active.hasUnsyncedActive(HomeActivity.this);
            System.out.println("UnsyncedActives: " + UnsyncedActives);

            if (UnsyncedActives || UnsyncedArticles){
                // Si hay datos pendientes, muestra un diálogo de confirmación
                new AlertDialog.Builder(this)
                        .setTitle("Datos pendientes por sincronizar")
                        .setMessage("Tienes datos pendientes por subir. ¿Estás seguro de que quieres cerrar sesión?")
                        .setPositiveButton("Cerrar sesión", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout();
                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();
            } else {
                logout();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    private void logout(){
        SharedPreferences sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
