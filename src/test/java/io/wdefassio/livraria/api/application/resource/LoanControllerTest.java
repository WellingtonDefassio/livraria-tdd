package io.wdefassio.livraria.api.application.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wdefassio.livraria.api.application.representation.LoanDTO;
import io.wdefassio.livraria.api.application.service.BookService;
import io.wdefassio.livraria.api.application.service.LoanService;
import io.wdefassio.livraria.api.domain.entity.Loan;
import io.wdefassio.livraria.api.domain.entity.Book;
import io.wdefassio.livraria.api.exceptions.BookAlreadyLoanException;
import io.wdefassio.livraria.api.exceptions.BookNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {LoanController.class})
@AutoConfigureMockMvc
public class LoanControllerTest {

    static final String LOAN_API = "/api/loans";

    @Autowired
    MockMvc mvc;
    @MockBean
    private BookService bookService;
    @MockBean
    private LoanService loanService;


    LoanDTO loanDTO;
    Book book;
    Loan loan;


    @BeforeEach
    public void init() {
        createData();
    }
    @Test
    @DisplayName("should be able to realize a loan")
    public void createLoanSuccess() throws Exception {

        String json = new ObjectMapper().writeValueAsString(loanDTO);

        BDDMockito.given(bookService.getBookByIsbn(Mockito.anyString())).willReturn(book);
        BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willReturn(loan);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("1"));


    }

    @Test
    @DisplayName("should throw a error when a invalid isbn is provided")
    public void createLoanIsbnFail() throws Exception {

        String json = new ObjectMapper().writeValueAsString(loanDTO);

        BDDMockito.given(bookService.getBookByIsbn(Mockito.anyString())).willThrow(new BookNotFoundException());
        BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willReturn(loan);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("should throw when try to loan a loaned book")
    public void createLoanWithLoanedBook() throws Exception {

        String json = new ObjectMapper().writeValueAsString(loanDTO);

        BDDMockito.given(bookService.getBookByIsbn(Mockito.anyString())).willReturn(book);
        BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willThrow(new BookAlreadyLoanException());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(LOAN_API)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value("book already loan"));

    }



    public void createData() {
        loanDTO = new LoanDTO("123", "Joe");
        book = new Book(1L, "As aventuras", "Arthur", "123");
        loan = new Loan(1L, "Arthur", book, LocalDate.now(), Boolean.TRUE);
    }

}
