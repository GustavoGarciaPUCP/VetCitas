package pe.edu.pucp.vetcitas.usuario.dao;

import pe.edu.pucp.vetcitas.dao.IDAO;
import pe.edu.pucp.vetcitas.usuario.model.Administrador;
import pe.edu.pucp.vetcitas.usuario.model.RolSistema;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;

import java.util.List;

public interface IAdministradorDAO extends IDAO<Administrador> {
    void asignarRol(int idUsuario, String codigoRol);
    void revocarRol(int idUsuario, String codigoRol);
    List<RolSistema> listarRolesDeUsuario(int idUsuario);
    List<String> listarPermisosDeUsuario(int idUsuario);
    boolean existeUsername(String username, Integer idExcluir);
    int modificarUsuarioBasico(Usuario usuario, int modifiedBy);
    List<Usuario> listarUsuariosFiltrados(String texto, String codigoRol, Boolean activo);
}
