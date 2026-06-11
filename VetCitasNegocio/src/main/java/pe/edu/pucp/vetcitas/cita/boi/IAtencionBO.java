package pe.edu.pucp.vetcitas.cita.boi;

import pe.edu.pucp.vetcitas.bo.IBaseBO;
import pe.edu.pucp.vetcitas.cita.model.Atencion;
import pe.edu.pucp.vetcitas.cita.model.ServicioAtencionResumen;
import pe.edu.pucp.vetcitas.cita.model.VeterinarioAtencionResumen;

import java.time.LocalDate;
import java.util.List;

public interface IAtencionBO extends IBaseBO<Atencion> {
    Atencion buscarPorCita(int idCita) throws Exception;
    List<Atencion> listarFiltradas(Integer idVeterinario, String estadoCita, LocalDate fecha, String textoBusqueda) throws Exception;
    List<Atencion> listarHistorialPorMascota(int idMascota) throws Exception;

    List<Atencion> listarUltimasPorVeterinario(int idVeterinario, int limite) throws Exception;

    int contarPorVeterinarioEnMes(int idVeterinario, int anio, int mes) throws Exception;

    double sumarMontosNetosPorMes(int anio, int mes) throws Exception;

    List<ServicioAtencionResumen> topServiciosPorVeterinario(
            int idVeterinario,
            int anio,
            int mes,
            int limite
    ) throws Exception;

    List<VeterinarioAtencionResumen> topVeterinariosPorAtenciones(
            int anio,
            int mes,
            int limite
    ) throws Exception;
}
