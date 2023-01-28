package io.wdefassio.livraria.api.application.service;

import io.wdefassio.livraria.api.domain.entity.Book;
import io.wdefassio.livraria.api.exceptions.BookAlreadyExistsException;
import io.wdefassio.livraria.api.infra.repositories.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {

    BookService bookService;
    @MockBean
    BookRepository repository;
    Book book;

    @BeforeEach
    public void setUp() {
        this.bookService = new BookServiceImp(repository);
        initialValues();
    }

    @Test
    @DisplayName("should save a book")
    public void saveCorrectBook() {
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(Boolean.FALSE);
        Mockito.when(repository.save(book)).thenReturn(book);


        Book savedBook = bookService.save(book);


        Assertions.assertThat(savedBook.getId()).isNotNull();
        Assertions.assertThat(savedBook.getTitle()).isEqualTo("As aventuras test");
        Assertions.assertThat(savedBook.getAuthor()).isEqualTo("Manoel");
        Assertions.assertThat(savedBook.getIsbn()).isEqualTo("123");


    }

    @Test
    @DisplayName("should not save a book with existing isbn")
    public void saveDuplicateIsbnBook() {

        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(Boolean.TRUE);
        book.setId(null);
        Throwable exception = Assertions.catchThrowable(() -> bookService.save(book));
        Assertions.assertThat(exception).isInstanceOf(BookAlreadyExistsException.class)
                .hasMessage("isbn already exists");

        Mockito.verify(repository, Mockito.never()).save(book);
    }

    public void initialValues() {
        book = new Book(1L,"As aventuras test", "Manoel", "123");
    }
}
