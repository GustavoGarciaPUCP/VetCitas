package pe.edu.pucp.vetcitas.servicio.dao;

import pe.edu.pucp.vetcitas.dao.IDAO;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;

public interface IServicioDAO extends IDAO<Servicio> {
    int deshabilitar(int id);
}