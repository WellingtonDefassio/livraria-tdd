package io.wdefassio.livraria.api.application.service;

import io.wdefassio.livraria.api.domain.entity.Book;
import io.wdefassio.livraria.api.exceptions.BookAlreadyExistsException;
import io.wdefassio.livraria.api.exceptions.BookNotFoundException;
import io.wdefassio.livraria.api.infra.repositories.BookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

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

    @Test
    @DisplayName("should return a book when correct id is provided")
    public void getBookById() {
        Long id = 1L;
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(book));


        Book serviceById = bookService.findById(id);


        Assertions.assertThat(serviceById.getId()).isNotNull();
        Assertions.assertThat(serviceById.getTitle()).isEqualTo("As aventuras test");
        Assertions.assertThat(serviceById.getAuthor()).isEqualTo("Manoel");
        Assertions.assertThat(serviceById.getIsbn()).isEqualTo("123");

    }

    @Test
    @DisplayName("should throw a error when a nonexistent id is provided")
    public void getBookByIdError() {

        Long id = 1L;
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(() -> bookService.findById(id));
        Assertions.assertThat(exception).isInstanceOf(BookNotFoundException.class)
                .hasMessage("book not found");

    }

    @Test
    @DisplayName("should return true when find a existing isbn")
    public void existingIsbn() {
        String isbn = "123";
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(Boolean.TRUE);
        Boolean aBoolean = bookService.existsByIsbn(isbn);
        Assertions.assertThat(aBoolean).isTrue();

    }

    @Test
    @DisplayName("should return false when not find a existing isbn")
    public void notExistingIsbn() {
        String isbn = "123";
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(Boolean.FALSE);
        Boolean aBoolean = bookService.existsByIsbn(isbn);
        Assertions.assertThat(aBoolean).isFalse();
    }

    @Test
    @DisplayName("should delete a existing book")
    public void deleteBookSuccess() {
        Long id = 1L;
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(book));

        bookService.delete(id);

        Mockito.verify(repository, Mockito.times(1)).delete(book);
        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }

    @Test
    @DisplayName("should throws if try delete a nonexistent book")
    public void deleteBookFail() {
        Long id = 1L;
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable(() -> bookService.delete(id));
        Assertions.assertThat(exception).isInstanceOf(BookNotFoundException.class)
                .hasMessage("book not found");


        Mockito.verify(repository, Mockito.times(0)).delete(book);
        Mockito.verify(repository, Mockito.times(1)).findById(id);
    }

    @Test
    @DisplayName("should update a existing book")
    public void updateBookSuccess() {

        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(book));
        Mockito.when(repository.save(Mockito.any())).thenReturn(book);
        Book updateBook = new Book(1L, "new tittle", "new author", "123");
        Book updatedBook = bookService.update(updateBook);

        Mockito.verify(repository, Mockito.times(1)).findById(book.getId());
        Mockito.verify(repository, Mockito.times(1)).save(updateBook);

        Assertions.assertThat(updatedBook.getId()).isNotNull();
        Assertions.assertThat(updatedBook.getAuthor()).isEqualTo(book.getAuthor());
        Assertions.assertThat(updatedBook.getTitle()).isEqualTo(book.getTitle());
        Assertions.assertThat(updatedBook.getIsbn()).isEqualTo(book.getIsbn());


    }

    @Test
    @DisplayName("should throw when try to update a book with a existent isbn")
    public void updateBookIsbnFail() {

        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(book));
        Mockito.when(repository.existsByIsbn(Mockito.anyString())).thenReturn(Boolean.TRUE);
        Book updateBook = new Book(1L, "new tittle", "new author", "5555");


        Throwable exception = Assertions.catchThrowable(() -> bookService.update(updateBook));

        Assertions.assertThat(exception).isInstanceOf(BookAlreadyExistsException.class)
                .hasMessage("isbn already exists");


        Mockito.verify(repository, Mockito.never()).save(updateBook);
        Mockito.verify(repository, Mockito.times(1)).findById(updateBook.getId());


    }

    @Test
    @DisplayName("should filter book's by properties")
    public void findBookTest() {

        Page<Book> page = new PageImpl<>(List.of(book), PageRequest.of(0,10), 1);

        Mockito.when(repository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);

        Page<Book> result = bookService.find(book, PageRequest.of(0, 10));

        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getContent()).isEqualTo(List.of(book));
        Assertions.assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        Assertions.assertThat(result.getPageable().getPageSize()).isEqualTo(10);

    }

    @Test
    @DisplayName("should be able to find a Book by isbn")
    public void findByIsbnSuccess() {
        Long id = 1L;
        Mockito.when(repository.findBookByIsbn(Mockito.anyString())).thenReturn(Optional.of(book));

        Book bookByIsbn = bookService.getBookByIsbn("123");

      Assertions.assertThat(bookByIsbn.getIsbn()).isEqualTo(book.getIsbn());
      Assertions.assertThat(bookByIsbn.getId()).isEqualTo(book.getId());
      Assertions.assertThat(bookByIsbn.getAuthor()).isEqualTo(book.getAuthor());
      Assertions.assertThat(bookByIsbn.getTitle()).isEqualTo(book.getTitle());
      Mockito.verify(repository, Mockito.times(1)).findBookByIsbn("123");
    }

    @Test
    @DisplayName("should throw when a book is not found")
    public void findByIsbnFail() {
        Long id = 1L;
        Mockito.when(repository.findBookByIsbn(Mockito.anyString())).thenReturn(Optional.empty());
        Throwable exception = Assertions.catchThrowable(() -> bookService.getBookByIsbn("123"));
        Assertions.assertThat(exception).isInstanceOf(BookNotFoundException.class)
                .hasMessage("book not found");
        Mockito.verify(repository, Mockito.never()).save(book);
    }



    public void initialValues() {
        book = new Book(1L, "As aventuras test", "Manoel", "123");
    }
}
