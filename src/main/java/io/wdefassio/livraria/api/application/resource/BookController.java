package io.wdefassio.livraria.api.application.resource;

import io.wdefassio.livraria.api.application.representation.BookDTO;
import io.wdefassio.livraria.api.application.service.BookService;
import io.wdefassio.livraria.api.domain.entity.Book;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final ModelMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@Valid @RequestBody BookDTO dto) {
        Book book = bookService.save(dto.toModel());
        return mapper.map(book, BookDTO.class);
    }




}
