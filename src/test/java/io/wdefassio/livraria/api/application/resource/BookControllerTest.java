package io.wdefassio.livraria.api.application.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.wdefassio.livraria.api.exceptions.BookAlreadyExistsException;
import io.wdefassio.livraria.api.application.representation.BookDTO;
import io.wdefassio.livraria.api.application.service.BookService;
import io.wdefassio.livraria.api.domain.entity.Book;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = {BookController.class})
@AutoConfigureMockMvc
public class BookControllerTest {
    static String BOOK_API = "/api/books";

    @Autowired
    MockMvc mvc;
    @MockBean
    BookService bookService;
    Book book;
    BookDTO bookDTO;

    @BeforeEach
    public void init() {
        createData();
    }

    @Test
    @DisplayName("should create a book when correct params is provided")
    public void createBookSuccess() throws Exception {
        BDDMockito.given(bookService.save(Mockito.any(Book.class))).willReturn(book);

        String json = new ObjectMapper().writeValueAsString(bookDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);


        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(bookDTO.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value(bookDTO.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("isbn").value(bookDTO.getIsbn()));


    }

    @Test
    @DisplayName("should throw an error when BookDTO not provide valid data")
    public void createBookFailed() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new BookDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(3)));

    }

    @Test
    @DisplayName("should throw an error when try create a book with existent isbn")
    public void createBookWithDuplicatedIsbn() throws Exception {

        String json = new ObjectMapper().writeValueAsString(bookDTO);

        BDDMockito.given(bookService.save(Mockito.any(Book.class))).willThrow(new BookAlreadyExistsException());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(BOOK_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("errors[0]").value("isbn already exists"));

    }

    @Test
    @DisplayName("should return a book infos when that exists")
    public void getBookDetailsSuccess() throws Exception {

        Long id = 1L;

        BDDMockito.given(bookService.findById(id)).willReturn(book);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(book.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value(book.getTitle()))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value(book.getAuthor()))
                .andExpect(MockMvcResultMatchers.jsonPath("isbn").value(book.getIsbn()));

    }

    @Test
    @DisplayName("should throw a error when book not exists")
    public void getBookDetailsFail() throws Exception {

        Long id = 1L;

        BDDMockito.given(bookService.findById(Mockito.anyLong())).willThrow(new BookNotFoundException());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(BOOK_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("should be able to delete a existing book")
    public void deleteBookSuccess() throws Exception {
        Long id = 1L;

        BDDMockito.given(bookService.findById(id)).willReturn(book);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNoContent());

    }

    @Test
    @DisplayName("should throws when a book is not found")
    public void deleteBookFail() throws Exception {

        Long id = 1L;

        BDDMockito.doThrow(new BookNotFoundException()).when(bookService).delete(id);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(BOOK_API.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("should be able to update a existing book")
    public void updateBookSuccess() throws Exception {

        Long id = 1L;
        BookDTO bookDTOUpdate = new BookDTO(null, "book update", "author update", "0005");
        String json = new ObjectMapper().writeValueAsString(bookDTOUpdate);
        bookDTOUpdate.setId(id);
        BDDMockito.given(bookService.update(Mockito.any())).willReturn(bookDTOUpdate.toModel());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + id))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value("book update"))
                .andExpect(MockMvcResultMatchers.jsonPath("author").value("author update"))
                .andExpect(MockMvcResultMatchers.jsonPath("isbn").value("0005"));

    }

    @Test
    @DisplayName("should not be able to update a nonexistent book")
    public void updateBookFail() throws Exception {

        Long id = 1L;
        String json = new ObjectMapper().writeValueAsString(new BookDTO(null ,"book update", "author update", "0005"));

        BDDMockito.given(bookService.update(Mockito.any())).willThrow(new BookNotFoundException());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(BOOK_API.concat("/" + id))
                .content(json)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mvc.perform(request)
                .andExpect(status().isNotFound());

    }

    @Test
    @DisplayName("should be able to filter a book")
    public void findBookSuccess() throws Exception {

        BDDMockito.given(bookService.find(Mockito.any(Book.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<Book>(Arrays.asList(book), PageRequest.of(0,100), 1));

        String queryString = String.format("?title=%s&author=%s&page=0&size=100", book.getTitle(), book.getAuthor());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(BOOK_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mvc
                .perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }


    public void createData() {
        bookDTO = new BookDTO(null, "As aventuras", "Arthur", "001");
        book = bookDTO.toModel();
        book.setId(1L);
    }

}
