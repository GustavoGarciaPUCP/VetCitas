package pe.edu.pucp.vetcitas.cita.boi;

import pe.edu.pucp.vetcitas.bo.IBaseBO;
import pe.edu.pucp.vetcitas.cita.model.Cita;

import java.time.LocalDate;
import java.util.List;

public interface ICitaBO extends IBaseBO<Cita> {
    void cancelarCita(int idCita, int modifiedBy) throws Exception;
    void confirmarCita(int idCita, int modifiedBy) throws Exception;
    void marcarAtendida(int idCita, int modifiedBy) throws Exception;
    void marcarNoAsistio(int idCita, int modifiedBy) throws Exception;
    List<Cita> listarPorVeterinarioYFecha(int idVeterinario, LocalDate fecha) throws Exception;
    List<Cita> listarFiltradas(Integer idVeterinario, LocalDate fechaInicio, LocalDate fechaFin, String estado, String textoBusqueda) throws Exception;
    void marcarEnConsulta(int idCita, int idUsuario) throws Exception;
}
