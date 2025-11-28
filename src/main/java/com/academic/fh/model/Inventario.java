package com.academic.fh.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventario_id")
    private Integer inventarioId;

    @Column(name = "inventario_stock_actual")
    private Integer inventarioStockActual;

    @Column(name = "inventario_stock_minimo")
    private Integer inventarioStockMinimo;

    @Column(name = "inventario_stock_maximo")
    private Integer inventarioStockMaximo;

    @Column(name = "inventario_ubicacion")
    private String inventarioUbicacion;

    @Column(name = "inventario_fecha_actualizacion")
    private LocalDate inventarioFechaActualizacion;

    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters & Setters

    public Integer getInventarioId() {
        return inventarioId;
    }

    public void setInventarioId(Integer inventarioId) {
        this.inventarioId = inventarioId;
    }

    public Integer getInventarioStockActual() {
        return inventarioStockActual;
    }

    public void setInventarioStockActual(Integer inventarioStockActual) {
        this.inventarioStockActual = inventarioStockActual;
    }

    public Integer getInventarioStockMinimo() {
        return inventarioStockMinimo;
    }

    public void setInventarioStockMinimo(Integer inventarioStockMinimo) {
        this.inventarioStockMinimo = inventarioStockMinimo;
    }

    public Integer getInventarioStockMaximo() {
        return inventarioStockMaximo;
    }

    public void setInventarioStockMaximo(Integer inventarioStockMaximo) {
        this.inventarioStockMaximo = inventarioStockMaximo;
    }

    public String getInventarioUbicacion() {
        return inventarioUbicacion;
    }

    public void setInventarioUbicacion(String inventarioUbicacion) {
        this.inventarioUbicacion = inventarioUbicacion;
    }

    public LocalDate getInventarioFechaActualizacion() {
        return inventarioFechaActualizacion;
    }

    public void setInventarioFechaActualizacion(LocalDate inventarioFechaActualizacion) {
        this.inventarioFechaActualizacion = inventarioFechaActualizacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
