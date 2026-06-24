package pe.edu.pucp.vetcitas.usuario.bo;

import pe.edu.pucp.vetcitas.usuario.boi.IHorarioVeterinarioBO;
import pe.edu.pucp.vetcitas.usuario.dao.IHorarioVeterinarioDAO;
import pe.edu.pucp.vetcitas.usuario.impl.HorarioVeterinarioImpl;
import pe.edu.pucp.vetcitas.usuario.model.HorarioVeterinario;

import java.time.LocalTime;
import java.util.List;

public class HorarioVeterinarioBOImpl implements IHorarioVeterinarioBO {
    private IHorarioVeterinarioDAO horarioDAO;

    public HorarioVeterinarioBOImpl() {
        this.horarioDAO = new HorarioVeterinarioImpl();
    }

    @Override
    public int insertar(HorarioVeterinario horario) throws Exception {
        validar(horario, false);
        validarDiaUnico(horario);
        validarSinSolapamiento(horario);
        return horarioDAO.insertar(horario);
    }

    @Override
    public int modificar(HorarioVeterinario horario) throws Exception {
        validar(horario, true);
        validarDiaUnico(horario);
        validarSinSolapamiento(horario);
        return horarioDAO.modificar(horario);
    }

    @Override
    public int eliminar(int id) throws Exception {
        return eliminar(id, 0);
    }

    @Override
    public int eliminar(int id, int modifiedBy) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del horario debe ser mayor que cero.");
        }
        return horarioDAO.eliminar(id, modifiedBy);
    }

    @Override
    public List<HorarioVeterinario> listarTodos() throws Exception {
        return horarioDAO.listarTodas();
    }

    @Override
    public HorarioVeterinario buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del horario debe ser mayor que cero.");
        }
        HorarioVeterinario horario = horarioDAO.buscarPorId(id);
        if (horario == null) {
            throw new Exception("El horario con id " + id + " no existe.");
        }
        return horario;
    }

    @Override
    public List<HorarioVeterinario> listarPorVeterinario(int idVeterinario) throws Exception {
        if (idVeterinario <= 0) {
            throw new Exception("El id del veterinario debe ser mayor que cero.");
        }
        return horarioDAO.listarPorVeterinario(idVeterinario);
    }

    @Override
    public List<HorarioVeterinario> listarHorarioSemanalPorVeterinario(int idVeterinario) throws Exception {
        if (idVeterinario <= 0) {
            throw new Exception("El id del veterinario debe ser mayor que cero.");
        }
        return horarioDAO.listarHorarioSemanalPorVeterinario(idVeterinario);
    }


    private void validar(HorarioVeterinario horario, boolean esModificacion) throws Exception {
        if (horario == null) {
            throw new Exception("El horario no puede ser nulo.");
        }
        if (esModificacion && horario.getId() <= 0) {
            throw new Exception("El id del horario es obligatorio para la modificación.");
        }
        if (horario.getVeterinario() == null || horario.getVeterinario().getId() <= 0) {
            throw new Exception("El veterinario asociado al horario es obligatorio.");
        }
        if (horario.getDiaSemana() < 1 || horario.getDiaSemana() > 7) {
            throw new Exception("El día de la semana debe estar entre 1 (lunes) y 7 (domingo).");
        }
        LocalTime inicio = horario.getHoraInicio();
        LocalTime fin = horario.getHoraFin();
        if (inicio == null) {
            throw new Exception("La hora de inicio es obligatoria.");
        }
        if (fin == null) {
            throw new Exception("La hora de fin es obligatoria.");
        }
        if (!fin.isAfter(inicio)) {
            throw new Exception("La hora de fin debe ser posterior a la hora de inicio.");
        }

        LocalTime descansoInicio = horario.getHoraDescansoInicio();
        LocalTime descansoFin = horario.getHoraDescansoFin();
        if (descansoInicio != null || descansoFin != null) {
            if (descansoInicio == null || descansoFin == null) {
                throw new Exception("Si se define un descanso, deben indicarse tanto la hora de inicio como la de fin.");
            }
            if (!descansoFin.isAfter(descansoInicio)) {
                throw new Exception("La hora de fin del descanso debe ser posterior a su hora de inicio.");
            }
            if (descansoInicio.isBefore(inicio) || descansoFin.isAfter(fin)) {
                throw new Exception("El descanso debe estar dentro del rango de la jornada.");
            }
        }
    }

    private void validarSinSolapamiento(HorarioVeterinario horario) throws Exception {
        if (!horario.isActivo()) {
            return;
        }

        List<HorarioVeterinario> horarios = horarioDAO.listarPorVeterinario(horario.getVeterinario().getId());
        for (HorarioVeterinario existente : horarios) {
            if (!existente.isActivo()) {
                continue;
            }
            if (existente.getId() == horario.getId()) {
                continue;
            }
            if (existente.getDiaSemana() != horario.getDiaSemana()) {
                continue;
            }
            if (horario.getHoraInicio().isBefore(existente.getHoraFin())
                    && horario.getHoraFin().isAfter(existente.getHoraInicio())) {
                throw new Exception("El horario se cruza con otro horario activo del veterinario.");
            }
        }
    }

    private void validarDiaUnico(HorarioVeterinario horario) throws Exception {
        List<HorarioVeterinario> horarios = horarioDAO.listarPorVeterinario(horario.getVeterinario().getId());
        for (HorarioVeterinario existente : horarios) {
            if (existente.getId() == horario.getId()) {
                continue;
            }
            if (existente.getDiaSemana() == horario.getDiaSemana()) {
                throw new Exception("El veterinario ya tiene un horario registrado para ese dia. Edita o reactiva el horario existente.");
            }
        }
    }
}
