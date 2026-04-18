package pe.edu.pucp.vetcitas.cita.model;

import pe.edu.pucp.vetcitas.common.enums.CanalRecordatorio;
import pe.edu.pucp.vetcitas.common.enums.EstadoSeguimiento;
import pe.edu.pucp.vetcitas.common.model.EntidadAuditable;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;

import java.time.LocalDateTime;

public class Recordatorio extends EntidadAuditable {
    private int id;
    private LocalDateTime fechaProgramada;
    private CanalRecordatorio canal;
    private EstadoSeguimiento estadoSeguimiento;
    private String mensaje;
    private Cita cita;

    public Recordatorio() {
        super();
        this.id = 0;
        this.fechaProgramada = null;
        this.canal = null;
        this.estadoSeguimiento = null;
        this.mensaje = "";
        this.cita = null;
    }

    public Recordatorio(int id, LocalDateTime fechaProgramada, CanalRecordatorio canal,
                        EstadoSeguimiento estadoSeguimiento, String mensaje, Cita cita,
                        LocalDateTime createdOn, LocalDateTime modifiedOn,
                        Usuario modifiedBy) {
        super(createdOn, modifiedOn, modifiedBy);
        this.id = id;
        this.fechaProgramada = fechaProgramada;
        this.canal = canal;
        this.estadoSeguimiento = estadoSeguimiento;
        this.mensaje = mensaje;
        this.cita = (cita == null) ? null : new Cita(cita);
    }

    public Recordatorio(Recordatorio otro) {
        super(otro);
        this.id = otro.id;
        this.fechaProgramada = otro.fechaProgramada;
        this.canal = otro.canal;
        this.estadoSeguimiento = otro.estadoSeguimiento;
        this.mensaje = otro.mensaje;
        this.cita = (otro.cita == null) ? null : new Cita(otro.cita);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFechaProgramada() {
        return this.fechaProgramada;
    }

    public void setFechaProgramada(LocalDateTime fechaProgramada) {
        this.fechaProgramada = fechaProgramada;
    }

    public CanalRecordatorio getCanal() {
        return this.canal;
    }

    public void setCanal(CanalRecordatorio canal) {
        this.canal = canal;
    }

    public EstadoSeguimiento getEstadoSeguimiento() {
        return this.estadoSeguimiento;
    }

    public void setEstadoSeguimiento(EstadoSeguimiento estadoSeguimiento) {
        this.estadoSeguimiento = estadoSeguimiento;
    }

    public String getMensaje() {
        return this.mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Cita getCita() {
        return (this.cita == null) ? null : new Cita(this.cita);
    }

    public void setCita(Cita cita) {
        this.cita = (cita == null) ? null : new Cita(cita);
    }

    public void marcarEnviado() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void registrarResultado(String notaResultado) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void cerrarSeguimiento() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }
}
