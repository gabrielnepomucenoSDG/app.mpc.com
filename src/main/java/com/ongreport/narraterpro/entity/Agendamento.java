package com.ongreport.narraterpro.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
//@AllArgsConstructor
@Entity
@Table(name = "agendamento")
public class Agendamento {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAgendamento;

//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate data_inicio;

//    @Temporal(TemporalType.DATE)
    private LocalDate data_fim;

//    @Temporal(TemporalType.DATE)
    private LocalDateTime data_hora_criacao;

    private String turno;
    private String local;
    private Integer publico_estimado;
    private String feedback_observ;
    private BigDecimal custos;
    private Integer favorito;
    private Integer recorrencia;
    private Integer exclusao_logica;

    // Relacionamento: Muitos Agendamentos para uma Atividade
    @ManyToOne
    @JoinColumn(name = "Atividade_idAtividade", nullable = false)
    private Atividade atividade;

    // Relacionamento: Muitos Agendamentos para um Usuário
    @ManyToOne
    @JoinColumn(name = "Usuario_idUsuario", nullable = false)
    private Usuario usuario;

    // Getters e Setters...
}