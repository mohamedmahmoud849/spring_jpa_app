package com.vodafone.service;

import com.vodafone.errorhandlling.IncompleteRequest;
import com.vodafone.errorhandlling.NotFoundException;
import com.vodafone.model.Article;
import com.vodafone.model.Author;
import com.vodafone.repo.ArticleRepo;
import com.vodafone.repo.AuthorRepo;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceImplTest {
    @Mock
    ArticleRepo articleRepo;
    @Mock
    AuthorRepo authorRepo;
    ArticleServiceImpl articleService;
    @BeforeAll
    public void initTest(){
        articleService = new ArticleServiceImpl(articleRepo,new AuthorServiceImpl(authorRepo,articleRepo));
    }

    @Test
    @DisplayName("addArticleTestSuccess")
    public void addArticleTest_sendValidArticleWithExistingAuthorDetails_returnInstanceOfArticle(){
        Article article1 = Article.builder().id(1).name("article1").author(Author.builder().id(1).build()).build();

        when(authorRepo.existsById(1)).thenReturn(true);
        when(articleRepo.existsById(1)).thenReturn(false);
        when(articleRepo.save(article1)).thenReturn(article1);

        assertNotNull(articleService.addArticle(article1));
    }
    @Test
    @DisplayName("addArticleWithoutAuthorTestFail")
    public void addArticleTest_sendArticleWithoutExistingAuthorDetails_returnIncompleteRequestException(){
        Article article1 = Article.builder().id(1).name("article1").build();

        assertThrows(IncompleteRequest.class,()->articleService.addArticle(article1));
    }
    @Test
    @DisplayName("addArticleWithNotExistingAuthorTestFail")
    public void addArticleTest_sendArticleWithNotExistingAuthorDetails_returnNotFoundException(){
        Article article1 = Article.builder().id(1).name("article1").author(Author.builder().id(1).build()).build();

        when(authorRepo.existsById(1)).thenReturn(false);

        assertThrows(NotFoundException.class,()->articleService.addArticle(article1));
    }
    @Test
    @DisplayName("addArticleExistInAuthorArticlesTestFail")
    public void addArticleTest_sendArticleExistInExistingAuthorDetails_returnDuplicateEntityException(){
        Article article1 = Article.builder().id(1).name("article1").author(Author.builder().id(1).build()).build();

        when(authorRepo.existsById(1)).thenReturn(true);
        when(articleRepo.existsById(1)).thenReturn(true);


        assertThrows(NotFoundException.class,()->articleService.addArticle(article1));
    }
    @Test
    public void getAllArticlesTest(){
        Article article1 = Article.builder().id(1).name("article1").author(Author.builder().id(1).build()).build();
        Article article2 = Article.builder().id(2).name("article2").author(Author.builder().id(1).build()).build();
        Article article3 = Article.builder().id(3).name("article3").author(Author.builder().id(1).build()).build();
        List<Article> articles = List.of(article1,article2,article3);
        when(articleRepo.findAll()).thenReturn(articles);
        assertEquals(3,articleService.getAllArticles().size());
    }

}
