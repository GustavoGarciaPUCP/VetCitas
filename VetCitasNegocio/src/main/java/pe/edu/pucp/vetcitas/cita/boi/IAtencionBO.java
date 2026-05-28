package pe.edu.pucp.vetcitas.cita.boi;

import pe.edu.pucp.vetcitas.bo.IBaseBO;
import pe.edu.pucp.vetcitas.cita.model.Atencion;

import java.time.LocalDate;
import java.util.List;

public interface IAtencionBO extends IBaseBO<Atencion> {
    Atencion buscarPorCita(int idCita) throws Exception;
    List<Atencion> listarFiltradas(Integer idVeterinario, String estadoCita, LocalDate fecha, String textoBusqueda) throws Exception;
    List<Atencion> listarHistorialPorMascota(int idMascota) throws Exception;
}
