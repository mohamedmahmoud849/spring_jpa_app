package com.vodafone.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vodafone.model.Author;

import java.util.Optional;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Integer>
{
}
