package com.vodafone.service;

import com.vodafone.contoller.ArticlesController;
import com.vodafone.contoller.AuthorController;
import com.vodafone.errorhandlling.DuplicateEntity;
import com.vodafone.errorhandlling.NotFoundException;
import com.vodafone.model.Article;
import com.vodafone.model.Links;
import com.vodafone.repo.ArticleRepo;
import lombok.RequiredArgsConstructor;
import org.hibernate.id.IntegralDataTypeHolder;
import org.springframework.context.annotation.Primary;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vodafone.model.Author;
import com.vodafone.repo.AuthorRepo;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@Primary
@RequiredArgsConstructor
@Transactional
public class AuthorServiceImplUsingRepo implements AuthorService
{
    private final AuthorRepo authorRepo;
    private final ArticleRepo articleRepo;


    @Override
    public List<Author> getAllAuthors()
    {
        List<Author> authors = authorRepo.findAll();
        if(authors.isEmpty()){
            throw  new NotFoundException("No Authors Found");
        }else{
            for(var author : authors)
            {
                addAuthorLinks(author);
            }
            return authors;
        }
    }
    @Override
    public Author getAuthorById(Integer id) 
    {
        Author author = authorRepo.findById(id).orElseThrow(()->new NotFoundException("this author id not found"));
        addAuthorLinks(author);
        return author;
    }
    @Override
    public Author addAuthor(Author author)
    {
        if(isAuthorExist(author.getId()) != null){
            throw new DuplicateEntity("This Entity Already in our DataBase ");
        }else{

            return authorRepo.save(author);
        }

    }
    @Override
    public Integer deleteAuthorById(Integer id)
    {
        getAuthorById(id);
        authorRepo.deleteById(id);
        return id;
    }
    @Override
    public Author updateAuthor(Author author)
    {
        if(isAuthorExist(author.getId()) != null){
            return authorRepo.save(author);
        }else{
            throw new NotFoundException("There's no user with such id");
        }
    }
    @Override
    public List<Article> getAuthorArticles(Integer id)
    {
//        checking if there's author with that id or not
        if(isAuthorExist(id) != null){
            var articles = articleRepo.findAllByAuthor_Id(id);
//            if author exist check if he has articles or not
            if(articles.isEmpty()){
                throw  new NotFoundException("There's no articles for this author yet");
            }else{
                for(var article : articles)
                {
                    addArticlesLinks(article);
                }
                return articles;
            }
        }else{
            throw  new NotFoundException("this author not found");
        }
    }
    @Override
    public Author isAuthorExist(Integer id)
    {
        return authorRepo.findById(id).orElse(null);
    }
    private void addArticlesLinks(Article article)
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
    private void addAuthorLinks(Author author)
    {
        List<Links> links = new ArrayList<>();
        Link selfLink = linkTo(methodOn(AuthorController.class)
                .getAuthorById(author.getId())).withRel("self");
        links.add(Links.builder().rel("self").href(selfLink.getHref()).build());
        Link updateLink = linkTo(methodOn(AuthorController.class)
                .updateAuthor(author)).withRel("update");
        links.add(Links.builder().rel("update").href(updateLink.getHref()).build());
        Link articlesLink = linkTo(methodOn(AuthorController.class)
                .getAutherArticles(author.getId())).withRel("articles");
        links.add(Links.builder().rel("articles").href(articlesLink.getHref()).build());
        author.setLinks(links);
    }



}
