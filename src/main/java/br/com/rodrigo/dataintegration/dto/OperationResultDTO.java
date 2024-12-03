package br.com.rodrigo.dataintegration.dto;

import lombok.*;

import java.io.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class OperationResultDTO implements Serializable {

    @Serial
    private static final long serialVersionUid = 24923042L;

    private OperationDTO originalOperation;

    private ResultDTO result;
}
