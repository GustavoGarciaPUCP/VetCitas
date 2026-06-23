package pe.edu.pucp.vetcitas.usuario.impl;

import pe.edu.pucp.vetcitas.config.DBManager;
import pe.edu.pucp.vetcitas.usuario.dao.IHorarioVeterinarioDAO;
import pe.edu.pucp.vetcitas.usuario.model.HorarioVeterinario;
import pe.edu.pucp.vetcitas.usuario.model.Veterinario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorarioVeterinarioImpl implements IHorarioVeterinarioDAO {
    @Override
    public int insertar(HorarioVeterinario horario) {
        int idGenerado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL insertar_horario_veterinario(?, ?, ?, ?, ?, ?, ?, ?)}");

            cs.setInt(1, horario.getVeterinario().getId());
            cs.setInt(2, horario.getDiaSemana());
            cs.setTime(3, Time.valueOf(horario.getHoraInicio()));
            cs.setTime(4, Time.valueOf(horario.getHoraFin()));

            if (horario.getHoraDescansoInicio() != null) {
                cs.setTime(5, Time.valueOf(horario.getHoraDescansoInicio()));
            } else {
                cs.setNull(5, Types.TIME);
            }

            if (horario.getHoraDescansoFin() != null) {
                cs.setTime(6, Time.valueOf(horario.getHoraDescansoFin()));
            } else {
                cs.setNull(6, Types.TIME);
            }

            if (horario.getModifiedBy() != null) {
                cs.setInt(7, horario.getModifiedBy().getId());
            } else {
                cs.setNull(7, Types.INTEGER);
            }

            cs.registerOutParameter(8, Types.INTEGER);
            cs.executeUpdate();

            idGenerado = cs.getInt(8);
            horario.setId(idGenerado);

        } catch (Exception ex) {
            System.out.println("ERROR insertando horario veterinario: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en insertar: " + ex.getMessage());
            }
        }
        return idGenerado;
    }

    @Override
    public int modificar(HorarioVeterinario horario) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL modificar_horario_veterinario(?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            cs.setInt(1, horario.getId());
            cs.setInt(2, horario.getVeterinario().getId());
            cs.setInt(3, horario.getDiaSemana());
            cs.setTime(4, Time.valueOf(horario.getHoraInicio()));
            cs.setTime(5, Time.valueOf(horario.getHoraFin()));

            if (horario.getHoraDescansoInicio() != null) {
                cs.setTime(6, Time.valueOf(horario.getHoraDescansoInicio()));
            } else {
                cs.setNull(6, Types.TIME);
            }

            if (horario.getHoraDescansoFin() != null) {
                cs.setTime(7, Time.valueOf(horario.getHoraDescansoFin()));
            } else {
                cs.setNull(7, Types.TIME);
            }

            cs.setBoolean(8, horario.isActivo());

            if (horario.getModifiedBy() != null) {
                cs.setInt(9, horario.getModifiedBy().getId());
            } else {
                cs.setNull(9, Types.INTEGER);
            }

            resultado = cs.executeUpdate();

        } catch (Exception ex) {
            System.out.println("ERROR modificando horario veterinario: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en modificar: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        return eliminar(id, 0);
    }

    public int eliminar(int id, int modifiedBy) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL eliminar_horario_veterinario(?, ?)}");
            cs.setInt(1, id);
            if (modifiedBy > 0) {
                cs.setInt(2, modifiedBy);
            } else {
                cs.setNull(2, Types.INTEGER);
            }
            resultado = cs.executeUpdate();

        } catch (Exception ex) {
            System.out.println("ERROR eliminando horario veterinario: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en eliminar: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public HorarioVeterinario buscarPorId(int id) {
        HorarioVeterinario horario = null;
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL buscar_horario_veterinario_por_id(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();

            if (rs.next()) {
                horario = mapearHorario(rs);
            }

        } catch (Exception ex) {
            System.out.println("ERROR buscando horario por id: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en buscarPorId: " + ex.getMessage());
            }
        }
        return horario;
    }

    @Override
    public List<HorarioVeterinario> listarTodas() {
        List<HorarioVeterinario> lista = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_horarios_veterinario_todos()}");
            rs = cs.executeQuery();

            while (rs.next()) {
                lista.add(mapearHorario(rs));
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando todos los horarios: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en listarTodas: " + ex.getMessage());
            }
        }
        return lista;
    }

    @Override
    public List<HorarioVeterinario> listarPorVeterinario(int idVeterinario) {
        List<HorarioVeterinario> lista = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_horarios_veterinario(?)}");
            cs.setInt(1, idVeterinario);
            rs = cs.executeQuery();

            while (rs.next()) {
                lista.add(mapearHorario(rs));
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando horarios de veterinario: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en listarPorVeterinario: " + ex.getMessage());
            }
        }
        return lista;
    }

    private HorarioVeterinario mapearHorario(ResultSet rs) throws Exception {
        HorarioVeterinario horario = new HorarioVeterinario();
        horario.setId(rs.getInt("id_horario"));

        Veterinario veterinario = new Veterinario();
        veterinario.setId(rs.getInt("id_veterinario"));
        horario.setVeterinario(veterinario);

        horario.setDiaSemana(rs.getInt("dia_semana"));
        horario.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
        horario.setHoraFin(rs.getTime("hora_fin").toLocalTime());

        Time descansoInicio = rs.getTime("hora_descanso_inicio");
        if (descansoInicio != null) {
            horario.setHoraDescansoInicio(descansoInicio.toLocalTime());
        }

        Time descansoFin = rs.getTime("hora_descanso_fin");
        if (descansoFin != null) {
            horario.setHoraDescansoFin(descansoFin.toLocalTime());
        }

        horario.setActivo(rs.getBoolean("activo"));
        return horario;
    }

    @Override
    public List<HorarioVeterinario> listarHorarioSemanalPorVeterinario(int idVeterinario) {
        List<HorarioVeterinario> horarios = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL listar_horario_semanal_por_veterinario(?)}";
            cs = con.prepareCall(sql);
            cs.setInt(1, idVeterinario);
            rs = cs.executeQuery();

            while (rs.next()) {
                HorarioVeterinario hv = new HorarioVeterinario();
                hv.setId(rs.getInt("id_horario"));
                hv.setDiaSemana(rs.getInt("dia_semana"));
                hv.setHoraInicio(rs.getTime("hora_inicio").toLocalTime());
                hv.setHoraFin(rs.getTime("hora_fin").toLocalTime());

                if (rs.getTime("hora_descanso_inicio") != null)
                    hv.setHoraDescansoInicio(rs.getTime("hora_descanso_inicio").toLocalTime());

                if (rs.getTime("hora_descanso_fin") != null)
                    hv.setHoraDescansoFin(rs.getTime("hora_descanso_fin").toLocalTime());

                hv.setActivo(rs.getBoolean("activo"));

                Veterinario vet = new Veterinario();
                vet.setId(rs.getInt("id_veterinario"));
                vet.setNombres(rs.getString("nombres"));
                vet.setApellidos(rs.getString("apellidos"));

                hv.setVeterinario(vet);
                horarios.add(hv);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando horario semanal por veterinario: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en HorarioVeterinarioImpl: " + ex.getMessage());
            }
        }

        return horarios;
    }

}
