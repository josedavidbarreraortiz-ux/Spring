package com.academic.fh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "garantias")
public class Garantia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer garantiaId;

    private String garantiaNombre;

    private String descripcion;

    @OneToMany(mappedBy = "garantia")
    @JsonIgnore
    private List<ProductoGarantia> productos;

    // Getters & Setters
    public Integer getGarantiaId() { return garantiaId; }
    public void setGarantiaId(Integer garantiaId) { this.garantiaId = garantiaId; }

    public String getGarantiaNombre() { return garantiaNombre; }
    public void setGarantiaNombre(String garantiaNombre) { this.garantiaNombre = garantiaNombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<ProductoGarantia> getProductos() { return productos; }
    public void setProductos(List<ProductoGarantia> productos) { this.productos = productos; }
}
