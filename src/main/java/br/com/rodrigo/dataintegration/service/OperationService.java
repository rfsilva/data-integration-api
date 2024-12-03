package br.com.rodrigo.dataintegration.service;

import br.com.rodrigo.dataintegration.dto.*;

public interface OperationService {

    String insert(OperationDTO operacao);

    BaseOperationDTO getOperationData(String uuid);
}
