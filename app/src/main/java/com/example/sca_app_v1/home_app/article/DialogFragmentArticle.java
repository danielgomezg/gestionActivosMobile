package com.example.sca_app_v1.home_app.article;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.Manifest;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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

    private ArticleFragment parentFragment;

    private int mode;

    private int position;

    private Article article;
    private EditText editTextName;
    private EditText editTextDescription;
    private EditText editTextCode;
    private AutoCompleteTextView categorySelect;
    private ArrayAdapter<String> adapterItems;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private ImageView photoArticle;



    // Método estático para crear una instancia del DialogFragment en modo edicion
    public static DialogFragmentArticle newInstance(int mode, int position, Article article, ArticleFragment parentFragment) {
        DialogFragmentArticle fragment = new DialogFragmentArticle();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        args.putInt(ARG_POSITION, position);
        args.putSerializable(ARG_ARTICLE, article);
        fragment.setArguments(args);
        // Guardar una referencia al fragmento padre (ArticleFragment)
        fragment.setParentFragment(parentFragment);

        return fragment;
    }

    // Método estático para crear una instancia del DialogFragment en modo creacion
    public static DialogFragmentArticle newInstance(int mode, ArticleFragment parentFragment) {
        DialogFragmentArticle fragment = new DialogFragmentArticle();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        fragment.setArguments(args);
        // Guardar una referencia al fragmento padre (ArticleFragment)
        fragment.setParentFragment(parentFragment);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt(ARG_MODE);
            if (mode == MODE_EDIT){
                position = getArguments().getInt(ARG_POSITION);
                article = (Article) getArguments().getSerializable(ARG_ARTICLE);
            }

        }
        // Inicializar los ActivityResultLauncher para la galería y la cámara
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri selectedImageUri = result.getData().getData();
                            photoArticle.setImageURI(selectedImageUri);
                        }
                    }
                });

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Bundle extras = result.getData().getExtras();
                            Bitmap photo = (Bitmap) extras.get("data");
                            photoArticle.setImageBitmap(photo);
                        }
                    }
                });
    }

    // Método para establecer el fragmento padre (ArticleFragment)
    private void setParentFragment(ArticleFragment parentFragment) {
        this.parentFragment = parentFragment;
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

        if(mode == MODE_EDIT){
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
        }

        photoArticle = view.findViewById(R.id.imageView);
        Button buttonAddPhoto = view.findViewById(R.id.buttonAddPhoto);

        buttonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddPhotoClick();
            }
        });


        categorySelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(parent);
                System.out.println("position " + position);
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(requireContext(), "Item: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        //obtener la company
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        Integer company_id = sharedPreferences.getInt("company_id", 0);

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

                        // Creacion del artículo
                        String newName = editTextName.getText().toString();
                        String newDescription = editTextDescription.getText().toString();
                        String newCode = editTextCode.getText().toString();
                        String selectedCategory = categorySelect.getText().toString();
                        int idCategory = category.getCategoryID(selectedCategory, categoryList);

                        // Crear un objeto Article con los nuevos valores
                        Article newArticle = new Article();
                        newArticle.setName(newName);
                        newArticle.setDescription(newDescription);
                        newArticle.setCode(newCode);
                        newArticle.setCategory_id(idCategory);
                        newArticle.setPhoto("");

                        if (mode == MODE_EDIT) {
                            System.out.println("newName " + newName);
                            System.out.println("newDescription " + newDescription);
                            System.out.println("newCode " + newCode);
                            System.out.println("selectedCategory " + selectedCategory);
                            System.out.println("selectedCategory " + idCategory);
                            System.out.println("position " + position);

                            newArticle.setId(article.getId());
                            newArticle.setSync(article.getSync());

                            // Actualizar el artículo en la base de datos
                            boolean updateSuccessful = newArticle.updateArticle(getContext());
                            System.out.println(updateSuccessful);

                            if (updateSuccessful && parentFragment != null) {
                                // La actualización fue exitosa
                                Toast.makeText(getContext(), "Artículo actualizado correctamente", Toast.LENGTH_SHORT).show();

                                // Actualizar la lista de artículos en el fragmento padre (ArticleFragment)
                                parentFragment.updateArticles(getContext(), position);
                            } else {
                                // Ocurrió un error durante la actualización
                                Toast.makeText(getContext(), "Error al actualizar el artículo", Toast.LENGTH_SHORT).show();
                            }

                            // Lógica para guardar los cambios
                        } else{

                            newArticle.setPhoto("");
                            newArticle.setCompany_id(company_id);

                            boolean createSuccessful = newArticle.createArticle(getContext());
                            System.out.println(createSuccessful);

                            if (createSuccessful && parentFragment != null) {
                                // La creacion fue exitosa
                                Toast.makeText(getContext(), "Artículo creado correctamente", Toast.LENGTH_SHORT).show();

                                // Actualizar la lista de artículos en el fragmento padre (ArticleFragment)
                                parentFragment.showArticles(getContext());
                            } else {
                                // Ocurrió un error durante la actualización
                                Toast.makeText(getContext(), "Error al crear el artículo", Toast.LENGTH_SHORT).show();
                            }
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

    // Método llamado cuando el usuario hace clic en el botón "Añadir Foto"
    public void onAddPhotoClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Seleccionar Fuente de Imagen")
                .setItems(new CharSequence[]{"Galería", "Cámara"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Abrir la galería
                                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                galleryLauncher.launch(galleryIntent);
                                break;
                            case 1:
                                // Verificar los permisos de la cámara
                                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                    // No hay permisos de cámara, solicitarlos
                                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                                } else {
                                    // Ya se tienen permisos de cámara, abrir la cámara
                                    openCamera();
                                }
                                break;
                        }
                    }
                })
                .show();
    }

    // Método para abrir la cámara
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(cameraIntent);
    }

}
