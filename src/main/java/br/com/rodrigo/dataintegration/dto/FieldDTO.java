package br.com.rodrigo.dataintegration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import jakarta.xml.bind.annotation.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@ToString
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class FieldDTO {

    @JsonProperty(required = true)
    @JsonPropertyDescription("Classificação do campo")
    private String classType;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Nome od campo")
    private String name;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Tipo do campo passado")
    private String type;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Valor do campo")
    private String value;

    @JsonPropertyDescription("Infome do número de casas decimais do valor passado")
    private String decimalPlaces;

}
