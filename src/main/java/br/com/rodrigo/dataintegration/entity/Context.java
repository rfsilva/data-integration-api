package br.com.rodrigo.dataintegration.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "tb_context")
public class Context implements Serializable {

    private static final long serialVersionUID = 239052353L;

    @Id
    @Column(name = "context_id", nullable = false, unique = true)
    private Long internalId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "inst_code")
    private Long institution;

    @Column(name = "branch_code")
    private Long branch;

    @Column(name = "type")
    private Integer type;

    @Column(name = "sequential")
    private Long sequential;

    @Column(name = "status")
    private Integer status;
}

