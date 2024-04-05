package com.example.sca_app_v1.home_app.article;

import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.sca_app_v1.databinding.FragmentItemListDialogListDialogItemBinding;
import com.example.sca_app_v1.databinding.FragmentItemListDialogListDialogBinding;
import com.example.sca_app_v1.databinding.FragmentFormArticleBinding;

public class FormCreateArticle extends BottomSheetDialogFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_ITEM_COUNT = "item_count";
    private FragmentFormArticleBinding binding;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        final RecyclerView recyclerView = (RecyclerView) view;
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(new ItemAdapter(5));
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