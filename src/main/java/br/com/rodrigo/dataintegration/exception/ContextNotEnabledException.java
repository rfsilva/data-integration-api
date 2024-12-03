package br.com.rodrigo.dataintegration.exception;

import java.io.*;

public class ContextNotEnabledException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 9032390L;

    public ContextNotEnabledException(String contextId) {
        super(contextId);
    }
}
