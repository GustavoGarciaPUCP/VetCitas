package pe.edu.pucp.vetcitas.configuracion.model;

public class Configuracion {
    private int id;
    private int umbralClienteFrecuente;
    private double descuentoMaximoPermitido;

    public Configuracion() {
        this.id = 0;
        this.umbralClienteFrecuente = 0;
        this.descuentoMaximoPermitido = 0.0;
    }

    public Configuracion(int id, int umbralClienteFrecuente, double descuentoMaximoPermitido) {
        this.id = id;
        this.umbralClienteFrecuente = umbralClienteFrecuente;
        this.descuentoMaximoPermitido = descuentoMaximoPermitido;
    }

    public Configuracion(Configuracion otra) {
        this.id = otra.id;
        this.umbralClienteFrecuente = otra.umbralClienteFrecuente;
        this.descuentoMaximoPermitido = otra.descuentoMaximoPermitido;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUmbralClienteFrecuente() {
        return this.umbralClienteFrecuente;
    }

    public void setUmbralClienteFrecuente(int umbralClienteFrecuente) {
        this.umbralClienteFrecuente = umbralClienteFrecuente;
    }

    public double getDescuentoMaximoPermitido() {
        return this.descuentoMaximoPermitido;
    }

    public void setDescuentoMaximoPermitido(double descuentoMaximoPermitido) {
        this.descuentoMaximoPermitido = descuentoMaximoPermitido;
    }

    public void actualizarParametros() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }
}
