package pe.edu.pucp.vetcitas.usuario.bo;

import pe.edu.pucp.vetcitas.common.enums.CodigoRol;
import pe.edu.pucp.vetcitas.usuario.boi.IUsuarioBO;
import pe.edu.pucp.vetcitas.usuario.dao.IUsuarioDAO;
import pe.edu.pucp.vetcitas.usuario.impl.UsuarioImpl;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class UsuarioBOImpl implements IUsuarioBO {
    private IUsuarioDAO usuarioDAO;

    public UsuarioBOImpl() {
        this.usuarioDAO = new UsuarioImpl();
    }

    @Override
    public Usuario autenticar(String username, String contrasenaPlana) throws Exception {
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("El username es obligatorio.");
        }

        if (contrasenaPlana == null || contrasenaPlana.trim().isEmpty()) {
            throw new Exception("La contraseña es obligatoria.");
        }

        username = username.trim();
        contrasenaPlana = contrasenaPlana.trim();

        String hash = generarSha256(contrasenaPlana);

        Usuario usuario = usuarioDAO.autenticar(username, hash);

        if (usuario == null) {
            throw new Exception("Credenciales inválidas o usuario inactivo.");
        }

        return usuario;
    }

    @Override
    public void restablecerContrasena(int idUsuarioObjetivo,
                                      String nuevaContrasena,
                                      int idAdmin) throws Exception {
        if (idUsuarioObjetivo <= 0) {
            throw new Exception("El id del usuario objetivo debe ser mayor que cero.");
        }

        if (idAdmin <= 0) {
            throw new Exception("El id del administrador debe ser mayor que cero.");
        }

        if (idUsuarioObjetivo == idAdmin) {
            throw new Exception("Este método es solo para restablecer contraseñas de otros usuarios.");
        }

        if (nuevaContrasena == null || nuevaContrasena.trim().isEmpty()) {
            throw new Exception("La nueva contraseña es obligatoria.");
        }

        nuevaContrasena = nuevaContrasena.trim();

        if (nuevaContrasena.length() < 8) {
            throw new Exception("La nueva contraseña debe tener al menos 8 caracteres.");
        }

        boolean esAdministrador = usuarioDAO.tieneRol(idAdmin, CodigoRol.ADMINISTRADOR.name());

        if (!esAdministrador) {
            throw new Exception("Solo un administrador puede restablecer contraseñas de otros usuarios.");
        }

        String hashNuevo = generarSha256(nuevaContrasena);

        boolean actualizado = usuarioDAO.restablecerContrasena(
                idUsuarioObjetivo,
                hashNuevo,
                idAdmin
        );

        if (!actualizado) {
            throw new Exception("No se pudo restablecer la contraseña. Verifique que el usuario objetivo exista y esté activo.");
        }
    }

    @Override
    public void cambiarContrasena(int idUsuario,
                                  String contrasenaActual,
                                  String nuevaContrasena) throws Exception {
        if (idUsuario <= 0) {
            throw new Exception("El id de usuario debe ser mayor que cero.");
        }

        if (contrasenaActual == null || contrasenaActual.trim().isEmpty()) {
            throw new Exception("La contraseña actual es obligatoria.");
        }

        if (nuevaContrasena == null || nuevaContrasena.trim().isEmpty()) {
            throw new Exception("La nueva contraseña es obligatoria.");
        }

        contrasenaActual = contrasenaActual.trim();
        nuevaContrasena = nuevaContrasena.trim();

        if (nuevaContrasena.length() < 8) {
            throw new Exception("La nueva contraseña debe tener al menos 8 caracteres.");
        }

        if (contrasenaActual.equals(nuevaContrasena)) {
            throw new Exception("La nueva contraseña no puede ser igual a la contraseña actual.");
        }

        String hashActual = generarSha256(contrasenaActual);
        String hashNuevo = generarSha256(nuevaContrasena);

        boolean actualizado = usuarioDAO.cambiarContrasena(idUsuario, hashActual, hashNuevo);

        if (!actualizado) {
            throw new Exception("No se pudo cambiar la contraseña. Verifique la contraseña actual.");
        }
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
