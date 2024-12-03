package br.com.rodrigo.dataintegration.mapper;

import br.com.rodrigo.dataintegration.dto.*;
import br.com.rodrigo.dataintegration.entity.Operation;

import java.util.*;

public final class Mapper {

    public static Operation toEntity(OperationDTO obj) {
        return Operation.builder()
                .uuid(UUID.randomUUID().toString())
                .productOperationId(obj.getProductOperationId())
                .sequential(obj.getContextData().getSequential())
                .metadata(obj.getDescription())
                .build();
    }

    public static BaseOperationDTO toDto(Operation obj) {
        return BaseOperationDTO.builder()
                .contextSequential(obj.getContext().getSequential())
                .productOperationId(obj.getProductOperationId())
                .operationSequential(obj.getSequential())
                .uuid(obj.getUuid())
                .metadata(obj.getMetadata())
                .build();
    }
}
