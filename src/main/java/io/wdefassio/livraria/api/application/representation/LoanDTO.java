package io.wdefassio.livraria.api.application.representation;

import io.wdefassio.livraria.api.domain.entity.Book;
import io.wdefassio.livraria.api.domain.entity.Loan;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class LoanDTO {

    private Long id;
    @NotEmpty
    private String isbn;
    @NotEmpty
    private String customer;
    @NotEmpty
    private String email;
    private BookDTO book;



    public Loan toModel(Book book) {
        return new Loan(null, customer, email, book, LocalDate.now(), Boolean.FALSE);
    }
    public static LoanDTO fromModel(Loan loan) {
        return new LoanDTO(loan.getId(), loan.getBook().getIsbn(), loan.getCustomer(), loan.getEmail(), BookDTO.fromModel(loan.getBook()));
    }
}
