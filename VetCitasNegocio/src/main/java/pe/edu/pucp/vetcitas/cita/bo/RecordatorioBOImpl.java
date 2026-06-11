package pe.edu.pucp.vetcitas.cita.bo;

import pe.edu.pucp.vetcitas.cita.boi.IRecordatorioBO;
import pe.edu.pucp.vetcitas.cita.dao.IRecordatorioDAO;
import pe.edu.pucp.vetcitas.cita.impl.RecordatorioImpl;
import pe.edu.pucp.vetcitas.cita.model.Recordatorio;
import pe.edu.pucp.vetcitas.common.enums.EstadoSeguimiento;

import java.time.LocalDate;
import java.util.List;

public class RecordatorioBOImpl implements IRecordatorioBO {
    private IRecordatorioDAO recordatorioDAO;

    public RecordatorioBOImpl() {
        this.recordatorioDAO = new RecordatorioImpl();
    }

    @Override
    public int insertar(Recordatorio recordatorio) throws Exception {
        validar(recordatorio, false);
        return recordatorioDAO.insertar(recordatorio);
    }

    @Override
    public int modificar(Recordatorio recordatorio) throws Exception {
        validar(recordatorio, true);
        return recordatorioDAO.modificar(recordatorio);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del recordatorio debe ser mayor que cero.");
        }
        return recordatorioDAO.eliminar(id);
    }

    @Override
    public List<Recordatorio> listarTodos() throws Exception {
        return recordatorioDAO.listarTodas();
    }

    @Override
    public Recordatorio buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del recordatorio debe ser mayor que cero.");
        }
        Recordatorio recordatorio = recordatorioDAO.buscarPorId(id);
        if (recordatorio == null) {
            throw new Exception("El recordatorio con id " + id + " no existe.");
        }
        return recordatorio;
    }

    @Override
    public List<Recordatorio> listarPorMascotaOCliente(String texto) throws Exception {
        if (texto == null) texto = "";
        texto = texto.trim();
        return recordatorioDAO.listarPorMascotaOCliente(texto);
    }

    @Override
    public List<Recordatorio> listarPorEstadoYFecha(String estado, LocalDate fecha) throws Exception {
        if (estado == null) estado = "";
        estado = estado.trim();

        if (!estado.isEmpty()) {
            try {
                EstadoSeguimiento.valueOf(estado);
            } catch (Exception ex) {
                throw new Exception("El estado de seguimiento no es válido.");
            }
        }

        return recordatorioDAO.listarPorEstadoYFecha(estado, fecha);
    }

    @Override
    public void marcarEnviado(int idRecordatorio, int modifiedBy) throws Exception {
        if (idRecordatorio <= 0) {
            throw new Exception("El id del recordatorio debe ser mayor que cero.");
        }

        if (modifiedBy <= 0) {
            throw new Exception("El usuario que modifica es obligatorio.");
        }

        Recordatorio recordatorio = buscarPorId(idRecordatorio);

        if (recordatorio.getEstadoSeguimiento() != EstadoSeguimiento.PENDIENTE) {
            throw new Exception("Solo se pueden marcar como enviados los recordatorios pendientes.");
        }

        recordatorioDAO.marcarEnviado(idRecordatorio, modifiedBy);
    }

    @Override
    public int contarPendientes() throws Exception {
        return recordatorioDAO.contarPendientes();
    }


    private void validar(Recordatorio recordatorio, boolean esModificacion) throws Exception {
        if (recordatorio == null) {
            throw new Exception("El recordatorio no puede ser nulo.");
        }
        if (esModificacion && recordatorio.getId() <= 0) {
            throw new Exception("El id del recordatorio es obligatorio para la modificación.");
        }
        if (recordatorio.getFechaProgramada() == null) {
            throw new Exception("La fecha programada del recordatorio es obligatoria.");
        }
        if (recordatorio.getCanal() == null) {
            throw new Exception("El canal del recordatorio es obligatorio.");
        }
        if (recordatorio.getEstadoSeguimiento() == null) {
            throw new Exception("El estado de seguimiento es obligatorio.");
        }
        if (recordatorio.getMensaje() == null || recordatorio.getMensaje().trim().isEmpty()) {
            throw new Exception("El mensaje del recordatorio es obligatorio.");
        }
        if (recordatorio.getCita() == null || recordatorio.getCita().getId() <= 0) {
            throw new Exception("La cita asociada al recordatorio es obligatoria.");
        }
    }
}
