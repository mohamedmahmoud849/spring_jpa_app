package com.vodafone.controller;

import com.vodafone.model.Article;
import com.vodafone.service.ArticleService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/articles")
public class ArticlesController {

    private final ArticleService articleService;


    @GetMapping("")
    public ResponseEntity<List<Article>> getAllArticles()
    {
        return new ResponseEntity<>(articleService.getAllArticles(), HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<Article> addArticle(@RequestBody Article article)
    {
        return ResponseEntity.ok(articleService.addArticle(article));
    }
    @PutMapping("/update")
    public ResponseEntity<Article> updateArticle(@RequestBody Article article)
    {
        return ResponseEntity.ok(articleService.updateArticle(article));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticle(@PathVariable(name = "id") Integer id) {
        return new ResponseEntity<>(articleService.getArticleById(id), HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteArticleById(@PathVariable(name = "id") Integer id)
    {
        return ResponseEntity.ok(articleService.deleteArticle(id));
    }

}
