package io.wdefassio.livraria.api.application.service;

import io.wdefassio.livraria.api.application.representation.LoanFilterDTO;
import io.wdefassio.livraria.api.domain.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoanService {

    Loan save(Loan loan);
    Loan getById(Long id);
    Loan update(Loan loan);
    Page<Loan> find(LoanFilterDTO filter, Pageable page);
}
