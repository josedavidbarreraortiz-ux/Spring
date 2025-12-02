package com.academic.fh.model;

import java.io.Serializable;
import java.util.Objects;

public class ProductoAtributoId implements Serializable {

    private Integer producto;
    private Integer atributo;

    public ProductoAtributoId() {
    }

    public ProductoAtributoId(Integer producto, Integer atributo) {
        this.producto = producto;
        this.atributo = atributo;
    }

    public Integer getProducto() {
        return producto;
    }

    public void setProducto(Integer producto) {
        this.producto = producto;
    }

    public Integer getAtributo() {
        return atributo;
    }

    public void setAtributo(Integer atributo) {
        this.atributo = atributo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProductoAtributoId that = (ProductoAtributoId) o;
        return Objects.equals(producto, that.producto) &&
                Objects.equals(atributo, that.atributo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producto, atributo);
    }
}
