package pe.edu.pucp.vetcitas.cliente.boi;

import pe.edu.pucp.vetcitas.bo.IBaseBO;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;

import java.util.List;

public interface IMascotaBO extends IBaseBO<Mascota> {
    List<Mascota> listarPorNombreODueno(String texto) throws Exception;
    List<Mascota> listarPorCliente(int idCliente) throws Exception;
    int contarActivas() throws Exception;
}
