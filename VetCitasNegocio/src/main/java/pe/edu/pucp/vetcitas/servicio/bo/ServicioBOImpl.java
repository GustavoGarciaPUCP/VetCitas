package pe.edu.pucp.vetcitas.servicio.bo;

import pe.edu.pucp.vetcitas.cita.model.ServicioAtencionResumen;
import pe.edu.pucp.vetcitas.servicio.boi.IServicioBO;
import pe.edu.pucp.vetcitas.servicio.dao.IServicioDAO;
import pe.edu.pucp.vetcitas.servicio.impl.ServicioImpl;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;

import java.time.LocalDateTime;
import java.util.List;

public class ServicioBOImpl implements IServicioBO {
    private IServicioDAO servicioDAO;

    public ServicioBOImpl() {
        this.servicioDAO = new ServicioImpl();
    }

    @Override
    public int insertar(Servicio servicio) throws Exception {
        validar(servicio, false);
        return servicioDAO.insertar(servicio);
    }

    @Override
    public int modificar(Servicio servicio) throws Exception {
        validar(servicio, true);
        return servicioDAO.modificar(servicio);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del servicio debe ser mayor que cero.");
        }
        return servicioDAO.eliminar(id);
    }

    @Override
    public List<Servicio> listarTodos() throws Exception {
        return servicioDAO.listarTodas();
    }

    @Override
    public Servicio buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del servicio debe ser mayor que cero.");
        }
        Servicio servicio = servicioDAO.buscarPorId(id);
        if (servicio == null) {
            throw new Exception("El servicio con id " + id + " no existe.");
        }
        return servicio;
    }

    @Override
    public int deshabilitar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del servicio debe ser mayor que cero.");
        }
        Servicio servicio = servicioDAO.buscarPorId(id);
        if (servicio == null) {
            throw new Exception("El servicio con id " + id + " no existe.");
        }
        if (!servicio.isActivo()) {
            throw new Exception("El servicio ya se encuentra deshabilitado.");
        }
        return servicioDAO.deshabilitar(id);
    }

    @Override
    public List<Servicio> listarPorNombreOTipo(String texto) throws Exception {
        if (texto == null) texto = "";
        texto = texto.trim();
        return servicioDAO.listarPorNombreOTipo(texto);
    }

    @Override
    public List<Servicio> listarPorEstado(boolean activo) throws Exception {
        return servicioDAO.listarPorEstado(activo);
    }

    @Override
    public List<ServicioAtencionResumen> topNMasDemandados(
            LocalDateTime desde,
            LocalDateTime hasta,
            int limite
    ) throws Exception {
        if (desde == null || hasta == null) {
            throw new Exception("El rango de fechas es obligatorio.");
        }

        if (hasta.isBefore(desde)) {
            throw new Exception("La fecha final no puede ser menor que la fecha inicial.");
        }

        if (limite <= 0) {
            throw new Exception("El límite debe ser mayor que cero.");
        }

        return servicioDAO.topNMasDemandados(desde, hasta, limite);
    }

    private void validar(Servicio servicio, boolean esModificacion) throws Exception {
        if (servicio == null) {
            throw new Exception("El servicio no puede ser nulo.");
        }
        if (esModificacion && servicio.getId() <= 0) {
            throw new Exception("El id del servicio es obligatorio para la modificación.");
        }
        if (servicio.getNombre() == null || servicio.getNombre().trim().isEmpty()) {
            throw new Exception("El nombre del servicio es obligatorio.");
        }
        if (servicio.getTipoServicio() == null) {
            throw new Exception("El tipo de servicio es obligatorio.");
        }
        if (servicio.getDuracionMinutos() <= 0) {
            throw new Exception("La duración en minutos debe ser mayor que cero.");
        }
        if (servicio.getPrecioReferencial() < 0) {
            throw new Exception("El precio referencial no puede ser negativo.");
        }
        if (servicio.getDescripcion() != null) {
            String descripcion = servicio.getDescripcion().trim();

            if (descripcion.length() > 255) {
                throw new Exception("La descripción del servicio no puede superar los 255 caracteres.");
            }

            servicio.setDescripcion(descripcion);
        }
    }
}
