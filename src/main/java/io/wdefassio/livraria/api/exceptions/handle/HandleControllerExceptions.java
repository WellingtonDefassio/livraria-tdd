package io.wdefassio.livraria.api.exceptions.handle;

import io.wdefassio.livraria.api.exceptions.ApiError;
import io.wdefassio.livraria.api.exceptions.BookAlreadyExistsException;
import io.wdefassio.livraria.api.exceptions.BookNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class HandleControllerExceptions {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> objectNotFound(MethodArgumentNotValidException ex, HttpServletRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        return ResponseEntity.badRequest().body(new ApiError(bindingResult));
    }
    @ExceptionHandler(BookAlreadyExistsException.class)
    public ResponseEntity<?> isbnAlreadyExists(BookAlreadyExistsException ex, HttpServletRequest request) {
        String message = ex.getMessage();
        return ResponseEntity.badRequest().body(new ApiError(message));
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<?> isbnAlreadyExists(BookNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity.notFound().build();
    }


}
