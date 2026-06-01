package pe.edu.pucp.vetcitas.usuario.model;

import pe.edu.pucp.vetcitas.common.enums.CodigoRol;

import java.time.LocalDateTime;
import java.util.List;

public class Administrador extends Usuario{
    private String area;
    private boolean esSuperAdmin;

    public Administrador() {
        super();
        this.area = "";
        this.esSuperAdmin = false;
    }

    public Administrador(int id, String username, String contrasenaHash, String nombres,
                         String apellidos, boolean activo, String telefono,String email,
                         List<RolSistema> roles, String area, boolean esSuperAdmin,
                         LocalDateTime createdOn, LocalDateTime modifiedOn,
                         Usuario modifiedBy) {
        super(id, username, contrasenaHash, nombres, apellidos, activo, telefono,email,
                roles, createdOn, modifiedOn, modifiedBy);
        this.area = area;
        this.esSuperAdmin = esSuperAdmin;
    }

    public Administrador(Administrador otro) {
        super(otro);
        this.area = otro.area;
        this.esSuperAdmin = otro.esSuperAdmin;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isEsSuperAdmin() {
        return esSuperAdmin;
    }

    public void setEsSuperAdmin(boolean esSuperAdmin) {
        this.esSuperAdmin = esSuperAdmin;
    }

    public void crearUsuario(Usuario u) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void desactivarUsuario(int idUsuario) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void gestionarServicios() {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void generarReporteCitas(LocalDateTime desde, LocalDateTime hasta) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void generarReporteFrecuenciaServicios(LocalDateTime desde, LocalDateTime hasta) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void asignarPermiso(Usuario usuario, Permiso permiso) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public void revocarPermiso(Usuario usuario, Permiso permiso) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }
}
