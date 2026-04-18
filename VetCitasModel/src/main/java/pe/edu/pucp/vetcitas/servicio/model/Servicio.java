package pe.edu.pucp.vetcitas.servicio.model;

import pe.edu.pucp.vetcitas.common.enums.TipoServicio;
import pe.edu.pucp.vetcitas.common.model.EntidadAuditable;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;

import java.time.LocalDateTime;

public class Servicio extends EntidadAuditable{
    private int id;
    private String nombre;
    private TipoServicio tipoServicio;
    private int duracionMinutos;
    private double precioReferencial;
    private boolean activo;

    public Servicio() {
        super();
        this.id = 0;
        this.nombre = "";
        this.tipoServicio = null;
        this.duracionMinutos = 0;
        this.precioReferencial = 0.0;
        this.activo = true;
    }

    public Servicio(int id, String nombre, TipoServicio tipoServicio,
                    int duracionMinutos, double precioReferencial, boolean activo,
                    LocalDateTime createdOn, LocalDateTime modifiedOn,
                    Usuario modifiedBy) {
        super(createdOn, modifiedOn, modifiedBy);
        this.id = id;
        this.nombre = nombre;
        this.tipoServicio = tipoServicio;
        this.duracionMinutos = duracionMinutos;
        this.precioReferencial = precioReferencial;
        this.activo = activo;
    }

    public Servicio(Servicio otro) {
        super(otro);
        this.id = otro.id;
        this.nombre = otro.nombre;
        this.tipoServicio = otro.tipoServicio;
        this.duracionMinutos = otro.duracionMinutos;
        this.precioReferencial = otro.precioReferencial;
        this.activo = otro.activo;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoServicio getTipoServicio() {
        return this.tipoServicio;
    }

    public void setTipoServicio(TipoServicio tipoServicio) {
        this.tipoServicio = tipoServicio;
    }

    public int getDuracionMinutos() {
        return this.duracionMinutos;
    }

    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public double getPrecioReferencial() {
        return this.precioReferencial;
    }

    public void setPrecioReferencial(double precioReferencial) {
        this.precioReferencial = precioReferencial;
    }

    public boolean isActivo() {
        return this.activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public void activar() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void desactivar() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }
}
