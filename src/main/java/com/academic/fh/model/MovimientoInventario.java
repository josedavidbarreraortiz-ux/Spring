package com.academic.fh.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "movimientos_inventario")
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movimiento_id")
    private Integer movimientoId;

    @Column(name = "movimiento_tipo")
    private String movimientoTipo;

    @Column(name = "movimiento_cantidad")
    private Integer movimientoCantidad;

    @Column(name = "movimiento_fecha")
    private LocalDate movimientoFecha;

    @Column(name = "movimiento_motivo")
    private String movimientoMotivo;

    @Column(name = "movimiento_stock_anterior")
    private Integer movimientoStockAnterior;

    @Column(name = "movimiento_stock_nuevo")
    private Integer movimientoStockNuevo;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    // Getters & Setters

    public Integer getMovimientoId() {
        return movimientoId;
    }

    public void setMovimientoId(Integer movimientoId) {
        this.movimientoId = movimientoId;
    }

    public String getMovimientoTipo() {
        return movimientoTipo;
    }

    public void setMovimientoTipo(String movimientoTipo) {
        this.movimientoTipo = movimientoTipo;
    }

    public Integer getMovimientoCantidad() {
        return movimientoCantidad;
    }

    public void setMovimientoCantidad(Integer movimientoCantidad) {
        this.movimientoCantidad = movimientoCantidad;
    }

    public LocalDate getMovimientoFecha() {
        return movimientoFecha;
    }

    public void setMovimientoFecha(LocalDate movimientoFecha) {
        this.movimientoFecha = movimientoFecha;
    }

    public String getMovimientoMotivo() {
        return movimientoMotivo;
    }

    public void setMovimientoMotivo(String movimientoMotivo) {
        this.movimientoMotivo = movimientoMotivo;
    }

    public Integer getMovimientoStockAnterior() {
        return movimientoStockAnterior;
    }

    public void setMovimientoStockAnterior(Integer movimientoStockAnterior) {
        this.movimientoStockAnterior = movimientoStockAnterior;
    }

    public Integer getMovimientoStockNuevo() {
        return movimientoStockNuevo;
    }

    public void setMovimientoStockNuevo(Integer movimientoStockNuevo) {
        this.movimientoStockNuevo = movimientoStockNuevo;
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

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }
}
