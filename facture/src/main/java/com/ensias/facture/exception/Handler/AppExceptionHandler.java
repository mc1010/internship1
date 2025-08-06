package com.ensias.facture.exception.Handler;

import com.ensias.facture.exception.AlreadyExistsException;
import com.ensias.facture.exception.NotFoundException;
import com.ensias.facture.shared.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> NotFoundException(NotFoundException exp){
        ErrorMessage errorMessage= ErrorMessage.builder()
                .message(exp.getMessage())
                .timestamp(new Date())
                .code(404)
                .build();
        return new ResponseEntity<>(errorMessage,HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(value = {AlreadyExistsException.class})
    public ResponseEntity<Object> AlreadyExistsException(AlreadyExistsException exp){
        ErrorMessage errorMessage= ErrorMessage.builder()
                .message(exp.getMessage())
                .timestamp(new Date())
                .code(409)
                .build();
        return new ResponseEntity<>(errorMessage,HttpStatus.CONFLICT);

    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        ErrorMessage error = ErrorMessage.builder()
                .message(message)
                .timestamp(new Date())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }







}
