package io.wdefassio.livraria.api.application.service;

import io.wdefassio.livraria.api.application.representation.LoanFilterDTO;
import io.wdefassio.livraria.api.domain.entity.Book;
import io.wdefassio.livraria.api.domain.entity.Loan;
import io.wdefassio.livraria.api.exceptions.BookAlreadyLoanException;
import io.wdefassio.livraria.api.exceptions.LoanNotFoundException;
import io.wdefassio.livraria.api.infra.repositories.LoanRepository;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class LoanServiceTest {

    LoanService loanService;
    @MockBean
    LoanRepository loanRepository;
    Loan loan;
    Book book;
    LoanFilterDTO loanFilter;

    @BeforeEach
    public void setUp() {
        this.loanService = new LoanServiceImp(loanRepository);
        initialValues();
    }


    @Test
    @DisplayName("should be able to save a loan")
    public void saveLoanSuccess() {
        Mockito.when(loanRepository.save(loan)).thenReturn(loan);
        Mockito.when(loanRepository.existsByBookAndNotReturned(book)).thenReturn(Boolean.FALSE);

        Loan saveLoan = loanService.save(loan);

        Assertions.assertThat(saveLoan.getId()).isNotNull();
        Assertions.assertThat(saveLoan.getBook()).isEqualTo(book);
    }

    @Test
    @DisplayName("should throw wen a book is already loan")
    public void saveLoanFail() {
        Mockito.when(loanRepository.save(loan)).thenReturn(loan);
        Mockito.when(loanRepository.existsByBookAndNotReturned(book)).thenReturn(Boolean.TRUE);

        Throwable throwable = Assertions.catchThrowable(() -> loanService.save(loan));

        Assertions.assertThat(throwable).isInstanceOf(BookAlreadyLoanException.class);
        Assertions.assertThat(throwable.getMessage()).isEqualTo("book already loan");
        Mockito.verify(loanRepository, Mockito.never()).save(loan);

    }

    @Test
    @DisplayName("should return a loan info by id")
    public void getLoanSuccess() {
        Long id = 1L;
        Mockito.when(loanRepository.findById(id)).thenReturn(Optional.of(loan));

        Loan loanServiceById = loanService.getById(id);

        Assertions.assertThat(loanServiceById).isNotNull();
        Assertions.assertThat(loanServiceById.getBook()).isEqualTo(book);
        Assertions.assertThat(loanServiceById.getCustomer()).isEqualTo(loan.getCustomer());


    }
    @Test
    @DisplayName("should throw wen a loan not exists")
    public void getLoanFail() {
        Long id = 1L;
        Mockito.when(loanRepository.findById(id)).thenReturn(Optional.empty());

        Throwable throwable = Assertions.catchThrowable(() -> loanService.getById(id));

        Assertions.assertThat(throwable).isInstanceOf(LoanNotFoundException.class);
        Assertions.assertThat(throwable.getMessage()).isEqualTo("loan not found");

    }

    @Test
    @DisplayName("should update a loan")
    public void updateLoanSuccess() {
        loan.setReturned(Boolean.TRUE);

        Mockito.when(loanRepository.save(loan)).thenReturn(loan);

        Loan update = loanService.update(loan);

        Assertions.assertThat(update.getReturned()).isTrue();
        Mockito.verify(loanRepository, Mockito.times(1)).save(loan);

    }

    @Test
    @DisplayName("should filter loan's by properties")
    public void findLoanTest() {

        Page<Loan> page = new PageImpl<>(List.of(loan), PageRequest.of(0,10), 1);

        Mockito.when(loanRepository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class))).thenReturn(page);

        Page<Loan> result = loanService.find(loanFilter, PageRequest.of(0, 10));

        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getContent()).isEqualTo(List.of(loan));
        Assertions.assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        Assertions.assertThat(result.getPageable().getPageSize()).isEqualTo(10);

    }


    public void initialValues() {
        book = new Book(1L, "As aventuras test", "Manoel", "123");
        loan = new Loan(1L, "Fulano", "any@email.com", book, LocalDate.now(), Boolean.FALSE);
        loanFilter = new LoanFilterDTO(loan.getBook().getIsbn(), loan.getCustomer());
    }



}
