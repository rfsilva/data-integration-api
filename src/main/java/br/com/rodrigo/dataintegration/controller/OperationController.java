package br.com.rodrigo.dataintegration.controller;

import br.com.rodrigo.dataintegration.dto.*;
import br.com.rodrigo.dataintegration.service.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class OperationController {

    @Autowired
    private OperationService operationService;

    @PostMapping
    public ResponseEntity<String> insert(@RequestBody OperationDTO operation) {
        log.info("Processando operação {}", operation.toString());
        String resultado = operationService.insert(operation);
        return Optional.ofNullable(resultado)
                .map(id -> ResponseEntity.ok().body(id))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseOperationDTO> getDataIntegration(@PathVariable("id") String productOperationId) {
        log.info("Obtendo dados da operação {}", productOperationId);
        return Optional.ofNullable(operationService.getOperationData(productOperationId))
                .map(op -> ResponseEntity.ok().body(op))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> apagar(@PathVariable("id") String id) {
        return ResponseEntity.ok(id);
    }

    @PutMapping
    public ResponseEntity<OperationDTO> apagar(@RequestBody OperationDTO operacao) {
        return ResponseEntity.ok(operacao);
    }
}
