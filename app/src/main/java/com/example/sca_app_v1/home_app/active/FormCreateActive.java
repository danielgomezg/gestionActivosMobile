package com.example.sca_app_v1.home_app.active;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

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
import android.widget.FrameLayout;
import android.widget.Toast;

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
import java.util.List;
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
        TextInputLayout textInputLayoutArticle = view.findViewById(R.id.article_options);
        AutoCompleteTextView autoCompleteTextViewArticle = textInputLayoutArticle.findViewById(R.id.article_select);

        //obtener sucursales
        initializeStores(view);

        //obtener articulos
        Article article = new Article();
        List<Article> articleList = article.getArticles(requireContext());
        System.out.println("ARTICLE LIST " + articleList);
        List<String> items = articleList.stream()
                .map(Article::getName)
                .collect(Collectors.toList());
        adapterItems = new ArrayAdapter<String>(requireContext(), R.layout.list_item, items);
        autoCompleteTextViewArticle.setAdapter(adapterItems);

        autoCompleteTextViewArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                String selectedArticle = autoCompleteTextViewArticle.getText().toString();
                int articleId = article.getArticleID(selectedArticle, articleList);

                Active newActive = new Active();
                newActive.setBar_code(bar_code);
                newActive.setModel(model);
                newActive.setSerie(serie);
                newActive.setArticle_id(articleId);
                newActive.setAccounting_document("");

                boolean createSuccessful = newActive.createActive(getContext());
                System.out.println(createSuccessful);

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
    }

}
