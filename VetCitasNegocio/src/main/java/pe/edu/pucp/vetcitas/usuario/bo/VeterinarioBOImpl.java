package pe.edu.pucp.vetcitas.usuario.bo;

import pe.edu.pucp.vetcitas.usuario.boi.IVeterinarioBO;
import pe.edu.pucp.vetcitas.usuario.dao.IVeterinarioDAO;
import pe.edu.pucp.vetcitas.usuario.impl.VeterinarioImpl;
import pe.edu.pucp.vetcitas.usuario.model.Veterinario;

import java.time.LocalDateTime;
import java.util.List;

public class VeterinarioBOImpl implements IVeterinarioBO {
    private IVeterinarioDAO veterinarioDAO;

    public VeterinarioBOImpl() {
        this.veterinarioDAO = new VeterinarioImpl();
    }

    @Override
    public int insertar(Veterinario veterinario) throws Exception {
        validar(veterinario, false);
        return veterinarioDAO.insertar(veterinario);
    }

    @Override
    public int modificar(Veterinario veterinario) throws Exception {
        validar(veterinario, true);
        return veterinarioDAO.modificar(veterinario);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del veterinario debe ser mayor que cero.");
        }
        return veterinarioDAO.eliminar(id);
    }

    @Override
    public List<Veterinario> listarTodos() throws Exception {
        return veterinarioDAO.listarTodas();
    }

    @Override
    public Veterinario buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del veterinario debe ser mayor que cero.");
        }
        Veterinario vet = veterinarioDAO.buscarPorId(id);
        if (vet == null) {
            throw new Exception("El veterinario con id " + id + " no existe.");
        }
        return vet;
    }

    @Override
    public List<Veterinario> listarDisponibles(LocalDateTime fechaHoraInicio, int idServicio) throws Exception {
        if (fechaHoraInicio == null) {
            throw new Exception("La fecha y hora son obligatorias.");
        }
        if (idServicio <= 0) {
            throw new Exception("El id del servicio debe ser mayor que cero.");
        }
        return veterinarioDAO.listarDisponibles(fechaHoraInicio, idServicio);
    }

    private void validar(Veterinario vet, boolean esModificacion) throws Exception {
        if (vet == null) {
            throw new Exception("El veterinario no puede ser nulo.");
        }
        if (esModificacion && vet.getId() <= 0) {
            throw new Exception("El id del veterinario es obligatorio para la modificación.");
        }
        if (vet.getUsername() == null || vet.getUsername().trim().isEmpty()) {
            throw new Exception("El nombre de usuario es obligatorio.");
        }
        if (vet.getContrasenaHash() == null || vet.getContrasenaHash().trim().isEmpty()) {
            throw new Exception("La contraseña es obligatoria.");
        }
        if (vet.getNombres() == null || vet.getNombres().trim().isEmpty()) {
            throw new Exception("Los nombres son obligatorios.");
        }
        if (vet.getApellidos() == null || vet.getApellidos().trim().isEmpty()) {
            throw new Exception("Los apellidos son obligatorios.");
        }
        if (vet.getCmpv() == null || vet.getCmpv().trim().isEmpty()) {
            throw new Exception("El CMPV es obligatorio.");
        }
        if (vet.getEspecialidad() == null || vet.getEspecialidad().trim().isEmpty()) {
            throw new Exception("La especialidad es obligatoria.");
        }
    }
}
