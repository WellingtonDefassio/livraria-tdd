package io.wdefassio.livraria.api.application.representation;

import io.wdefassio.livraria.api.domain.entity.Book;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class BookDTO {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String author;
    @NotBlank
    private String isbn;

    public BookDTO(String title, String author, String isbn) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
    }

    public Book toModel() {
        return new Book(title, author, isbn);
    }
}
