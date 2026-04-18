package pe.edu.pucp.vetcitas.dao;

import java.util.List;

public interface IDAO<T> {
    int insertar(T objeto);
    int modificar(T objeto);
    int eliminar(int id);
    T buscarPorId(int id);
    List<T> listarTodas();
}
