package com.example.sca_app_v1.home_app.active;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sca_app_v1.R;
import com.example.sca_app_v1.home_app.article.ArticleFragment;
import com.example.sca_app_v1.home_app.article.DialogFragmentArticle;
import com.example.sca_app_v1.home_app.article.FormCreateArticle;
import com.example.sca_app_v1.models.Active;
import com.example.sca_app_v1.models.Article;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ActiveFragment  extends Fragment {

    private List<Active> actives;
    private RecyclerView activeList;
    private AdapterActive adapterActive;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("active fragment oncreate view");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active, container, false);
        activeList = view.findViewById(R.id.list_actives);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        activeList.setLayoutManager(linearLayoutManager);
        adapterActive = new AdapterActive();
        activeList.setAdapter(adapterActive);

        FloatingActionButton fabAddArticle = view.findViewById(R.id.fab_add_active);
        fabAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormCreateActive bottomSheet = new FormCreateActive();
                bottomSheet.setActiveFragment(ActiveFragment.this);
                bottomSheet.show(getChildFragmentManager(), "formCreateActive");
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showActives(getContext());
    }

    public void showActives(Context context) {
        System.out.println("IN SHOW ACTIVES");
        Active active = new Active();
        actives = active.getActives(context);
        System.out.println("articles size: " + actives.size());
        adapterActive.notifyItemRangeInserted(actives.size(), 1);
    }

    public void updateActives(Context context, int position) {
        System.out.println("IN UPDATE SHOW ACTIVES");
        Active active = new Active();
        actives = active.getActives(context);
        adapterActive.notifyItemChanged(position);
    }

    private class AdapterActive extends RecyclerView.Adapter<AdapterActive.AdapterActiveHolder> {

        @NonNull
        @Override
        public AdapterActiveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdapterActiveHolder(getLayoutInflater().inflate(R.layout.active_card, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterActiveHolder holder, int position) {
            System.out.println("on binding view holder");
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            // System.out.println("get item count");
            return actives != null ? actives.size() : 0;
        }

        class AdapterActiveHolder extends RecyclerView.ViewHolder {

            TextView tvBarcode;
            TextView tvModel;

            TextView tvSerie;
            ImageView ivPhoto;

            ImageButton btnOptions;

            public AdapterActiveHolder(@NonNull View itemView) {
                super(itemView);

                tvBarcode = itemView.findViewById(R.id.tvBarcodeActive);
                tvModel = itemView.findViewById(R.id.tvModelActive);
                tvSerie = itemView.findViewById(R.id.tvSerieActive);

                btnOptions = itemView.findViewById(R.id.btnOptions);
//                ivPhoto = itemView.findViewById(R.id.ivPhoto);

                btnOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showMenu(getAdapterPosition());
                    }
                });
            }

            public void imprimir(int position) {
                System.out.println("Inprmir posicion " + position);
                System.out.println("actives size imp: " + actives.size());
                Active active = actives.get(position);
                System.out.println(active);
                System.out.println(active.getBar_code());
                System.out.println(active.getModel());

                tvBarcode.setText(active.getBar_code());
                tvModel.setText(active.getModel());
                tvSerie.setText(active.getSerie());
            }

            // Método para mostrar el menú contextual
            private void showMenu(int position) {
                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), btnOptions);
                popupMenu.inflate(R.menu.article_options_menu);
                System.out.println("position");
                System.out.println(position);

                // Referencia al ArticleFragment
                ActiveFragment activeFragment = ActiveFragment.this;

                // Obtener el artículo seleccionado
                Active active = actives.get(position);

                // Establecer el listener de clic para los elementos del menú
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        System.out.println("id " + id);
                        if (id == R.id.edit_option_active) {
                            // Acción para editar el artículo
                            Toast.makeText(itemView.getContext(), "Editar activo seleccionado " + active.getBar_code(), Toast.LENGTH_SHORT).show();
                            // Crear una instancia del DialogFragment y pasar una referencia al fragmento padre (ArticleFragment)
                            //DialogFragmentArticle editDialog = DialogFragmentArticle.newInstance(DialogFragmentArticle.MODE_EDIT, position, active, activeFragment);
                            //editDialog.show(requireActivity().getSupportFragmentManager(), "edit_article_dialog");
                            return true;
                        } else if (id == R.id.delete_option_active) {
                            // Acción para eliminar el artículo
                            Toast.makeText(itemView.getContext(), "Eliminar artículo seleccionado " + active.getBar_code(), Toast.LENGTH_SHORT).show();
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                // Mostrar el menú contextual
                popupMenu.show();
            }
        }

    }
}
