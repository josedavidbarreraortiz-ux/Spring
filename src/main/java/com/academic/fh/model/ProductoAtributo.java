package com.academic.fh.model;

import jakarta.persistence.*;

@Entity
@Table(name = "producto_atributos")
public class ProductoAtributo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "atributo_id")
    private Atributo atributo;

    @ManyToOne
    @JoinColumn(name = "valor_id")
    private AtributoValor valor;

    private String valorTexto;

    private Double valorNumero;

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Atributo getAtributo() { return atributo; }
    public void setAtributo(Atributo atributo) { this.atributo = atributo; }

    public AtributoValor getValor() { return valor; }
    public void setValor(AtributoValor valor) { this.valor = valor; }

    public String getValorTexto() { return valorTexto; }
    public void setValorTexto(String valorTexto) { this.valorTexto = valorTexto; }

    public Double getValorNumero() { return valorNumero; }
    public void setValorNumero(Double valorNumero) { this.valorNumero = valorNumero; }
}
