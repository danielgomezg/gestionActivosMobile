package com.example.sca_app_v1.home_app.sync;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sync, container, false);

        setUploadAction(view);



        return view;
    }

    public void setUploadAction(View view) {
        btnSyncUpload = view.findViewById(R.id.btnSyncUpload);

        btnSyncUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("UPLOAD!!!");

                boolean UnsyncedArticles = Article.hasUnsyncedArticles(getContext());
                System.out.println("UnsyncedArticles: " + UnsyncedArticles);
                boolean UnsyncedActives = Active.hasUnsyncedActive(getContext());
                System.out.println("UnsyncedActives: " + UnsyncedActives);

                if (UnsyncedArticles) {

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
