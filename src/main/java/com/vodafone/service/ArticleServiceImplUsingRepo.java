package com.vodafone.service;

import java.util.ArrayList;
import java.util.List;

import com.vodafone.errorhandlling.DuplicateEntity;
import com.vodafone.errorhandlling.IncompleteRequest;
import com.vodafone.repo.AuthorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vodafone.contoller.ArticlesController;
import com.vodafone.contoller.AuthorController;
import com.vodafone.errorhandlling.NotFoundException;
import com.vodafone.model.*;
import com.vodafone.repo.ArticleRepo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Primary
@Transactional
@RequiredArgsConstructor
public class ArticleServiceImplUsingRepo implements ArticleService
{
    private final ArticleRepo articleRepo;
    private final AuthorService authorService;
    @Override
    public Article getArticleById(Integer id) {
        Article article = articleRepo.findById(id).orElseThrow(()->new NotFoundException("this article id not found"));
        addArticleLinks(article);
        return article;
    }

    @Override
    public Article isArticleExist(Integer id) {
        return articleRepo.findById(id).orElse(null);
    }

    @Override
    public Article addArticle(Article article) {
        if(article.getAuthor()==null){
            throw new IncompleteRequest("please Enter auther details");
        }else{
            if (authorService.isAuthorExist(article.getAuthor().getId()) == null){
                throw new NotFoundException("There's no such author");
            }
        }
        if(isArticleExist(article.getId()) != null){
            throw new DuplicateEntity("This Entity Already in our DataBase ");
        }else{

            return articleRepo.save(article);
        }
    }

    @Override
    public List<Article> getAllArticles() {
        List<Article> articles = articleRepo.findAll();
        if(articles.isEmpty()){
            throw  new NotFoundException("No Authors Found");
        }else{
            for(var article : articles)
            {
                addArticleLinks(article);
            }
            return articles;
        }
    }

    @Override
    public Integer deleteArticle(Integer id) {
        getArticleById(id);
        articleRepo.deleteById(id);
        return id;
    }

    @Override
    public Article updateArticle(Article article) {
        Article existingArticle = isArticleExist(article.getId());
        if(existingArticle != null){
            article.setAuthor(existingArticle.getAuthor());
            return articleRepo.save(article);
        }else{
            throw new NotFoundException("There's no user with such id");
        }
    }

    private void addArticleLinks(Article article)
    {
        List<Links> links = new ArrayList<>();
        Links self = new Links();

        Link selfLink = linkTo(methodOn(ArticlesController.class)
                .getArticle(article.getId())).withRel("self");

        self.setRel("self");
        self.setHref(selfLink.getHref());

        Links authorLink = new Links();
        Link authLink = linkTo(methodOn(AuthorController.class)
                .getAuthorById(article.getAuthor().getId())).withRel("author");
        authorLink.setRel("author");
        authorLink.setHref(authLink.getHref());

        links.add(self);
        links.add(authorLink);
        article.setLinks(links);
    }
}
