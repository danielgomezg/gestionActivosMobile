package com.example.sca_app_v1.home_app.article;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sca_app_v1.R;
import com.example.sca_app_v1.models.Article;

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
            ImageView ivPhoto;

            public AdapterArticleHolder(@NonNull View itemView) {
                super(itemView);

                tvName = itemView.findViewById(R.id.tvNameArticle);
                tvCode = itemView.findViewById(R.id.tvCodeArticle);
//                ivPhoto = itemView.findViewById(R.id.ivPhoto);
            }

            public void imprimir(int position) {
                System.out.println("Inprmir posicion " + position);
                System.out.println("articles size imp: " + articles.size());
                Article article = articles.get(position);
                System.out.println(article);
                System.out.println(article.getName());
                System.out.println(article.getCode());

                tvName.setText(article.getName());
                tvCode.setText(article.getCode());
            }
        }

    }
}
