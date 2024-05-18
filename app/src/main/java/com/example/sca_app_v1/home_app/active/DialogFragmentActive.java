package com.example.sca_app_v1.home_app.active;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.sca_app_v1.R;
import com.example.sca_app_v1.home_app.article.ArticleFragment;
import com.example.sca_app_v1.home_app.article.DialogFragmentArticle;
import com.example.sca_app_v1.models.Active;
import com.example.sca_app_v1.models.Article;
import com.example.sca_app_v1.models.Office;
import com.example.sca_app_v1.models.Store;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class DialogFragmentActive extends DialogFragment {
    private static final String ARG_MODE = "mode";
    public static final int MODE_EDIT = 1;
    public static final int MODE_CREATE = 2;

    public static final String ARG_POSITION = "";

    private static final String ARG_ACTIVE = "active";
    private ActiveFragment parentFragment;
    private int mode;
    private int position;
    private ArrayAdapter<String> adapterArticles;
    private ArrayAdapter<String> adapterStores;
    private ArrayAdapter<String> adapterOffices;
    private Active active;
    private EditText editTextBarcode;
    private EditText editTextModel;
    private EditText editTextSerie;
    private EditText editTextcomment;
    private EditText editTextNamecharge;
    private EditText editTextRutcharge;
    private EditText editTextRecordnumber;
    private EditText adquisitionDateEditText;
    private EditText editTextbrand;
    private AutoCompleteTextView autoCompleteTextViewArticles;
    private TextInputLayout textInputLayoutArticle;
    private AutoCompleteTextView autoCompleteTextViewStores;
    private TextInputLayout textInputLayoutStores;
    private AutoCompleteTextView autoCompleteTextViewOffices;
    private TextInputLayout textInputLayoutOffices;
    private AutoCompleteTextView autoCompleteTextViewStates;
    private TextInputLayout textInputLayoutStates;
    int officeId = 0;
    int storeId = 0;
    int articleId = 0;
    String stateActive = "";
    private String selectedDate = "";
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private ImageView photoActive, photoActive2, photoActive3, photoActive4;
    private Integer countAddImage = 0;
    private Button buttonAddPhoto;
    private Button btnDeleteImg1, btnDeleteImg2, btnDeleteImg3, btnDeleteImg4;
    private String photosActiveEdit = "";
    private ActivityResultLauncher<Intent> galleryLauncher;
    private ActivityResultLauncher<Intent> cameraLauncher;
    //Si la foto proviene de la camara se guarda aca
    private Bitmap photoCam = null;
    private List<Bitmap> photosCam = new ArrayList<>(Arrays.asList(null, null, null, null));
    //Si la foto proviene de la galeria se guarda aca
    private Uri selectedImageUri = null;
    private List<Uri> photosGallery = new ArrayList<>(Arrays.asList(null, null, null, null));
    private List<String> photosServer = new ArrayList<>(Arrays.asList(null, null, null, null));
    private CheckBox virtualCode;

    // Método estático para crear una instancia del DialogFragment modo edit
    public static DialogFragmentActive newInstance(int mode, int position, Active active, ActiveFragment parentFragment) {
        DialogFragmentActive fragment = new DialogFragmentActive();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        args.putInt(ARG_POSITION, position);
        args.putSerializable(ARG_ACTIVE, active);
        fragment.setArguments(args);
        // Guardar una referencia al fragmento padre (ArticleFragment)
        fragment.setParentFragment(parentFragment);

        return fragment;
    }

    // Método estático para crear una instancia del DialogFragment en modo creacion
    public static DialogFragmentActive newInstance(int mode, ActiveFragment parentFragment) {
        DialogFragmentActive fragment = new DialogFragmentActive();
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
                active = (Active) getArguments().getSerializable(ARG_ACTIVE);
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

                            if (photosGallery.get(0) == null && photosCam.get(0) == null && photosServer.get(0) == null){
                                photoActive.setImageURI(selectedImageUri);
                                photosGallery.set(0, selectedImageUri);
                                countAddImage++;
                            }
                            else if (photosGallery.get(1) == null && photosCam.get(1) == null && photosServer.get(1) == null){
                                photoActive2.setImageURI(selectedImageUri);
                                photosGallery.set(1, selectedImageUri);
                                countAddImage++;
                            }
                            else if (photosGallery.get(2) == null && photosCam.get(2) == null && photosServer.get(2) == null){
                                photoActive3.setImageURI(selectedImageUri);
                                photosGallery.set(2, selectedImageUri);
                                countAddImage++;
                            }
                            else if (photosGallery.get(3) == null && photosCam.get(3) == null && photosServer.get(3) == null){
                                photoActive4.setImageURI(selectedImageUri);
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

                            if (photosGallery.get(0) == null && photosCam.get(0) == null && photosServer.get(0) == null){
                                photoActive.setImageBitmap(photoCam);
                                photosCam.set(0, photoCam);
                                countAddImage++;
                            }
                            else if (photosGallery.get(1) == null && photosCam.get(1) == null && photosServer.get(1) == null){
                                photoActive2.setImageBitmap(photoCam);
                                photosCam.set(1, photoCam);
                                countAddImage++;
                            }
                            else if (photosGallery.get(2) == null && photosCam.get(2) == null && photosServer.get(2) == null){
                                photoActive3.setImageBitmap(photoCam);
                                photosCam.set(2, photoCam);
                                countAddImage++;
                            }
                            else if (photosGallery.get(3) == null && photosCam.get(3) == null && photosServer.get(3) == null){
                                photoActive4.setImageBitmap(photoCam);
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

    // Método para establecer el fragmento padre (ActiveFragment)
    private void setParentFragment(ActiveFragment parentFragment) {
        this.parentFragment = parentFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        System.out.println("Creando dialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_active, null);

        TextInputLayout textInputLayoutBarcode = view.findViewById(R.id.editTextbar_code);
        editTextBarcode = textInputLayoutBarcode.getEditText();

        TextInputLayout textInputLayoutModel = view.findViewById(R.id.editTextModel);
        editTextModel = textInputLayoutModel.getEditText();

        TextInputLayout textInputLayoutSerie = view.findViewById(R.id.editTextSerie);
        editTextSerie = textInputLayoutSerie.getEditText();

        TextInputLayout textInputLayoutComment = view.findViewById(R.id.editTextComment);
        editTextcomment = textInputLayoutComment.getEditText();

        TextInputLayout textInputLayoutBrand = view.findViewById(R.id.editTextBrand);
        editTextbrand = textInputLayoutBrand.getEditText();

        //obtener estados
        initializeState(view);

        TextInputLayout textInputLayoutNameCharge = view.findViewById(R.id.editTextNameCharge);
        editTextNamecharge = textInputLayoutNameCharge.getEditText();

        TextInputLayout textInputLayoutRutCharge = view.findViewById(R.id.editTextRutCharge);
        editTextRutcharge = textInputLayoutRutCharge.getEditText();

        virtualCode = view.findViewById(R.id.virtualCode);
        virtualCode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
//                    Toast.makeText(getContext(), "virtual checked", Toast.LENGTH_SHORT).show();
                    editTextBarcode.setEnabled(false);
                    editTextBarcode.setText("");
                }
                else {
//                    Toast.makeText(getContext(), "no virtual checked", Toast.LENGTH_SHORT).show();
                    editTextBarcode.setEnabled(true);
                }
            }
        });

        editTextRutcharge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String rut = s.toString();
                String formattedRut = formatRut(rut);
                editTextRutcharge.removeTextChangedListener(this); // Evitar el bucle infinito
                editTextRutcharge.setText(formattedRut);
                editTextRutcharge.setSelection(formattedRut.length()); // Colocar el cursor al final del texto
                editTextRutcharge.addTextChangedListener(this);
            }
        });

        TextInputLayout textInputLayoutRecordNumber = view.findViewById(R.id.editTextRecordNumber);
        editTextRecordnumber = textInputLayoutRecordNumber.getEditText();

        TextInputLayout textInputLayoutDateAdquisition = view.findViewById(R.id.editTextDate);
        adquisitionDateEditText = textInputLayoutDateAdquisition.getEditText();

        //obtener stores
        initializeStores(view);

        textInputLayoutOffices = view.findViewById(R.id.office);
        autoCompleteTextViewOffices = textInputLayoutOffices.findViewById(R.id.office_select);

        //obtener articles
        initializeArticle(view);

        adquisitionDateEditText = view.findViewById(R.id.adquisitionDate);
        adquisitionDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        photoActive = view.findViewById(R.id.imageView);
        photoActive2 = view.findViewById(R.id.imageView2);
        photoActive3 = view.findViewById(R.id.imageView3);
        photoActive4 = view.findViewById(R.id.imageView4);

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
                photoActive.setImageResource(R.drawable.photo);
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
                photoActive2.setImageResource(R.drawable.photo);
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
                photoActive3.setImageResource(R.drawable.photo);
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
                photoActive4.setImageResource(R.drawable.photo);
                buttonAddPhoto.setEnabled(true);
                photosCam.set(3, null);
                photosGallery.set(3, null);
                countAddImage--;
            }
        });

        // setea los campos de activo
        if(active != null){
            if (active.getBar_code().equals("")){
                editTextBarcode.setEnabled(false);
                virtualCode.setChecked(true);
                virtualCode.setEnabled(false);
            }else {
                editTextBarcode.setText(active.getBar_code());
                virtualCode.setEnabled(false);
            }
            editTextModel.setText(active.getModel());
            editTextSerie.setText(active.getSerie());
            editTextcomment.setText(active.getComment());
            editTextRecordnumber.setText(active.getAccounting_record_number());
            editTextNamecharge.setText(active.getName_in_charge_active());
            editTextRutcharge.setText(active.getRut_in_charge_active());
            adquisitionDateEditText.setText(active.getAcquisition_date());
            editTextbrand.setText(active.getBrand());

            articleId = active.getArticle_id();
            selectedDate = active.getAcquisition_date();
            officeId = active.getOffice_id();
            stateActive = active.getState();

            photosActiveEdit = (active.getPhoto1() == null) ? "" : active.getPhoto1();
            System.out.println("images articulos " + photosActiveEdit);
            if(!photosActiveEdit.isEmpty() && photosActiveEdit != null){
                countAddImage++;
            }
            photosActiveEdit = (active.getPhoto2() == null) ? "" : active.getPhoto2();
            if(!photosActiveEdit.isEmpty()){
                countAddImage++;
            }
            photosActiveEdit = (active.getPhoto3() == null) ? "" : active.getPhoto3();
            if(!photosActiveEdit.isEmpty()){
                countAddImage++;
            }
            photosActiveEdit = (active.getPhoto4() == null) ? "" : active.getPhoto4();
            if(!photosActiveEdit.isEmpty()){
                countAddImage++;
            }

            try{
                Uri photoUri;
                // Verificar si las rutas de las fotos no es nula
                if (!active.getPhoto1().isEmpty()) {
                    photoUri = Uri.parse(active.getPhoto1());
                    if (active.getPhoto1().contains("mobile_local")) {
                        photoActive.setImageURI(photoUri);
                        photosGallery.set(0, photoUri);
                    }
                    else {
                        photoActive.setImageResource(R.drawable.sca_logo_2);
                        photosServer.set(0, "photoLoad");
                        btnDeleteImg1.setEnabled(false);
                    }
                }

                if (!active.getPhoto2().isEmpty()) {
                    photoUri = Uri.parse(active.getPhoto2());
                    if (active.getPhoto2().contains("mobile_local")) {
                        photoActive2.setImageURI(photoUri);
                        photosGallery.set(1, photoUri);
                    }
                    else {
                        photoActive2.setImageResource(R.drawable.sca_logo_2);
                        photosServer.set(1, "photoLoad");
                        btnDeleteImg2.setEnabled(false);
                    }
                }

                if (!active.getPhoto3().isEmpty()) {
                    photoUri = Uri.parse(active.getPhoto3());
                    if (active.getPhoto3().contains("mobile_local")) {
                        photoActive3.setImageURI(photoUri);
                        photosGallery.set(2, photoUri);
                    }
                    else {
                        photoActive3.setImageResource(R.drawable.sca_logo_2);
                        photosServer.set(2, "photoLoad");
                        btnDeleteImg3.setEnabled(false);
                    }
                }

                if (!active.getPhoto4().isEmpty()) {
                    photoUri = Uri.parse(active.getPhoto4());
                    if (active.getPhoto4().contains("mobile_local")) {
                        photoActive4.setImageURI(photoUri);
                        photosGallery.set(3, photoUri);
                    }
                    else {
                        photoActive4.setImageResource(R.drawable.sca_logo_2);
                        photosServer.set(3, "photoLoad");
                        btnDeleteImg4.setEnabled(false);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error al cargar la foto, Mostrando imagen por defecto.");
            }
        }

        AlertDialog dialog = builder.setView(view)
                .setTitle(mode == MODE_EDIT ? "Editar activo" : "Crear nuevo activo")
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

                        // Creacion del activo
                        String newBarcode = editTextBarcode.getText().toString();
                        String newModel = editTextModel.getText().toString();
                        String newSerie = editTextSerie.getText().toString();
                        String newcomment = editTextcomment.getText().toString();
                        String newRecordNumber = editTextRecordnumber.getText().toString();
                        String newNameCharge = editTextNamecharge.getText().toString();
                        String newRutCharge = editTextRutcharge.getText().toString();
                        String newBrand = editTextbrand.getText().toString();

                        System.out.println("Virtual code " + virtualCode.isChecked());
                        // Validacion de datos.
                        if (newBarcode.isEmpty() && !virtualCode.isChecked()) {
                            textInputLayoutBarcode.setError("Codigo de barra o virtual requerido");
                            continueExecution = false;
                        } else {
                            textInputLayoutBarcode.setError(null);
                        }

                        if (newModel.isEmpty()) {
                            textInputLayoutModel.setError("Modelo requerido");
                            continueExecution = false;
                        } else {
                            textInputLayoutModel.setError(null);
                        }

                        if (newSerie.isEmpty()) {
                            textInputLayoutSerie.setError("Serie requerido");
                            continueExecution = false;
                        } else {
                            textInputLayoutSerie.setError(null);
                        }
                        if (newBrand.isEmpty()) {
                            textInputLayoutBrand.setError("Marca requerido");
                            continueExecution = false;
                        } else {
                            textInputLayoutBrand.setError(null);
                        }
                        if (selectedDate.equals("")){
                            adquisitionDateEditText.setError("Seleccione activo");
                            textInputLayoutDateAdquisition.setError("Fecha de adquisición requerida");
                            continueExecution = false;
                        }else {
                            adquisitionDateEditText.setError(null);
                            textInputLayoutDateAdquisition.setError(null);
                        }
                        if (articleId == 0) {
                            System.out.println("falta el artículo");
                            autoCompleteTextViewArticles.setError("Seleccione articulo");
                            textInputLayoutArticle.setError("Articulo requerido");
                            continueExecution = false;
                        } else {
                            autoCompleteTextViewArticles.setError(null);
                            textInputLayoutArticle.setError(null);
                        }
                        if (storeId == 0) {
                            System.out.println("falta la sucursal");
                            autoCompleteTextViewStores.setError("Seleccione sucursal");
                            textInputLayoutStores.setError("Sucursal requerida");
                            continueExecution = false;
                        } else {
                            autoCompleteTextViewStores.setError(null);
                            textInputLayoutStores.setError(null);
                        }
                        if (officeId == 0) {
                            System.out.println("falta la oficina");
                            autoCompleteTextViewOffices.setError("Seleccione oficina");
                            textInputLayoutOffices.setError("Oficina requerida");
                            continueExecution = false;
                        } else {
                            autoCompleteTextViewOffices.setError(null);
                            textInputLayoutOffices.setError(null);
                        }
                        if (stateActive.equals("")) {
                            System.out.println("falta el estado");
                            autoCompleteTextViewStates.setError("Seleccione estado");
                            textInputLayoutStates.setError("Estado requerido");
                            continueExecution = false;
                        } else {
                            autoCompleteTextViewStates.setError(null);
                            textInputLayoutStates.setError(null);
                        }

                        if(continueExecution) {
                            // Crear un objeto Active con los nuevos valores
                            Active newActive = new Active();
                            newActive.setBar_code(newBarcode);
                            newActive.setModel(newModel);
                            newActive.setSerie(newSerie);
                            newActive.setComment(newcomment);
                            newActive.setAccounting_record_number(newRecordNumber);
                            newActive.setName_in_charge_active(newNameCharge);
                            newActive.setRut_in_charge_active(newRutCharge);
                            newActive.setAccounting_document("");
                            newActive.setBrand(newBrand);

                            newActive.setAcquisition_date(selectedDate);
                            newActive.setArticle_id(articleId);
                            newActive.setOffice_id(officeId);
                            newActive.setState(stateActive);
                            System.out.println("office id " + officeId);

                            String nameImgActive = newBarcode;

                            List<String> photosUrl = new ArrayList<>();
                            boolean success = true;
                            Integer contNamePhoto = 0;
                            for(int i = 0; i < photosCam.size(); i++){
                                if (photosCam.get(i) == null) {
                                    continue;
                                }
                                String url = Active.savePhoto(requireContext(), photosCam.get(i),String.valueOf(contNamePhoto) + nameImgActive);
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
                                if (mode == MODE_EDIT){
                                    if (photosGallery.get(i).toString().equals(active.getPhoto1()) || photosGallery.get(i).toString().equals(active.getPhoto2())
                                            || photosGallery.get(i).toString().equals(active.getPhoto3()) || photosGallery.get(i).toString().equals(active.getPhoto4())){
                                        continue;
                                    }
                                }

                                String url = Active.savePhoto(requireContext(), photosGallery.get(i), String.valueOf(contNamePhoto) + nameImgActive);
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
                                Toast.makeText(requireContext(), "Error al guardar la foto seleecionada", Toast.LENGTH_SHORT).show();
                            }
                            photoCam = null;
                            selectedImageUri = null;

                            if (mode == MODE_EDIT) {
                                newActive.setPhoto1(active.getPhoto1());
                                newActive.setPhoto2(active.getPhoto2());
                                newActive.setPhoto3(active.getPhoto3());
                                newActive.setPhoto4(active.getPhoto4());

                                newActive.setId(active.getId());
                                newActive.setSync(active.getSync());
                                if (!photosUrl.isEmpty()){
                                    for (int i = 0; i < photosUrl.size(); i++){
                                        if(active.getPhoto1().isEmpty() || !(photosGallery.get(0).toString().equals(active.getPhoto1())) ){
                                            newActive.setPhoto1(photosUrl.get(i));
                                            active.setPhoto1(photosUrl.get(i));
                                        }
                                        else if(active.getPhoto2().isEmpty() || !(photosGallery.get(1).toString().equals(active.getPhoto2())) ){
                                            newActive.setPhoto2(photosUrl.get(i));
                                            active.setPhoto2(photosUrl.get(i));
                                        }
                                        else if(active.getPhoto3().isEmpty() || !(photosGallery.get(2).toString().equals(active.getPhoto3())) ){
                                            newActive.setPhoto3(photosUrl.get(i));
                                            active.setPhoto3(photosUrl.get(i));
                                        }
                                        else if(active.getPhoto4().isEmpty() || !(photosGallery.get(3).toString().equals(active.getPhoto4())) ){
                                            newActive.setPhoto4(photosUrl.get(i));
                                            active.setPhoto4(photosUrl.get(i));
                                        }
                                    }
                                }


                                // Actualizar el artículo en la base de datos
                                boolean updateSuccessful = newActive.updateActive(getContext());
                                System.out.println(updateSuccessful);

                                if (updateSuccessful && parentFragment != null) {
                                    // La actualización fue exitosa
                                    Toast.makeText(getContext(), "Activo actualizado correctamente", Toast.LENGTH_SHORT).show();

                                    // Actualizar la lista de artículos en el fragmento padre (ArticleFragment)
                                    parentFragment.updateActives(getContext(), position);
                                } else {
                                    // Ocurrió un error durante la actualización
                                    Toast.makeText(getContext(), "Error al actualizar el activo", Toast.LENGTH_SHORT).show();
                                }

                                // Dismiss once everything is OK.
                                dialog.dismiss();
                            } else {

                                newActive.setAccounting_document("");
                                newActive.setVirtual_code("false");
                                if (virtualCode.isChecked()) {
                                    newActive.setVirtual_code("true");
                                }


                                if (!photosUrl.isEmpty()){
                                    for (int i = 0; i < photosUrl.size(); i++){
                                        if(i == 0){
                                            newActive.setPhoto1(photosUrl.get(i));
                                        }
                                        if(i == 1){
                                            newActive.setPhoto2(photosUrl.get(i));
                                        }
                                        if(i == 2){
                                            newActive.setPhoto3(photosUrl.get(i));
                                        }
                                        if(i == 3){
                                            newActive.setPhoto4(photosUrl.get(i));
                                        }
                                    }
                                }else {
                                    newActive.setPhoto1("");
                                    newActive.setPhoto2("");
                                    newActive.setPhoto3("");
                                    newActive.setPhoto4("");
                                }

                                boolean createSuccessful = newActive.createActive(getContext());
                                System.out.println(createSuccessful);

                                if (createSuccessful && parentFragment != null) {
                                    // La actualización fue exitosa
                                    Toast.makeText(getContext(), "Activo creado correctamente", Toast.LENGTH_SHORT).show();

                                    // Actualizar la lista de artículos en el fragmento padre (ArticleFragment)
                                    parentFragment.showActives(getContext());
                                    dialog.dismiss();
                                } else {
                                    // Ocurrió un error durante la actualización
                                    Toast.makeText(getContext(), "Error al crear el activo", Toast.LENGTH_SHORT).show();
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

    public void initializeArticle(View view) {

        textInputLayoutArticle = view.findViewById(R.id.article_options);
        autoCompleteTextViewArticles = textInputLayoutArticle.findViewById(R.id.article_select);

        Article article = new Article();
        List<Article> articleList = article.getArticlesName(requireContext());

        List<String> items = articleList.stream()
                .map(Article::getName)
                .collect(Collectors.toList());

        adapterArticles = new ArrayAdapter<String>(requireContext(), R.layout.list_item, items);
        autoCompleteTextViewArticles.setAdapter(adapterArticles);

        if(mode == MODE_EDIT){
            String selectedArticleName = (active != null && active.getArticle_id() != null) ? article.getArticleName(active.getArticle_id(), articleList) : null;

            // Desactivar temporalmente el filtrado automático
            autoCompleteTextViewArticles.setThreshold(Integer.MAX_VALUE);
            System.out.println("selected article " +  selectedArticleName);
            // Si se encontró la descripción de la categoría, establecerla como texto en el AutoCompleteTextView
            if (selectedArticleName != null) {
                autoCompleteTextViewArticles.setText(selectedArticleName);
                System.out.println("selected article " +  selectedArticleName);
            }
            // Volver a activar el filtrado automático
            autoCompleteTextViewArticles.setThreshold(1);
        }

        autoCompleteTextViewArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View _view, int position, long id) {
                Article articleSelected;
                System.out.println("position " + position);
                String nameArticleSelected = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < articleList.size(); i++) {
                    articleSelected = articleList.get(i);
                    if (articleSelected.getName().equals(nameArticleSelected)){
                        System.out.println(articleSelected.getName());
                        System.out.println(articleSelected.getId());
                        articleId = articleSelected.getId();
                        break;
                    }
                }
            }
        });

    }

    public void initializeStores(View view) {

        textInputLayoutStores = view.findViewById(R.id.stores);
        autoCompleteTextViewStores = textInputLayoutStores.findViewById(R.id.stores_select);

        Store store = new Store();
        List<Store> storeList = store.getStores(requireContext());

        List<String> items = storeList.stream()
                .map(Store::getFullName)
                .collect(Collectors.toList());

        adapterStores = new ArrayAdapter<String>(requireContext(), R.layout.list_item, items);
        autoCompleteTextViewStores.setAdapter(adapterStores);

        if(mode == MODE_EDIT){

            Office office = new Office();
            office = office.getOfficeId(requireContext() ,active.getOffice_id());
            //office.getOfficeId(requireContext() ,active.getOffice_id());

            String selectedStore = (active != null && office != null) ? store.getStoreInfo(office.getSucursal_id(), storeList) : null;

            // Desactivar temporalmente el filtrado automático
            autoCompleteTextViewStores.setThreshold(Integer.MAX_VALUE);

            // Si se encontró la descripción de la categoría, establecerla como texto en el AutoCompleteTextView
            if (selectedStore != null) {
                autoCompleteTextViewStores.setText(selectedStore);
                System.out.println("selectedstore " +  selectedStore);
            }

            // Volver a activar el filtrado automático
            autoCompleteTextViewStores.setThreshold(1);

            storeId = office.getSucursal_id();
            initializeOffice(view, storeId);
        }

        autoCompleteTextViewStores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View _view, int position, long id) {
                Store storeSelected;
                System.out.println("position " + position);
                String nameStoreSelected = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < storeList.size(); i++) {
                    storeSelected = storeList.get(i);
                    if (storeSelected.getFullName().equals(nameStoreSelected)){
                        System.out.println(storeSelected.getFullName());
                        System.out.println(storeSelected.getId());
                        storeId = storeSelected.getId();
                        initializeOffice(view, storeId);
                        String item = parent.getItemAtPosition(position).toString();
                        Toast.makeText(requireContext(), "Item: "+item, Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            }
        });

    }

    public void initializeOffice(View view, Integer idSucursal) {
        textInputLayoutOffices = view.findViewById(R.id.office);
        autoCompleteTextViewOffices = textInputLayoutOffices.findViewById(R.id.office_select);

        if (adapterOffices != null) {
            adapterOffices.clear();
            autoCompleteTextViewOffices.setText("");
        }

        Office office = new Office();
        List<Office> officeList = office.getOfficeStore(requireContext(), idSucursal);
        System.out.println(officeList);
        List<String> items = officeList.stream()
                .map(Office::getFullName)
                .collect(Collectors.toList());

        adapterOffices = new ArrayAdapter<String>(requireContext(), R.layout.list_item, items);
        autoCompleteTextViewOffices.setAdapter(adapterOffices);

        if(mode == MODE_EDIT){

            String selectedofficeInfo = (active != null && active.getOffice_id() != null) ? office.getOfficeInfo(active.getOffice_id(), officeList) : null;

            // Desactivar temporalmente el filtrado automático
            autoCompleteTextViewOffices.setThreshold(Integer.MAX_VALUE);

            // Si se encontró la descripción de la categoría, establecerla como texto en el AutoCompleteTextView
            if (selectedofficeInfo != null) {
                autoCompleteTextViewOffices.setText(selectedofficeInfo);
                System.out.println("selectedofficeinfo --" +  selectedofficeInfo);
            }

            // Volver a activar el filtrado automático
            autoCompleteTextViewOffices.setThreshold(1);
        }

        autoCompleteTextViewOffices.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View _view, int position, long id) {
                Office officeSelected;
                System.out.println("position " + position);
                String nameOfficeSelected = (String) parent.getItemAtPosition(position);
                for (int i = 0; i < officeList.size(); i++) {
                    officeSelected = officeList.get(i);
                    if (officeSelected.getFullName().equals(nameOfficeSelected)){
                        System.out.println(officeSelected.getFullName());
                        System.out.println(officeSelected.getId());
                        officeId = officeSelected.getId();
                        break;
                    }
                }
            }
        });
    }

    public void initializeState(View view) {
        textInputLayoutStates = view.findViewById(R.id.state_options);
        autoCompleteTextViewStates = textInputLayoutStates.findViewById(R.id.state_select);

        List<String> states = Arrays.asList("Reparación", "Nuevo", "Operativo", "Perdida o Robo", "Dañado", "Obsoleto", "Otro");
        ArrayAdapter<String> adapterStates = new ArrayAdapter<>(requireContext(), R.layout.list_item, states);
        autoCompleteTextViewStates.setAdapter(adapterStates);

        if(mode == MODE_EDIT){

            String selectedState = (active != null && active.getState() != null) ? active.getState() : null;

            // Desactivar temporalmente el filtrado automático
            autoCompleteTextViewStates.setThreshold(Integer.MAX_VALUE);

            // Si se encontró la descripción de la categoría, establecerla como texto en el AutoCompleteTextView
            if (selectedState != null) {
                autoCompleteTextViewStates.setText(selectedState);
                System.out.println("selectedCategoryDescription " +  selectedState);
            }

            // Volver a activar el filtrado automático
            autoCompleteTextViewStates.setThreshold(1);
        }

        autoCompleteTextViewStates.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View _view, int position, long id) {
                stateActive = (String) parent.getItemAtPosition(position);
            }
        });
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // Aquí obtén la fecha seleccionada y actualiza el campo de texto
                        calendar.set(year, month, dayOfMonth);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        selectedDate = dateFormat.format(calendar.getTime());
                        adquisitionDateEditText.setText(selectedDate);
                    }
                },
                year, month, dayOfMonth
        );

        datePickerDialog.show();
    }

    private String formatRut(String rut) {
        rut = rut.replaceAll("[^0-9kK]", ""); // Eliminar cualquier carácter que no sea un dígito o la letra 'k' para validar RUT
        int rutLength = rut.length();

        // Si el RUT es menor o igual a 1, no hay formato que aplicar
        if (rutLength <= 1) {
            return rut;
        }

        String dv = rut.substring(rutLength - 1); // Digito verificador
        String digits = rut.substring(0, rutLength - 1); // Dígitos del RUT sin el dígito verificador

        StringBuilder formattedRut = new StringBuilder();
        int count = 0;

        // Agregar los puntos separadores
        for (int i = digits.length() - 1; i >= 0; i--) {
            char c = digits.charAt(i);
            formattedRut.insert(0, c);
            count++;
            if (count % 3 == 0 && i != 0) {
                formattedRut.insert(0, ".");
            }
        }

        // Agregar el guion y el dígito verificador al final
        formattedRut.append("-");
        formattedRut.append(dv);

        return formattedRut.toString();
    }
}
