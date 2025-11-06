package com.ongreport.narraterpro.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
//@AllArgsConstructor

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "idUsuario") // opcional por conta do PhysicalNamingStrategyStandardImpl no properties
    private Long idUsuario;

    private LocalDate data_entrada;

    private String nome;
    private String sobrenome;
    private String cpf;
    private String email;
    private String whatsapp;
    private String telefone_1;
    private String telefone_2;
    private String senha;
    private Integer pontuacao;
    private Integer adm;
    private String funcao;

    // Relacionamento: Muitos Agendamentos para uma Atividade
    @ManyToOne
    @JoinColumn(name = "Cidade_idCidade", nullable = false)
    private Cidade cidade;

}
