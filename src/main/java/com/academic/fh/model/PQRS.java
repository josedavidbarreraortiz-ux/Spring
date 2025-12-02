package com.academic.fh.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pqrs")
public class PQRS {

    @Id
    @Column(name = "pqrs_id")
    private Integer pqrsId;

    @Column(name = "tipo_pqrs")
    private String tipoPqrs;

    @Column(name = "estado_pqrs")
    private String estadoPqrs;

    @Column(name = "prioridad_pqrs")
    private String prioridadPqrs;

    @Column(name = "categoria_pqrs")
    private String categoriaPqrs;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @Column(name = "fecha_respuesta")
    private LocalDate fechaRespuesta;

    @Column(columnDefinition = "TEXT")
    private String seguimiento;

    @Column(name = "respuesta_pqrs", columnDefinition = "TEXT")
    private String respuestaPqrs;

    @Column(name = "archivo_pqrs")
    private String archivoPqrs;

    @Column(name = "evaluacion_pqrs")
    private Integer evaluacionPqrs;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters & Setters

    public Integer getPqrsId() {
        return pqrsId;
    }

    public void setPqrsId(Integer pqrsId) {
        this.pqrsId = pqrsId;
    }

    public String getTipoPqrs() {
        return tipoPqrs;
    }

    public void setTipoPqrs(String tipoPqrs) {
        this.tipoPqrs = tipoPqrs;
    }

    public String getEstadoPqrs() {
        return estadoPqrs;
    }

    public void setEstadoPqrs(String estadoPqrs) {
        this.estadoPqrs = estadoPqrs;
    }

    public String getPrioridadPqrs() {
        return prioridadPqrs;
    }

    public void setPrioridadPqrs(String prioridadPqrs) {
        this.prioridadPqrs = prioridadPqrs;
    }

    public String getCategoriaPqrs() {
        return categoriaPqrs;
    }

    public void setCategoriaPqrs(String categoriaPqrs) {
        this.categoriaPqrs = categoriaPqrs;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDate getFechaRespuesta() {
        return fechaRespuesta;
    }

    public void setFechaRespuesta(LocalDate fechaRespuesta) {
        this.fechaRespuesta = fechaRespuesta;
    }

    public String getSeguimiento() {
        return seguimiento;
    }

    public void setSeguimiento(String seguimiento) {
        this.seguimiento = seguimiento;
    }

    public String getRespuestaPqrs() {
        return respuestaPqrs;
    }

    public void setRespuestaPqrs(String respuestaPqrs) {
        this.respuestaPqrs = respuestaPqrs;
    }

    public String getArchivoPqrs() {
        return archivoPqrs;
    }

    public void setArchivoPqrs(String archivoPqrs) {
        this.archivoPqrs = archivoPqrs;
    }

    public Integer getEvaluacionPqrs() {
        return evaluacionPqrs;
    }

    public void setEvaluacionPqrs(Integer evaluacionPqrs) {
        this.evaluacionPqrs = evaluacionPqrs;
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
}
