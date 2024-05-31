package com.example.sca_app_v1.home_app.sync;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sca_app_v1.R;
import com.example.sca_app_v1.home_app.HomeActivity;
import com.example.sca_app_v1.home_app.bdLocal.GetLocalBD;
import com.example.sca_app_v1.home_app.bdLocal.LoadData;
import com.example.sca_app_v1.models.Active;
import com.example.sca_app_v1.models.Article;

import org.w3c.dom.Text;

public class SyncFragment extends Fragment {

    private Button btnSyncUpload;
    private Button btnSyncDownload;
    private String token;
    private Integer companyId;
    private LinearLayout loadingSync;
    private TextView errorUpload;
    private TextView errorDownload;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("session", MODE_PRIVATE);
        token = sharedPreferences.getString("accessToken", null);
        companyId = sharedPreferences.getInt("company_id", 0);
        loadingSync = view.findViewById(R.id.loadingSync);
        errorDownload = view.findViewById(R.id.downloadErrorMsg);
        errorUpload = view.findViewById(R.id.uploadErrorMsg);

        setUploadAction(view);
        setDownloadAction(view);

        return view;
    }

    public void setUploadAction(View view) {
        btnSyncUpload = view.findViewById(R.id.btnSyncUpload);

        btnSyncUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("UPLOAD!!!");
                loadingSync.setVisibility(View.VISIBLE);
                boolean UnsyncedArticles = Article.hasUnsyncedArticles(getContext());
                //boolean UnsyncedActives = Active.hasUnsyncedActive(getContext());
                //System.out.println("UnsyncedActives --> " + UnsyncedActives);
                System.out.println("UnsyncedArticles --> " + UnsyncedArticles);

                if (UnsyncedArticles) {

                    Article.syncUploadArticles(getContext(), token, companyId, new Article.SyncCallback() {
                        @Override
                        public void onSuccess() {
                            syncActives(UnsyncedArticles);
                        }

                        @Override
                        public void onError(Exception e) {
                            System.err.println("Error sincronizando artículos: " + e.getMessage());
                            Toast.makeText(getContext(), "Error al sincronizar artículos", Toast.LENGTH_SHORT).show();
                            loadingSync.setVisibility(View.INVISIBLE);
                        }
                    });

                } else {
                    syncActives(UnsyncedArticles);
                }

            }

            private void syncActives(boolean syncArticles) {
                boolean unsyncedActives = Active.hasUnsyncedActive(getContext());
                System.out.println("UnsyncedActives --> " + unsyncedActives);

                // Si UnsyncedActives es verdadero, ejecuta la sincronización de activos
                if (unsyncedActives) {
                    Active.syncUploadActives(getContext(), token, companyId, new Active.syncCallback(){
                        @Override
                        public void onSuccess() {
                            // Toast.makeText(getContext(),"Se han sincronizado los datos exitosamente.", Toast.LENGTH_SHORT).show();
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "Se han sincronizado los datos exitosamente.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            loadingSync.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {
                            System.err.println("Error sincronizando artículos: " + e.getMessage());
                            // Toast.makeText(getContext(),"ha ocurrido un error sincronizando los datos.", Toast.LENGTH_SHORT).show();
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "Ha ocurrido un error sincronizando los datos.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            loadingSync.setVisibility(View.INVISIBLE);
                        }
                    });
                }else {
                    if (syncArticles){
                        Toast.makeText(getContext(),"Se han sincronizado los datos exitosamente.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(),"Los datos están sincronizados.", Toast.LENGTH_SHORT).show();
                    }
                    loadingSync.setVisibility(View.INVISIBLE);
                }
            }
        });

    }

    public void setDownloadAction(View view) {
        btnSyncDownload = view.findViewById(R.id.btnSyncDownload);
        btnSyncDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean UnsyncedArticles = Article.hasUnsyncedArticles(getContext());
                boolean UnsyncedActives = Active.hasUnsyncedActive(getContext());
                System.out.println("UnsyncedActives --> " + UnsyncedActives);
                System.out.println("UnsyncedArticles --> " + UnsyncedArticles);

                if (UnsyncedArticles && UnsyncedActives) {
                    Toast.makeText(getContext(), "Tienes articulos y activos sin subir", Toast.LENGTH_SHORT).show();
                }else if (UnsyncedArticles) {
                    Toast.makeText(getContext(), "Tienes articulos sin subir", Toast.LENGTH_SHORT).show();
                } else if (UnsyncedActives) {
                    Toast.makeText(getContext(), "Tienes activos sin subir", Toast.LENGTH_SHORT).show();
                }else if (!UnsyncedArticles && !UnsyncedActives) {
                    loadingSync.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), "Inicio descarga", Toast.LENGTH_SHORT).show();
                    GetLocalBD.syncProductionDB(getContext(), token, companyId, new GetLocalBD.GetLocalBDCallback(){
                        @Override
                        public void onSuccess(String response) {
                            System.out.println("SUCCESS SYNC");
                            //Toast.makeText(getContext(), "Descarga completada", Toast.LENGTH_SHORT).show();
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "Descarga completada", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            loadingSync.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError(String error) {
                            System.out.println("ERROR SYNC");
                            //Toast.makeText(getContext(), "Error al descargar", Toast.LENGTH_SHORT).show();
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "Error al descargar", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            loadingSync.setVisibility(View.INVISIBLE);
                            errorDownload.setText("Descarga incompleta.");
                            errorDownload.setVisibility(View.VISIBLE);
                        }
                    });
                }

            }
        });


    }
}
