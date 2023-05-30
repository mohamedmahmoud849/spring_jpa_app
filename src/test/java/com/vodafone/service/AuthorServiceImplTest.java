package com.vodafone.service;

import static org.junit.jupiter.api.Assertions.*;

import com.vodafone.errorhandlling.DuplicateEntity;
import com.vodafone.errorhandlling.NotFoundException;
import com.vodafone.model.Author;
import com.vodafone.repo.ArticleRepo;
import com.vodafone.repo.AuthorRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {
    @Mock
    ArticleRepo articleRepo;
    @Mock
    AuthorRepo authorRepo;
    AuthorServiceImpl authorService;

    @BeforeAll
    public void initTest(){
        authorService = new AuthorServiceImpl(authorRepo,articleRepo);
    }
    @Test
    @DisplayName("isAuthorExistTestSuccess")
    public void isAuthorExistTest_sendValidAuthorId_returnTrue(){
        Mockito.when(authorRepo.existsById(1)).thenReturn(true);
        assertTrue(authorService.isAuthorExist(1));
    }
    @Test
    @DisplayName("isAuthorExistTestFail")
    public void isAuthorExistTest_sendNonValidAuthorId_returnFalse(){
        Mockito.when(authorRepo.existsById(2)).thenReturn(false);
        assertFalse(authorService.isAuthorExist(2));
    }
    @Test
    @DisplayName("addAuthorTestSuccess")
    public void addAuthorTest_sendUniqueAndValidAuthor_returnAuthorInstance(){
        Author newAuthor = Author.builder().id(2).name("hasan").build();
        Mockito.when(authorRepo.save(newAuthor)).thenReturn(newAuthor);
        assertNotNull(authorService.addAuthor(newAuthor));

    }
    @Test
    @DisplayName("addAuthorTestFail")
    public void addAuthorTest_sendDuplicateAndNonValidAuthor_duplicateEntityExceptionThrown(){
        Author existedAuthor = Author.builder().id(1).name("mohamed").build();
        Mockito.when(authorRepo.existsById(1)).thenReturn(true);
        assertThrows(DuplicateEntity.class,()->authorService.addAuthor(existedAuthor));
    }

    @Test
    @DisplayName("deleteAuthorTestFail")
    public void deleteAuthorTest_sendExistedAndValidAuthorId_ReturnAuthorId(){

        Mockito.when(authorRepo.existsById(3)).thenReturn(false);

        assertThrows(NotFoundException.class,()->authorService.deleteAuthorById(3));
    }
    @Test
    @DisplayName("updateAuthorTestSuccess")
    public void updateAuthorTest_sendExistedAndValidAuthor_returnNewAuthorInstance(){
        Author newAuthor = Author.builder().id(1).name("hasan").build();
        Mockito.when(authorRepo.existsById(1)).thenReturn(true);
        Mockito.when(authorRepo.save(newAuthor)).thenReturn(newAuthor);

        assertNotNull(authorService.updateAuthor(newAuthor));

    }
    @Test
    @DisplayName("updateAuthorTestFail")
    public void updateAuthorTest_sendNonValidAuthor_NotFoundExceptionThrown(){
        Author existedAuthor = Author.builder().id(2).name("mohamed").build();
        Mockito.when(authorRepo.existsById(2)).thenReturn(false);
        assertThrows(NotFoundException.class,()->authorService.updateAuthor(existedAuthor));
    }

}
