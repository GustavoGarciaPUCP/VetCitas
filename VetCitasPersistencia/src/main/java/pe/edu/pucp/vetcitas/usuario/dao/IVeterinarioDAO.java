package pe.edu.pucp.vetcitas.usuario.dao;

import pe.edu.pucp.vetcitas.dao.IDAO;
import pe.edu.pucp.vetcitas.usuario.model.Veterinario;

import java.time.LocalDateTime;
import java.util.List;

public interface IVeterinarioDAO extends IDAO<Veterinario> {
    List<Veterinario> listarDisponibles(LocalDateTime fechaHoraInicio, int idServicio);
}
