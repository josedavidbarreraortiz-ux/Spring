package com.academic.fh.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ubicacion")
public class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ubicacion_id")
    private Integer ubicacionId;

    @Column(name = "ubicacion_cod")
    private String ubicacionCod;

    @Column(name = "ubicacion_nombre")
    private String ubicacionNombre;

    @Column(name = "ubicacion_tipo")
    private String ubicacionTipo;

    @Column(name = "ubicacion_bodega")
    private String ubicacionBodega;

    private Integer pasillo;

    private Integer estante;

    private Integer nivel;

    private String capacidad;

    @Column(name = "ubicacion_estado")
    private Integer ubicacionEstado;

    @Column(name = "ubicacion_descripcion", columnDefinition = "TEXT")
    private String ubicacionDescripcion;

    // Getters & Setters

    public Integer getUbicacionId() {
        return ubicacionId;
    }

    public void setUbicacionId(Integer ubicacionId) {
        this.ubicacionId = ubicacionId;
    }

    public String getUbicacionCod() {
        return ubicacionCod;
    }

    public void setUbicacionCod(String ubicacionCod) {
        this.ubicacionCod = ubicacionCod;
    }

    public String getUbicacionNombre() {
        return ubicacionNombre;
    }

    public void setUbicacionNombre(String ubicacionNombre) {
        this.ubicacionNombre = ubicacionNombre;
    }

    public String getUbicacionTipo() {
        return ubicacionTipo;
    }

    public void setUbicacionTipo(String ubicacionTipo) {
        this.ubicacionTipo = ubicacionTipo;
    }

    public String getUbicacionBodega() {
        return ubicacionBodega;
    }

    public void setUbicacionBodega(String ubicacionBodega) {
        this.ubicacionBodega = ubicacionBodega;
    }

    public Integer getPasillo() {
        return pasillo;
    }

    public void setPasillo(Integer pasillo) {
        this.pasillo = pasillo;
    }

    public Integer getEstante() {
        return estante;
    }

    public void setEstante(Integer estante) {
        this.estante = estante;
    }

    public Integer getNivel() {
        return nivel;
    }

    public void setNivel(Integer nivel) {
        this.nivel = nivel;
    }

    public String getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(String capacidad) {
        this.capacidad = capacidad;
    }

    public Integer getUbicacionEstado() {
        return ubicacionEstado;
    }

    public void setUbicacionEstado(Integer ubicacionEstado) {
        this.ubicacionEstado = ubicacionEstado;
    }

    public String getUbicacionDescripcion() {
        return ubicacionDescripcion;
    }

    public void setUbicacionDescripcion(String ubicacionDescripcion) {
        this.ubicacionDescripcion = ubicacionDescripcion;
    }
}
