package pe.edu.pucp.vetcitas.usuario.boi;

import pe.edu.pucp.vetcitas.bo.IBaseBO;
import pe.edu.pucp.vetcitas.usuario.model.Administrador;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;

import java.util.List;

public interface IAdministradorBO extends IBaseBO<Administrador> {
    void asignarRol(int idUsuario, String codigoRol) throws Exception;
    void revocarRol(int idUsuario, String codigoRol) throws Exception;
    int modificarUsuarioBasico(Usuario usuario, int modifiedBy) throws Exception;
    List<Usuario> listarUsuariosFiltrados(String texto, String codigoRol, Boolean activo) throws Exception;
}
