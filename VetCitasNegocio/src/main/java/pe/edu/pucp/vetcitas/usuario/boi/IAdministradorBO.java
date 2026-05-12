package pe.edu.pucp.vetcitas.usuario.boi;

import pe.edu.pucp.vetcitas.bo.IBaseBO;
import pe.edu.pucp.vetcitas.usuario.model.Administrador;

public interface IAdministradorBO extends IBaseBO<Administrador> {
    void asignarRol(int idUsuario, String codigoRol) throws Exception;
    void revocarRol(int idUsuario, String codigoRol) throws Exception;
}
