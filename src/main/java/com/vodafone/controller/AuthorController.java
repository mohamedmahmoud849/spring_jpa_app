package com.vodafone.controller;

import com.vodafone.model.Article;
import com.vodafone.model.Author;
import com.vodafone.service.AuthorService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping("")
    public ResponseEntity<List<Author>> getAllAuthers()
    {
            return new ResponseEntity<>(authorService.getAllAuthors(), HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity<Author> addAuthor(@RequestBody Author author)
    {
        return ResponseEntity.ok(authorService.addAuthor(author));
    }
    @PutMapping("/update")
    public ResponseEntity<Author> updateAuthor(@RequestBody Author author)
    {
        return ResponseEntity.ok(authorService.updateAuthor(author));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable(name = "id") Integer id)
    {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteAuthorById(@PathVariable(name = "id") Integer id)
    {
        return ResponseEntity.ok(authorService.deleteAuthorById(id));
    }
    @GetMapping("/{id}/articles")
    public ResponseEntity<List<Article>> getAutherArticles(@PathVariable(name = "id") Integer id)
    {
            return ResponseEntity.ok(authorService.getAuthorArticles(id));
    }


    }

