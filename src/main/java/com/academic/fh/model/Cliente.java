package com.academic.fh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer clienteId;

    private String clienteNombre;
    private String clienteTipoDocumento;
    private String clienteNumeroDocumento;
    private String clienteDireccion;
    private String clienteTelefono;
    private String clienteEmail;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<Venta> ventas;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<PQRS> pqrsList;

    // Getters y Setters

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public String getClienteTipoDocumento() {
        return clienteTipoDocumento;
    }

    public void setClienteTipoDocumento(String clienteTipoDocumento) {
        this.clienteTipoDocumento = clienteTipoDocumento;
    }

    public String getClienteNumeroDocumento() {
        return clienteNumeroDocumento;
    }

    public void setClienteNumeroDocumento(String clienteNumeroDocumento) {
        this.clienteNumeroDocumento = clienteNumeroDocumento;
    }

    public String getClienteDireccion() {
        return clienteDireccion;
    }

    public void setClienteDireccion(String clienteDireccion) {
        this.clienteDireccion = clienteDireccion;
    }

    public String getClienteTelefono() {
        return clienteTelefono;
    }

    public void setClienteTelefono(String clienteTelefono) {
        this.clienteTelefono = clienteTelefono;
    }

    public String getClienteEmail() {
        return clienteEmail;
    }

    public void setClienteEmail(String clienteEmail) {
        this.clienteEmail = clienteEmail;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    public void setVentas(List<Venta> ventas) {
        this.ventas = ventas;
    }

    public List<PQRS> getPqrsList() {
        return pqrsList;
    }

    public void setPqrsList(List<PQRS> pqrsList) {
        this.pqrsList = pqrsList;
    }
}
