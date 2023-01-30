package io.wdefassio.livraria.api.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.PrePersist;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Loan {
    private Long id;
    private String customer;
    private Book book;
    private LocalDate loanDate;
    private Boolean returned;

    @PrePersist
    private void persist() {
        loanDate = LocalDate.now();
    }

}
