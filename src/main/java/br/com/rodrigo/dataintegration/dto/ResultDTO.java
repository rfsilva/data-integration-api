package br.com.rodrigo.dataintegration.dto;

import lombok.*;

import java.io.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResultDTO implements Serializable {

    @Serial
    private static final long serialVersionUid = 390530593L;

    private String code;

    private String receivedId;

    private String description;
}
