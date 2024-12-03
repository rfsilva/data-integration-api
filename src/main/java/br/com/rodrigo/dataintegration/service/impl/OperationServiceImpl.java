package br.com.rodrigo.dataintegration.service.impl;

import br.com.rodrigo.dataintegration.constants.*;
import br.com.rodrigo.dataintegration.dto.*;
import br.com.rodrigo.dataintegration.dto.acl.*;
import br.com.rodrigo.dataintegration.dto.company.*;
import br.com.rodrigo.dataintegration.dto.person.*;
import br.com.rodrigo.dataintegration.entity.*;
import br.com.rodrigo.dataintegration.exception.*;
import br.com.rodrigo.dataintegration.mapper.*;
import br.com.rodrigo.dataintegration.message.*;
import br.com.rodrigo.dataintegration.repository.*;
import br.com.rodrigo.dataintegration.service.*;
import jakarta.transaction.*;
import jakarta.xml.bind.*;
import lombok.extern.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

@Service
@Slf4j
public class OperationServiceImpl implements OperationService {

    @Autowired
    private ContextRepository contextRepository;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PersonService personService;

    @Autowired
    private ACLService aclService;

    @Override
    @Transactional
    public String insert(OperationDTO operation) {

        //Existencia e validade do contexto
        log.info("Validando o contexto {}", operation.getContextId());
        Context context = validateContext(operation);
        log.info("Contexto {} é válido", operation.getContextId());

        //Unicidade Operacao
        log.info("Validando se operação {} já existe", operation.getProductOperationId());
        validateOperation(operation.getProductOperationId());

        //Conversão - Person
        log.info("Verificando dados de pessoa");
        PersonDTO person = getPersonData(operation.getFields());
        log.info("Dados de pessoa verificados");

        //Conversão - Company
        log.info("Verificando dados de empresa");
        CompanyDTO company = getCompanyData(operation.getFields());
        log.info("Dados de empresa verificados");

        //Montagem do metadados
        String metadata = generateMetadata(operation, person, company);

        //Chamada ao legado
        Long legacyOperationId = callAcl(metadata);

        generateXml(operation);

        //Inserção do operação
        return insertOperation(operation, context, legacyOperationId, metadata);
    }

    private Context validateContext(OperationDTO operation) {
        log.info("ID de contexto: {}", operation.getContextId());
        ContextDTO contextDTO = operation.getContextData();
        Optional<Context> res = contextRepository.getContext(
                contextDTO.getDate(),
                contextDTO.getInstitution(),
                contextDTO.getBranch(),
                contextDTO.getType(),
                contextDTO.getSequential());
        log.info("Dados do contexto: {}", contextDTO);

        if (res.isEmpty()) {
            log.info("Contexto {} não cadastrado na base", operation.getContextId());
            throw new InvalidContextException(MessageManager.getMessage(MessageConstants.MSG_CONTEXT_NOT_FOUND, operation.getContextId()));
        }
        Context context = res.get();
        if (context.getStatus() == null || !context.getStatus().equals(1)) {
            log.info("Contexto {} não está habilitado", operation.getContextId());
            throw new InvalidContextException(MessageManager.getMessage(MessageConstants.MSG_CONTEXT_NOT_ENABLED, operation.getContextId()));
        }
        return context;
    }

    private void validateOperation(String operationId) {
        Optional<Operation> result = operationRepository.findByProductOperationId(operationId);
        if (result.isPresent()) {
            throw new OperationAlreadyExistsException(MessageManager.getMessage(MessageConstants.MSG_OPERATION_ALREADY_EXISTS, operationId));
        }
    }

    private String insertOperation(OperationDTO operationDTO, Context context, Long operationSequential, String metadata) {
        log.info("Iniciando registro da operação na base de dados.");
        Operation operation = Mapper.toEntity(operationDTO);
        operation.setUuid(UUID.randomUUID().toString());
        operation.setProductOperationId(operationDTO.getProductOperationId());
        operation.setContext(context);
        operation.setMetadata(metadata);
        operation.setSequential(operationSequential);

        operation = operationRepository.save(operation);
        log.info("Registro da operação inserido com sucesso na base de dados");
        return operation.getUuid();
    }

    private PersonDTO getPersonData(List<FieldDTO> fields) {
        PersonDTO personData = null;
        String personId = getValue(fields, GeneralConstants.PERSON_KEY);
        log.info("ID de Pessoa: {}", personId);
        if (personId != null && !personId.isEmpty()) {
            personData = personService.getPersonData(personId);
        }
        return personData;
    }

    private CompanyDTO getCompanyData(List<FieldDTO> fields) {
        CompanyDTO companyData = null;
        String companyId = getValue(fields, GeneralConstants.COMPANY_KEY);
        log.info("ID de Empresa: {}", companyId);
        if (companyId != null && !companyId.isEmpty()) {
            companyData = companyService.getCompanyData(companyId);
        }
        return companyData;
    }

    private Long callAcl(String metadados) {
        MainObjectDTO mainObject = MainObjectDTO.builder()
                .description("Security Info 229")
                .channel("110")
                .stepCode("3")
                .mainData(metadados)
                .option("JSON")
                .networkIpAddress("192.168.3.12")
                .conversionStatus("4")
                .correlationId("V0000003X")
                .build();

        Long resultId = aclService.insert(mainObject);
        log.info("ID gerado pelo ACL: {}", resultId);
        return resultId;
    }

    private String generateMetadata(OperationDTO operation, PersonDTO person, CompanyDTO company) {
        StringBuilder sb = new StringBuilder();
        operation.getFields().forEach(c -> {
            sb.append(c.getName()).append(":").append(c.getValue()).append(";");
            if (c.getName().equals("PES_ID")) {
                sb.append("PES_NOME").append(":").append(person.getName()).append(";");
                sb.append("PES_TIPO").append(":").append(person.getType()).append(";");
                sb.append("PES_EMAI").append(":").append(person.getEmail()).append(";");
                sb.append("PES_TLFN").append(":").append(person.getTelephone()).append(";");
            }
            if (c.getName().equals("EMP_ID")) {
                sb.append("EMP_IDEN").append(":").append(company.getIdentity()).append(";");
                sb.append("EMP_VDIG").append(":").append(company.getVerifDigit()).append(";");
                sb.append("EMP_TYPE").append(":").append(company.getType()).append(";");
            }
        });
        ContextDTO context = operation.getContextData();
        sb.append("CON_DATA").append(":").append(context.getDate()).append(";");
        sb.append("CON_INST").append(":").append(context.getInstitution()).append(";");
        sb.append("CON_BRAN").append(":").append(context.getBranch()).append(";");
        sb.append("CON_TYPE").append(":").append(context.getType()).append(";");
        sb.append("CON_SEQU").append(":").append(context.getSequential()).append(";");
        return sb.toString();
    }

    private String getValue(List<FieldDTO> fields, String fieldName) {
        Optional<FieldDTO> operationField = fields.stream().filter(campo -> campo.getName().equals(fieldName)).findFirst();
        if (operationField.isPresent())
            return operationField.get().getValue();
        return "";
    }

    public BaseOperationDTO getOperationData(String uuid) {
        Optional<Operation> result = operationRepository.findById(uuid);
        return result.map(Mapper::toDto).orElse(null);
    }

    private void generateXml(OperationDTO operation) {
        String filePath = "C:/develop/data-integration/output/operation_" + operation.getProductOperationId() + ".xml";
        try {
            JAXBContext context = JAXBContext.newInstance((OperationDTO.class));
            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(operation, new FileOutputStream(filePath));
        } catch (JAXBException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}

