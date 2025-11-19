package com.academic.fh.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer productoId;

    private String productoCodigoBar;
    private String productoNombre;
    private String tipoUnidad;
    private BigDecimal productoPrecioVenta;
    private String productoMarca;
    private String productoEstado;
    private String foto;
    private String especificacion;
    private String resumen;

    // Categoría principal
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoriaPrincipal;

    // Relación con inventario
    @OneToMany(mappedBy = "producto")
    @JsonIgnore
    private List<Inventario> inventarios;

    // Relación con atributos del producto (tabla pivote)
    @OneToMany(mappedBy = "producto")
    @JsonIgnore
    private List<ProductoAtributo> atributos;

    // Variantes del producto
    @OneToMany(mappedBy = "producto")
    @JsonIgnore
    private List<ProductoVariante> variantes;

    // Relación con movimientos de inventario
    @OneToMany(mappedBy = "producto")
    @JsonIgnore
    private List<MovimientoInventario> movimientosInventario;

    // Relación con garantías del producto
    @OneToMany(mappedBy = "producto")
    @JsonIgnore
    private List<ProductoGarantia> garantias;

    // Relación con venta_detalle
    @OneToMany(mappedBy = "producto")
    @JsonIgnore
    private List<VentaDetalle> ventaDetalles;

    // Relación N-N con categorías secundarias (tabla pivote producto_categoria)
    @OneToMany(mappedBy = "producto")
    @JsonIgnore
    private List<ProductoCategoria> categoriasSecundarias;

    // Getters & Setters

    public Integer getProductoId() { return productoId; }
    public void setProductoId(Integer productoId) { this.productoId = productoId; }

    public String getProductoCodigoBar() { return productoCodigoBar; }
    public void setProductoCodigoBar(String productoCodigoBar) { this.productoCodigoBar = productoCodigoBar; }

    public String getProductoNombre() { return productoNombre; }
    public void setProductoNombre(String productoNombre) { this.productoNombre = productoNombre; }

    public String getTipoUnidad() { return tipoUnidad; }
    public void setTipoUnidad(String tipoUnidad) { this.tipoUnidad = tipoUnidad; }

    public BigDecimal getProductoPrecioVenta() { return productoPrecioVenta; }
    public void setProductoPrecioVenta(BigDecimal productoPrecioVenta) { this.productoPrecioVenta = productoPrecioVenta; }

    public String getProductoMarca() { return productoMarca; }
    public void setProductoMarca(String productoMarca) { this.productoMarca = productoMarca; }

    public String getProductoEstado() { return productoEstado; }
    public void setProductoEstado(String productoEstado) { this.productoEstado = productoEstado; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public String getEspecificacion() { return especificacion; }
    public void setEspecificacion(String especificacion) { this.especificacion = especificacion; }

    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }

    public Categoria getCategoriaPrincipal() { return categoriaPrincipal; }
    public void setCategoriaPrincipal(Categoria categoriaPrincipal) { this.categoriaPrincipal = categoriaPrincipal; }

    public List<Inventario> getInventarios() { return inventarios; }
    public void setInventarios(List<Inventario> inventarios) { this.inventarios = inventarios; }

    public List<ProductoAtributo> getAtributos() { return atributos; }
    public void setAtributos(List<ProductoAtributo> atributos) { this.atributos = atributos; }

    public List<ProductoVariante> getVariantes() { return variantes; }
    public void setVariantes(List<ProductoVariante> variantes) { this.variantes = variantes; }

    public List<MovimientoInventario> getMovimientosInventario() { return movimientosInventario; }
    public void setMovimientosInventario(List<MovimientoInventario> movimientosInventario) { this.movimientosInventario = movimientosInventario; }

    public List<ProductoGarantia> getGarantias() { return garantias; }
    public void setGarantias(List<ProductoGarantia> garantias) { this.garantias = garantias; }

    public List<VentaDetalle> getVentaDetalles() { return ventaDetalles; }
    public void setVentaDetalles(List<VentaDetalle> ventaDetalles) { this.ventaDetalles = ventaDetalles; }

    public List<ProductoCategoria> getCategoriasSecundarias() { return categoriasSecundarias; }
    public void setCategoriasSecundarias(List<ProductoCategoria> categoriasSecundarias) { this.categoriasSecundarias = categoriasSecundarias; }
}
