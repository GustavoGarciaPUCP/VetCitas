package pe.edu.pucp.vetcitas.cita.boi;

import pe.edu.pucp.vetcitas.bo.IBaseBO;
import pe.edu.pucp.vetcitas.cita.model.Cita;

import java.time.LocalDate;
import java.util.List;
import java.time.LocalDateTime;

public interface ICitaBO extends IBaseBO<Cita> {
    void cancelarCita(int idCita, String motivoCancelacion,int modifiedBy) throws Exception;
    void confirmarCita(int idCita, int modifiedBy) throws Exception;
    void marcarAtendida(int idCita, int modifiedBy) throws Exception;
    void marcarNoAsistio(int idCita, int modifiedBy) throws Exception;
    List<Cita> listarPorVeterinarioYFecha(int idVeterinario, LocalDate fecha) throws Exception;
    List<Cita> listarFiltradas(Integer idVeterinario, LocalDate fechaInicio, LocalDate fechaFin, String estado, String textoBusqueda) throws Exception;
    void marcarEnConsulta(int idCita, int idUsuario) throws Exception;
    //Nuevo
    void reprogramar(int idCita,
                     LocalDateTime nuevaFechaHoraInicio,
                     LocalDateTime nuevaFechaHoraFin,
                     String motivoReprogramacion,
                     int modifiedBy) throws Exception;

    void cambiarVeterinario(int idCita, int idNuevoVeterinario, int modifiedBy) throws Exception;

    boolean validarDisponibilidadSlot(int idVeterinario,
                                      LocalDateTime fechaHoraInicio,
                                      LocalDateTime fechaHoraFin) throws Exception;

    int contarPorEstadoEnRango(String estado, LocalDateTime desde, LocalDateTime hasta) throws Exception;

    int contarPorVeterinarioEnRango(int idVeterinario, LocalDateTime desde, LocalDateTime hasta) throws Exception;
}
