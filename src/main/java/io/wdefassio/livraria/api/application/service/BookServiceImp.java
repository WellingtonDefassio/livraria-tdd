package io.wdefassio.livraria.api.application.service;

import io.wdefassio.livraria.api.domain.entity.Book;
import io.wdefassio.livraria.api.exceptions.BookAlreadyExistsException;
import io.wdefassio.livraria.api.exceptions.BookNotFoundException;
import io.wdefassio.livraria.api.infra.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookServiceImp implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        if (existsByIsbn(book.getIsbn()) && Objects.isNull(book.getId())) {
            throw new BookAlreadyExistsException();
        }
        return bookRepository.save(book);
    }

    @Override
    public Boolean existsByIsbn(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        Optional<Book> optionalBook = bookRepository.findBookByIsbn(isbn);
        return optionalBook.orElseThrow(BookNotFoundException::new);
    }


    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
    }

    @Override
    public void delete(Long id) {
        Book book = findById(id);
        bookRepository.delete(book);
    }

    @Override
    public Book update(Book book) {
        Book bookById = findById(book.getId());
        if (book.getIsbn().equals(bookById.getIsbn())) {
            return save(book);
        } else if (!existsByIsbn(book.getIsbn())) {
            return save(book);
        } else {
            throw new BookAlreadyExistsException();
        }
    }

    @Override
    public Page<Book> find(Book book, Pageable page) {
        Example<Book> example = Example.of(book, ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

        return bookRepository.findAll(example, page);
    }

}
