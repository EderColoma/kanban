package br.com.facilit.kanban.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.status;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class ErrorHandler {

	private static final String ERROR = "erros";

	@ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(final Exception ex) {
        return status(INTERNAL_SERVER_ERROR).body(Map.of(ERROR, Map.of("servidor", "Erro inesperado no servidor")));
    }

	@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {
        final Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        return badRequest().body(Map.of(ERROR, errors));
    }

	@ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex) {
        return badRequest().body(Map.of(ERROR, "O corpo da requisição está ausente."));
    }

	@ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(final Exception ex) {
		return badRequest().body(Map.of(ERROR, ex.getMessage()));
    }

	@ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrity(final DataIntegrityViolationException ex) {
        final String message = "Operação não permitida: o registro está sendo usado em um relacionamento.";

        return badRequest().body(Map.of(ERROR, message));
    }
}