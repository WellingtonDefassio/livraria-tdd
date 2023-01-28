package io.wdefassio.livraria.api.exceptions.handle;

import io.wdefassio.livraria.api.exceptions.ApiError;
import io.wdefassio.livraria.api.exceptions.BookAlreadyExistsException;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> objectNotFound(MethodArgumentNotValidException ex, HttpServletRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        return ResponseEntity.badRequest().body(new ApiError(bindingResult));
    }
    @ExceptionHandler(BookAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> isbnAlreadyExists(BookAlreadyExistsException ex, HttpServletRequest request) {
        String message = ex.getMessage();
        return ResponseEntity.badRequest().body(new ApiError(message));
    }


}
