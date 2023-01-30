package io.wdefassio.livraria.infra.repositories;

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
    @DisplayName("sould return a True when isbn exists in database")
    public void isbnExisting() {
        entityManager.persist(book);
        Boolean bookExists = bookRepository.existsByIsbn(book.getIsbn());
        Assertions.assertThat(bookExists).isTrue();
    }

    @Test
    @DisplayName("sould return False when isbn does not exists in database")
    public void isbnNotExisting() {
        Boolean bookExists = bookRepository.existsByIsbn(book.getIsbn());
        Assertions.assertThat(bookExists).isFalse();
    }

    @Test
    @DisplayName("sould return a book by id")
    public void findBookById() {
        entityManager.persist(book);
        Optional<Book> bookOptional = bookRepository.findById(1L);

        Assertions.assertThat(bookOptional.isPresent()).isTrue();

    }

    @Test
    @DisplayName("should save a book")
    public void saveBookSuccess() {
        Book savedBook = bookRepository.save(book);

        Assertions.assertThat(savedBook.getId()).isNotNull();

    }

    @Test
    @DisplayName("should delete a book")
    public void deleteBookSuccess() {
        entityManager.persist(book);

        Book foundBook = entityManager.find(Book.class, book.getId());

        bookRepository.delete(foundBook);

        Book deletedBook = entityManager.find(Book.class, book.getId());

        Assertions.assertThat(deletedBook).isNull();

    }

    public void initialValues() {
        book = new Book(null, "As aventuras test", "Manoel", "123");
    }

}
