package com.vodafone.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vodafone.errorhandlling.NotFoundException;
import com.vodafone.model.Article;
import com.vodafone.model.Author;
import com.vodafone.service.ArticleServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ArticlesControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    ArticleServiceImpl articleService;


    private String jsonObjectMapper(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    @Test
    public void getAllArticlesSuccessTest_returnAllArticlesRightWithOKResponse() throws Exception {
        Article article1 = Article.builder().id(1).name("article1").build();
        Article article2 = Article.builder().id(2).name("article2").build();
        Article article3 = Article.builder().id(3).name("article3").build();
        List<Article> articles = List.of(article1,article2,article3);
        when(articleService.getAllArticles()).thenReturn(articles);

        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].name",equalTo(article1.getName())))
                .andExpect(jsonPath("$[1].name",equalTo(article2.getName())))
                .andExpect(jsonPath("$[2].name",equalTo(article3.getName())));
    }
    @Test
    public void getAllAuthorsFailTest_WhenNoAuthorsInDatabase_returnNotFoundException() throws Exception {
        when(articleService.getAllArticles()).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/articles"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getArticleSuccessTest_sendValidAndUniqueArticleWithValidAndExistingAuthor_returnArticleRightWithOKResponse() throws Exception {
        Author author = Author.builder().id(1).name("mohamed").build();
        Article article1 = Article.builder().id(1).name("article1").author(author).build();
        when(articleService.getArticleById(article1.getId())).thenReturn(article1);


        mockMvc.perform(get("/articles/"+article1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",equalTo(article1.getName())));
    }
    @Test
    public void getArticleFailTest_sendArticleIdThatDoesNotExist_returnNotFoundException() throws Exception {
        Author author = Author.builder().id(1).name("mohamed").build();
        Article article1 = Article.builder().id(1).name("article1").author(author).build();
        when(articleService.getArticleById(article1.getId())).thenThrow(NotFoundException.class);


        mockMvc.perform(get("/articles/"+article1.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteArticleSuccessTest_sendValidAndUniqueArticleWithValidAndExistingAuthor_returnArticleRightWithOKResponse() throws Exception {
        Author author = Author.builder().id(1).name("mohamed").build();
        Article article1 = Article.builder().id(1).name("article1").author(author).build();
        when(articleService.deleteArticle(article1.getId())).thenReturn(article1.getId());


        mockMvc.perform(delete("/articles/"+article1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",equalTo(article1.getId())));
    }
    @Test
    public void deleteArticleFailTest_sendArticleIdThatDoesNotExist_returnNotFoundException() throws Exception {
        Author author = Author.builder().id(1).name("mohamed").build();
        Article article1 = Article.builder().id(1).name("article1").author(author).build();
        when(articleService.deleteArticle(article1.getId())).thenThrow(NotFoundException.class);


        mockMvc.perform(delete("/articles/"+article1.getId()))
                .andExpect(status().isNotFound());
    }

//    @Test
//    public void addArticleSuccessTest_sendValidAndUniqueArticleWithValidAndExistingAuthor_returnArticleRightWithOKResponse() throws Exception {
//        Author author = Author.builder().id(1).name("mohamed").build();
//        Article article1 = Article.builder().id(1).name("article1").author(author).build();
//
//        when(articleService.addArticle(article1)).thenReturn(article1);
//
//
//        mockMvc.perform(post("/articles/add").contentType(MediaType.APPLICATION_JSON).content(jsonObjectMapper(article1)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name",equalTo(article1.getName())));
//    }
//
//    @Test
//    public void addArticleFailTest_sendValidArticleButAuthorDoesNotExist_returnNotFoundException() throws Exception {
//        Author author = Author.builder().id(1).name("mohamed").build();
//        Article article1 = Article.builder().id(1).name("article1").author(author).build();
//        when(articleService.addArticle(article1)).thenThrow(NotFoundException.class);
//
//        mockMvc.perform(post("/articles/add").contentType(MediaType.APPLICATION_JSON).content(jsonObjectMapper(article1)))
//                .andExpect(status().isNotFound());
//    }
//    @Test
//    public void addArticleFailTest_sendValidArticleButWithNoAuthorData_returnIncompleteRequestException() throws Exception {
//        Article article1 = Article.builder().id(1).name("article1").build();
//        when(articleService.addArticle(article1)).thenThrow(IncompleteRequest.class);
//
//        mockMvc.perform(post("/articles/add").contentType(MediaType.APPLICATION_JSON).content(jsonObjectMapper(article1)))
//                .andExpect(status().isBadRequest());
//    }
//    @Test
//    public void addArticleFailTest_sendExistingArticleForThisAuthor_returnDuplicateEntityException() throws Exception {
//        Author author = Author.builder().id(1).name("mohamed").build();
//        Article article1 = Article.builder().id(1).name("article1").author(author).build();
//        when(articleService.addArticle(article1)).thenThrow(DuplicateEntity.class);
//        when(articleService.isArticleExistForThisUser(article1,author.getId())).thenReturn(true);
//
//        mockMvc.perform(post("/articles/add").contentType(MediaType.APPLICATION_JSON).content(jsonObjectMapper(article1)))
//                .andExpect(status().isBadRequest());
//    }

    //    @Test
//    public void updateArticleSuccessTest_sendValidAndUniqueArticleWithValidAndExistingAuthor_returnArticleRightWithOKResponse() throws Exception {
//        Author author = Author.builder().id(1).name("mohamed").build();
//        Article article1 = Article.builder().id(1).name("article1").author(author).build();
//
//        when(articleService.updateArticle(article1)).thenReturn(article1) ;
//
//
//        mockMvc.perform(put("/articles/update").contentType(MediaType.APPLICATION_JSON).content(jsonObjectMapper(article1)))
//                .andExpect(status().isOk()).andExpect(jsonPath("$.name",equalTo(article1.getName())))
//                .andReturn();
//    }
}
