package pe.edu.pucp.vetcitas.cita.boi;

import pe.edu.pucp.vetcitas.bo.IBaseBO;
import pe.edu.pucp.vetcitas.cita.model.Recordatorio;

import java.time.LocalDate;
import java.util.List;

public interface IRecordatorioBO extends IBaseBO<Recordatorio> {
    List<Recordatorio> listarPorMascotaOCliente(String texto) throws Exception;
    List<Recordatorio> listarPorEstadoYFecha(String estado, LocalDate fecha) throws Exception;
}
