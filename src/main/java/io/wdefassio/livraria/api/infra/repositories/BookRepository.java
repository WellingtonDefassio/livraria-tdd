package io.wdefassio.livraria.api.infra.repositories;

import io.wdefassio.livraria.api.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Boolean existsByIsbn(String isbn);
}
