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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.sca_app_v1.R;
import com.example.sca_app_v1.models.Article;
import com.example.sca_app_v1.models.Category;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
    private ImageView photoArticle2;
    private ImageView photoArticle3;
    private ImageView photoArticle4;
    private Integer countAddImage = 0;
    private Button buttonAddPhoto;
    private String photoPath = "";
    //Si la foto proviene de la camara se guarda aca
    private Bitmap photoCam = null;
    private List<Bitmap> photosCam = new ArrayList<>(Arrays.asList(null, null, null, null));
    //Si la foto proviene de la galeria se guarda aca
    private Uri selectedImageUri = null;
    private List<Uri> photosGallery = new ArrayList<>(Arrays.asList(null, null, null, null));
    //Variable usada para saber si habian fotos een el articulo a editar
    private String photosArticleEdit = "";
    private Button btnDeleteImg1;
    private Button btnDeleteImg2;
    private Button btnDeleteImg3;
    private Button btnDeleteImg4;

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
        // Callback de la galería
        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            selectedImageUri = result.getData().getData();

                            photoCam = null;

                            if (photosGallery.get(0) == null && photosCam.get(0) == null) {
                                photoArticle.setImageURI(selectedImageUri);
                                photosGallery.set(0, selectedImageUri);
                                
                                countAddImage++;
                            }
                            else if (photosGallery.get(1) == null && photosCam.get(1) == null) {
                                photoArticle2.setImageURI(selectedImageUri);
                                photosGallery.set(1, selectedImageUri);
                                countAddImage++;
                            }
                            else if (photosGallery.get(2) == null && photosCam.get(2) == null) {
                                photoArticle3.setImageURI(selectedImageUri);
                                photosGallery.set(2, selectedImageUri);
                                countAddImage++;
                            }
                            else if (photosGallery.get(3) == null && photosCam.get(3) == null) {
                                photoArticle4.setImageURI(selectedImageUri);
                                photosGallery.set(3, selectedImageUri);
                                countAddImage++;
                            }
                            else {
                                System.out.println("No hay espacio para más fotos");
                                Toast.makeText(getContext(), "Solo se pueden agregar hasta 4 imagenes", Toast.LENGTH_SHORT).show();
                            }

                            if (countAddImage == 4) {
                                // Desabilitar buttonAddPhoto
                                buttonAddPhoto.setEnabled(false);
                            }

                            // 

                        }
                    }
                }
        );

        // Callback de la cámara
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Bundle extras = result.getData().getExtras();
                            photoCam = (Bitmap) extras.get("data");

                            selectedImageUri = null;

                            if (photosGallery.get(0) == null && photosCam.get(0) == null) {
                                photoArticle.setImageBitmap(photoCam);
                                photosCam.set(0, photoCam);
                                countAddImage++;
                            }
                            else if (photosGallery.get(1) == null && photosCam.get(1) == null) {
                                photoArticle2.setImageBitmap(photoCam);
                                photosCam.set(1, photoCam);
                                countAddImage++;
                            }
                            else if (photosGallery.get(2) == null && photosCam.get(2) == null) {
                                photoArticle3.setImageBitmap(photoCam);
                                photosCam.set(2, photoCam);
                                countAddImage++;
                            }
                            else if (photosGallery.get(3) == null && photosCam.get(3) == null) {
                                photoArticle4.setImageBitmap(photoCam);
                                photosCam.set(3, photoCam);
                                countAddImage++;
                            }
                            else {
                                System.out.println("No hay espacio para más fotos");
                                Toast.makeText(getContext(), "Solo se pueden agregar hasta 4 imagenes", Toast.LENGTH_SHORT).show();
                            }

                            if (countAddImage == 4) {
                                // Desabilitar buttonAddPhoto
                                buttonAddPhoto.setEnabled(false);
                            }

                        }
                    }
                }
        );
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

        TextInputLayout textInputLayoutCategory = view.findViewById(R.id.editTextCategoryS);
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

            photosArticleEdit = article.getPhoto();
            System.out.println("images articulos " + photosArticleEdit);
            if (photosArticleEdit.isEmpty()) {
                countAddImage = 0;
            } else {
                String[] photosUrl = photosArticleEdit.split(",");
                countAddImage = photosUrl.length;
            }
        }

        photoArticle = view.findViewById(R.id.imageView);
        photoArticle2 = view.findViewById(R.id.imageView2);
        photoArticle3 = view.findViewById(R.id.imageView3);
        photoArticle4 = view.findViewById(R.id.imageView4);

        buttonAddPhoto = view.findViewById(R.id.buttonAddPhoto);

        buttonAddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddPhotoClick();
            }
        });
        btnDeleteImg1 = view.findViewById(R.id.btnDeleteImg1);
        btnDeleteImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoArticle.setImageResource(R.drawable.photo);
                buttonAddPhoto.setEnabled(true);
                photosCam.set(0, null);
                photosGallery.set(0, null);
                countAddImage--;
            }
        });
        btnDeleteImg2 = view.findViewById(R.id.btnDeleteImg2);
        btnDeleteImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoArticle2.setImageResource(R.drawable.photo);
                buttonAddPhoto.setEnabled(true);
                photosCam.set(1, null);
                photosGallery.set(1, null);
                countAddImage--;
            }
        });
        btnDeleteImg3 = view.findViewById(R.id.btnDeleteImg3);
        btnDeleteImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoArticle3.setImageResource(R.drawable.photo);
                buttonAddPhoto.setEnabled(true);
                photosCam.set(2, null);
                photosGallery.set(2, null);
                countAddImage--;
            }
        });
        btnDeleteImg4 = view.findViewById(R.id.btnDeleteImg4);
        btnDeleteImg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoArticle4.setImageResource(R.drawable.photo);
                buttonAddPhoto.setEnabled(true);
                photosCam.set(3, null);
                photosGallery.set(3, null);
                countAddImage--;
            }
        });

        categorySelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(requireContext(), "Item: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        //obtener la company
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        Integer company_id = sharedPreferences.getInt("company_id", 0);

        System.out.println("PRE SETEO " + photoPath);

        // Verificar si el artículo está presente y establecer los valores de los campos
        if (article != null) {
            editTextName.setText(article.getName());
            editTextCode.setText(article.getCode());
            editTextDescription.setText(article.getDescription());
            photoPath = article.getPhoto();
            System.out.println("SETEO " + photoPath);
            System.out.println("category " + article.getCategory_id());

            // Verificar si la ruta de la foto no es nula
            if (photoPath == null || photoPath.isEmpty()) {
                photoArticle.setImageResource(R.drawable.photo);
                System.out.println("photoPath está vacío o es nulo. Mostrando imagen por defecto.");
            } else {
                try {
                    System.out.println("PHOTO PATH ARTICULO");
                    System.out.println(photoPath);
                    String[] photos = photoPath.split(",");
                    if (photos.length == 4) {
                        buttonAddPhoto.setEnabled(false);
                    }
                    for(int i = 0; i < photos.length; i++) {
                        Uri photoUri = Uri.parse(photos[i]);
                        System.out.println("photo uri " + photoUri);
                        if (i == 0) {
                            if (photoUri.toString().contains("mobile_local")) {
                                photoArticle.setImageURI(photoUri);
                            }
                            else {
                                photoArticle.setImageResource(R.drawable.sca_logo_2);
                            }
                        }
                        else if (i == 1) {
                            if (photoUri.toString().contains("mobile_local")) {
                                photoArticle2.setImageURI(photoUri);
                            }
                            else {
                                photoArticle2.setImageResource(R.drawable.sca_logo_2);
                            }
                        }
                        else if (i == 2) {
                            if (photoUri.toString().contains("mobile_local")) {
                                photoArticle3.setImageURI(photoUri);
                            }
                            else {
                                photoArticle3.setImageResource(R.drawable.sca_logo_2);
                            }
                        }
                        else if (i == 3) {
                            if (photoUri.toString().contains("mobile_local")) {
                                photoArticle4.setImageURI(photoUri);
                            }
                            else {
                                photoArticle4.setImageResource(R.drawable.sca_logo_2);
                            }
                        }
                    }
                    System.out.println("Mostrando foto en ImageView: " + photoPath);
                } catch (Exception e) {
                    photoArticle.setImageResource(R.drawable.photo);
                    System.out.println("Error al cargar la foto desde " + photoPath + ". Mostrando imagen por defecto.");
                }
            }
        }

        AlertDialog dialog = builder.setView(view)
            .setTitle(mode == MODE_EDIT ? "Editar artículo" : "Crear nuevo artículo")
            .setPositiveButton("Guardar", null)
            .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     // Acción al hacer clic en el botón de cancelar
                     dismiss();
                 }
            }).create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {
        
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
        
                    @Override
                    public void onClick(View view) {
                        // Variable para indicar si se debe continuar con la ejecución
                        boolean continueExecution = true;

                        // Creacion del artículo
                        String newName = editTextName.getText().toString();
                        String newDescription = editTextDescription.getText().toString();
                        String newCode = editTextCode.getText().toString();
                        String selectedCategory = categorySelect.getText().toString();
                        int idCategory = category.getCategoryID(selectedCategory, categoryList);
                        System.out.println("categoria seleccionada " + idCategory);
                        // Validacion de datos.
                        if (newName.isEmpty()) {
                            textInputLayoutName.setError("Nombre requerido");
                            continueExecution = false;
                        } else {
                            textInputLayoutName.setError(null);
                        }

                        if (newCode.isEmpty()) {
                            textInputLayoutCode.setError("Codigo requerido");
                            continueExecution = false;
                        } else {
                            textInputLayoutCode.setError(null);
                        }

                        if (newDescription.isEmpty()) {
                            textInputLayoutDescription.setError("Descripción requerido");
                            continueExecution = false;
                        } else {
                            textInputLayoutDescription.setError(null);
                        }

                        if (idCategory == 0) {
                            System.out.println("falta categoria");
                            categorySelect.setError("Seleccione categoria");
                            textInputLayoutCategory.setError("Categoria requerido");
                            continueExecution = false;
                        } else {
                            categorySelect.setError(null);
                            textInputLayoutCategory.setError(null);
                        }

                        if(continueExecution){
                            // Crear un objeto Article con los nuevos valores
                            Article newArticle = new Article();
                            newArticle.setName(newName);
                            newArticle.setDescription(newDescription);
                            newArticle.setCode(newCode);
                            newArticle.setCategory_id(idCategory);
                            List<String> photosUrl = new ArrayList<>();
                            boolean success = true;
                            Integer contNamePhoto = 0;
                            for(int i = 0; i < photosCam.size(); i++){
                                if (photosCam.get(i) == null) {
                                    continue;
                                }
                                String url = article.savePhoto(requireContext(), photosCam.get(i),String.valueOf(contNamePhoto) + newName);
                                if (url == null) {
                                    success = false;
                                }
                                else {
                                    photosUrl.add(url);
                                    contNamePhoto++;
                                }
                            }
                            for(int i = 0; i < photosGallery.size(); i++){
                                if (photosGallery.get(i) == null) {
                                    continue;
                                }
                                String url = article.savePhoto(requireContext(), photosGallery.get(i), String.valueOf(contNamePhoto) + newName);
                                if (url == null) {
                                    success = false;
                                }
                                else {
                                    photosUrl.add(url);
                                    contNamePhoto++;
                                }
                            }

                            if (!success) {
                                // Manejar el error de guardar la foto
                                photoPath = "";
                                Toast.makeText(requireContext(), "Error al guardar la foto seleecionada", Toast.LENGTH_SHORT).show();
                            }
                            photoCam = null;
                            selectedImageUri = null;
                            photoPath = String.join(",", photosUrl);
                            System.out.println("GUARDANDO PHOTO " + photoPath);

                            if (mode == MODE_EDIT) {
                                System.out.println("newName " + newName);
                                System.out.println("newDescription " + newDescription);
                                System.out.println("newCode " + newCode);
                                System.out.println("selectedCategory " + selectedCategory);
                                System.out.println("selectedCategory " + idCategory);
                                System.out.println("position " + position);

                                newArticle.setId(article.getId());
                                newArticle.setSync(article.getSync());
                                if (photosArticleEdit.isEmpty()){
                                    newArticle.setPhoto(photoPath);
                                }else {
                                    newArticle.setPhoto(article.getPhoto() + "," + photoPath);
                                }

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

                                // Dismiss once everything is OK.
                                dialog.dismiss();
                            } else{
                                System.out.println("CREATEEEEEEEEEE");

                                newArticle.setCompany_id(company_id);
                                newArticle.setPhoto(photoPath);
                                boolean createSuccessful = newArticle.createArticle(getContext());
                                System.out.println(createSuccessful);

                                if (createSuccessful && parentFragment != null) {
                                    dialog.dismiss();

                                    // Actualizar la lista de artículos en el fragmento padre (ArticleFragment)
                                    parentFragment.showAlert("Artículo creado correctamente");
                                    parentFragment.showArticles(getContext());

                                } else {
                                    // Ocurrió un error durante la actualización
                                    Toast.makeText(getContext(), "Error al crear el artículo", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            }
        });

        return dialog;
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
