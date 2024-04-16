package com.example.sca_app_v1.home_app.sync;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sca_app_v1.R;
import com.example.sca_app_v1.home_app.HomeActivity;
import com.example.sca_app_v1.models.Active;
import com.example.sca_app_v1.models.Article;

public class SyncFragment extends Fragment {

    private Button btnSyncUpload;
    private Button btnSyncDownload;
    private String token;
    private Integer companyId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("session", MODE_PRIVATE);
        token = sharedPreferences.getString("accessToken", null);
        companyId = sharedPreferences.getInt("company_id", 0);

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

                boolean UnsyncedArticles = Article.hasUnsyncedArticles(getContext());
                boolean UnsyncedActives = Active.hasUnsyncedActive(getContext());

                if (UnsyncedArticles) {

                    Article.syncUploadActives(getContext(), token, companyId);

                } else if (UnsyncedActives) {

                }

            }
        });

    }

    public void setDownloadAction(View view) {
        btnSyncDownload = view.findViewById(R.id.btnSyncDownload);

        btnSyncDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Llamar a funcion download, la creada en load data.");
            }
        });


    }
}
