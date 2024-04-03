package com.example.sca_app_v1.home_app.article;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

public class ArticleFragment extends Fragment {

    private List<Article> articles;
    private RecyclerView articleList;
    private AdapterArticle adapterArticle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("article fragment oncreate view");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        articleList = view.findViewById(R.id.list_articles);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        articleList.setLayoutManager(linearLayoutManager);
        adapterArticle = new AdapterArticle();
        articleList.setAdapter(adapterArticle);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showArticles(getContext());

    }

    public void showArticles(Context context) {
        System.out.println("IN SHOW ARTICLES");
        Article article = new Article();
        articles = article.getArticles(context);
        System.out.println("articles size: " + articles.size());
        for (Article a : articles) {
            System.out.println(a);
            adapterArticle.notifyItemRangeInserted(articles.size(), 1);
        }

//        adapterArticle.notifyDataSetChanged();
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
            System.out.println("get item count");
            return articles != null ? articles.size() : 0;
        }

        class AdapterArticleHolder extends RecyclerView.ViewHolder {

            TextView tvName;
            TextView tvCode;

            TextView tvDescription;
            ImageView ivPhoto;

            ImageButton btnOptions;

            public AdapterArticleHolder(@NonNull View itemView) {
                super(itemView);

                tvName = itemView.findViewById(R.id.tvNameArticle);
                tvCode = itemView.findViewById(R.id.tvCodeArticle);
                tvDescription = itemView.findViewById(R.id.tvdescriptionArticle);

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

                tvName.setText(article.getName());
                tvDescription.setText(article.getDescription());
                tvCode.setText(article.getCode());
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
                            DialogFragmentArticle editDialog = DialogFragmentArticle.newInstance(DialogFragmentArticle.MODE_EDIT);
                            editDialog.show(requireActivity().getSupportFragmentManager(), "edit_article_dialog");
                            return true;
                        } else if (id == R.id.delete_option_article) {
                            // Acción para eliminar el artículo
                            Toast.makeText(itemView.getContext(), "Eliminar artículo seleccionado " + article.getName(), Toast.LENGTH_SHORT).show();
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
