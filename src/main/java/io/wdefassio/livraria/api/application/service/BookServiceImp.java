package io.wdefassio.livraria.api.application.service;

import io.wdefassio.livraria.api.domain.entity.Book;
import io.wdefassio.livraria.api.exceptions.BookAlreadyExistsException;
import io.wdefassio.livraria.api.exceptions.BookNotFoundException;
import io.wdefassio.livraria.api.infra.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookServiceImp implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Book save(Book book) {
        if(existsByIsbn(book.getIsbn()) && Objects.isNull(book.getId())) {
            throw new BookAlreadyExistsException();
        }
        return bookRepository.save(book);
    }

    @Override
    public Boolean existsByIsbn(String isbn) {
      return bookRepository.existsByIsbn(isbn);
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }

}
