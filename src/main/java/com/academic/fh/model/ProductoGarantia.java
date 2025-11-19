package com.academic.fh.model;

import jakarta.persistence.*;

@Entity
@Table(name = "producto_garantias")
public class ProductoGarantia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "garantia_id")
    private Garantia garantia;

    private Integer mesesDuracion;

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public Garantia getGarantia() { return garantia; }
    public void setGarantia(Garantia garantia) { this.garantia = garantia; }

    public Integer getMesesDuracion() { return mesesDuracion; }
    public void setMesesDuracion(Integer mesesDuracion) { this.mesesDuracion = mesesDuracion; }
}
