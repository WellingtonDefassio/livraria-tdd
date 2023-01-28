package io.wdefassio.livraria.infra.repositories;

import io.wdefassio.livraria.api.application.service.BookServiceImp;
import io.wdefassio.livraria.api.domain.entity.Book;
import io.wdefassio.livraria.api.infra.repositories.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    Book book;

    @BeforeEach
    public void setUp() {
        initialValues();
    }

    @Test
    @DisplayName("sould return a not empty optinal when isbn exists in database")
    public void isbnExisting() {
        entityManager.persist(book);
        Optional<Book> optionalBook = bookRepository.findByIsbn(book.getIsbn());
        Assertions.assertThat(optionalBook).isNotEmpty();
    }
    @Test
    @DisplayName("sould return a  empty optinal when isbn does not exists in database")
    public void isbnNotExisting() {
        Optional<Book> optionalBook = bookRepository.findByIsbn(book.getIsbn());
        Assertions.assertThat(optionalBook).isEmpty();
    }



    public void initialValues() {
        book = new Book("As aventuras test", "Manoel", "123");
    }

}
