package br.com.rodrigo.dataintegration.exception;

import br.com.rodrigo.dataintegration.dto.*;
import lombok.*;

import java.io.*;

public class ApiException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 202005220603L;

    @Getter
    private final String api;

    @Getter
    private final ErrorDTO erro;

    public ApiException(Throwable cause, String api, ErrorDTO error) {
        super(cause);
        this.api = api;
        this.erro = error;
    }
    
    
}