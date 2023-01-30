package io.wdefassio.livraria.api.application.representation;

import io.wdefassio.livraria.api.domain.entity.Book;
import io.wdefassio.livraria.api.domain.entity.Loan;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class LoanDTO {

    private Long id;
    private String isbn;
    private String customer;
    private BookDTO book;


    public Loan toModel(Book book) {
        return new Loan(null, customer, book, LocalDate.now(), Boolean.FALSE);
    }
    public static LoanDTO fromModel(Loan loan) {
        return new LoanDTO(loan.getId(), loan.getBook().getIsbn(), loan.getCustomer(), BookDTO.fromModel(loan.getBook()));
    }
}
