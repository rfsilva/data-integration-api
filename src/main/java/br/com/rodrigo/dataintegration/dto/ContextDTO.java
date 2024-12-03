package br.com.rodrigo.dataintegration.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ContextDTO implements Serializable {

    private static final Long serialVersionUID = 39503593L;

    private LocalDate date;
    private Long institution;
    private Long branch;
    private Integer type;
    private Long sequential;
}
