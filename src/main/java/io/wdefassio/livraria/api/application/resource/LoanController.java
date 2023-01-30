package io.wdefassio.livraria.api.application.resource;

import io.wdefassio.livraria.api.application.representation.LoanDTO;
import io.wdefassio.livraria.api.application.representation.LoanFilterDTO;
import io.wdefassio.livraria.api.application.representation.ReturnedLoanDTO;
import io.wdefassio.livraria.api.application.service.BookService;
import io.wdefassio.livraria.api.application.service.LoanService;
import io.wdefassio.livraria.api.domain.entity.Book;
import io.wdefassio.livraria.api.domain.entity.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<Long> create(@RequestBody LoanDTO dto) {
        Book book = bookService.getBookByIsbn(dto.getIsbn());
        Loan loan = dto.toModel(book);
        Loan save = loanService.save(loan);
        return ResponseEntity.status(HttpStatus.CREATED).body(save.getId());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> returnedBook(@PathVariable Long id, @RequestBody ReturnedLoanDTO dto) {

        Loan loan = loanService.getById(id);
        loan.setReturned(dto.getReturned());
        loanService.update(loan);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public Page<LoanDTO> find(LoanFilterDTO dto, Pageable page) {
        Page<Loan> loanPage = loanService.find(dto, page);
        List<LoanDTO> loanDTOS = loanPage.getContent().stream().map(LoanDTO::fromModel).toList();
        return new PageImpl<>(loanDTOS, page, loanPage.getTotalElements());
    }


}
