package pe.edu.pucp.vetcitas.usuario.model;

public class Permiso {
    private int id;
    private String nombre;
    private String descripcion;

    public Permiso() {
        this.id = 0;
        this.nombre = "";
        this.descripcion = "";
    }

    public Permiso(int id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Permiso(Permiso otro) {
        this.id = otro.id;
        this.nombre = otro.nombre;
        this.descripcion = otro.descripcion;
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

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
