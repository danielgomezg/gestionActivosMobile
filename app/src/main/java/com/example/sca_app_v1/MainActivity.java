package com.example.sca_app_v1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.example.sca_app_v1.home.HomeActivity;
import com.example.sca_app_v1.home_app.HomeActivity;
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
        //getSupportActionBar().hide();

        // Vinculamos los elementos del diseño con las variables Java
        editTextEmail = findViewById(R.id.correo_user);
        editTextPassword = findViewById(R.id.pass_user);
        editTextRutCompany = findViewById(R.id.rut_company_select);
        buttonLogin = findViewById(R.id.buttonLogin);

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
//                setContentView(R.layout.home);
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);

            }

            @Override
            public void onError(String error) {
                // Manejar el error durante el inicio de sesión
                System.out.println(error);
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });


//        try {
//            String response = Login.signIn(email, password);
//            System.out.println("Inicio de sesión exitoso: " + response);
//        }catch (LoginException e){
//            System.err.println("Error durante el inicio de sesión: " + e.getMessage());
//            Toast.makeText(MainActivity.this, "Ocurrio un error al iniciar sesión", Toast.LENGTH_SHORT).show();
//        }

    }
}

