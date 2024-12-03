package br.com.rodrigo.dataintegration.repository;

import br.com.rodrigo.dataintegration.entity.*;
import org.springframework.data.jpa.repository.*;

import java.util.*;

public interface OperationRepository extends JpaRepository<Operation, String> {

    Optional<Operation> findByProductOperationId(String productOperationId);
}
