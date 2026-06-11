package pe.edu.pucp.vetcitas.cita.model;

import pe.edu.pucp.vetcitas.usuario.model.Veterinario;

public class VeterinarioAtencionResumen {
    private Veterinario veterinario;
    private int totalAtenciones;
    private double montoNetoTotal;

    public VeterinarioAtencionResumen() {
        this.veterinario = null;
        this.totalAtenciones = 0;
        this.montoNetoTotal = 0.0;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Veterinario veterinario) {
        this.veterinario = veterinario;
    }

    public int getTotalAtenciones() {
        return totalAtenciones;
    }

    public void setTotalAtenciones(int totalAtenciones) {
        this.totalAtenciones = totalAtenciones;
    }

    public double getMontoNetoTotal() {
        return montoNetoTotal;
    }

    public void setMontoNetoTotal(double montoNetoTotal) {
        this.montoNetoTotal = montoNetoTotal;
    }
}
