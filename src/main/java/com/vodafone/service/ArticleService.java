package com.vodafone.service;

import com.vodafone.model.Article;
import com.vodafone.model.Article;

import java.util.List;

public interface ArticleService {
    Article getArticleById(Integer id);
    boolean isArticleExist(Integer id);

    boolean isArticleExistForThisUser(Article article, Integer authorId);

    Article addArticle(Article article);

    List<Article> getAllArticles();

    Integer deleteArticle(Integer id);

    Article updateArticle(Article article);
}
