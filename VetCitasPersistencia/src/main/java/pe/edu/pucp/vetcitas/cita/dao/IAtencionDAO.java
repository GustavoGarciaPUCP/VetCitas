package pe.edu.pucp.vetcitas.cita.dao;

import pe.edu.pucp.vetcitas.dao.IDAO;
import pe.edu.pucp.vetcitas.cita.model.Atencion;
import pe.edu.pucp.vetcitas.cita.model.ServicioAtencionResumen;
import pe.edu.pucp.vetcitas.cita.model.VeterinarioAtencionResumen;


import java.time.LocalDate;
import java.util.List;

public interface IAtencionDAO extends IDAO<Atencion> {
    Atencion buscarPorCita(int idCita);
    List<Atencion> listarFiltradas(Integer idVeterinario, String estadoCita, LocalDate fecha, String textoBusqueda);
    List<Atencion> listarHistorialPorMascota(int idMascota);

    //Nuevo
    List<Atencion> listarUltimasPorVeterinario(int idVeterinario, int limite);

    int contarPorVeterinarioEnMes(int idVeterinario, int anio, int mes);

    double sumarMontosNetosPorMes(int anio, int mes);

    List<ServicioAtencionResumen> topServiciosPorVeterinario(
            int idVeterinario,
            int anio,
            int mes,
            int limite
    );

    List<VeterinarioAtencionResumen> topVeterinariosPorAtenciones(
            int anio,
            int mes,
            int limite
    );


}