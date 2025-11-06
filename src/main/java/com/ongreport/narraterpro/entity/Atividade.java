package com.ongreport.narraterpro.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@AllArgsConstructor
@Entity
@Table(name = "atividade")
public class Atividade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAtividade;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "Projeto_idProjeto", nullable = false)
    private Projeto projeto;
}
