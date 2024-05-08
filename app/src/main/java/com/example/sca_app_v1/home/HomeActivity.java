package com.example.sca_app_v1.home;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sca_app_v1.R;


public class HomeActivity extends AppCompatActivity {

    String[] items = { "Funa", "Savory", "Trendy", "Bresler" };

    AutoCompleteTextView companySelect;
    ArrayAdapter<String> adapterItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        System.out.println("In on create home activity");

        getCompanyList();

        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
        companySelect.setAdapter(adapterItems);

        companySelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Item: "+item, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getCompanyList() {
        System.out.println("get company List");
//        SharedPreferences sharedPreferences = getSharedPreferences("session", MODE_PRIVATE);
//        String token = sharedPreferences.getString("accessToken", null);
//        String url = "http://192.168.100.8:9000/companiesIdName";

//        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//
//            new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    System.out.println(response.toString());
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    System.out.println("Error: "+error);
//                }
//            }
//
//
//        ) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> headers = new HashMap<>();
//                headers.put("Content-Type", "application/json");
//                headers.put("Authorization", "Bearer " + token); // Reemplaza 'token' con tu token de autenticación
//                return headers;
//            }
//        };

    }
}
