package br.com.rodrigo.dataintegration.dto.person;


import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;
import lombok.*;

import java.io.*;
import java.time.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private Long identity;
    private Integer type;
    private String name;

    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private String telephone;
    private String email;
    private String address;
    private Integer genre;
    private String status;

}
