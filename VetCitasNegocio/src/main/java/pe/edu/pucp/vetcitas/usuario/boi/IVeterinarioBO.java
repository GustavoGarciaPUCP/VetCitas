package pe.edu.pucp.vetcitas.usuario.boi;

import pe.edu.pucp.vetcitas.bo.IBaseBO;
import pe.edu.pucp.vetcitas.usuario.model.Veterinario;

import java.time.LocalDateTime;
import java.util.List;

public interface IVeterinarioBO extends IBaseBO<Veterinario> {
    List<Veterinario> listarDisponibles(LocalDateTime fechaHoraInicio, int idServicio) throws Exception;
}
