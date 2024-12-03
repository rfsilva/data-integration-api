package br.com.rodrigo.dataintegration.exception;

import java.io.*;

public class OperationAlreadyExistsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 9032390L;

    public OperationAlreadyExistsException(String productOperationId) {
        super(productOperationId);
    }
}
