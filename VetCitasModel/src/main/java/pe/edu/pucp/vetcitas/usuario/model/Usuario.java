package pe.edu.pucp.vetcitas.usuario.model;

import pe.edu.pucp.vetcitas.common.enums.Rol;
import pe.edu.pucp.vetcitas.common.model.EntidadAuditable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Usuario extends EntidadAuditable {
    private int id;
    private String username;
    private String contrasenaHash;
    private String nombres;
    private String apellidos;
    private boolean activo;
    private Rol rol;
    private String telefono;
    private List<Permiso> permisos;

    public Usuario() {
        this.id = 0;
        this.username = "";
        this.contrasenaHash = "";
        this.nombres = "";
        this.apellidos = "";
        this.activo = true;
        this.rol = null;
        this.telefono = "";
        this.permisos = new ArrayList<>();
    }

    public Usuario(int id, String username, String contrasenaHash, String nombres,
                   String apellidos, boolean activo, Rol rol, String telefono,
                   List<Permiso> permisos, LocalDateTime createdOn,
                   LocalDateTime modifiedOn, Usuario modifiedBy) {
        super(createdOn, modifiedOn, modifiedBy);
        this.id = id;
        this.username = username;
        this.contrasenaHash = contrasenaHash;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.activo = activo;
        this.rol = rol;
        this.telefono = telefono;
        this.permisos = new ArrayList<>();

        if (permisos != null) {
            for (Permiso permiso : permisos) {
                this.permisos.add(new Permiso(permiso));
            }
        }
    }

    public Usuario(Usuario otro) {
        super(otro);
        this.id = otro.id;
        this.username = otro.username;
        this.contrasenaHash = otro.contrasenaHash;
        this.nombres = otro.nombres;
        this.apellidos = otro.apellidos;
        this.activo = otro.activo;
        this.rol = otro.rol;
        this.telefono = otro.telefono;
        this.permisos = new ArrayList<>();

        if (otro.permisos != null) {
            for (Permiso permiso : otro.permisos) {
                this.permisos.add(new Permiso(permiso));
            }
        }
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContrasenaHash() {
        return this.contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
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

    public boolean isActivo() {
        return this.activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Rol getRol() {
        return this.rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<Permiso> getPermisos() {
        List<Permiso> copia = new ArrayList<>();

        for (Permiso permiso : this.permisos) {
            copia.add(new Permiso(permiso));
        }

        return copia;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = new ArrayList<>();

        if (permisos != null) {
            for (Permiso permiso : permisos) {
                this.permisos.add(new Permiso(permiso));
            }
        }
    }

    public boolean validarCredenciales(String passwordPlano) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void activar() {
        this.activo = true;
    }

    public void desactivar() {
        this.activo = false;
    }
}
