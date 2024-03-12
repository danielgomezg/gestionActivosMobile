package com.example.sca_app_v1.login_app;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Login {

    public static void signIn(Context context, String email, String password, final LoginCallback callback) {
        String url = "http://127.0.0.1:9000/login"; // Reemplaza con la URL de tu servidor
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Se llamó a la devolución de llamada en caso de éxito
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Se llamó a la devolución de llamada en caso de error
                callback.onError("Error durante el inicio de sesión: " + error.getMessage());
            }
        }) {
            // Se pueden agregar parámetros de la solicitud, si es necesario
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                params.put("password", password);
                return params;
            }
        };

        // Agregar la solicitud a la cola
        queue.add(stringRequest);
    }

    // Interfaz para la devolución de llamada de inicio de sesión
    public interface LoginCallback {
        void onSuccess(String response);
        void onError(String error);
    }
}

/*

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.security.auth.login.LoginException;
public class Login {

    public static String signIn(String email, String password) throws LoginException {

        try{
            // URL del servidor
            URL url = new URL("http://127.0.0.1:9000/login");

            // conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            // Parámetros de la solicitud
            String params = "email=" + email + "&password=" + password;

            // Habilitar el envío de datos en la solicitud
            connection.setDoOutput(true);

            // Escribir los parámetros en el cuerpo de la solicitud
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(params);
            out.flush();
            out.close();

            // Leer la respuesta del servidor
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null){
                response.append(inputLine);
            }
            in.close();

            return response.toString();
        }catch (IOException e){
            throw new LoginException("Error durante el inicio de sesión: " + e.getMessage());
        }
    }
}*/
