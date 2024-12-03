package br.com.rodrigo.dataintegration.entity;

import jakarta.persistence.*;
import jakarta.validation.*;
import lombok.*;

import java.io.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "tb_context_operation")
public class Operation implements Serializable {

    @Serial
    private static final long serialVersionUID = 39530539L;

    @Id
    @Column(name = "uuid", length = 36)
    private String uuid;

    @Column(name = "uuid_oper_prod", length = 36)
    private String productOperationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "context_id", nullable = false)
    private @Valid Context context;

    @Column(name = "sequential")
    private Long sequential;

    @Column(name = "metadata", length = 1024)
    private String metadata;
}
