package br.com.rodrigo.dataintegration.service.impl;

import br.com.rodrigo.dataintegration.config.*;
import br.com.rodrigo.dataintegration.dto.*;
import br.com.rodrigo.dataintegration.service.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.*;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Service
@Slf4j
public class RabbitMQServiceImpl implements RabbitMQService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OperationService operationService;

    @Override
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME_INPUT)
    public void receive(String message) {
        log.info("Mensagem bruta recebida: {}", message);
        try {
            OperationDTO dto = objectMapper.readValue(message, OperationDTO.class);
            log.info("Mensagem convertida: {}", dto);
            OperationResultDTO resultadoOperacao = OperationResultDTO.builder()
                    .originalOperation(dto)
                    .build();
            try {
                String idRecebido = operationService.insert(dto);
                resultadoOperacao.setResult(ResultDTO.builder()
                        .code("0")
                        .receivedId(idRecebido)
                        .description("Sucesso")
                        .build());
            } catch (Exception e) {
                resultadoOperacao.setResult(ResultDTO.builder()
                        .code("99")
                        .description(e.getMessage())
                        .build());
            }
            send(resultadoOperacao);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void send(OperationResultDTO resultado) {
        try {
            String message = objectMapper.writeValueAsString(resultado);
            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME_OUTPUT, message);
            log.info("Mensagem enviada: {}, {}", resultado, message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
