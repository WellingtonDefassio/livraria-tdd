package io.wdefassio.livraria.api.application.service;

import io.wdefassio.livraria.api.application.representation.LoanFilterDTO;
import io.wdefassio.livraria.api.domain.entity.Book;
import io.wdefassio.livraria.api.domain.entity.Loan;
import io.wdefassio.livraria.api.exceptions.BookAlreadyLoanException;
import io.wdefassio.livraria.api.exceptions.LoanNotFoundException;
import io.wdefassio.livraria.api.infra.repositories.LoanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LoanServiceImp implements LoanService {

    private final LoanRepository loanRepository;

    @Override
    public Loan save(Loan loan) {
        if(loanRepository.existsByBookAndNotReturned(loan.getBook())){
            throw new BookAlreadyLoanException();
        }
        return loanRepository.save(loan);
    }

    @Override
    public Loan getById(Long id) {
        return loanRepository.findById(id).orElseThrow(LoanNotFoundException::new);
    }

    @Override
    public Loan update(Loan loan) {
        loan.setReturned(Boolean.TRUE);
        return loanRepository.save(loan);
    }

    @Override
    public Page<Loan> find(LoanFilterDTO filter, Pageable page) {
        Loan loan = new Loan(null, filter.getCustomer(), null, new Book(null, null, null, filter.getIsbn()), null, null);
        Example<Loan> example = Example.of(loan, ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT)
        );

        return loanRepository.findAll(example, page);
    }

    @Override
    public Page<Loan> getLoansByBook(Book book, Pageable page) {
        return loanRepository.findByBook(book, page);
    }

    @Override
    public List<Loan> getAllLateLoans() {
        final Integer loanDays = 4;
        LocalDate threeDaysAgo = LocalDate.now().minusDays(loanDays);

        return loanRepository.findByLoanDateLessThanAndNotReturned(threeDaysAgo);
    }
}
