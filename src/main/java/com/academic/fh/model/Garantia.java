package com.academic.fh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "garantias")
public class Garantia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "garantia_id")
    private Integer garantiaId;

    @Column(name = "nombre_garantia")
    private String nombreGarantia;

    @Column(name = "duracion_meses")
    private Integer duracionMeses;

    @Column(columnDefinition = "TEXT")
    private String cobertura;

    private Integer estado;

    @OneToMany(mappedBy = "garantia")
    @JsonIgnore
    private List<ProductoGarantia> productosGarantia;

    // Getters & Setters

    public Integer getGarantiaId() {
        return garantiaId;
    }

    public void setGarantiaId(Integer garantiaId) {
        this.garantiaId = garantiaId;
    }

    public String getNombreGarantia() {
        return nombreGarantia;
    }

    public void setNombreGarantia(String nombreGarantia) {
        this.nombreGarantia = nombreGarantia;
    }

    public Integer getDuracionMeses() {
        return duracionMeses;
    }

    public void setDuracionMeses(Integer duracionMeses) {
        this.duracionMeses = duracionMeses;
    }

    public String getCobertura() {
        return cobertura;
    }

    public void setCobertura(String cobertura) {
        this.cobertura = cobertura;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public List<ProductoGarantia> getProductosGarantia() {
        return productosGarantia;
    }

    public void setProductosGarantia(List<ProductoGarantia> productosGarantia) {
        this.productosGarantia = productosGarantia;
    }
}
