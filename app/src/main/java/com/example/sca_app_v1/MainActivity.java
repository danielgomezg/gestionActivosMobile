package com.example.sca_app_v1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.sca_app_v1.home.HomeActivity;
import com.example.sca_app_v1.home_app.HomeActivity;
import com.example.sca_app_v1.home_app.bdLocal.LoadData;
import com.example.sca_app_v1.login_app.*;
import com.example.sca_app_v1.home_app.*;
import com.example.sca_app_v1.models.Article;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private Button buttonLogin;
    private EditText editTextEmail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        SharedPreferences sharedPreferences = this.getSharedPreferences("session", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken", null);
        int companyId = sharedPreferences.getInt("company_id", 0);
        System.out.println("ON CREATE AT -->> " + accessToken);
        System.out.println("ON CREATE CID -->> " + companyId);
        if (accessToken != null && companyId != 0) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }

        // Vinculamos los elementos del diseño con las variables Java
        TextInputLayout textInputLayoutEmail = findViewById(R.id.correo_user);
        editTextEmail = textInputLayoutEmail.getEditText();
        TextInputLayout textInputLayoutPassword = findViewById(R.id.pass_user);
        editTextPassword = textInputLayoutPassword.getEditText();
        buttonLogin = findViewById(R.id.buttonLogin);

        nextField();

        // Agregamos un OnClickListener al botón de inicio de sesión
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenemos el correo electrónico y la contraseña ingresados por el usuario
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Validamos que ambos campos no estén vacíos
                if (email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Falta ingresar el correo", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Falta ingresar la contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!EmailValidator.validate(email)){
                    Toast.makeText(MainActivity.this, "Correo inválido", Toast.LENGTH_SHORT).show();
                    return;
                }
                //iniciar sesión
                login(v, email, password);
            }
        });
    }

    public void login(View v, String email, String password){

        Login.signIn(MainActivity.this, email, password, new Login.LoginCallback(){
            @Override
            public void onSuccess(String response) {
                // Manejar el inicio de sesión exitoso
                Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoadData.class);
                startActivity(intent);

            }

            @Override
            public void onError(String error) {
                // Manejar el error durante el inicio de sesión
                System.out.println(error);
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void nextField(){
        editTextEmail.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, android.view.KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    editTextPassword.requestFocus();
                    return true;
                }
                return false;
            }
        });

        editTextPassword.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, android.view.KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    buttonLogin.performClick();
                    return true;
                }
                return false;
            }
        });
    }
}

