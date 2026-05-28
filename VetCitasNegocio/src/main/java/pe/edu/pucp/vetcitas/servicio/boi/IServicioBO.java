package pe.edu.pucp.vetcitas.servicio.boi;

import pe.edu.pucp.vetcitas.bo.IBaseBO;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;

import java.util.List;

public interface IServicioBO extends IBaseBO<Servicio> {
    int deshabilitar(int id) throws Exception;
    List<Servicio> listarPorNombreOTipo(String texto) throws Exception;
    List<Servicio> listarPorEstado(boolean activo) throws Exception;
}
