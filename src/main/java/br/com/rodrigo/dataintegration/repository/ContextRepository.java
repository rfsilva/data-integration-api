package br.com.rodrigo.dataintegration.repository;

import br.com.rodrigo.dataintegration.entity.Context;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface ContextRepository extends JpaRepository<Context, Long> {

    @Query(value = "select c from Context c where c.date = :date and c.institution = :institution and c.branch = :branch and c.type = :type and c.sequential = :sequential")
    Optional<Context> getContext(
            @Param("date") LocalDate date,
            @Param("institution") Long institution,
            @Param("branch") Long branch,
            @Param("type") Integer type,
            @Param("sequential") Long sequential);
}
