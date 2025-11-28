package com.academic.fh.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "venta_detalle")
public class VentaDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venta_detalle_id")
    private Integer ventaDetalleId;

    @Column(name = "venta_detalle_cantidad")
    private Integer ventaDetalleCantidad;

    @Column(name = "venta_detalle_precio_venta")
    private BigDecimal ventaDetallePrecioVenta;

    @Column(name = "venta_detalle_iva")
    private BigDecimal ventaDetalleIva;

    @Column(name = "venta_detalle_descripcion")
    private String ventaDetalleDescripcion;

    private BigDecimal subtotal;

    @ManyToOne
    @JoinColumn(name = "venta_codigo")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    // Getters & Setters

    public Integer getVentaDetalleId() {
        return ventaDetalleId;
    }

    public void setVentaDetalleId(Integer ventaDetalleId) {
        this.ventaDetalleId = ventaDetalleId;
    }

    public Integer getVentaDetalleCantidad() {
        return ventaDetalleCantidad;
    }

    public void setVentaDetalleCantidad(Integer ventaDetalleCantidad) {
        this.ventaDetalleCantidad = ventaDetalleCantidad;
    }

    public BigDecimal getVentaDetallePrecioVenta() {
        return ventaDetallePrecioVenta;
    }

    public void setVentaDetallePrecioVenta(BigDecimal ventaDetallePrecioVenta) {
        this.ventaDetallePrecioVenta = ventaDetallePrecioVenta;
    }

    public BigDecimal getVentaDetalleIva() {
        return ventaDetalleIva;
    }

    public void setVentaDetalleIva(BigDecimal ventaDetalleIva) {
        this.ventaDetalleIva = ventaDetalleIva;
    }

    public String getVentaDetalleDescripcion() {
        return ventaDetalleDescripcion;
    }

    public void setVentaDetalleDescripcion(String ventaDetalleDescripcion) {
        this.ventaDetalleDescripcion = ventaDetalleDescripcion;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
}
