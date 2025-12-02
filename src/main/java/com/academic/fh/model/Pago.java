package com.academic.fh.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @Column(name = "pago_id")
    private Integer pagoId;

    @Column(name = "pago_monto")
    private BigDecimal pagoMonto;

    @Column(name = "pago_fecha")
    private LocalDate pagoFecha;

    @Column(name = "pago_referencia")
    private String pagoReferencia;

    @Column(name = "pago_estado")
    private String pagoEstado;

    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "venta_id")
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "metodo_pago_id")
    private MetodoPago metodoPago;

    // Getters & Setters

    public Integer getPagoId() {
        return pagoId;
    }

    public void setPagoId(Integer pagoId) {
        this.pagoId = pagoId;
    }

    public BigDecimal getPagoMonto() {
        return pagoMonto;
    }

    public void setPagoMonto(BigDecimal pagoMonto) {
        this.pagoMonto = pagoMonto;
    }

    public LocalDate getPagoFecha() {
        return pagoFecha;
    }

    public void setPagoFecha(LocalDate pagoFecha) {
        this.pagoFecha = pagoFecha;
    }

    public String getPagoReferencia() {
        return pagoReferencia;
    }

    public void setPagoReferencia(String pagoReferencia) {
        this.pagoReferencia = pagoReferencia;
    }

    public String getPagoEstado() {
        return pagoEstado;
    }

    public void setPagoEstado(String pagoEstado) {
        this.pagoEstado = pagoEstado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public MetodoPago getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(MetodoPago metodoPago) {
        this.metodoPago = metodoPago;
    }
}
