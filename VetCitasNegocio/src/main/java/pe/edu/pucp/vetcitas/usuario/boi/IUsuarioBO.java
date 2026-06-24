package pe.edu.pucp.vetcitas.usuario.boi;

import pe.edu.pucp.vetcitas.usuario.model.Usuario;

public interface IUsuarioBO {
    boolean estaActivo(int idUsuario) throws Exception;

    Usuario autenticar(String username, String contrasenaPlana) throws Exception;

    void cambiarContrasena(int idUsuario,
                           String contrasenaActual,
                           String nuevaContrasena) throws Exception;

    void restablecerContrasena(int idUsuarioObjetivo,
                               String nuevaContrasena,
                               int idAdmin) throws Exception;
}
