package br.com.rodrigo.dataintegration.dto.company;


import lombok.*;

import java.io.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class CompanyDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long identity;
    private Long verifDigit;
    private Integer type;
    private String status;

}
