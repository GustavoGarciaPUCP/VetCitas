package pe.edu.pucp.vetcitas.usuario.bo;

import pe.edu.pucp.vetcitas.usuario.dao.IAdministradorDAO;
import pe.edu.pucp.vetcitas.usuario.impl.AdministradorImpl;
import pe.edu.pucp.vetcitas.usuario.model.Administrador;
import pe.edu.pucp.vetcitas.usuario.model.RolSistema;
import java.util.List;

public class AdministradorBO {
    private IAdministradorDAO administradorDAO;

    public AdministradorBO() {
        this.administradorDAO = new AdministradorImpl();
    }

    public int insertar(Administrador admin) throws Exception {

        if (admin.getUsername() == null || admin.getUsername().trim().isEmpty()) {
            throw new Exception("El nombre de usuario es obligatorio.");
        }
        if (admin.getContrasenaHash() == null || admin.getContrasenaHash().trim().isEmpty()) {
            throw new Exception("La contraseña es obligatoria.");
        }

        if (administradorDAO.existeUsername(admin.getUsername(), null)) {
            throw new Exception("El username '" + admin.getUsername() + "' ya está en uso.");
        }

        return administradorDAO.insertar(admin);
    }

    public int modificar(Administrador admin) throws Exception {
        if (admin.getId() <= 0) {
            throw new Exception("ID de administrador no válido.");
        }

        if (administradorDAO.existeUsername(admin.getUsername(), admin.getId())) {
            throw new Exception("El username '" + admin.getUsername() + "' ya está ocupado por otro usuario.");
        }

        return administradorDAO.modificar(admin);
    }

    public int eliminar(int id) throws Exception {
        Administrador adminActual = administradorDAO.buscarPorId(id);
        if (adminActual == null) {
            throw new Exception("El administrador no existe.");
        }
        if (adminActual.isEsSuperAdmin()) {
            throw new Exception("Operación denegada: No se puede eliminar al Super Administrador.");
        }
        return administradorDAO.eliminar(id);
    }

    public void asignarRol(int idUsuario, String codigoRol) throws Exception {
        List<RolSistema> rolesActuales = administradorDAO.listarRolesDeUsuario(idUsuario);
        for (RolSistema rol : rolesActuales) {
            if (rol.getCodigo().equals(codigoRol)) {
                throw new Exception("El usuario ya tiene asignado el rol: " + codigoRol);
            }
        }
        administradorDAO.asignarRol(idUsuario, codigoRol);
    }
}