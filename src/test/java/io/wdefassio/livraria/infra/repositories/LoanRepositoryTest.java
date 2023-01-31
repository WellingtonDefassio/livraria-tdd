package io.wdefassio.livraria.infra.repositories;

import io.wdefassio.livraria.api.domain.entity.Book;
import io.wdefassio.livraria.api.domain.entity.Loan;
import io.wdefassio.livraria.api.infra.repositories.LoanRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class LoanRepositoryTest {

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private TestEntityManager entityManager;

    Book book;
    Loan loan;

    @BeforeEach
    public void setUp() {
        initialValues();
    }

    @Test
    @DisplayName("should verify if a book is loaned")
    public void existsByBookAndNotReturnedTest() {

        entityManager.persist(book);
        entityManager.persist(loan);

        Boolean aBoolean = loanRepository.existsByBookAndNotReturned(book);

        Assertions.assertThat(aBoolean).isTrue();


    }

    @Test
    @DisplayName("should be obtain loan of 3 days ago")
    public void findByLoanDateLessThanAndNotReturned() {
        entityManager.persist(book);
        entityManager.persist(loan);

        List<Loan> result = loanRepository.findByLoanDateLessThanAndNotReturned(LocalDate.now().plusDays(1));

        Assertions.assertThat(result).hasSize(1).contains(loan);

    }

    @Test
    @DisplayName("should not obtain loan of less 3 days ago")
    public void notFindByLoanDateLessThanAndNotReturned() {
        entityManager.persist(book);
        entityManager.persist(loan);

        List<Loan> result = loanRepository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(1));

        Assertions.assertThat(result.isEmpty());

    }


    public void initialValues() {
        book = new Book(null, "As aventuras test", "Manoel", "123");
        loan = new Loan(null, "Joao", "any@email.com", book, LocalDate.now(), null);
    }

}
