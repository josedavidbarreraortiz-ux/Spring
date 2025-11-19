package com.academic.fh.model;

import jakarta.persistence.*;

@Entity
@Table(name = "movimientos_inventario")
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer movimientoId;

    private String tipoMovimiento;

    private Integer cantidad;

    private Integer movimientoStockAnterior;

    private Integer movimientoStockNuevo;

    @ManyToOne
    @JoinColumn(name = "inventario_id")
    private Inventario inventario;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters & Setters

    public Integer getMovimientoId() { return movimientoId; }
    public void setMovimientoId(Integer movimientoId) { this.movimientoId = movimientoId; }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public void setTipoMovimiento(String tipoMovimiento) { this.tipoMovimiento = tipoMovimiento; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Integer getMovimientoStockAnterior() { return movimientoStockAnterior; }
    public void setMovimientoStockAnterior(Integer movimientoStockAnterior) { this.movimientoStockAnterior = movimientoStockAnterior; }

    public Integer getMovimientoStockNuevo() { return movimientoStockNuevo; }
    public void setMovimientoStockNuevo(Integer movimientoStockNuevo) { this.movimientoStockNuevo = movimientoStockNuevo; }

    public Inventario getInventario() { return inventario; }
    public void setInventario(Inventario inventario) { this.inventario = inventario; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
