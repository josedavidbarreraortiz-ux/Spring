package com.academic.fh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "producto_variantes")
public class ProductoVariante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer varianteId;

    private String sku;

    private Double precioExtra;

    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @OneToMany(mappedBy = "variante")
    @JsonIgnore
    private List<VarianteAtributo> atributos;

    // Getters & Setters
    public Integer getVarianteId() { return varianteId; }
    public void setVarianteId(Integer varianteId) { this.varianteId = varianteId; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public Double getPrecioExtra() { return precioExtra; }
    public void setPrecioExtra(Double precioExtra) { this.precioExtra = precioExtra; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public List<VarianteAtributo> getAtributos() { return atributos; }
    public void setAtributos(List<VarianteAtributo> atributos) { this.atributos = atributos; }
}
