package com.academic.fh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "ventas")
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venta_codigo")
    private Integer ventaId;

    @Column(name = "venta_fecha")
    private LocalDate ventaFecha;

    @Column(name = "venta_hora")
    private LocalTime ventaHora;

    @Column(name = "venta_total")
    private BigDecimal ventaTotal;

    private Integer cantidad;

    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "metodo_pago_id")
    private MetodoPago metodoPago;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<VentaDetalle> detalles;

    @OneToMany(mappedBy = "venta")
    @JsonIgnore
    private List<Pago> pagos;

    @OneToMany(mappedBy = "venta")
    @JsonIgnore
    private List<MovimientoInventario> movimientos;

    // Getters & Setters

    public Integer getVentaId() {
        return ventaId;
    }

    public void setVentaId(Integer ventaId) {
        this.ventaId = ventaId;
    }

    public LocalDate getVentaFecha() {
        return ventaFecha;
    }

    public void setVentaFecha(LocalDate ventaFecha) {
        this.ventaFecha = ventaFecha;
    }

    public LocalTime getVentaHora() {
        return ventaHora;
    }

    public void setVentaHora(LocalTime ventaHora) {
        this.ventaHora = ventaHora;
    }

    public BigDecimal getVentaTotal() {
        return ventaTotal;
    }

    public void setVentaTotal(BigDecimal ventaTotal) {
        this.ventaTotal = ventaTotal;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }

    public List<VentaDetalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<VentaDetalle> detalles) {
        this.detalles = detalles;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public List<MovimientoInventario> getMovimientos() {
        return movimientos;
    }

    public void setMovimientos(List<MovimientoInventario> movimientos) {
        this.movimientos = movimientos;
    }
}
