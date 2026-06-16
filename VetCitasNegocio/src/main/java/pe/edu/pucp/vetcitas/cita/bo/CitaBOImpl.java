package pe.edu.pucp.vetcitas.cita.bo;

import pe.edu.pucp.vetcitas.cita.boi.ICitaBO;
import pe.edu.pucp.vetcitas.cita.dao.ICitaDAO;
import pe.edu.pucp.vetcitas.cita.impl.CitaImpl;
import pe.edu.pucp.vetcitas.cita.model.Cita;
import pe.edu.pucp.vetcitas.common.enums.EstadoCita;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CitaBOImpl implements ICitaBO {
    private ICitaDAO citaDAO;

    public CitaBOImpl() {
        this.citaDAO = new CitaImpl();
    }

    @Override
    public int insertar(Cita cita) throws Exception {
        validar(cita, false);
        return citaDAO.insertar(cita);
    }

    @Override
    public int modificar(Cita cita) throws Exception {
        validar(cita, true);
        return citaDAO.modificar(cita);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id de la cita debe ser mayor que cero.");
        }
        return citaDAO.eliminar(id);
    }

    @Override
    public List<Cita> listarTodos() throws Exception {
        return citaDAO.listarTodas();
    }

    @Override
    public void reprogramar(int idCita,
                            LocalDateTime nuevaFechaHoraInicio,
                            LocalDateTime nuevaFechaHoraFin,
                            String motivoReprogramacion,
                            int modifiedBy) throws Exception {
        if (idCita <= 0) {
            throw new Exception("El id de la cita debe ser mayor que cero.");
        }

        if (nuevaFechaHoraInicio == null || nuevaFechaHoraFin == null) {
            throw new Exception("La nueva fecha de inicio y fin son obligatorias.");
        }

        if (!nuevaFechaHoraFin.isAfter(nuevaFechaHoraInicio)) {
            throw new Exception("La nueva fecha de fin debe ser posterior a la fecha de inicio.");
        }

        if (motivoReprogramacion == null || motivoReprogramacion.trim().isEmpty()) {
            throw new Exception("El motivo de reprogramación es obligatorio.");
        }

        motivoReprogramacion = motivoReprogramacion.trim();

        if (motivoReprogramacion.length() > 255) {
            throw new Exception("El motivo de reprogramación no puede superar los 255 caracteres.");
        }

        if (modifiedBy <= 0) {
            throw new Exception("El usuario que modifica es obligatorio.");
        }

        Cita cita = buscarPorId(idCita);

        if (cita.getEstado() != EstadoCita.PENDIENTE &&
                cita.getEstado() != EstadoCita.CONFIRMADA) {
            throw new Exception("Solo se pueden reprogramar citas pendientes o confirmadas.");
        }

        boolean disponible = citaDAO.validarDisponibilidadSlot(
                cita.getVeterinario().getId(),
                nuevaFechaHoraInicio,
                nuevaFechaHoraFin
        );

        if (!disponible) {
            throw new Exception("El veterinario no está disponible en el nuevo horario.");
        }

        citaDAO.reprogramar(
                idCita,
                nuevaFechaHoraInicio,
                nuevaFechaHoraFin,
                motivoReprogramacion,
                modifiedBy
        );
    }

    @Override
    public void cambiarVeterinario(int idCita, int idNuevoVeterinario, int modifiedBy) throws Exception {
        if (idCita <= 0) {
            throw new Exception("El id de la cita debe ser mayor que cero.");
        }

        if (idNuevoVeterinario <= 0) {
            throw new Exception("El id del nuevo veterinario debe ser mayor que cero.");
        }

        if (modifiedBy <= 0) {
            throw new Exception("El usuario que modifica es obligatorio.");
        }

        Cita cita = buscarPorId(idCita);

        if (cita.getEstado() != EstadoCita.PENDIENTE &&
                cita.getEstado() != EstadoCita.CONFIRMADA) {
            throw new Exception("Solo se puede cambiar el veterinario de citas pendientes o confirmadas.");
        }

        boolean disponible = citaDAO.validarDisponibilidadSlot(
                idNuevoVeterinario,
                cita.getFechaHoraInicio(),
                cita.getFechaHoraFin()
        );

        if (!disponible) {
            throw new Exception("El nuevo veterinario no está disponible en el horario de la cita.");
        }

        citaDAO.cambiarVeterinario(idCita, idNuevoVeterinario, modifiedBy);
    }

    @Override
    public boolean validarDisponibilidadSlot(int idVeterinario,
                                             LocalDateTime fechaHoraInicio,
                                             LocalDateTime fechaHoraFin) throws Exception {
        if (idVeterinario <= 0) {
            throw new Exception("El id del veterinario debe ser mayor que cero.");
        }

        if (fechaHoraInicio == null || fechaHoraFin == null) {
            throw new Exception("La fecha de inicio y fin son obligatorias.");
        }

        if (!fechaHoraFin.isAfter(fechaHoraInicio)) {
            throw new Exception("La fecha de fin debe ser posterior a la fecha de inicio.");
        }

        return citaDAO.validarDisponibilidadSlot(idVeterinario, fechaHoraInicio, fechaHoraFin);
    }

    @Override
    public int contarPorEstadoEnRango(String estado, LocalDateTime desde, LocalDateTime hasta) throws Exception {
        if (estado == null || estado.trim().isEmpty()) {
            throw new Exception("El estado es obligatorio.");
        }

        estado = estado.trim();

        try {
            EstadoCita.valueOf(estado);
        } catch (Exception ex) {
            throw new Exception("El estado de cita no es válido.");
        }

        if (desde == null || hasta == null) {
            throw new Exception("El rango de fechas es obligatorio.");
        }

        if (hasta.isBefore(desde)) {
            throw new Exception("La fecha final no puede ser menor que la fecha inicial.");
        }

        return citaDAO.contarPorEstadoEnRango(estado, desde, hasta);
    }

    @Override
    public int contarPorVeterinarioEnRango(int idVeterinario, LocalDateTime desde, LocalDateTime hasta) throws Exception {
        if (idVeterinario <= 0) {
            throw new Exception("El id del veterinario debe ser mayor que cero.");
        }

        if (desde == null || hasta == null) {
            throw new Exception("El rango de fechas es obligatorio.");
        }

        if (hasta.isBefore(desde)) {
            throw new Exception("La fecha final no puede ser menor que la fecha inicial.");
        }

        return citaDAO.contarPorVeterinarioEnRango(idVeterinario, desde, hasta);
    }

    @Override
    public Cita buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id de la cita debe ser mayor que cero.");
        }
        Cita cita = citaDAO.buscarPorId(id);
        if (cita == null) {
            throw new Exception("La cita con id " + id + " no existe.");
        }
        return cita;
    }

    @Override
    public void cancelarCita(int idCita, String motivoCancelacion,int modifiedBy) throws Exception {
        if (idCita <= 0) {
            throw new Exception("El id de la cita debe ser mayor que cero.");
        }

        if (modifiedBy <= 0) {
            throw new Exception("El id del usuario que cancela debe ser mayor que cero.");
        }

        if (motivoCancelacion == null || motivoCancelacion.trim().isEmpty()) {
            throw new Exception("El motivo de cancelación es obligatorio.");
        }

        motivoCancelacion = motivoCancelacion.trim();

        if (motivoCancelacion.length() > 255) {
            throw new Exception("El motivo de cancelación no puede superar los 255 caracteres.");
        }
        Cita cita = buscarPorId(idCita);
        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new Exception("La cita ya se encuentra cancelada.");
        }
        if (cita.getEstado() == EstadoCita.ATENDIDA) {
            throw new Exception("No se puede cancelar una cita que ya fue atendida.");
        }
        if (modifiedBy <= 0) {
            throw new Exception("El usuario que modifica es obligatorio.");
        }
        citaDAO.cancelarCita(idCita,motivoCancelacion, modifiedBy);
    }

    @Override
    public void confirmarCita(int idCita, int modifiedBy) throws Exception {
        Cita cita = buscarPorId(idCita);
        if (cita.getEstado() != EstadoCita.PENDIENTE) {
            throw new Exception("Solo se pueden confirmar citas en estado PENDIENTE.");
        }
        if (modifiedBy <= 0) {
            throw new Exception("El usuario que modifica es obligatorio.");
        }
        citaDAO.confirmarCita(idCita, modifiedBy);
    }

    @Override
    public void marcarAtendida(int idCita, int modifiedBy) throws Exception {
        Cita cita = buscarPorId(idCita);
        if (cita == null) {
            throw new Exception("La cita no existe.");
        }

        if (cita.getEstado() != EstadoCita.EN_CONSULTA) {
            throw new Exception("Solo una cita en consulta puede marcarse como atendida.");
        }
        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new Exception("No se puede marcar como atendida una cita cancelada.");
        }
        if (cita.getEstado() == EstadoCita.ATENDIDA) {
            throw new Exception("La cita ya fue marcada como atendida.");
        }
        if (modifiedBy <= 0) {
            throw new Exception("El usuario que modifica es obligatorio.");
        }
        citaDAO.marcarAtendida(idCita, modifiedBy);
    }

    @Override
    public void marcarNoAsistio(int idCita, int modifiedBy) throws Exception {
        Cita cita = buscarPorId(idCita);
        if (cita.getEstado() == EstadoCita.CANCELADA) {
            throw new Exception("No se puede marcar como no asistida una cita cancelada.");
        }
        if (cita.getEstado() == EstadoCita.ATENDIDA) {
            throw new Exception("No se puede marcar como no asistida una cita ya atendida.");
        }
        if (modifiedBy <= 0) {
            throw new Exception("El usuario que modifica es obligatorio.");
        }
        citaDAO.marcarNoAsistio(idCita, modifiedBy);
    }

    @Override
    public List<Cita> listarPorVeterinarioYFecha(int idVeterinario, LocalDate fecha) throws Exception {
        if (idVeterinario <= 0) {
            throw new Exception("El id del veterinario debe ser mayor que cero.");
        }
        if (fecha == null) {
            throw new Exception("La fecha es obligatoria.");
        }
        return citaDAO.listarPorVeterinarioYFecha(idVeterinario, fecha);
    }

    @Override
    public List<Cita> listarFiltradas(Integer idVeterinario, LocalDate fechaInicio, LocalDate fechaFin, String estado, String textoBusqueda) throws Exception {
        if (idVeterinario != null && idVeterinario <= 0) {
            throw new Exception("El id del veterinario debe ser mayor que cero.");
        }

        if (fechaInicio != null && fechaFin != null && fechaInicio.isAfter(fechaFin)) {
            throw new Exception("La fecha inicial no puede ser mayor que la fecha final.");
        }

        if (estado == null) estado = "";
        estado = estado.trim();

        if (!estado.isEmpty()) {
            try {
                EstadoCita.valueOf(estado);
            } catch (Exception ex) {
                throw new Exception("El estado de cita no es válido.");
            }
        }

        if (textoBusqueda == null) textoBusqueda = "";
        textoBusqueda = textoBusqueda.trim();

        return citaDAO.listarFiltradas(idVeterinario, fechaInicio, fechaFin, estado, textoBusqueda);
    }

    @Override
    public void marcarEnConsulta(int idCita, int idUsuario) throws Exception {
        if (idCita <= 0) {
            throw new Exception("El id de la cita debe ser mayor que cero.");
        }

        if (idUsuario <= 0) {
            throw new Exception("El id del usuario que modifica debe ser mayor que cero.");
        }

        Cita cita = citaDAO.buscarPorId(idCita);

        if (cita == null) {
            throw new Exception("La cita no existe.");
        }

        if (cita.getEstado() != EstadoCita.CONFIRMADA) {
            throw new Exception("Solo una cita confirmada puede pasar a EN_CONSULTA.");
        }

        citaDAO.marcarEnConsulta(idCita, idUsuario);
    }

    private void validar(Cita cita, boolean esModificacion) throws Exception {
        if (cita == null) {
            throw new Exception("La cita no puede ser nula.");
        }
        if (esModificacion && cita.getId() <= 0) {
            throw new Exception("El id de la cita es obligatorio para la modificación.");
        }
        LocalDateTime inicio = cita.getFechaHoraInicio();
        LocalDateTime fin = cita.getFechaHoraFin();
        if (inicio == null) {
            throw new Exception("La fecha y hora de inicio son obligatorias.");
        }
        if (fin == null) {
            throw new Exception("La fecha y hora de fin son obligatorias.");
        }
        if (!fin.isAfter(inicio)) {
            throw new Exception("La hora de fin debe ser posterior a la hora de inicio.");
        }
        if (!esModificacion && inicio.isBefore(LocalDateTime.now())) {
            throw new Exception("La fecha y hora de inicio no puede ser en el pasado.");
        }
        if (cita.getMascota() == null || cita.getMascota().getId() <= 0) {
            throw new Exception("La mascota es obligatoria.");
        }
        if (cita.getVeterinario() == null || cita.getVeterinario().getId() <= 0) {
            throw new Exception("El veterinario es obligatorio.");
        }
        if (cita.getServicio() == null || cita.getServicio().getId() <= 0) {
            throw new Exception("El servicio es obligatorio.");
        }
        if (cita.getEstado() == null) {
            throw new Exception("El estado de la cita es obligatorio.");
        }
    }
}
