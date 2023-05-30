package com.vodafone.service;

import com.vodafone.model.Article;
import com.vodafone.model.Author;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AuthorService {
    Author getAuthorById(Integer id);

    boolean isAuthorExist(Integer id);

    Author addAuthor(Author author);

    List<Author> getAllAuthors();

    List<Article> getAuthorArticles(Integer id);

    Integer deleteAuthorById(Integer id);

    Author updateAuthor(Author author);
}
