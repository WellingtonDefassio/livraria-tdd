package io.wdefassio.livraria.api.application.service;

import io.wdefassio.livraria.api.domain.entity.Book;

import java.util.Optional;

public interface BookService {


    Book save(Book book);
    Optional<Book> findByIsbn(String isbn);
}
