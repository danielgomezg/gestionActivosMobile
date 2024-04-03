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
import com.example.sca_app_v1.models.Article;
import com.example.sca_app_v1.models.Category;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class DialogFragmentArticle extends DialogFragment {
    private static final String ARG_MODE = "mode";
    public static final int MODE_EDIT = 1;
    public static final int MODE_CREATE = 2;

    public static final String ARG_POSITION = "";

    private static final String ARG_ARTICLE = "article";


    // private String[] items = { "Cargador", "Pila", "Bateria", "Cosito" };

    private int mode;

    private int position;

    private Article article;
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextCode;
    private AutoCompleteTextView categorySelect;
    private ArrayAdapter<String> adapterItems;

    // Método estático para crear una instancia del DialogFragment con un modo específico
    public static DialogFragmentArticle newInstance(int mode, int position, Article article) {
        DialogFragmentArticle fragment = new DialogFragmentArticle();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        args.putInt(ARG_POSITION, position);
        args.putSerializable(ARG_ARTICLE, article);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt(ARG_MODE);
            position = getArguments().getInt(ARG_POSITION);
            article = (Article) getArguments().getSerializable(ARG_ARTICLE);
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
        editTextCode = textInputLayoutCode.getEditText();

        categorySelect = view.findViewById(R.id.category_select);
        Category category = new Category();
        List<Category> categoryList = category.getCategories(requireContext());
        System.out.println("CATEGORY LIST " + categoryList);
        List<String> items = categoryList.stream()
        .map(Category::getDescription)
        .collect(Collectors.toList());
        adapterItems = new ArrayAdapter<String>(requireContext(), R.layout.list_item, items);
        categorySelect.setAdapter(adapterItems);

        String selectedCategoryDescription = (article != null && article.getCategory_id() != null) ? category.getCategoryDescription(article.getCategory_id(), categoryList) : null;

        // Desactivar temporalmente el filtrado automático
        categorySelect.setThreshold(Integer.MAX_VALUE);

        // Si se encontró la descripción de la categoría, establecerla como texto en el AutoCompleteTextView
        if (selectedCategoryDescription != null) {
            categorySelect.setText(selectedCategoryDescription);
            System.out.println("selectedCategoryDescription" +  selectedCategoryDescription);
        }

        // Volver a activar el filtrado automático
        categorySelect.setThreshold(1);

        categorySelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(parent);
                System.out.println("position " + position);
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(requireContext(), "Item: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        // Verificar si el artículo está presente y establecer los valores de los campos
        if (article != null) {
            editTextName.setText(article.getName());
            editTextCode.setText(article.getCode());
            editTextDescription.setText(article.getDescription());
            System.out.println("category " + article.getCategory_id());
        }

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
                            String newCode = editTextCode.getText().toString();
                            String selectedCategory = categorySelect.getText().toString();
                            int idCategory = category.getCategoryID(selectedCategory, categoryList);
                            System.out.println("newName " + newName);
                            System.out.println("newDescription " + newDescription);
                            System.out.println("newCode " + newCode);
                            System.out.println("selectedCategory " + selectedCategory);
                            System.out.println("selectedCategory " + idCategory);
                            System.out.println("position " + position);

                            // Crear un objeto Article con los nuevos valores
                            Article updatedArticle = new Article();
                            updatedArticle.setId(article.getId());
                            updatedArticle.setName(newName);
                            updatedArticle.setDescription(newDescription);
                            updatedArticle.setCode(newCode);
                            updatedArticle.setCategory_id(idCategory);

                            // Actualizar el artículo en la base de datos
                            boolean updateSuccessful = updatedArticle.updateArticle(getContext());
                            System.out.println(updateSuccessful);

                            if (updateSuccessful) {
                                // La actualización fue exitosa
                                Toast.makeText(getContext(), "Artículo actualizado correctamente", Toast.LENGTH_SHORT).show();
                                // Actualizar la lista de artículos en el fragmento
                                if (getParentFragment() instanceof ArticleFragment) {
                                    System.out.println("ultimo if");
                                    ((ArticleFragment) getParentFragment()).updateArticleList(position);
                                }

                                //dismiss();
                            } else {
                                // Ocurrió un error durante la actualización
                                Toast.makeText(getContext(), "Error al actualizar el artículo", Toast.LENGTH_SHORT).show();
                            }

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
