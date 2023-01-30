package io.wdefassio.livraria.api.application.service;

import io.wdefassio.livraria.api.domain.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {


    Book save(Book book);
    Boolean existsByIsbn(String isbn);
    Book getBookByIsbn(String isbn);
    Book findById(Long id);
    void delete(Long id);
    Book update(Book book);
    Page<Book> find(Book book, Pageable page);

}
