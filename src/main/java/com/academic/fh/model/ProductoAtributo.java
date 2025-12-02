package com.academic.fh.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "producto_atributos")
@IdClass(ProductoAtributoId.class)
public class ProductoAtributo implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @Id
    @ManyToOne
    @JoinColumn(name = "atributo_id")
    private Atributo atributo;

    @ManyToOne
    @JoinColumn(name = "valor_id")
    private AtributoValor valor;

    @Column(name = "valor_texto")
    private String valorTexto;

    @Column(name = "valor_numero")
    private Double valorNumero;

    // Getters & Setters
    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Atributo getAtributo() {
        return atributo;
    }

    public void setAtributo(Atributo atributo) {
        this.atributo = atributo;
    }

    public AtributoValor getValor() {
        return valor;
    }

    public void setValor(AtributoValor valor) {
        this.valor = valor;
    }

    public String getValorTexto() {
        return valorTexto;
    }

    public void setValorTexto(String valorTexto) {
        this.valorTexto = valorTexto;
    }

    public Double getValorNumero() {
        return valorNumero;
    }

    public void setValorNumero(Double valorNumero) {
        this.valorNumero = valorNumero;
    }
}
