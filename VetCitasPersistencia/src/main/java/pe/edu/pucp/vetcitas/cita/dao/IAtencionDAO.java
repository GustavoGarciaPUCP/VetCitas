package pe.edu.pucp.vetcitas.cita.dao;

import pe.edu.pucp.vetcitas.dao.IDAO;
import pe.edu.pucp.vetcitas.cita.model.Atencion;

import java.time.LocalDate;
import java.util.List;

public interface IAtencionDAO extends IDAO<Atencion> {
    Atencion buscarPorCita(int idCita);
    List<Atencion> listarFiltradas(Integer idVeterinario, String estadoCita, LocalDate fecha, String textoBusqueda);
    List<Atencion> listarHistorialPorMascota(int idMascota);
}