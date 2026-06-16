package pe.edu.pucp.vetcitas.cliente.boi;

import pe.edu.pucp.vetcitas.bo.IBaseBO;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;

import java.util.List;

public interface IClienteBO extends IBaseBO<Cliente> {
    List<Cliente> listarPorNombreApellidoODni(String texto) throws Exception;
    int contarActivos() throws Exception;
    int contarNuevosEnMes(int anio, int mes) throws Exception;
}
