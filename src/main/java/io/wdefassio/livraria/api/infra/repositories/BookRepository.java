package io.wdefassio.livraria.api.infra.repositories;

import io.wdefassio.livraria.api.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
