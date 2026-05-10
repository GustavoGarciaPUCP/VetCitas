package pe.edu.pucp.vetcitas.usuario.model;

import pe.edu.pucp.vetcitas.common.model.EntidadAuditable;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class HorarioVeterinario extends EntidadAuditable {
    private int id;
    private Veterinario veterinario;
    private int diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private LocalTime horaDescansoInicio;
    private LocalTime horaDescansoFin;
    private boolean activo;

    public HorarioVeterinario() {
        super();
        this.id = 0;
        this.veterinario = null;
        this.diaSemana = 0;
        this.horaInicio = null;
        this.horaFin = null;
        this.horaDescansoInicio = null;
        this.horaDescansoFin = null;
        this.activo = true;
    }

    public HorarioVeterinario(int id, Veterinario veterinario, int diaSemana,
                              LocalTime horaInicio, LocalTime horaFin,
                              LocalTime horaDescansoInicio, LocalTime horaDescansoFin,
                              boolean activo, LocalDateTime createdOn,
                              LocalDateTime modifiedOn, Usuario modifiedBy) {
        super(createdOn, modifiedOn, modifiedBy);
        this.id = id;
        this.veterinario = veterinario == null ? null : new Veterinario(veterinario);
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.horaDescansoInicio = horaDescansoInicio;
        this.horaDescansoFin = horaDescansoFin;
        this.activo = activo;
    }

    public HorarioVeterinario(HorarioVeterinario otro) {
        super(otro);
        this.id = otro.id;
        this.veterinario = otro.veterinario == null ? null : new Veterinario(otro.veterinario);
        this.diaSemana = otro.diaSemana;
        this.horaInicio = otro.horaInicio;
        this.horaFin = otro.horaFin;
        this.horaDescansoInicio = otro.horaDescansoInicio;
        this.horaDescansoFin = otro.horaDescansoFin;
        this.activo = otro.activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Veterinario getVeterinario() {
        return veterinario == null ? null : new Veterinario(veterinario);
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario == null ? null : new Veterinario(veterinario);
    }

    public int getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(int diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public LocalTime getHoraDescansoInicio() {
        return horaDescansoInicio;
    }

    public void setHoraDescansoInicio(LocalTime horaDescansoInicio) {
        this.horaDescansoInicio = horaDescansoInicio;
    }

    public LocalTime getHoraDescansoFin() {
        return horaDescansoFin;
    }

    public void setHoraDescansoFin(LocalTime horaDescansoFin) {
        this.horaDescansoFin = horaDescansoFin;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
