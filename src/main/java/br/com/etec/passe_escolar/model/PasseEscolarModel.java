package br.com.etec.passe_escolar.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_passe_escolar")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasseEscolarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do estudante é obrigatório.")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
    @Column(name = "nome_estudante", nullable = false, length = 100)
    private String nomeEstudante;

    @NotBlank(message = "O CPF é obrigatório.")
    @Column(name = "cpf", nullable = false, length = 14, unique = true)
    private String cpf;

    @NotBlank(message = "A instituição de ensino é obrigatória.")
    @Column(name = "instituicao_ensino", nullable = false, length = 100)
    private String instituicaoEnsino;

    @PositiveOrZero(message = "O saldo de recarga não pode ser negativo.")
    @Column(name = "saldo_recarga", nullable = false, precision = 10, scale = 2)
    private BigDecimal saldoRecarga;

    @Column(name = "ativo", nullable = false, length = 10)
    private String ativo;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDateTime dataSolicitacao;
}