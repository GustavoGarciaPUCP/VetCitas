package pe.edu.pucp.vetcitas.cita.boi;

import pe.edu.pucp.vetcitas.bo.IBaseBO;
import pe.edu.pucp.vetcitas.cita.model.Atencion;

public interface IAtencionBO extends IBaseBO<Atencion> {
    Atencion buscarPorCita(int idCita) throws Exception;
}
