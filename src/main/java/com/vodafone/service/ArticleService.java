package com.vodafone.service;

import com.vodafone.model.Article;
import com.vodafone.model.Article;

import java.util.List;

public interface ArticleService {
    Article getArticleById(Integer id);
    Article isArticleExist(Integer id);

    Article addArticle(Article article);

    List<Article> getAllArticles();

    Integer deleteArticle(Integer id);

    Article updateArticle(Article article);
}
