package com.example.sca_app_v1.home_app.active;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.sca_app_v1.home_app.article.DialogFragmentArticle;
import com.example.sca_app_v1.models.Active;
import com.example.sca_app_v1.models.Article;
import com.example.sca_app_v1.models.Office;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ActiveFragment  extends Fragment {

    private List<Active> actives;
    private RecyclerView activeList;
    private AdapterActive adapterActive;
    // Referencia al ArticleFragment
    ActiveFragment activeFragment = ActiveFragment.this;

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
                DialogFragmentActive createDialog = DialogFragmentActive.newInstance(DialogFragmentActive.MODE_CREATE, activeFragment);
                createDialog.show(requireActivity().getSupportFragmentManager(), "create_active_dialog");
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
        adapterActive.notifyDataSetChanged();
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
            TextView tvActiveDate;
            TextView tvActiveOffice;
            TextView activeState;
            TextView tvActiveComment;
            TextView tvActiveArticle;
            TextView tvActiveBrand;
            TextView tvActiveNameCharge;
            TextView tvActiveRutCharge;
            ImageView ivPhoto;

            ImageButton btnOptions;

            public AdapterActiveHolder(@NonNull View itemView) {
                super(itemView);

                tvBarcode = itemView.findViewById(R.id.tvBarcodeActive);
                tvModel = itemView.findViewById(R.id.tvModelActive);
                tvSerie = itemView.findViewById(R.id.tvSerieActive);
                tvActiveDate = itemView.findViewById(R.id.tvActiveDate);
                tvActiveOffice = itemView.findViewById(R.id.tvActiveOffice);
                activeState = itemView.findViewById(R.id.tvActiveState);
                tvActiveComment = itemView.findViewById(R.id.tvActiveComment);
                tvActiveArticle = itemView.findViewById(R.id.tvActiveArticle);
                tvActiveBrand = itemView.findViewById(R.id.tvActiveBrand);
                tvActiveNameCharge = itemView.findViewById(R.id.tvActiveNameCharge);
                tvActiveRutCharge = itemView.findViewById(R.id.tvActiveRutCharge);



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
                tvActiveDate.setText(active.getAcquisition_date());
                Office office = new Office();
                office = office.getOfficeId(itemView.getContext(), active.getOffice_id());
                tvActiveOffice.setText(office.getFullName());
                activeState.setText(active.getState());
                tvActiveComment.setText(active.getComment());
                Article article = new Article();
                article = article.getArticleById(itemView.getContext(), active.getArticle_id());
                tvActiveArticle.setText(article.getName());
                tvActiveBrand.setText("Brand");
                tvActiveNameCharge.setText(active.getName_in_charge_active());
                tvActiveRutCharge.setText(active.getRut_in_charge_active());

            }

            // Método para mostrar el menú contextual
            private void showMenu(int position) {
                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), btnOptions);
                popupMenu.inflate(R.menu.active_options_menu);
                System.out.println("position");
                System.out.println(position);

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
                            DialogFragmentActive editDialog = DialogFragmentActive.newInstance(DialogFragmentActive.MODE_EDIT ,position, active, activeFragment);
                            editDialog.show(requireActivity().getSupportFragmentManager(), "edit_active_dialog");
                            return true;
                        } else if (id == R.id.delete_option_active) {
                            // Acción para eliminar el activo
                            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                            builder.setMessage("¿Está seguro que desea eliminar el activo " + active.getBar_code() +"?")
                                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // Si el usuario confirma la eliminación
                                            active.deleteActive(getContext());
                                            showActives(getContext());
                                            Toast.makeText(itemView.getContext(), "Activo eliminado correctamente", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                        }
                                    });
                            // Crea y muestra el cuadro de diálogo de confirmación
                            AlertDialog dialog = builder.create();
                            dialog.show();
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
