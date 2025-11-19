package com.academic.fh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "metodo_pago")
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer metodoId;

    private String metodoNombre;

    @OneToMany(mappedBy = "metodoPago")
    @JsonIgnore
    private List<Pago> pagos;

    // Getters & Setters
    public Integer getMetodoId() { return metodoId; }
    public void setMetodoId(Integer metodoId) { this.metodoId = metodoId; }

    public String getMetodoNombre() { return metodoNombre; }
    public void setMetodoNombre(String metodoNombre) { this.metodoNombre = metodoNombre; }

    public List<Pago> getPagos() { return pagos; }
    public void setPagos(List<Pago> pagos) { this.pagos = pagos; }
}
