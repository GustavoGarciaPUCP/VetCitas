package pe.edu.pucp.vetcitas.usuario.model;

import pe.edu.pucp.vetcitas.common.enums.CodigoRol;

import java.time.LocalDateTime;
import java.util.List;

public class Administrador extends Usuario{
    private String area;
    public Administrador() {
        super();
        this.area = "";
    }

    public Administrador(int id, String username, String contrasenaHash, String nombres,
                         String apellidos, boolean activo, String telefono,
                         List<RolSistema> roles, String area,
                         LocalDateTime createdOn, LocalDateTime modifiedOn,
                         Usuario modifiedBy) {
        super(id, username, contrasenaHash, nombres, apellidos, activo, telefono,
                roles, createdOn, modifiedOn, modifiedBy);
        this.area = area;
    }

    public Administrador(Administrador otro) {
        super(otro);
        this.area = otro.area;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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
