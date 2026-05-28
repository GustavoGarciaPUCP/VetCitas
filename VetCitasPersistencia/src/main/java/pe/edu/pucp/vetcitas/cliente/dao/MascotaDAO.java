package pe.edu.pucp.vetcitas.cliente.dao;

import pe.edu.pucp.vetcitas.cliente.model.Mascota;
import pe.edu.pucp.vetcitas.dao.IDAO;

import java.util.List;

public interface MascotaDAO extends IDAO<Mascota> {
    List<Mascota> listarPorNombreODueno(String texto);
    List<Mascota> listarPorCliente(int idCliente);
}
