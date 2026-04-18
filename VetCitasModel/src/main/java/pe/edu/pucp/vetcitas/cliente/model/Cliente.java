package pe.edu.pucp.vetcitas.cliente.model;

import pe.edu.pucp.vetcitas.common.model.EntidadAuditable;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends EntidadAuditable {
    private int id;
    private String nombres;
    private String apellidos;
    private String telefono;
    private String observaciones;
    private boolean activo;
    private List<Mascota> mascotas;

    public Cliente() {
        super();
        this.id = 0;
        this.nombres = "";
        this.apellidos = "";
        this.telefono = "";
        this.observaciones = "";
        this.activo = true;
        this.mascotas = new ArrayList<>();
    }

    public Cliente(int id, String nombres, String apellidos, String telefono,
                   String observaciones, boolean activo, List<Mascota> mascotas,
                   LocalDateTime createdOn, LocalDateTime modifiedOn,
                   Usuario modifiedBy) {
        super(createdOn, modifiedOn, modifiedBy);
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.telefono = telefono;
        this.observaciones = observaciones;
        this.activo = activo;
        this.mascotas = new ArrayList<>();

        if (mascotas != null) {
            for (Mascota mascota : mascotas) {
                this.mascotas.add(new Mascota(mascota));
            }
        }
    }

    public Cliente(Cliente otro) {
        super(otro);
        this.id = otro.id;
        this.nombres = otro.nombres;
        this.apellidos = otro.apellidos;
        this.telefono = otro.telefono;
        this.observaciones = otro.observaciones;
        this.activo = otro.activo;
        this.mascotas = new ArrayList<>();

        if (otro.mascotas != null) {
            for (Mascota mascota : otro.mascotas) {
                this.mascotas.add(new Mascota(mascota));
            }
        }
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return this.nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return this.apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getObservaciones() {
        return this.observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isActivo() {
        return this.activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public List<Mascota> getMascotas() {
        List<Mascota> copia = new ArrayList<>();

        for (Mascota mascota : this.mascotas) {
            copia.add(new Mascota(mascota));
        }

        return copia;
    }

    public void setMascotas(List<Mascota> mascotas) {
        this.mascotas = new ArrayList<>();

        if (mascotas != null) {
            for (Mascota mascota : mascotas) {
                this.mascotas.add(new Mascota(mascota));
            }
        }
    }

    public void agregarMascota(Mascota mascota) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void removerMascota(Mascota mascota) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public boolean esFrecuente(int umbralCitas, LocalDateTime desde, LocalDateTime hasta) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }
}
