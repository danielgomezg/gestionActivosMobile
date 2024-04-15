package com.example.sca_app_v1.home_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.sca_app_v1.MainActivity;
import com.example.sca_app_v1.R;
import com.example.sca_app_v1.databinding.ActivityMainBinding;
import com.example.sca_app_v1.home_app.article.ArticleFragment;
import com.example.sca_app_v1.home_app.article.FormCreateArticle;
import com.example.sca_app_v1.home_app.company.Company;
import com.example.sca_app_v1.home_app.company.CompanyAdapter;
import com.example.sca_app_v1.home_app.company.CompanyItem;
import com.example.sca_app_v1.models.Active;
import com.example.sca_app_v1.models.Article;
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

    private TextView nombreTextView;
    private TextView correoTextView;

    private String emailUser;
    private String nameUser;
    private String lastNameUser;
    private Button btnLogout;

    //AutoCompleteTextView companySelect;
    //CompanyAdapter adapterItems;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //boton add
        setSupportActionBar(binding.appBarMain.toolbar);
       /*binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormCreateArticle bottomSheet = new FormCreateArticle();
                bottomSheet.show(getSupportFragmentManager(), "formCreateArticle");
            }
        });*/

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

            //Log.d("Email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        nombreTextView.setText(nameUser + " " + lastNameUser);
        correoTextView.setText(emailUser);

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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.action_logout) {
            System.out.println("IN LOGOUT ACTION");
            System.out.println("IN LOGOUT ACTION");
            System.out.println("IN LOGOUT ACTION");

            boolean UnsyncedArticles = Article.hasUnsyncedArticles(HomeActivity.this);
            System.out.println("UnsyncedArticles: " + UnsyncedArticles);
            boolean UnsyncedActives = Active.hasUnsyncedActive(HomeActivity.this);
            System.out.println("UnsyncedActives: " + UnsyncedActives);

            if (UnsyncedArticles) {
                Toast.makeText(HomeActivity.this, "Tienes articulos sin sincronizar", Toast.LENGTH_SHORT).show();
            } else if (UnsyncedActives) {
                Toast.makeText(HomeActivity.this, "Tienes activos sin sincronizar", Toast.LENGTH_SHORT).show();
            } else {
                 SharedPreferences sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                 SharedPreferences.Editor editor = sharedPreferences.edit();
                 editor.clear();
                 editor.apply();

                 Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                 startActivity(intent);
                 finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
