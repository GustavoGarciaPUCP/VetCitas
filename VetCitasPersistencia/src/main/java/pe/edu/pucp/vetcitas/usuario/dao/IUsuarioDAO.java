package pe.edu.pucp.vetcitas.usuario.dao;

import pe.edu.pucp.vetcitas.usuario.model.Usuario;

public interface IUsuarioDAO {
    Usuario autenticar(String username, String contrasenaHash);

    boolean cambiarContrasena(int idUsuario,
                              String contrasenaActualHash,
                              String nuevaContrasenaHash);

    boolean tieneRol(int idUsuario, String codigoRol);

    boolean restablecerContrasena(int idUsuarioObjetivo,
                                  String nuevaContrasenaHash,
                                  int idAdmin);
}
