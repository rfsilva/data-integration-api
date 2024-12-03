package br.com.rodrigo.dataintegration.exception;

import java.io.*;

public class InvalidContextException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 9032390L;

    public InvalidContextException(String contextId) {
        super(contextId);
    }
}
