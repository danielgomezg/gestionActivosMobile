package com.example.sca_app_v1.home_app.article;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.sca_app_v1.R;
import com.example.sca_app_v1.models.Article;

import java.util.List;

public class ArticleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_article, container, false);
    }

    public void showArticles(Context context) {
        System.out.println("IN SHOW ARTICLES");
        Article article = new Article();
        List<Article> articles = article.getArticles(context);
        System.out.println("Articles: " + articles);
        for (Article art : articles) {
            System.out.println(art.printData());
        }
    }

}
