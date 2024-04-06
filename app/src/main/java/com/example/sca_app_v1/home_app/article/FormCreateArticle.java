package com.example.sca_app_v1.home_app.article;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.example.sca_app_v1.R;
import com.example.sca_app_v1.models.Article;
import com.example.sca_app_v1.models.Category;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sca_app_v1.databinding.FragmentItemListDialogListDialogItemBinding;
import com.example.sca_app_v1.databinding.FragmentItemListDialogListDialogBinding;
import com.example.sca_app_v1.databinding.FragmentFormArticleBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FormCreateArticle extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private FragmentFormArticleBinding binding;

    private ArrayAdapter<String> adapterItems;

    private ArticleFragment articleFragment;

    // TODO: Customize parameters
    public static FormCreateArticle newInstance(int itemCount) {
        final FormCreateArticle fragment = new FormCreateArticle();
//        final Bundle args = new Bundle();
//        args.putInt(ARG_ITEM_COUNT, itemCount);
//        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentFormArticleBinding.inflate(inflater, container, false);
        FrameLayout bottomSheetDialog = binding.bsArticle;
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetDialog);
        bottomSheetBehavior.setState(STATE_EXPANDED);
        return binding.getRoot();

    }

    public void setArticleFragment(ArticleFragment articleFragment) {
        this.articleFragment = articleFragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        final RecyclerView recyclerView = (RecyclerView) view;
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(new ItemAdapter(5));
        TextInputLayout textInputLayoutName = view.findViewById(R.id.editTextName);
        TextInputLayout textInputLayoutCode = view.findViewById(R.id.editTextCode);
        TextInputLayout textInputLayoutDescription = view.findViewById(R.id.editTextDescription);
        TextInputLayout textInputLayoutCategory = view.findViewById(R.id.textInputLayout);
        AutoCompleteTextView autoCompleteTextViewCategory = textInputLayoutCategory.findViewById(R.id.category_select);

        //obtener categories
        Category category = new Category();
        List<Category> categoryList = category.getCategories(requireContext());
        System.out.println("CATEGORY LIST " + categoryList);
        List<String> items = categoryList.stream()
                .map(Category::getDescription)
                .collect(Collectors.toList());
        adapterItems = new ArrayAdapter<String>(requireContext(), R.layout.list_item, items);
        autoCompleteTextViewCategory.setAdapter(adapterItems);

        autoCompleteTextViewCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        Button btnSaveArticle = view.findViewById(R.id.saveArticle);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el cuadro de diálogo
                dismiss();
            }
        });

        btnSaveArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores de los TextInputEditText
                String name = Objects.requireNonNull(textInputLayoutName.getEditText()).getText().toString();
                String code = Objects.requireNonNull(textInputLayoutCode.getEditText()).getText().toString();
                String description = Objects.requireNonNull(textInputLayoutDescription.getEditText()).getText().toString();
                String selectedCategory = autoCompleteTextViewCategory.getText().toString();
                int categoryId = category.getCategoryID(selectedCategory, categoryList);

                Article newArticle = new Article();
                newArticle.setName(name);
                newArticle.setCode(code);
                newArticle.setDescription(description);
                newArticle.setCategory_id(categoryId);
                newArticle.setPhoto("");
                newArticle.setCompany_id(company_id);

                boolean createSuccessful = newArticle.createArticle(getContext());
                System.out.println(createSuccessful);

                if (createSuccessful && articleFragment != null) {
                    // La creacion fue exitosa
                    Toast.makeText(getContext(), "Artículo creado correctamente", Toast.LENGTH_SHORT).show();

                    // Actualizar la lista de artículos en el fragmento padre (ArticleFragment)
                    articleFragment.showArticles(getContext());
                } else {
                    // Ocurrió un error durante la actualización
                    Toast.makeText(getContext(), "Error al crear el artículo", Toast.LENGTH_SHORT).show();
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

//    private class ViewHolder extends RecyclerView.ViewHolder {
//
////        final TextView text;
//
//        ViewHolder(FragmentFormArticleBinding binding) {
//            super(binding.getRoot());
////            text = binding.text;/
//        }
//
//    }

//    private class ItemAdapter extends RecyclerView.Adapter<ViewHolder> {
//
//        private final int mItemCount;
//
//        ItemAdapter(int itemCount) {
//            mItemCount = itemCount;
//        }
//
//        @NonNull
//        @Override
//        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//
//            return new ViewHolder(FragmentItemListDialogListDialogItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
//
//        }
//
//        @Override
//        public void onBindViewHolder(ViewHolder holder, int position) {
////            holder.text.setText(String.valueOf(position));
//        }
//
//        @Override
//        public int getItemCount() {
//            return mItemCount;
//        }
//
//    }
}