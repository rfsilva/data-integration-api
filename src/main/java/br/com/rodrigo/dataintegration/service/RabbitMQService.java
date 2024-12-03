package br.com.rodrigo.dataintegration.service;

import br.com.rodrigo.dataintegration.dto.*;

public interface RabbitMQService {

    void receive(String message);

    void send(OperationResultDTO resultado);
}
