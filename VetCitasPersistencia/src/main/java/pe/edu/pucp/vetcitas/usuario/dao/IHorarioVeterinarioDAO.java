package pe.edu.pucp.vetcitas.usuario.dao;

import pe.edu.pucp.vetcitas.dao.IDAO;
import pe.edu.pucp.vetcitas.usuario.model.HorarioVeterinario;

import java.util.List;

public interface IHorarioVeterinarioDAO extends IDAO<HorarioVeterinario> {
    List<HorarioVeterinario> listarPorVeterinario(int idVeterinario);
}
