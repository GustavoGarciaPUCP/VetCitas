package pe.edu.pucp.vetcitas.usuario.bo;

import pe.edu.pucp.vetcitas.common.enums.CodigoRol;
import pe.edu.pucp.vetcitas.usuario.boi.IAdministradorBO;
import pe.edu.pucp.vetcitas.usuario.dao.IAdministradorDAO;
import pe.edu.pucp.vetcitas.usuario.impl.AdministradorImpl;
import pe.edu.pucp.vetcitas.usuario.model.Administrador;
import pe.edu.pucp.vetcitas.usuario.model.RolSistema;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;

import java.util.List;

public class AdministradorBOImpl implements IAdministradorBO {
    private IAdministradorDAO administradorDAO;

    public AdministradorBOImpl() {
        this.administradorDAO = new AdministradorImpl();
    }

    @Override
    public int insertar(Administrador admin) throws Exception {
        validar(admin, false);

        if (administradorDAO.existeUsername(admin.getUsername(), null)) {
            throw new Exception("El username '" + admin.getUsername() + "' ya está en uso.");
        }

        return administradorDAO.insertar(admin);
    }

    @Override
    public int modificar(Administrador admin) throws Exception {
        validar(admin, true);

        if (administradorDAO.existeUsername(admin.getUsername(), admin.getId())) {
            throw new Exception("El username '" + admin.getUsername() + "' ya está ocupado por otro usuario.");
        }

        return administradorDAO.modificar(admin);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del administrador debe ser mayor que cero.");
        }
        Administrador adminActual = administradorDAO.buscarPorId(id);
        if (adminActual == null) {
            throw new Exception("El administrador no existe.");
        }
        if (adminActual.isEsSuperAdmin()) {
            throw new Exception("Operación denegada: No se puede eliminar al Super Administrador.");
        }
        return administradorDAO.eliminar(id);
    }

    @Override
    public List<Administrador> listarTodos() throws Exception {
        return administradorDAO.listarTodas();
    }

    @Override
    public Administrador buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del administrador debe ser mayor que cero.");
        }
        Administrador admin = administradorDAO.buscarPorId(id);
        if (admin == null) {
            throw new Exception("El administrador con id " + id + " no existe.");
        }
        return admin;
    }

    @Override
    public void asignarRol(int idUsuario, String codigoRol) throws Exception {
        if (idUsuario <= 0) {
            throw new Exception("El id de usuario debe ser mayor que cero.");
        }
        if (codigoRol == null || codigoRol.trim().isEmpty()) {
            throw new Exception("El código de rol es obligatorio.");
        }

        List<RolSistema> rolesActuales = administradorDAO.listarRolesDeUsuario(idUsuario);
        for (RolSistema rol : rolesActuales) {
            if (rol.getCodigo() != null && rol.getCodigo().name().equals(codigoRol)) {
                throw new Exception("El usuario ya tiene asignado el rol: " + codigoRol);
            }
        }
        administradorDAO.asignarRol(idUsuario, codigoRol);
    }

    @Override
    public void revocarRol(int idUsuario, String codigoRol) throws Exception {
        if (idUsuario <= 0) {
            throw new Exception("El id de usuario debe ser mayor que cero.");
        }
        if (codigoRol == null || codigoRol.trim().isEmpty()) {
            throw new Exception("El código de rol es obligatorio.");
        }

        List<RolSistema> rolesActuales = administradorDAO.listarRolesDeUsuario(idUsuario);
        boolean tieneRol = false;
        for (RolSistema rol : rolesActuales) {
            if (rol.getCodigo() != null && rol.getCodigo().name().equals(codigoRol)) {
                tieneRol = true;
                break;
            }
        }
        if (!tieneRol) {
            throw new Exception("El usuario no tiene asignado el rol: " + codigoRol);
        }
        administradorDAO.revocarRol(idUsuario, codigoRol);
    }

    @Override
    public List<Usuario> listarUsuariosFiltrados(String texto, String codigoRol, Boolean activo) throws Exception {
        if (texto == null) texto = "";
        texto = texto.trim();

        if (codigoRol == null) codigoRol = "";
        codigoRol = codigoRol.trim();

        if (!codigoRol.isEmpty()) {
            try {
                CodigoRol.valueOf(codigoRol);
            } catch (Exception ex) {
                throw new Exception("El código de rol no es válido.");
            }
        }

        return administradorDAO.listarUsuariosFiltrados(texto, codigoRol, activo);
    }

    private void validar(Administrador admin, boolean esModificacion) throws Exception {
        if (admin == null) {
            throw new Exception("El administrador no puede ser nulo.");
        }
        if (esModificacion && admin.getId() <= 0) {
            throw new Exception("El id del administrador es obligatorio para la modificación.");
        }
        if (admin.getUsername() == null || admin.getUsername().trim().isEmpty()) {
            throw new Exception("El nombre de usuario es obligatorio.");
        }
        if (admin.getContrasenaHash() == null || admin.getContrasenaHash().trim().isEmpty()) {
            throw new Exception("La contraseña es obligatoria.");
        }
        if (admin.getNombres() == null || admin.getNombres().trim().isEmpty()) {
            throw new Exception("Los nombres son obligatorios.");
        }
        if (admin.getApellidos() == null || admin.getApellidos().trim().isEmpty()) {
            throw new Exception("Los apellidos son obligatorios.");
        }
    }
}
