package com.academic.fh.model;

import jakarta.persistence.*;

@Entity
@Table(name = "variante_atributos")
public class VarianteAtributo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "variante_id")
    private ProductoVariante variante;

    @ManyToOne
    @JoinColumn(name = "atributo_id")
    private Atributo atributo;

    @ManyToOne
    @JoinColumn(name = "valor_id")
    private AtributoValor valor;

    private String valorTexto;

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ProductoVariante getVariante() { return variante; }
    public void setVariante(ProductoVariante variante) { this.variante = variante; }

    public Atributo getAtributo() { return atributo; }
    public void setAtributo(Atributo atributo) { this.atributo = atributo; }

    public AtributoValor getValor() { return valor; }
    public void setValor(AtributoValor valor) { this.valor = valor; }

    public String getValorTexto() { return valorTexto; }
    public void setValorTexto(String valorTexto) { this.valorTexto = valorTexto; }
}
