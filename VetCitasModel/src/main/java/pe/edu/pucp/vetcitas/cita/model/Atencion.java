package pe.edu.pucp.vetcitas.cita.model;

import pe.edu.pucp.vetcitas.common.model.EntidadAuditable;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;

import java.time.LocalDateTime;

public class Atencion extends EntidadAuditable{
    private int id;
    private LocalDateTime fechaHora;
    private String notaClinica;
    private String notaPreOperatoria;
    private String notaPostOperatoria;
    private String recomendacionControl;
    private double montoReferencial;
    private double descuentoAplicado;
    private Cita cita;

    public Atencion() {
        super();
        this.id = 0;
        this.fechaHora = null;
        this.notaClinica = "";
        this.notaPreOperatoria = "";
        this.notaPostOperatoria = "";
        this.recomendacionControl = "";
        this.montoReferencial = 0.0;
        this.descuentoAplicado = 0.0;
        this.cita = null;
    }

    public Atencion(int id, LocalDateTime fechaHora, String notaClinica,
                    String notaPreOperatoria, String notaPostOperatoria,
                    String recomendacionControl, double montoReferencial,
                    double descuentoAplicado, Cita cita,
                    LocalDateTime createdOn, LocalDateTime modifiedOn,
                    Usuario modifiedBy) {
        super(createdOn, modifiedOn, modifiedBy);
        this.id = id;
        this.fechaHora = fechaHora;
        this.notaClinica = notaClinica;
        this.notaPreOperatoria = notaPreOperatoria;
        this.notaPostOperatoria = notaPostOperatoria;
        this.recomendacionControl = recomendacionControl;
        this.montoReferencial = montoReferencial;
        this.descuentoAplicado = descuentoAplicado;
        this.cita = (cita == null) ? null : new Cita(cita);
    }

    public Atencion(Atencion otra) {
        super(otra);
        this.id = otra.id;
        this.fechaHora = otra.fechaHora;
        this.notaClinica = otra.notaClinica;
        this.notaPreOperatoria = otra.notaPreOperatoria;
        this.notaPostOperatoria = otra.notaPostOperatoria;
        this.recomendacionControl = otra.recomendacionControl;
        this.montoReferencial = otra.montoReferencial;
        this.descuentoAplicado = otra.descuentoAplicado;
        this.cita = (otra.cita == null) ? null : new Cita(otra.cita);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getFechaHora() {
        return this.fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getNotaClinica() {
        return this.notaClinica;
    }

    public void setNotaClinica(String notaClinica) {
        this.notaClinica = notaClinica;
    }

    public String getNotaPreOperatoria() {
        return this.notaPreOperatoria;
    }

    public void setNotaPreOperatoria(String notaPreOperatoria) {
        this.notaPreOperatoria = notaPreOperatoria;
    }

    public String getNotaPostOperatoria() {
        return this.notaPostOperatoria;
    }

    public void setNotaPostOperatoria(String notaPostOperatoria) {
        this.notaPostOperatoria = notaPostOperatoria;
    }

    public String getRecomendacionControl() {
        return this.recomendacionControl;
    }

    public void setRecomendacionControl(String recomendacionControl) {
        this.recomendacionControl = recomendacionControl;
    }

    public double getMontoReferencial() {
        return this.montoReferencial;
    }

    public void setMontoReferencial(double montoReferencial) {
        this.montoReferencial = montoReferencial;
    }

    public double getDescuentoAplicado() {
        return this.descuentoAplicado;
    }

    public void setDescuentoAplicado(double descuentoAplicado) {
        this.descuentoAplicado = descuentoAplicado;
    }

    public Cita getCita() {
        return (this.cita == null) ? null : new Cita(this.cita);
    }

    public void setCita(Cita cita) {
        this.cita = (cita == null) ? null : new Cita(cita);
    }

    public void calcularMontoReferencialDesdeServicio() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void aplicarDescuento(double porcentaje) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public boolean esClinica() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }
}
