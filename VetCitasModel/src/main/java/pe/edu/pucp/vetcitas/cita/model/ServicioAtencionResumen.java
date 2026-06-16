package pe.edu.pucp.vetcitas.cita.model;

import pe.edu.pucp.vetcitas.servicio.model.Servicio;

public class ServicioAtencionResumen {
    private Servicio servicio;
    private int totalAtenciones;
    private double montoNetoTotal;

    public ServicioAtencionResumen() {
        this.servicio = null;
        this.totalAtenciones = 0;
        this.montoNetoTotal = 0.0;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
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
