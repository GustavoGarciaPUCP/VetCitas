package pe.edu.pucp.vetcitas.cliente.dao;

import pe.edu.pucp.vetcitas.cliente.model.Cliente;
import pe.edu.pucp.vetcitas.dao.IDAO;

import java.util.List;

public interface ClienteDAO extends IDAO<Cliente> {
    int eliminar(int idCliente, int modifiedBy);
    List<Cliente> listarPorNombreApellidoODni(String texto);
    int contarActivos();
    int contarNuevosEnMes(int anio, int mes);
}
