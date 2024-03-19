package com.example.sca_app_v1.login_app;

 import android.content.Context;
 import android.content.SharedPreferences;
 import android.util.Log;

 import com.android.volley.Request;
 import com.android.volley.RequestQueue;
 import com.android.volley.Response;
 import com.android.volley.VolleyError;
 import com.android.volley.toolbox.JsonObjectRequest;
 import com.android.volley.toolbox.StringRequest;
 import com.android.volley.toolbox.Volley;

 import org.json.JSONException;
 import org.json.JSONObject;

 import java.util.HashMap;
 import java.util.Map;

 public class Login {

     public static void signIn(Context context, String email, String password, final LoginCallback callback) {
        String url = "http://10.0.2.2:9000/login"; // Reemplaza con la URL de tu servidor
        RequestQueue queue = Volley.newRequestQueue(context);
         
        // Crear el objeto JSON para enviar en el cuerpo de la solicitud
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Se llamó a la devolución de llamada en caso de éxito
                        String code = null;
                        try {
                            code = response.getString("code");
                            System.out.println(response);
                        
                            if (code.equals("201")) {
                                JSONObject info_session = response.getJSONObject("result");
                                //String info_session = response.getString("result");
                                System.out.println(info_session);
                                String tkn = info_session.getString("access_token");
                                System.out.println(tkn);
                                String info_user = info_session.getJSONObject("user").toString();
                                System.out.println(info_user);
                                
                                SharedPreferences.Editor editor = context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE).edit();
                                editor.putString("token", tkn);
                                editor.putString("user", info_user);
                                editor.apply();                                                                                                callback.onSuccess(response.toString());
                            }
                            else {
                                callback.onError("Error durante el inicio de sesión: ");
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                        
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Se llamó a la devolución de llamada en caso de error
                callback.onError("Error durante el inicio de sesión: " + error.getMessage());
            }
        });

        // Agregar la solicitud a la cola
        queue.add(jsonRequest);
     }

     // Interfaz para la devolución de llamada de inicio de sesión
     public interface LoginCallback {
         void onSuccess(String response);
         void onError(String error);
     }
 }


//
//import java.io.BufferedReader;
//import java.io.DataOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//import javax.security.auth.login.LoginException;
//public class Login {
//
//    public static String signIn(String email, String password) throws LoginException {
//
//        try{
//            // Parámetros de la solicitud
//            String params = "email=" + email + "&password=" + password;
//            byte[] postData = params.getBytes(StandardCharsets.UTF_8);
//
//            // URL del servidor
//            URI uri = new URI("http://127.0.0.1:9000/login");
//            URL url = uri.toURL();
//
//            // conexión HTTP
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoOutput(true);
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//            connection.setRequestProperty("charset", "utf-8");
//            connection.setRequestProperty("Content-Length", Integer.toString(params.getBytes().length));
//
//            try (OutputStream out = connection.getOutputStream()) {
//                out.write(postData);
//                out.flush();
//            }
//
//            if (connection.getResponseCode() == 200) {
//                System.out.println("Respuesta del servidor: " + connection.getResponseMessage());
//            } else {
//                // throw new LoginException("Error durante el inicio de sesión: " + connection.getResponseMessage());
//                System.out.println("Error durante el inicio de sesión: " + connection.getResponseMessage());
//            }
//
//
//            // // Habilitar el envío de datos en la solicitud
//            // OutputStream out = new DataOutputStream(connection.getOutputStream());
//
//            // // Escribir los parámetros en el cuerpo de la solicitud
//            // out.close();
//
//
//            // Leer la respuesta del servidor
//            // BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            // StringBuilder response = new StringBuilder();
//            // String inputLine;
//            // while ((inputLine = in.readLine()) != null){
//            //     response.append(inputLine);
//            // }
//            // in.close();
//
//            // return response.toString();
//        }catch (IOException e){
//            throw new LoginException("Error durante el inicio de sesión: " + e.getMessage());
//        }
//    }
//}
