package com.example.sca_app_v1.home_app.article;

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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.sca_app_v1.MainActivity;
import com.example.sca_app_v1.R;
import com.example.sca_app_v1.models.Article;
import com.example.sca_app_v1.home_app.article.DialogFragmentArticle;
import com.example.sca_app_v1.models.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ArticleFragment extends Fragment {

    private List<Article> articles;
    private RecyclerView articleList;
    private AdapterArticle adapterArticle;

    private Integer offset = 0;

    // Referencia al ArticleFragment
    public ArticleFragment articleFragment = ArticleFragment.this;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("article fragment oncreate view");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        articleList = view.findViewById(R.id.list_articles);

        articleList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (layoutManager != null) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        // Llegamos al final del RecyclerView, cargar más datos aquí
                        System.out.println("Se llega al final del scroll");
                    }
                }
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        articleList.setLayoutManager(linearLayoutManager);
        adapterArticle = new AdapterArticle();
        articleList.setAdapter(adapterArticle);




        FloatingActionButton fabAddArticle = view.findViewById(R.id.fab_add_article);
        fabAddArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragmentArticle createDialog = DialogFragmentArticle.newInstance(DialogFragmentArticle.MODE_CREATE, articleFragment);
                createDialog.show(requireActivity().getSupportFragmentManager(), "create_article_dialog");
                //FormCreateArticle bottomSheet = new FormCreateArticle();
                //bottomSheet.setArticleFragment(ArticleFragment.this);
                //bottomSheet.show(getChildFragmentManager(), "formCreateArticle");
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showArticles(getContext());

    }

    public void showAlert(String msg) {
        System.out.println("SHOW ALERT FRANGEMENT ARTICLE");
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showArticles(Context context) {
        System.out.println("IN SHOW ARTICLES");
        Article article = new Article();
        articles = article.getArticles(context, offset);
        System.out.println("articles size: " + articles.size());

        adapterArticle.notifyDataSetChanged();
    }

    public void updateArticles(Context context, int position) {
        System.out.println("IN UPDATE SHOW ARTICLES");
        Article article = new Article();
        articles = article.getArticles(context, offset);
        adapterArticle.notifyItemChanged(position);
    }

    private class AdapterArticle extends RecyclerView.Adapter<AdapterArticle.AdapterArticleHolder> {

        @NonNull
        @Override
        public AdapterArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdapterArticleHolder(getLayoutInflater().inflate(R.layout.article_card, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdapterArticleHolder holder, int position) {
            System.out.println("on binding view holder");
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            // System.out.println("get item count");
            return articles != null ? articles.size() : 0;
        }

        class AdapterArticleHolder extends RecyclerView.ViewHolder {

            TextView tvName;
            TextView tvCode;

            TextView tvDescription;
            ImageView ivPhoto;

            TextView tvCountActive;

            TextView tvCategory;

            ImageButton btnOptions;

            public AdapterArticleHolder(@NonNull View itemView) {
                super(itemView);

                tvName = itemView.findViewById(R.id.tvNameArticle);
                tvCode = itemView.findViewById(R.id.tvCodeArticle);
                tvDescription = itemView.findViewById(R.id.tvdescriptionArticle);
                tvCountActive = itemView.findViewById(R.id.tvCountActive);
                tvCategory = itemView.findViewById(R.id.tvCategory);

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
                System.out.println("articles size imp: " + articles.size());
                Article article = articles.get(position);
                System.out.println(article);
                System.out.println(article.getName());
                System.out.println(article.getCode());

                Category category = new Category();
                category = category.getCategoryById(itemView.getContext(), article.getCategory_id());

                tvName.setText(article.getName());
                tvDescription.setText(article.getDescription());
                tvCode.setText(article.getCode());
                System.out.println("count active " + article.getCount_active());
                tvCountActive.setText(article.getCount_active().toString());
                tvCategory.setText(category.getDescription());
            }

            // Método para mostrar el menú contextual
            private void showMenu(int position) {
                PopupMenu popupMenu = new PopupMenu(itemView.getContext(), btnOptions);
                popupMenu.inflate(R.menu.article_options_menu);
                System.out.println("position");
                System.out.println(position);

                // Obtener el artículo seleccionado
                Article article = articles.get(position);

                // Establecer el listener de clic para los elementos del menú
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        System.out.println("id");
                        System.out.println(id);
                        if (id == R.id.edit_option_article) {
                            // Acción para editar el artículo
                            Toast.makeText(itemView.getContext(), "Editar artículo seleccionado " + article.getName(), Toast.LENGTH_SHORT).show();
                            // Crear una instancia del DialogFragment y pasar una referencia al fragmento padre (ArticleFragment)
                            DialogFragmentArticle editDialog = DialogFragmentArticle.newInstance(DialogFragmentArticle.MODE_EDIT, position, article, articleFragment);
                            editDialog.show(requireActivity().getSupportFragmentManager(), "edit_article_dialog");
                            return true;
                        } else if (id == R.id.delete_option_article) {
                            // Acción para eliminar el artículo
                            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                            builder.setMessage("¿Está seguro que desea eliminar el artículo " + article.getName() +"?")
                                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // Si el usuario confirma la eliminación
                                            boolean success;
                                            if (article.getSync().equals(1)) {
                                                success = article.deleteArticleLocal(getContext());
                                            } else {
                                                success = article.deleteArticle(getContext());
                                            }
                                            showArticles(getContext());
                                            if (success) {
                                                Toast.makeText(itemView.getContext(), "Artículo eliminado correctamente", Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(itemView.getContext(), "Error al eliminar articulo", Toast.LENGTH_SHORT).show();

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
