package com.example.sca_app_v1.login_app;

 import android.content.Context;
 import android.content.SharedPreferences;
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
        String url = "http://192.168.100.8:9000/login/app/android"; // Reemplaza con la URL de tu servidor
         RequestQueue queue = Volley.newRequestQueue(context);
         
        // Crear el objeto JSON para enviar en el cuerpo de la solicitud
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("email", email);
            jsonBody.put("password", password);
            //jsonBody.put("rutCompany", rutCompany);
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
                                JSONObject result = response.getJSONObject("result");
                                System.out.println("result " + result );
                                String info_user = result.getJSONObject("user").toString();

                                JSONObject user = result.getJSONObject("user");
                                System.out.println("token > " + result.get("access_token"));

                                System.out.println("email > " + user.get("email"));
                                System.out.println("fistName > " + user.get("firstName"));
                                System.out.println("lastName > " + user.get("lastName"));
                                System.out.println("secondName > " + user.get("secondName"));
                                System.out.println("secondLastName > " + user.get("secondLastName"));
                                System.out.println("rut > " + user.get("rut"));
                                System.out.println("companyID > " + user.get("company_id"));
                                System.out.println("profileID > " + user.get("profile_id"));

                                SharedPreferences sharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("accessToken", (String) result.get("access_token"));
                                editor.putString("user", info_user);
                                editor.putInt("profile_id", (Integer) user.get("profile_id"));
                                //editor.putInt("company_id", (Integer) result.get("company_id"));

                                /*if((Integer) user.get("profile_id") == 2){
                                    editor.putInt("company_id", (Integer) user.get("company_id"));
                                }*/

                                editor.apply();
                                callback.onSuccess(response.toString()); 
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
                callback.onError("Error al iniciar sesión ");
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
