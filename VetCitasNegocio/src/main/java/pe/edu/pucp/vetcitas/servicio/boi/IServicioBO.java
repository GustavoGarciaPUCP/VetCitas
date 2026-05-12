package pe.edu.pucp.vetcitas.servicio.boi;

import pe.edu.pucp.vetcitas.bo.IBaseBO;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;

public interface IServicioBO extends IBaseBO<Servicio> {
    int deshabilitar(int id) throws Exception;
}
