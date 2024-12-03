package br.com.rodrigo.dataintegration.service;

import br.com.rodrigo.dataintegration.dto.person.*;

public interface PersonService {

    PersonDTO getPersonData(String personId);
}
