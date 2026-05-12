package pe.edu.pucp.vetcitas.usuario.bo;

import pe.edu.pucp.vetcitas.usuario.boi.IPermisoBO;
import pe.edu.pucp.vetcitas.usuario.dao.IPermisoDAO;
import pe.edu.pucp.vetcitas.usuario.impl.PermisoImpl;
import pe.edu.pucp.vetcitas.usuario.model.Permiso;

import java.util.List;

public class PermisoBOImpl implements IPermisoBO {
    private IPermisoDAO permisoDAO;

    public PermisoBOImpl() {
        this.permisoDAO = new PermisoImpl();
    }

    @Override
    public int insertar(Permiso permiso) throws Exception {
        validar(permiso, false);
        return permisoDAO.insertar(permiso);
    }

    @Override
    public int modificar(Permiso permiso) throws Exception {
        validar(permiso, true);
        return permisoDAO.modificar(permiso);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del permiso debe ser mayor que cero.");
        }
        return permisoDAO.eliminar(id);
    }

    @Override
    public List<Permiso> listarTodos() throws Exception {
        return permisoDAO.listarTodas();
    }

    @Override
    public Permiso buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del permiso debe ser mayor que cero.");
        }
        Permiso permiso = permisoDAO.buscarPorId(id);
        if (permiso == null) {
            throw new Exception("El permiso con id " + id + " no existe.");
        }
        return permiso;
    }

    private void validar(Permiso permiso, boolean esModificacion) throws Exception {
        if (permiso == null) {
            throw new Exception("El permiso no puede ser nulo.");
        }
        if (esModificacion && permiso.getId() <= 0) {
            throw new Exception("El id del permiso es obligatorio para la modificación.");
        }
        if (permiso.getNombre() == null || permiso.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del permiso es obligatorio.");
        }
    }
}
