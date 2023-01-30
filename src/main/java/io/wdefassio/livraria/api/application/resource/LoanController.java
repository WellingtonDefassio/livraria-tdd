package io.wdefassio.livraria.api.application.resource;

import io.wdefassio.livraria.api.application.representation.LoanDTO;
import io.wdefassio.livraria.api.application.service.BookService;
import io.wdefassio.livraria.api.application.service.LoanService;
import io.wdefassio.livraria.api.domain.entity.Book;
import io.wdefassio.livraria.api.domain.entity.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody LoanDTO dto) {
        Book book = bookService.getBookByIsbn(dto.getIsbn());
        Loan loan = new Loan(null, dto.getCustomer(), book, LocalDate.now(), Boolean.FALSE);
        Loan save = loanService.save(loan);

        return ResponseEntity.status(HttpStatus.CREATED).body(save.getId());
    }


}
