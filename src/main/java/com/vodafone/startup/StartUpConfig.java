package com.vodafone.startup;

import com.vodafone.model.Article;
import com.vodafone.model.Author;
import com.vodafone.repo.ArticleRepo;
import com.vodafone.repo.AuthorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class StartUpConfig implements CommandLineRunner {
    private final AuthorRepo authorRepo;
    private final ArticleRepo articleRepo;
    @Override
    public void run(String... args) throws Exception {
        Author author = Author.builder().id(1).name("mohamed").build();
        authorRepo.save(author);
        Article article1 = Article.builder().id(1).name("article1").author(author).build();
        Article article2 = Article.builder().id(2).name("article2").author(author).build();
        articleRepo.saveAll(List.of(article1,article2));
    }
}
