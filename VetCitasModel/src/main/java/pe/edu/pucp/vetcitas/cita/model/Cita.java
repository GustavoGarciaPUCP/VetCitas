package pe.edu.pucp.vetcitas.cita.model;

import pe.edu.pucp.vetcitas.cliente.model.Mascota;
import pe.edu.pucp.vetcitas.common.enums.EstadoCita;
import pe.edu.pucp.vetcitas.common.model.EntidadAuditable;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;
import pe.edu.pucp.vetcitas.usuario.model.Veterinario;

import java.time.LocalDateTime;

public class Cita extends EntidadAuditable{
    private int id;
    private LocalDateTime fechaHoraInicio;
    private LocalDateTime fechaHoraFin;
    private EstadoCita estado;
    private Mascota mascota;
    private Veterinario veterinario;
    private Servicio servicio;

    public Cita() {
        super();
        this.id = 0;
        this.fechaHoraInicio = null;
        this.fechaHoraFin = null;
        this.estado = null;
        this.mascota = null;
        this.veterinario = null;
        this.servicio = null;
    }

    public Cita(int id, LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraFin,
                EstadoCita estado, Mascota mascota, Veterinario veterinario,
                Servicio servicio, LocalDateTime createdOn,
                LocalDateTime modifiedOn, Usuario modifiedBy) {
        super(createdOn, modifiedOn, modifiedBy);
        this.id = id;
        this.fechaHoraInicio = fechaHoraInicio;
        this.fechaHoraFin = fechaHoraFin;
        this.estado = estado;
        this.mascota = (mascota == null) ? null : new Mascota(mascota);
        this.veterinario = (veterinario == null) ? null : new Veterinario(veterinario);
        this.servicio = (servicio == null) ? null : new Servicio(servicio);
    }

    public Cita(Cita otra) {
        super(otra);
        this.id = otra.id;
        this.fechaHoraInicio = otra.fechaHoraInicio;
        this.fechaHoraFin = otra.fechaHoraFin;
        this.estado = otra.estado;
        this.mascota = (otra.mascota == null) ? null : new Mascota(otra.mascota);
        this.veterinario = (otra.veterinario == null) ? null : new Veterinario(otra.veterinario);
        this.servicio = (otra.servicio == null) ? null : new Servicio(otra.servicio);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFechaHoraInicio() {
        return this.fechaHoraInicio;
    }

    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }

    public LocalDateTime getFechaHoraFin() {
        return this.fechaHoraFin;
    }

    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }

    public EstadoCita getEstado() {
        return this.estado;
    }

    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public Mascota getMascota() {
        return (this.mascota == null) ? null : new Mascota(this.mascota);
    }

    public void setMascota(Mascota mascota) {
        this.mascota = (mascota == null) ? null : new Mascota(mascota);
    }

    public Veterinario getVeterinario() {
        return (this.veterinario == null) ? null : new Veterinario(this.veterinario);
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = (veterinario == null) ? null : new Veterinario(veterinario);
    }

    public Servicio getServicio() {
        return (this.servicio == null) ? null : new Servicio(this.servicio);
    }

    public void setServicio(Servicio servicio) {
        this.servicio = (servicio == null) ? null : new Servicio(servicio);
    }

    public void confirmar() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void cancelar() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void reprogramar(LocalDateTime nuevaFechaHoraInicio, LocalDateTime nuevaFechaHoraFin) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void marcarAtendida() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void marcarNoAsistio() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void agregarRecordatorio(Recordatorio recordatorio) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public int calcularDuracion() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

}
