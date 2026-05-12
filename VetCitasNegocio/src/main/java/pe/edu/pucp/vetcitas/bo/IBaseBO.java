package pe.edu.pucp.vetcitas.bo;

import java.util.List;

public interface IBaseBO<T> {
    int insertar(T objeto) throws Exception;
    int modificar(T objeto) throws Exception;
    int eliminar(int id) throws Exception;
    List<T> listarTodos() throws Exception;
    T buscarPorId(int id) throws Exception;
}
