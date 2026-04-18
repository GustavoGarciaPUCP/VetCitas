package pe.edu.pucp.vetcitas.cliente.model;

import pe.edu.pucp.vetcitas.cita.model.Atencion;
import pe.edu.pucp.vetcitas.common.model.EntidadAuditable;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Mascota extends EntidadAuditable {
    private int id;
    private String nombre;
    private String especie;
    private String raza;
    private LocalDate fechaNacimiento;
    private boolean esterilizado;
    private boolean activo;
    private Cliente cliente;
    private List<Atencion> atenciones;

    public Mascota() {
        super();
        this.id = 0;
        this.nombre = "";
        this.especie = "";
        this.raza = "";
        this.fechaNacimiento = null;
        this.esterilizado = false;
        this.activo = true;
        this.cliente = null;
        this.atenciones = new ArrayList<>();
    }

    public Mascota(int id, String nombre, String especie, String raza,
                   LocalDate fechaNacimiento, boolean esterilizado, boolean activo,
                   Cliente cliente, List<Atencion> atenciones,
                   LocalDateTime createdOn, LocalDateTime modifiedOn,
                   Usuario modifiedBy) {
        super(createdOn, modifiedOn, modifiedBy);
        this.id = id;
        this.nombre = nombre;
        this.especie = especie;
        this.raza = raza;
        this.fechaNacimiento = fechaNacimiento;
        this.esterilizado = esterilizado;
        this.activo = activo;
        this.cliente = (cliente == null) ? null : new Cliente(cliente);
        this.atenciones = new ArrayList<>();

        if (atenciones != null) {
            for (Atencion atencion : atenciones) {
                this.atenciones.add(new Atencion(atencion));
            }
        }
    }

    public Mascota(Mascota otra) {
        super(otra);
        this.id = otra.id;
        this.nombre = otra.nombre;
        this.especie = otra.especie;
        this.raza = otra.raza;
        this.fechaNacimiento = otra.fechaNacimiento;
        this.esterilizado = otra.esterilizado;
        this.activo = otra.activo;
        this.cliente = (otra.cliente == null) ? null : new Cliente(otra.cliente);
        this.atenciones = new ArrayList<>();

        if (otra.atenciones != null) {
            for (Atencion atencion : otra.atenciones) {
                this.atenciones.add(new Atencion(atencion));
            }
        }
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

    public String getEspecie() {
        return this.especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return this.raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public LocalDate getFechaNacimiento() {
        return this.fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public boolean isEsterilizado() {
        return this.esterilizado;
    }

    public void setEsterilizado(boolean esterilizado) {
        this.esterilizado = esterilizado;
    }

    public boolean isActivo() {
        return this.activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Cliente getCliente() {
        return (this.cliente == null) ? null : new Cliente(this.cliente);
    }

    public void setCliente(Cliente cliente) {
        this.cliente = (cliente == null) ? null : new Cliente(cliente);
    }

    public List<Atencion> getAtenciones() {
        List<Atencion> copia = new ArrayList<>();

        for (Atencion atencion : this.atenciones) {
            copia.add(new Atencion(atencion));
        }

        return copia;
    }

    public void setAtenciones(List<Atencion> atenciones) {
        this.atenciones = new ArrayList<>();

        if (atenciones != null) {
            for (Atencion atencion : atenciones) {
                this.atenciones.add(new Atencion(atencion));
            }
        }
    }

    public void agregarAtencion(Atencion atencion) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public List<Atencion> obtenerHistorialAtenciones() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void marcarEsterilizada() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }
}