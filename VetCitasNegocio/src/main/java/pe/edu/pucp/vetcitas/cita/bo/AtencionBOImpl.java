package pe.edu.pucp.vetcitas.cita.bo;

import pe.edu.pucp.vetcitas.cita.boi.IAtencionBO;
import pe.edu.pucp.vetcitas.cita.dao.IAtencionDAO;
import pe.edu.pucp.vetcitas.cita.impl.AtencionImpl;
import pe.edu.pucp.vetcitas.cita.model.Atencion;
import pe.edu.pucp.vetcitas.configuracion.bo.ConfiguracionBOImpl;
import pe.edu.pucp.vetcitas.configuracion.boi.IConfiguracionBO;
import pe.edu.pucp.vetcitas.configuracion.model.Configuracion;

import java.util.List;

public class AtencionBOImpl implements IAtencionBO {
    private IAtencionDAO atencionDAO;
    private IConfiguracionBO configuracionBO;

    public AtencionBOImpl() {
        this.atencionDAO = new AtencionImpl();
        this.configuracionBO = new ConfiguracionBOImpl();
    }

    @Override
    public int insertar(Atencion atencion) throws Exception {
        validar(atencion, false);
        return atencionDAO.insertar(atencion);
    }

    @Override
    public int modificar(Atencion atencion) throws Exception {
        validar(atencion, true);
        return atencionDAO.modificar(atencion);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id de la atención debe ser mayor que cero.");
        }
        return atencionDAO.eliminar(id);
    }

    @Override
    public List<Atencion> listarTodos() throws Exception {
        return atencionDAO.listarTodas();
    }

    @Override
    public Atencion buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id de la atención debe ser mayor que cero.");
        }
        Atencion atencion = atencionDAO.buscarPorId(id);
        if (atencion == null) {
            throw new Exception("La atención con id " + id + " no existe.");
        }
        return atencion;
    }

    @Override
    public Atencion buscarPorCita(int idCita) throws Exception {
        if (idCita <= 0) {
            throw new Exception("El id de la cita debe ser mayor que cero.");
        }
        return atencionDAO.buscarPorCita(idCita);
    }

    private void validar(Atencion atencion, boolean esModificacion) throws Exception {
        if (atencion == null) {
            throw new Exception("La atención no puede ser nula.");
        }
        if (esModificacion && atencion.getId() <= 0) {
            throw new Exception("El id de la atención es obligatorio para la modificación.");
        }
        if (atencion.getFechaHora() == null) {
            throw new Exception("La fecha y hora de la atención son obligatorias.");
        }
        if (atencion.getCita() == null || atencion.getCita().getId() <= 0) {
            throw new Exception("La cita asociada a la atención es obligatoria.");
        }
        if (atencion.getMontoReferencial() < 0) {
            throw new Exception("El monto referencial no puede ser negativo.");
        }
        if (atencion.getDescuentoAplicado() < 0) {
            throw new Exception("El descuento aplicado no puede ser negativo.");
        }

        Configuracion conf = configuracionBO.obtenerConfiguracionActual();
        if (atencion.getDescuentoAplicado() > conf.getDescuentoMaximoPermitido()) {
            throw new Exception("El descuento aplicado supera el máximo permitido ("
                    + conf.getDescuentoMaximoPermitido() + "%).");
        }
    }
}
