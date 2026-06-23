package pe.edu.pucp.vetcitas.usuario.boi;

import pe.edu.pucp.vetcitas.bo.IBaseBO;
import pe.edu.pucp.vetcitas.usuario.model.HorarioVeterinario;

import java.util.List;

public interface IHorarioVeterinarioBO extends IBaseBO<HorarioVeterinario> {
    int eliminar(int id, int modifiedBy) throws Exception;
    List<HorarioVeterinario> listarPorVeterinario(int idVeterinario) throws Exception;
    List<HorarioVeterinario> listarHorarioSemanalPorVeterinario(int idVeterinario) throws Exception;
}
