package com.example.sca_app_v1.home_app.article;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.sca_app_v1.R;
import com.example.sca_app_v1.models.Category;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class DialogFragmentArticle extends DialogFragment {
    private static final String ARG_MODE = "mode";
    public static final int MODE_EDIT = 1;
    public static final int MODE_CREATE = 2;

    // private String[] items = { "Cargador", "Pila", "Bateria", "Cosito" };

    private int mode;
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText etCode;
    private AutoCompleteTextView categorySelect;
    private ArrayAdapter<String> adapterItems;

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
        TextInputLayout textInputLayoutName = view.findViewById(R.id.editTextName);
        editTextName = textInputLayoutName.getEditText();

        TextInputLayout textInputLayoutDescription = view.findViewById(R.id.editTextDescription);
        editTextDescription = textInputLayoutDescription.getEditText();
//        editTextDescription = view.findViewById(R.id.editTextDescription);

        TextInputLayout textInputLayoutCode = view.findViewById(R.id.editTextCode);
        etCode = textInputLayoutCode.getEditText();

        categorySelect = view.findViewById(R.id.category_select);
        Category category = new Category();
        List<Category> categoryList = category.getCategories(requireContext());
        System.out.println("CATEGORY LIST " + categoryList);
        List<String> items = categoryList.stream()
        .map(Category::getDescription)
        .collect(Collectors.toList());
        adapterItems = new ArrayAdapter<String>(requireContext(), R.layout.list_item, items);
        categorySelect.setAdapter(adapterItems);
        categorySelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(parent);
                System.out.println("position " + position);
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(requireContext(), "Item: "+item, Toast.LENGTH_SHORT).show();
            }
        });

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
