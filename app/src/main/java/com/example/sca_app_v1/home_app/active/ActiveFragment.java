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
    private Integer offset = 0;
    private Integer limit  = 8;
    private Integer count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("active fragment oncreate view");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active, container, false);
        activeList = view.findViewById(R.id.list_actives);

        count = Active.getActivesCount(getContext()); //Article.getArticlesCount(getContext());
        offset = 0;
        activeList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                    System.out.println("visibleItemCount > " + visibleItemCount);
                    System.out.println("totalItemCount > " + totalItemCount);
                    System.out.println("firstVisibleItemPosition > " + firstVisibleItemPosition);
                    System.out.println("COUNT " + count);
                    System.out.println("offset " + offset);

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0 && totalItemCount < count) {
                        // Llegamos al final del RecyclerView, cargar más datos aquí
                        System.out.println("Se llega al final del scroll");
                        offset += limit;

                        //    showArticles(getContext());
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                showArticlesScroll(getContext());
                            }
                        });
                    }
                    else {
                        System.out.println("scrolling...");
                    }
                }
            }
        });

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

    //AÑADIR A ACTIVOS A LA LISTA ACTIVES CUANDO SE LLEGA AL FIN DEL SCROLL
    public void showArticlesScroll(Context context) {
        System.out.println("IN SHOW ACTIVES");
        Active active = new Active();
        List<Active> newActives = active.getActives(context, offset, limit);
        System.out.println("actives size: " + actives.size());
        if (newActives != null && !newActives.isEmpty()) {
            actives.addAll(newActives);
            adapterActive.notifyDataSetChanged();
        }
    }

    public void showActives(Context context) {
        System.out.println("IN SHOW ACTIVES");
        Active active = new Active();
        actives = active.getActives(context, offset, limit);
        System.out.println("articles size: " + actives.size());
        adapterActive.notifyDataSetChanged();
    }

    public void updateActives(Context context, int position) {
        System.out.println("IN UPDATE SHOW ACTIVES");
        Active active = new Active();
        Active activeList = actives.get(position);
        Active activeupdated = active.getActiveById(context, activeList.getId());
        actives.set(position, activeupdated);
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
                System.out.println("------");
                System.out.println("barcode ===> " + active.getBar_code());
                System.out.println("model ===> " + active.getModel());
                System.out.println("virtualcode ==> " + active.getVirtual_code());

                if (active.getVirtual_code().isEmpty() || active.getVirtual_code().equals("false"))
                    tvBarcode.setText(active.getBar_code());
                else if (active.getVirtual_code().equals("true"))
                    tvBarcode.setText("codigo virtual por generar");
                else
                    tvBarcode.setText(active.getVirtual_code() + "(virtual)");

                System.out.println("Codigo virtual " + active.getVirtual_code());

                tvModel.setText(active.getModel());
                tvSerie.setText(active.getSerie());
                tvActiveDate.setText(active.getAcquisition_date());
                Office office = new Office();
                office = office.getOfficeId(itemView.getContext(), active.getOffice_id());
                if (office != null){
                    tvActiveOffice.setText(office.getFullName());
                } else {
                    tvActiveOffice.setText("");
                }
                activeState.setText(active.getState());
                tvActiveComment.setText(active.getComment());
                Article article = new Article();
                article = article.getArticleById(itemView.getContext(), active.getArticle_id());
                if (article != null) {
                    tvActiveArticle.setText(article.getName());
                } else {
                    tvActiveArticle.setText("");
                }
                tvActiveBrand.setText(active.getBrand());
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
                            //Toast.makeText(itemView.getContext(), "Editar activo seleccionado " + active.getBar_code(), Toast.LENGTH_SHORT).show();
                            // Crear una instancia del DialogFragment y pasar una referencia al fragmento padre (ArticleFragment)
                            DialogFragmentActive editDialog = DialogFragmentActive.newInstance(DialogFragmentActive.MODE_EDIT ,position, active, activeFragment);
                            editDialog.show(requireActivity().getSupportFragmentManager(), "edit_active_dialog");
                            return true;
                        } else if (id == R.id.delete_option_active) {
                            String codigo = active.getBar_code().equals("") ? active.getVirtual_code() : active.getBar_code();
                            // Acción para eliminar el activo
                            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                            builder.setMessage("¿Está seguro que desea eliminar el activo " + codigo +"?")
                                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // Si el usuario confirma la eliminación
                                            boolean success;
                                            if (active.getSync().equals(1)) {
                                                success = active.deleteActiveLocal(getContext(), active.getPhoto1(), active.getPhoto2(), active.getPhoto3(), active.getPhoto4());
                                            } else {
                                                success = active.deleteActive(getContext());
                                            }
                                            showActives(getContext());
                                            if (success) {
                                                Toast.makeText(itemView.getContext(), "Activo eliminado correctamente", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(itemView.getContext(), "Error al eliminar activo", Toast.LENGTH_SHORT).show();

                                            }
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
