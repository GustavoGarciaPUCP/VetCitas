package pe.edu.pucp.vetcitas.cliente.bo;

import pe.edu.pucp.vetcitas.cliente.boi.IMascotaBO;
import pe.edu.pucp.vetcitas.cliente.dao.MascotaDAO;
import pe.edu.pucp.vetcitas.cliente.impl.MascotaImpl;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;

import java.time.LocalDate;
import java.util.List;

public class MascotaBOImpl implements IMascotaBO {
    private MascotaDAO mascotaDAO;

    public MascotaBOImpl() {
        this.mascotaDAO = new MascotaImpl();
    }

    @Override
    public int insertar(Mascota mascota) throws Exception {
        validar(mascota, false);
        return mascotaDAO.insertar(mascota);
    }

    @Override
    public int modificar(Mascota mascota) throws Exception {
        validar(mascota, true);
        return mascotaDAO.modificar(mascota);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id de la mascota debe ser mayor que cero.");
        }
        return mascotaDAO.eliminar(id);
    }

    @Override
    public List<Mascota> listarTodos() throws Exception {
        return mascotaDAO.listarTodas();
    }

    @Override
    public Mascota buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id de la mascota debe ser mayor que cero.");
        }
        Mascota mascota = mascotaDAO.buscarPorId(id);
        if (mascota == null) {
            throw new Exception("La mascota con id " + id + " no existe.");
        }
        return mascota;
    }

    private void validar(Mascota mascota, boolean esModificacion) throws Exception {
        if (mascota == null) {
            throw new Exception("La mascota no puede ser nula.");
        }
        if (esModificacion && mascota.getId() <= 0) {
            throw new Exception("El id de la mascota es obligatorio para la modificación.");
        }
        if (mascota.getNombre() == null || mascota.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre de la mascota es obligatorio.");
        }
        if (mascota.getEspecie() == null || mascota.getEspecie().trim().isEmpty()) {
            throw new Exception("La especie es obligatoria.");
        }
        if (mascota.getFechaNacimiento() != null && mascota.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new Exception("La fecha de nacimiento no puede ser futura.");
        }
        if (mascota.getCliente() == null || mascota.getCliente().getId() <= 0) {
            throw new Exception("El cliente dueño de la mascota es obligatorio.");
        }
    }
}
