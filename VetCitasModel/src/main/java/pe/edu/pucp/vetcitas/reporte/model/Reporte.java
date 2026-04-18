package pe.edu.pucp.vetcitas.reporte.model;

import pe.edu.pucp.vetcitas.common.enums.TipoReporte;

import java.time.LocalDateTime;

public class Reporte {
    private int id;
    private TipoReporte tipo;
    private LocalDateTime periodoInicio;
    private LocalDateTime periodoFin;
    private LocalDateTime fechaGeneracion;
    private String descripcion;
    private String formato;

    public Reporte() {
        this.id = 0;
        this.tipo = null;
        this.periodoInicio = null;
        this.periodoFin = null;
        this.fechaGeneracion = null;
        this.descripcion = "";
        this.formato = "";
    }

    public Reporte(int id, TipoReporte tipo, LocalDateTime periodoInicio,
                   LocalDateTime periodoFin, LocalDateTime fechaGeneracion,
                   String descripcion, String formato) {
        this.id = id;
        this.tipo = tipo;
        this.periodoInicio = periodoInicio;
        this.periodoFin = periodoFin;
        this.fechaGeneracion = fechaGeneracion;
        this.descripcion = descripcion;
        this.formato = formato;
    }

    public Reporte(Reporte otro) {
        this.id = otro.id;
        this.tipo = otro.tipo;
        this.periodoInicio = otro.periodoInicio;
        this.periodoFin = otro.periodoFin;
        this.fechaGeneracion = otro.fechaGeneracion;
        this.descripcion = otro.descripcion;
        this.formato = otro.formato;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoReporte getTipo() {
        return this.tipo;
    }

    public void setTipo(TipoReporte tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getPeriodoInicio() {
        return this.periodoInicio;
    }

    public void setPeriodoInicio(LocalDateTime periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public LocalDateTime getPeriodoFin() {
        return this.periodoFin;
    }

    public void setPeriodoFin(LocalDateTime periodoFin) {
        this.periodoFin = periodoFin;
    }

    public LocalDateTime getFechaGeneracion() {
        return this.fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDateTime fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFormato() {
        return this.formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public void generar() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void exportar() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }
}
