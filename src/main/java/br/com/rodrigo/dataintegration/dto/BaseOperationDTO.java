package br.com.rodrigo.dataintegration.dto;

import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseOperationDTO {

    private String uuid;

    private String productOperationId;

    private Long contextSequential;

    private Long operationSequential;

    private String metadata;
}
