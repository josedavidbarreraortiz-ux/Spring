package com.academic.fh.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "producto_garantia")
public class ProductoGarantia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_garantia_id")
    private Integer productoGarantiaId;

    @Column(name = "fecha_compra")
    private LocalDate fechaCompra;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "estado_garantia")
    private String estadoGarantia;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "garantia_id")
    private Garantia garantia;

    // Getters & Setters

    public Integer getProductoGarantiaId() {
        return productoGarantiaId;
    }

    public void setProductoGarantiaId(Integer productoGarantiaId) {
        this.productoGarantiaId = productoGarantiaId;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstadoGarantia() {
        return estadoGarantia;
    }

    public void setEstadoGarantia(String estadoGarantia) {
        this.estadoGarantia = estadoGarantia;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Garantia getGarantia() {
        return garantia;
    }

    public void setGarantia(Garantia garantia) {
        this.garantia = garantia;
    }
}
