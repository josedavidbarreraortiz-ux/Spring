package com.academic.fh.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pqrs")
public class PQRS {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pqrsId;

    private String tipo;

    private String descripcion;

    private LocalDateTime fecha;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters & Setters
    public Integer getPqrsId() { return pqrsId; }
    public void setPqrsId(Integer pqrsId) { this.pqrsId = pqrsId; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
