package pe.edu.pucp.vetcitas.servicio.dao;

import pe.edu.pucp.vetcitas.cita.model.ServicioAtencionResumen;
import pe.edu.pucp.vetcitas.dao.IDAO;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;

import java.time.LocalDateTime;
import java.util.List;

public interface IServicioDAO extends IDAO<Servicio> {
    int deshabilitar(int id);
    List<Servicio> listarPorNombreOTipo(String texto);
    List<Servicio> listarPorEstado(boolean activo);
    List<ServicioAtencionResumen> topNMasDemandados(
            LocalDateTime desde,
            LocalDateTime hasta,
            int limite
    );
}