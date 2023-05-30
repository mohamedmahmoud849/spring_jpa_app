package com.vodafone.service;

import java.util.ArrayList;
import java.util.List;

import com.vodafone.errorhandlling.DuplicateEntity;
import com.vodafone.errorhandlling.IncompleteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vodafone.controller.ArticlesController;
import com.vodafone.controller.AuthorController;
import com.vodafone.errorhandlling.NotFoundException;
import com.vodafone.model.*;
import com.vodafone.repo.ArticleRepo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Primary
@Transactional
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService
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
    public boolean isArticleExist(Integer id) {
        return articleRepo.existsById(id);
    }
    @Override
    public boolean isArticleExistForThisUser(Article article, Integer authorId) {
        List<Article> articlesList = articleRepo.findAllByAuthor_Id(authorId);

        return articlesList.stream().anyMatch(x->x.getId() == article.getId());
    }

    @Override
    public Article addArticle(Article article) {
        if(article.getAuthor()==null){
            throw new IncompleteRequest("please Enter auther details");
        }else{
            if (!authorService.isAuthorExist(article.getAuthor().getId())){
                throw new NotFoundException("There's no such author");
            }
        }
        if(isArticleExistForThisUser(article,article.getAuthor().getId())){
            throw new DuplicateEntity("This Entity Already Found ");
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

        if(isArticleExist(article.getId())){
            Article existingArticle = articleRepo.findById(article.getId()).get();
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
