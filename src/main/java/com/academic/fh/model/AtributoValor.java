package com.academic.fh.model;

import jakarta.persistence.*;



@Entity
@Table(name = "atributo_valores")
public class AtributoValor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer valorId;

    private String valor;

    @ManyToOne
    @JoinColumn(name = "atributo_id")
    private Atributo atributo;

    // Getters & Setters
    public Integer getValorId() { return valorId; }
    public void setValorId(Integer valorId) { this.valorId = valorId; }

    public String getValor() { return valor; }
    public void setValor(String valor) { this.valor = valor; }

    public Atributo getAtributo() { return atributo; }
    public void setAtributo(Atributo atributo) { this.atributo = atributo; }
}
                                                