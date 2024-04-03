package com.example.sca_app_v1.home_app.article;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;

import com.example.sca_app_v1.R;


public class DialogFragmentArticle extends DialogFragment {
    private static final String ARG_MODE = "mode";
    public static final int MODE_EDIT = 1;
    public static final int MODE_CREATE = 2;

    private int mode;
    private EditText editTextName;
    private EditText editTextDescription;

    // Método estático para crear una instancia del DialogFragment con un modo específico
    public static DialogFragmentArticle newInstance(int mode) {
        DialogFragmentArticle fragment = new DialogFragmentArticle();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt(ARG_MODE);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_article, null);
        editTextName = view.findViewById(R.id.editTextName);
        editTextDescription = view.findViewById(R.id.editTextDescription);

        builder.setView(view)
                .setTitle(mode == MODE_EDIT ? "Editar artículo" : "Crear nuevo artículo")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción al hacer clic en el botón de guardar
                        if (mode == MODE_EDIT) {
                            // Procesar la edición del artículo
                            String newName = editTextName.getText().toString();
                            String newDescription = editTextDescription.getText().toString();
                            // Lógica para guardar los cambios
                        } else if (mode == MODE_CREATE) {
                            // Procesar la creación de un nuevo artículo
                            String newName = editTextName.getText().toString();
                            String newDescription = editTextDescription.getText().toString();
                            // Lógica para crear el nuevo artículo
                        }
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción al hacer clic en el botón de cancelar
                        dismiss();
                    }
                });

        return builder.create();
    }
}
