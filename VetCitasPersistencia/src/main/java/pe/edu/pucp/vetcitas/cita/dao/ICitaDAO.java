package pe.edu.pucp.vetcitas.cita.dao;


import pe.edu.pucp.vetcitas.cita.model.Cita;
import pe.edu.pucp.vetcitas.dao.IDAO;

import java.time.LocalDate;
import java.util.List;

public interface ICitaDAO extends IDAO<Cita> {
    void cancelarCita(int idCita, int modifiedBy);
    void confirmarCita(int idCita, int modifiedBy);
    void marcarAtendida(int idCita, int modifiedBy);
    void marcarNoAsistio(int idCita, int modifiedBy);
    List<Cita> listarPorVeterinarioYFecha(int idVeterinario, LocalDate fecha);
    List<Cita> listarFiltradas(Integer idVeterinario, LocalDate fechaInicio, LocalDate fechaFin, String estado, String textoBusqueda);
}
