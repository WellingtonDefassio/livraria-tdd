package io.wdefassio.livraria.api.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String customer;

    @JoinColumn(name = "book_id")
    @ManyToOne
    private Book book;
    private LocalDate loanDate;
    private Boolean returned;

    @PrePersist
    private void persist() {
        loanDate = LocalDate.now();
    }

}
