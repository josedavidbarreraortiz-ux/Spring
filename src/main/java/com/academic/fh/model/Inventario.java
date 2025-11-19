package com.academic.fh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "inventario")
public class Inventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer inventarioId;

    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "inventario")
    @JsonIgnore
    private List<MovimientoInventario> movimientos;

    // Getters & Setters

    public Integer getInventarioId() { return inventarioId; }
    public void setInventarioId(Integer inventarioId) { this.inventarioId = inventarioId; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public Producto getProducto() { return producto; }
    public void setProducto(Producto producto) { this.producto = producto; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<MovimientoInventario> getMovimientos() { return movimientos; }
    public void setMovimientos(List<MovimientoInventario> movimientos) { this.movimientos = movimientos; }
}
