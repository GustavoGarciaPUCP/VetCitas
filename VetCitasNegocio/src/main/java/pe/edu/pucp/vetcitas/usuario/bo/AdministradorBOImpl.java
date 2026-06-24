package pe.edu.pucp.vetcitas.usuario.bo;

import pe.edu.pucp.vetcitas.common.enums.CodigoRol;
import pe.edu.pucp.vetcitas.usuario.boi.IAdministradorBO;
import pe.edu.pucp.vetcitas.usuario.dao.IAdministradorDAO;
import pe.edu.pucp.vetcitas.usuario.impl.AdministradorImpl;
import pe.edu.pucp.vetcitas.usuario.model.Administrador;
import pe.edu.pucp.vetcitas.usuario.model.RolSistema;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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

        admin.setContrasenaHash(hashSiNoEstaHasheada(admin.getContrasenaHash()));

        return administradorDAO.insertar(admin);
    }

    @Override
    public int modificar(Administrador admin) throws Exception {
        validar(admin, true);

        if (administradorDAO.existeUsername(admin.getUsername(), admin.getId())) {
            throw new Exception("El username '" + admin.getUsername() + "' ya está ocupado por otro usuario.");
        }

        admin.setContrasenaHash(hashSiNoEstaHasheada(admin.getContrasenaHash()));

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
    public int modificarUsuarioBasico(Usuario usuario, int modifiedBy) throws Exception {
        validarUsuarioBasico(usuario);

        if (modifiedBy <= 0) {
            throw new Exception("El usuario que modifica es obligatorio.");
        }

        if (administradorDAO.existeUsername(usuario.getUsername(), usuario.getId())) {
            throw new Exception("El username '" + usuario.getUsername() + "' ya está ocupado por otro usuario.");
        }

        // Un SuperAdmin solo puede ser editado por si mismo: otro administrador no puede modificarlo.
        Administrador objetivo = administradorDAO.buscarPorId(usuario.getId());
        if (objetivo != null && objetivo.isEsSuperAdmin() && usuario.getId() != modifiedBy) {
            throw new Exception("Operación denegada: solo el Super Administrador puede editar su propia cuenta.");
        }

        if (!usuario.isActivo() && objetivo != null && objetivo.isEsSuperAdmin()) {
            throw new Exception("Operación denegada: No se puede inactivar al Super Administrador.");
        }

        return administradorDAO.modificarUsuarioBasico(usuario, modifiedBy);
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

    @Override
    public List<RolSistema> listarRolesDeUsuario(int idUsuario) throws Exception {
        if (idUsuario <= 0) {
            throw new Exception("El id del usuario debe ser mayor que cero.");
        }
        return administradorDAO.listarRolesDeUsuario(idUsuario);
    }

    @Override
    public boolean existeUsername(String username) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        return administradorDAO.existeUsername(username.trim(), null);
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
        validarEmailObligatorio(admin.getEmail());
        admin.setEmail(admin.getEmail().trim());
    }

    private void validarUsuarioBasico(Usuario usuario) throws Exception {
        if (usuario == null) {
            throw new Exception("El usuario no puede ser nulo.");
        }
        if (usuario.getId() <= 0) {
            throw new Exception("El id del usuario es obligatorio.");
        }
        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new Exception("El nombre de usuario es obligatorio.");
        }
        if (usuario.getNombres() == null || usuario.getNombres().trim().isEmpty()) {
            throw new Exception("Los nombres son obligatorios.");
        }
        if (usuario.getApellidos() == null || usuario.getApellidos().trim().isEmpty()) {
            throw new Exception("Los apellidos son obligatorios.");
        }
        validarEmailObligatorio(usuario.getEmail());

        usuario.setUsername(usuario.getUsername().trim());
        usuario.setNombres(usuario.getNombres().trim());
        usuario.setApellidos(usuario.getApellidos().trim());
        usuario.setEmail(usuario.getEmail().trim());
        usuario.setTelefono(usuario.getTelefono() == null ? "" : usuario.getTelefono().trim());
    }

    private void validarEmailObligatorio(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("El email es obligatorio.");
        }

        email = email.trim();

        if (email.length() > 100) {
            throw new Exception("El email no puede superar los 100 caracteres.");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new Exception("El email no tiene un formato válido.");
        }
    }

    private String hashSiNoEstaHasheada(String contrasena) throws Exception {
        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new Exception("La contraseña es obligatoria.");
        }

        contrasena = contrasena.trim();

        if (esSha256(contrasena)) {
            return contrasena.toLowerCase();
        }

        return generarSha256(contrasena);
    }

    private boolean esSha256(String texto) {
        return texto != null && texto.matches("^[0-9a-fA-F]{64}$");
    }

    private String generarSha256(String texto) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(texto.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();

        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}
