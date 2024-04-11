package com.example.sca_app_v1.home_app.active;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.sca_app_v1.R;
import com.example.sca_app_v1.home_app.article.ArticleFragment;
import com.example.sca_app_v1.home_app.article.DialogFragmentArticle;
import com.example.sca_app_v1.models.Active;
import com.example.sca_app_v1.models.Article;
import com.example.sca_app_v1.models.Office;
import com.example.sca_app_v1.models.Store;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
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
    //private Article article;
    private EditText editTextBarcode;
    private EditText editTextModel;
    private EditText editTextSerie;
    private EditText editTextcomment;
    private EditText editTextNamecharge;
    private EditText editTextRutcharge;
    private EditText editTextRecordnumber;
    private EditText adquisitionDateEditText;
    int officeId;
    int articleId;
    String stateActive;
    private String selectedDate;

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

        //obtener estados
        initializeState(view);

        TextInputLayout textInputLayoutNameCharge = view.findViewById(R.id.editTextNameCharge);
        editTextNamecharge = textInputLayoutNameCharge.getEditText();

        TextInputLayout textInputLayoutRutCharge = view.findViewById(R.id.editTextRutCharge);
        editTextRutcharge = textInputLayoutRutCharge.getEditText();

        TextInputLayout textInputLayoutRecordNumber = view.findViewById(R.id.editTextRecordNumber);
        editTextRecordnumber = textInputLayoutRecordNumber.getEditText();

        TextInputLayout textInputLayoutDateAdquisition = view.findViewById(R.id.editTextDate);
        adquisitionDateEditText = textInputLayoutDateAdquisition.getEditText();

        //obtener stores
        initializeStores(view);

        //obtener articles
        initializeArticle(view);

        adquisitionDateEditText = view.findViewById(R.id.adquisitionDate);
        adquisitionDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        // setea los campos de activo

        if(active != null){
            editTextBarcode.setText(active.getBar_code());
            editTextModel.setText(active.getModel());
            editTextSerie.setText(active.getSerie());
            editTextcomment.setText(active.getComment());
            editTextRecordnumber.setText(active.getAccounting_record_number());
            editTextNamecharge.setText(active.getName_in_charge_active());
            editTextRutcharge.setText(active.getRut_in_charge_active());
            adquisitionDateEditText.setText(active.getAcquisition_date());

            articleId = active.getArticle_id();
            selectedDate = active.getAcquisition_date();
            officeId = active.getOffice_id();
            stateActive = active.getState();
        }

        builder.setView(view)
                .setTitle(mode == MODE_EDIT ? "Editar activo" : "Crear nuevo activo")
                .setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Procesar la edición del artículo
                        String newBarcode = editTextBarcode.getText().toString();
                        String newModel = editTextModel.getText().toString();
                        String newSerie = editTextSerie.getText().toString();
                        String newcomment = editTextcomment.getText().toString();
                        String newRecordNumber = editTextRecordnumber.getText().toString();
                        String newNameCharge = editTextNamecharge.getText().toString();
                        String newRutCharge = editTextRutcharge.getText().toString();
                        //String newDateAquisition = adquisitionDateEditText.getText().toString();

                        // Crear un objeto Article con los nuevos valores
                        Active newActive = new Active();
                        newActive.setBar_code(newBarcode);
                        newActive.setModel(newModel);
                        newActive.setSerie(newSerie);
                        newActive.setComment(newcomment);
                        newActive.setAccounting_record_number(newRecordNumber);
                        newActive.setName_in_charge_active(newNameCharge);
                        newActive.setRut_in_charge_active(newRutCharge);
                        newActive.setAccounting_document("");

                        newActive.setAcquisition_date(selectedDate);
                        newActive.setArticle_id(articleId);
                        newActive.setOffice_id(officeId);
                        newActive.setState(stateActive);

                        System.out.println("selected Date " + selectedDate);

                        if(mode == MODE_EDIT){
                            newActive.setId(active.getId());

                            // Actualizar el artículo en la base de datos
                            boolean updateSuccessful = newActive.updateActive(getContext());
                            System.out.println(updateSuccessful);

                            if (updateSuccessful && parentFragment != null) {
                                // La actualización fue exitosa
                                Toast.makeText(getContext(), "Artículo actualizado correctamente", Toast.LENGTH_SHORT).show();

                                // Actualizar la lista de artículos en el fragmento padre (ArticleFragment)
                                parentFragment.updateActives(getContext(), position);
                            } else {
                                // Ocurrió un error durante la actualización
                                Toast.makeText(getContext(), "Error al actualizar el artículo", Toast.LENGTH_SHORT).show();
                            }
                        }

                        else{
                            newActive.setAccounting_document("");

                            boolean createSuccessful = newActive.createActive(getContext());
                            System.out.println(createSuccessful);

                            if (createSuccessful && parentFragment != null) {
                                // La actualización fue exitosa
                                Toast.makeText(getContext(), "Activo creado correctamente", Toast.LENGTH_SHORT).show();

                                // Actualizar la lista de artículos en el fragmento padre (ArticleFragment)
                                parentFragment.showActives(getContext());
                            } else {
                                // Ocurrió un error durante la actualización
                                Toast.makeText(getContext(), "Error al crear el activo", Toast.LENGTH_SHORT).show();
                            }
                        }

                        // Lógica para guardar los cambios

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

    public void initializeArticle(View view) {

        TextInputLayout textInputLayoutArticle = view.findViewById(R.id.article_options);
        AutoCompleteTextView autoCompleteTextViewArticles = textInputLayoutArticle.findViewById(R.id.article_select);

        Article article = new Article();
        List<Article> articleList = article.getArticles(requireContext());

        List<String> items = articleList.stream()
                .map(Article::getName)
                .collect(Collectors.toList());

        adapterArticles = new ArrayAdapter<String>(requireContext(), R.layout.list_item, items);
        autoCompleteTextViewArticles.setAdapter(adapterArticles);

        if(mode == MODE_EDIT){
            String selectedArticleName = (active != null && active.getArticle_id() != null) ? article.getArticleName(active.getArticle_id(), articleList) : null;

            // Desactivar temporalmente el filtrado automático
            autoCompleteTextViewArticles.setThreshold(Integer.MAX_VALUE);

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
                Article articleSelected = articleList.get(position);
                articleId = articleSelected.getId();

                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(requireContext(), "Item: "+item, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void initializeStores(View view) {

        TextInputLayout textInputLayoutStores = view.findViewById(R.id.stores);
        AutoCompleteTextView autoCompleteTextViewStores = textInputLayoutStores.findViewById(R.id.stores_select);

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

            initializeOffice(view, office.getSucursal_id());
        }

        autoCompleteTextViewStores.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View _view, int position, long id) {
                System.out.println(parent);
                System.out.println("position " + position);
                Store storeSelected = storeList.get(position);
                System.out.println(storeSelected.getId());
                System.out.println(storeSelected.getAddress());
                initializeOffice(view, storeSelected.getId());

                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(requireContext(), "Item: "+item, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void initializeOffice(View view, Integer idSucursal) {
        TextInputLayout textInputLayoutOffices = view.findViewById(R.id.office);
        AutoCompleteTextView autoCompleteTextViewOffices = textInputLayoutOffices.findViewById(R.id.office_select);

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
                Office officeSelected = officeList.get(position);
                officeId = officeSelected.getId();
            }
        });
    }

    public void initializeState(View view) {
        TextInputLayout textInputLayoutStates = view.findViewById(R.id.state_options);
        AutoCompleteTextView autoCompleteTextViewStates = textInputLayoutStates.findViewById(R.id.state_select);

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
}
