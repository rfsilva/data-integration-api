package br.com.rodrigo.dataintegration.service;

import br.com.rodrigo.dataintegration.dto.acl.*;

public interface ACLService {

    MainObjectDTO getOperationData(Long contextSequential, Long operationSequential);

    Long insert(MainObjectDTO operation);
}
