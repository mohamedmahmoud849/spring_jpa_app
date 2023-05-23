package com.vodafone.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vodafone.model.Article;
import java.util.List;

@Repository
public interface ArticleRepo extends JpaRepository<Article, Integer>
{
    List<Article> findAllByAuthor_Id(Integer id);
}