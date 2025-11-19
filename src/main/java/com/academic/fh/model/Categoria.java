package com.academic.fh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoriaId;

    private String categoriaNombre;

    private String descripcion;

    private Integer estado;

    // Relación con categoría padre
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Categoria parent;

    // Subcategorías
    @OneToMany(mappedBy = "parent")
    @JsonIgnore
    private List<Categoria> subcategorias;

    // Relación N-N con productos (tabla pivote producto_categoria)
    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<ProductoCategoria> productos;

    // Relación con productos como categoría principal (productos.categoria_id)
    @OneToMany(mappedBy = "categoriaPrincipal")
    @JsonIgnore
    private List<Producto> productosPrincipales;

    // Getters & Setters
    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }

    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getEstado() { return estado; }
    public void setEstado(Integer estado) { this.estado = estado; }

    public Categoria getParent() { return parent; }
    public void setParent(Categoria parent) { this.parent = parent; }

    public List<Categoria> getSubcategorias() { return subcategorias; }
    public void setSubcategorias(List<Categoria> subcategorias) { this.subcategorias = subcategorias; }

    public List<ProductoCategoria> getProductos() { return productos; }
    public void setProductos(List<ProductoCategoria> productos) { this.productos = productos; }

    public List<Producto> getProductosPrincipales() { return productosPrincipales; }
    public void setProductosPrincipales(List<Producto> productosPrincipales) { this.productosPrincipales = productosPrincipales; }
}
