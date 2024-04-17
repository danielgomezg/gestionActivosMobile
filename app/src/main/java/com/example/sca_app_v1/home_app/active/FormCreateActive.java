package com.example.sca_app_v1.home_app.active;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sca_app_v1.R;
import com.example.sca_app_v1.databinding.FragmentFormActiveBinding;
import com.example.sca_app_v1.databinding.FragmentFormArticleBinding;
import com.example.sca_app_v1.home_app.article.ArticleFragment;
import com.example.sca_app_v1.home_app.article.FormCreateArticle;
import com.example.sca_app_v1.models.Active;
import com.example.sca_app_v1.models.Article;
import com.example.sca_app_v1.models.Category;
import com.example.sca_app_v1.models.Office;
import com.example.sca_app_v1.models.Store;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

public class FormCreateActive extends BottomSheetDialogFragment{
    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private FragmentFormActiveBinding binding;
    private ArrayAdapter<String> adapterItems;
    private ArrayAdapter<String> adapterStores;
    private ArrayAdapter<String> adapterOffices;
    private ActiveFragment activeFragment;
    int officeId;
    int articleId;
    String stateActive;
    private EditText adquisitionDateEditText;
    private String selectedDate;



    // TODO: Customize parameters
    public static FormCreateActive newInstance(int itemCount) {
        final FormCreateActive fragment = new FormCreateActive();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentFormActiveBinding.inflate(inflater, container, false);
        FrameLayout bottomSheetDialog = binding.bsActive;
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetDialog);
        bottomSheetBehavior.setState(STATE_EXPANDED);
        return binding.getRoot();

    }

    public void setActiveFragment(ActiveFragment activeFragment) {
        this.activeFragment = activeFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextInputLayout textInputLayoutBarcode = view.findViewById(R.id.editTextBarcode);
        TextInputLayout textInputLayoutmodel = view.findViewById(R.id.editTextModel);
        TextInputLayout textInputLayoutSerie = view.findViewById(R.id.editTextSerie);
        //TextInputLayout textInputLayoutDateAdquisition = view.findViewById(R.id.editTextDate);
        TextInputLayout textInputLayoutcomment = view.findViewById(R.id.editTextComment);
        TextInputLayout textInputLayoutNameCharge = view.findViewById(R.id.editTextNameCharge);
        TextInputLayout textInputLayoutRutCharge = view.findViewById(R.id.editTextRutCharge);


        adquisitionDateEditText = view.findViewById(R.id.adquisitionDate);
        adquisitionDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        //Obtener stados
        initializeState(view);

        //obtener sucursales
        initializeStores(view);

        //obtener articulos
        initializeArticles(view);

        //obtener la company
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("session", Context.MODE_PRIVATE);
        Integer company_id = sharedPreferences.getInt("company_id", 0);

        Button btnCancel = view.findViewById(R.id.cancel);
        Button btnSaveActive = view.findViewById(R.id.saveArticle);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el cuadro de diálogo
                dismiss();
            }
        });

        btnSaveActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores de los TextInputEditText
                String bar_code = Objects.requireNonNull(textInputLayoutBarcode.getEditText()).getText().toString();
                String model = Objects.requireNonNull(textInputLayoutmodel.getEditText()).getText().toString();
                String serie = Objects.requireNonNull(textInputLayoutSerie.getEditText()).getText().toString();
                String comment = Objects.requireNonNull(textInputLayoutcomment.getEditText()).getText().toString();
                String nameCharge = Objects.requireNonNull(textInputLayoutNameCharge.getEditText()).getText().toString();
                String rutCharge = Objects.requireNonNull(textInputLayoutRutCharge.getEditText()).getText().toString();


                Active newActive = new Active();
                newActive.setBar_code(bar_code);
                newActive.setModel(model);
                newActive.setSerie(serie);
                newActive.setState(stateActive);
                newActive.setArticle_id(articleId);
                newActive.setAccounting_document("");
                newActive.setComment(comment);
                newActive.setName_in_charge_active(nameCharge);
                newActive.setRut_in_charge_active(rutCharge);
                newActive.setOffice_id(officeId);
                newActive.setAcquisition_date(selectedDate);
                newActive.setAccounting_record_number("");

                boolean createSuccessful = newActive.createActive(getContext());
                System.out.println(createSuccessful);
                System.out.println("seleccion " + selectedDate);
//                selectedDate = selectedDate.replace("/", "-");

                if (createSuccessful && activeFragment != null) {
                    // La creacion fue exitosa
                    Toast.makeText(getContext(), "Activo creado correctamente", Toast.LENGTH_SHORT).show();

                    // Actualizar la lista de artículos en el fragmento padre (ArticleFragment)
                    activeFragment.showActives(getContext());
                } else {
                    // Ocurrió un error durante la actualización
                    Toast.makeText(getContext(), "Error al crear el activo", Toast.LENGTH_SHORT).show();
                }

                // Cerrar el cuadro de diálogo
                dismiss();
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void initializeArticles(View view) {

        TextInputLayout textInputLayoutArticle = view.findViewById(R.id.article_options);
        AutoCompleteTextView autoCompleteTextViewArticle = textInputLayoutArticle.findViewById(R.id.article_select);
        Article article = new Article();
        List<Article> articleList = article.getArticles(requireContext(), 0);
        System.out.println("ARTICLE LIST " + articleList);
        List<String> items = articleList.stream()
                .map(Article::getName)
                .collect(Collectors.toList());
        adapterItems = new ArrayAdapter<String>(requireContext(), R.layout.list_item, items);
        autoCompleteTextViewArticle.setAdapter(adapterItems);

        autoCompleteTextViewArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = articleList.get(position);
                articleId = article.getId();
//                String item = parent.getItemAtPosition(position).toString();
//                Toast.makeText(requireContext(), "Item: "+item, Toast.LENGTH_SHORT).show();
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
