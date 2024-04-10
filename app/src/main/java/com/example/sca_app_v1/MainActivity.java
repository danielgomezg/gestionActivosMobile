package com.example.sca_app_v1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.example.sca_app_v1.home.HomeActivity;
import com.example.sca_app_v1.home_app.HomeActivity;
import com.example.sca_app_v1.home_app.bdLocal.LoadData;
import com.example.sca_app_v1.login_app.*;
import com.example.sca_app_v1.home_app.*;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonLogin;
    private EditText editTextEmail, editTextPassword, editTextRutCompany;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        SharedPreferences sharedPreferences = this.getSharedPreferences("session", MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accessToken", null);
        System.out.println("ON CREATE SP -->> " + accessToken);
        if (accessToken != null) {
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
        }
        //getSupportActionBar().hide();

        // Vinculamos los elementos del diseño con las variables Java
        editTextEmail = findViewById(R.id.correo_user);
        editTextPassword = findViewById(R.id.pass_user);
        editTextRutCompany = findViewById(R.id.rut_company_select);
        buttonLogin = findViewById(R.id.buttonLogin);

        editTextRutCompany.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String rut = s.toString();
                String formattedRut = formatRut(rut);
                editTextRutCompany.removeTextChangedListener(this); // Evitar el bucle infinito
                editTextRutCompany.setText(formattedRut);
                editTextRutCompany.setSelection(formattedRut.length()); // Colocar el cursor al final del texto
                editTextRutCompany.addTextChangedListener(this);
            }
        });

        // Agregamos un OnClickListener al botón de inicio de sesión
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenemos el correo electrónico y la contraseña ingresados por el usuario
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                String rutcompany = editTextRutCompany.getText().toString().trim();


                // Validamos que ambos campos no estén vacíos
                if (email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Falta ingresar el correo", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Falta ingresar la contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (rutcompany.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Falta ingresar el rut de la compañia", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!EmailValidator.validate(email)){
                    Toast.makeText(MainActivity.this, "Correo inválido", Toast.LENGTH_SHORT).show();
                    return;
                }

                //iniciar sesión
                login(v, email, password, rutcompany);


                // Simulamos un inicio de sesión exitoso para este ejemplo
//                Toast.makeText(MainActivity.this, "Inicio de sesión exitoso " + email, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void login(View v, String email, String password, String rutCompany){

        Login.signIn(MainActivity.this, email, password, rutCompany, new Login.LoginCallback(){
            @Override
            public void onSuccess(String response) {
                // Manejar el inicio de sesión exitoso
                Toast.makeText(MainActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
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

    private String formatRut(String rut) {
        rut = rut.replaceAll("[^0-9kK]", ""); // Eliminar cualquier carácter que no sea un dígito o la letra 'k' para validar RUT
        int rutLength = rut.length();

        // Si el RUT es menor o igual a 1, no hay formato que aplicar
        if (rutLength <= 1) {
            return rut;
        }

        String dv = rut.substring(rutLength - 1); // Digito verificador
        String digits = rut.substring(0, rutLength - 1); // Dígitos del RUT sin el dígito verificador

        StringBuilder formattedRut = new StringBuilder();
        int count = 0;

        // Agregar los puntos separadores
        for (int i = digits.length() - 1; i >= 0; i--) {
            char c = digits.charAt(i);
            formattedRut.insert(0, c);
            count++;
            if (count % 3 == 0 && i != 0) {
                formattedRut.insert(0, ".");
            }
        }

        // Agregar el guion y el dígito verificador al final
        formattedRut.append("-");
        formattedRut.append(dv);

        return formattedRut.toString();
    }
}

