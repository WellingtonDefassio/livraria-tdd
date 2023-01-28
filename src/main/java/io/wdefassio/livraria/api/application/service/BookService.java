package io.wdefassio.livraria.api.application.service;

import io.wdefassio.livraria.api.domain.entity.Book;

public interface BookService {


    Book save(Book book);
    Boolean existsByIsbn(String isbn);
    Book findById(Long id);
    void delete(Book book);
}
