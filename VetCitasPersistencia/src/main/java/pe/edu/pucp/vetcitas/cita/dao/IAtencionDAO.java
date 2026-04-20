package pe.edu.pucp.vetcitas.cita.dao;

import pe.edu.pucp.vetcitas.dao.IDAO;
import pe.edu.pucp.vetcitas.cita.model.Atencion;

public interface IAtencionDAO extends IDAO<Atencion> {
    Atencion buscarPorCita(int idCita);
}