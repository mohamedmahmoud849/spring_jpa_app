package com.vodafone.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vodafone.errorhandlling.DuplicateEntity;
import com.vodafone.errorhandlling.NotFoundException;
import com.vodafone.model.Article;
import com.vodafone.model.Author;
import com.vodafone.service.AuthorServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc

public class AuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    AuthorServiceImpl authorService;

    private String jsonObjectMapper(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
    @Test
    public void getAllAuthorsSuccessTest_returnAllAuthorsRightWithOKResponse() throws Exception {
        Author author1 = Author.builder().id(1).name("mohamed").build();
        Author author2 = Author.builder().id(2).name("hassan").build();
        Author author3 = Author.builder().id(3).name("ali").build();
        List<Author> authors = List.of(author1,author2,author3);
        when(authorService.getAllAuthors()).thenReturn(authors);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].name",equalTo(author1.getName())))
                .andExpect(jsonPath("$[1].name",equalTo(author2.getName())))
                .andExpect(jsonPath("$[2].name",equalTo(author3.getName())));
    }
    @Test
    public void getAllAuthorsFailTest_WhenNoAuthorsInDatabase_returnNotFoundException() throws Exception {
        when(authorService.getAllAuthors()).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addAuthorsSuccessTest_sendValidAndUniqueAuthor_returnAuthorRightWithOKResponse() throws Exception {
        Author author1 = Author.builder().id(1).name("mohamed").build();
        when(authorService.addAuthor(author1)).thenReturn(author1);

        mockMvc.perform(post("/authors/add").contentType(MediaType.APPLICATION_JSON).content(jsonObjectMapper(author1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",equalTo(author1.getName())));
    }
    @Test
    public void addAuthorsFailTest_sendExistingAuthor_returnDuplicateEntityException() throws Exception {
        Author author1 = Author.builder().id(1).name("mohamed").build();
        when(authorService.addAuthor(author1)).thenThrow(DuplicateEntity.class);

        mockMvc.perform(post("/authors/add").contentType(MediaType.APPLICATION_JSON).content(jsonObjectMapper(author1)))
                .andExpect(status().isBadRequest());
    }
    @Test
    public void deleteAuthorsSuccessTest_sendExistingAuthorId_returnEntityIdWithOkResponse() throws Exception {
        Author author1 = Author.builder().id(1).name("mohamed").build();
        when(authorService.deleteAuthorById(author1.getId())).thenReturn(author1.getId());

        mockMvc.perform(delete("/authors/"+author1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", equalTo(author1.getId())));
    }
    @Test
    public void deleteAuthorsFailTest_sendAuthorIdDoesNotExist_returnNotFoundException() throws Exception {
        Author author1 = Author.builder().id(2).name("mohamed").build();
        when(authorService.deleteAuthorById(author1.getId())).thenThrow(NotFoundException.class);

        mockMvc.perform(delete("/authors/"+author1.getId()))
                .andExpect(status().isNotFound());
    }
    @Test
    public void updateAuthorsSuccessTest_sendValidAndExistingAuthor_returnNewEntityWithOkResponse() throws Exception {
        Author author1 = Author.builder().id(1).name("mohamed").build();
        when(authorService.updateAuthor(author1)).thenReturn(author1);

        mockMvc.perform(put("/authors/update").contentType(MediaType.APPLICATION_JSON).content(jsonObjectMapper(author1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(author1.getId())));
    }
    @Test
    public void updateAuthorsFailTest_sendAuthorDoesNotExist_returnNotFoundException() throws Exception {
        Author author1 = Author.builder().id(1).name("mohamed").build();
        when(authorService.updateAuthor(author1)).thenThrow(NotFoundException.class);

        mockMvc.perform(put("/authors/update").contentType(MediaType.APPLICATION_JSON).content(jsonObjectMapper(author1)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAuthorByIdSuccessTest_sendAuthorIdThatExist_returnAuthorEntity() throws Exception {
        Author author1 = Author.builder().id(1).name("mohamed").build();
        when(authorService.getAuthorById(author1.getId())).thenReturn(author1);

        mockMvc.perform(get("/authors/"+author1.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",equalTo(author1.getName())));
    }
    @Test
    public void getAuthorByIdFailTest_sendAuthorIdThatDoesNotExist_returnNotFoundException() throws Exception {
        Author author1 = Author.builder().id(1).name("mohamed").build();
        when(authorService.getAuthorById(author1.getId())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/authors/"+author1.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAuthorArticlesByIdSuccessTest_sendAuthorIdThatExistAndHaveArticles_returnArticlesList() throws Exception {
        Article article1 = Article.builder().id(1).name("article1").build();
        Article article2 = Article.builder().id(2).name("article2").build();
        Article article3 = Article.builder().id(3).name("article3").build();
        List<Article> articles = List.of(article1,article2,article3);
        Author author1 = Author.builder().id(1).name("mohamed").articleList(articles).build();
        when(authorService.getAuthorArticles(author1.getId())).thenReturn(articles);

        mockMvc.perform(get("/authors/"+author1.getId()+"/articles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id",equalTo(article1.getId())))
                .andExpect(jsonPath("$[1].id",equalTo(article2.getId())))
                .andExpect(jsonPath("$[2].id",equalTo(article3.getId())));
    }
    @Test
    public void getAuthorArticlesByIdFailTest_sendAuthorIdThatDoesNotExist_returnNotFoundException() throws Exception {
        Author author1 = Author.builder().id(1).name("mohamed").build();
        when(authorService.getAuthorArticles(author1.getId())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/authors/"+author1.getId()+"/articles"))
                .andExpect(status().isNotFound());
    }






}
