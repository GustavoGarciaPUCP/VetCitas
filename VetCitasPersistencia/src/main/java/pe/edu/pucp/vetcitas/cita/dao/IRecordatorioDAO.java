package pe.edu.pucp.vetcitas.cita.dao;

import pe.edu.pucp.vetcitas.dao.IDAO;
import pe.edu.pucp.vetcitas.cita.model.Recordatorio;

import java.time.LocalDate;
import java.util.List;

public interface IRecordatorioDAO extends IDAO<Recordatorio> {
    List<Recordatorio> listarPorMascotaOCliente(String texto);
    List<Recordatorio> listarPorEstadoYFecha(String estado, LocalDate fecha);
    void marcarEnviado(int idRecordatorio, int modifiedBy);
    int contarPendientes();
}
