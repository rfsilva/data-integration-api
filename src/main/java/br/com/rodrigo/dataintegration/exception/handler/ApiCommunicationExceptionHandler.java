package br.com.rodrigo.dataintegration.exception.handler;

import br.com.rodrigo.dataintegration.dto.*;
import br.com.rodrigo.dataintegration.exception.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ApiCommunicationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidContextException.class)
    public ResponseEntity<String> invalidContext(InvalidContextException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ContextNotEnabledException.class)
    public ResponseEntity<String> contextNotEnabled(ContextNotEnabledException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(OperationAlreadyExistsException.class)
    public ResponseEntity<String> operationAlreadyExists(OperationAlreadyExistsException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(RedisException.class)
    public ResponseEntity<String> redis(RedisException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDTO> apiException(ApiException ex) {
        return ResponseEntity.internalServerError().body(ex.getErro());
    }
}
