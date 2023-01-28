package io.wdefassio.livraria.api.application.service;

import io.wdefassio.livraria.api.domain.entity.Book;
import io.wdefassio.livraria.api.infra.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImp implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        return bookRepository.save(book);
    }
}
