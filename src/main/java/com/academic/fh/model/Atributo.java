package com.academic.fh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "atributos")
public class Atributo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer atributoId;

    private String atributoNombre;

    private String atributoTipo;

    @OneToMany(mappedBy = "atributo")
    @JsonIgnore
    private List<AtributoValor> valores;

    @OneToMany(mappedBy = "atributo")
    @JsonIgnore
    private List<ProductoAtributo> productoAtributos;

    @OneToMany(mappedBy = "atributo")
    @JsonIgnore
    private List<VarianteAtributo> varianteAtributos;

    // Getters & Setters
    public Integer getAtributoId() { return atributoId; }
    public void setAtributoId(Integer atributoId) { this.atributoId = atributoId; }

    public String getAtributoNombre() { return atributoNombre; }
    public void setAtributoNombre(String atributoNombre) { this.atributoNombre = atributoNombre; }

    public String getAtributoTipo() { return atributoTipo; }
    public void setAtributoTipo(String atributoTipo) { this.atributoTipo = atributoTipo; }

    public List<AtributoValor> getValores() { return valores; }
    public void setValores(List<AtributoValor> valores) { this.valores = valores; }

    public List<ProductoAtributo> getProductoAtributos() { return productoAtributos; }
    public void setProductoAtributos(List<ProductoAtributo> productoAtributos) { this.productoAtributos = productoAtributos; }

    public List<VarianteAtributo> getVarianteAtributos() { return varianteAtributos; }
    public void setVarianteAtributos(List<VarianteAtributo> varianteAtributos) { this.varianteAtributos = varianteAtributos; }
}
