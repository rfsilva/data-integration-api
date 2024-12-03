package br.com.rodrigo.dataintegration.dto;

import br.com.rodrigo.dataintegration.exception.*;
import br.com.rodrigo.dataintegration.message.*;
import com.fasterxml.jackson.annotation.*;
import jakarta.validation.constraints.*;
import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.time.*;
import java.util.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@XmlRootElement(name = "operation")
@XmlAccessorType(XmlAccessType.FIELD)
public class OperationDTO {

    @JsonProperty(required = true)
    @JsonPropertyDescription("ID do Contexto informado pelo canal")
    @NotNull
    @NotEmpty
    private String contextId;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Código do tipo de movimentação")
    private String movementType;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Código do tipo de operação")
    private String operationType;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Nome da Operação")
    private String operationName;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Numero da Operação")
    private String operationNumber;

    @JsonProperty(required = true)
    @JsonPropertyDescription("Descricao completa da funcionalidade")
    private String description;

    @JsonProperty(required = true)
    @JsonPropertyDescription("URL da operação de estorno")
    private String returnOperationCode;

    @JsonProperty(required = true)
    @JsonPropertyDescription("ID de Operação do Produto")
    private String productOperationId;

    @JsonProperty(value = "operation", required = true)
    @JsonPropertyDescription("Complemento de campos de produto / operação")
    @XmlElementWrapper(name = "fields")
    @XmlElement(name = "field")
    private List<FieldDTO> fields;

    public ContextDTO getContextData() {
        // DDMMAAAAIIIIIIGGGGGTSSSSSSS
        if (contextId != null && contextId.length() == 27) {
            try {
                String day = contextId.substring(0, 2);
                String month = contextId.substring(2, 4);
                String year = contextId.substring(4, 8);
                String institution = contextId.substring(8, 14);
                String branch = contextId.substring(14, 19);
                String type = contextId.substring(19, 20);
                String sequential = contextId.substring(20, 27);
                return ContextDTO.builder()
                        .date(LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)))
                        .institution(Long.parseLong(institution))
                        .branch(Long.parseLong(branch))
                        .type(Integer.parseInt(type))
                        .sequential(Long.parseLong(sequential))
                        .build();
            } catch (Exception e) {
                throw new InvalidContextException(MessageManager.getMessage(MessageConstants.MSG_INVALID_CONTEXT_ID, getContextId()));
            }
        }
        throw new InvalidContextException(MessageManager.getMessage(MessageConstants.MSG_INVALID_CONTEXT_ID, getContextId()));
    }
}
