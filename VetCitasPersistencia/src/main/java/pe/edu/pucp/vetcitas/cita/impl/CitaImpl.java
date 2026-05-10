package pe.edu.pucp.vetcitas.cita.impl;

import pe.edu.pucp.vetcitas.cita.dao.ICitaDAO;
import pe.edu.pucp.vetcitas.cita.model.Cita;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;
import pe.edu.pucp.vetcitas.common.enums.EstadoCita;
import pe.edu.pucp.vetcitas.common.enums.CodigoRol;
import pe.edu.pucp.vetcitas.common.enums.TipoServicio;
import pe.edu.pucp.vetcitas.config.DBManager;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;
import pe.edu.pucp.vetcitas.usuario.model.Veterinario;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CitaImpl implements ICitaDAO {
    @Override
    public int insertar(Cita cita) {
        int idGenerado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL insertar_cita(?, ?, ?, ?, ?, ?, ?)}");

            cs.setTimestamp(1, Timestamp.valueOf(cita.getFechaHoraInicio()));
            cs.setString(2, cita.getEstado().name());
            cs.setInt(3, cita.getMascota().getId());
            cs.setInt(4, cita.getVeterinario().getId());
            cs.setInt(5, cita.getServicio().getId());
            cs.setTimestamp(6, Timestamp.valueOf(cita.getCreatedOn()));
            cs.registerOutParameter(7, Types.INTEGER);

            cs.executeUpdate();

            idGenerado = cs.getInt(7);
            cita.setId(idGenerado);

        } catch (Exception ex) {
            System.out.println("ERROR insertando cita: " + ex.getMessage());
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
    public int modificar(Cita cita) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL modificar_cita(?, ?, ?, ?, ?, ?, ?, ?)}");

            cs.setInt(1, cita.getId());
            cs.setTimestamp(2, Timestamp.valueOf(cita.getFechaHoraInicio()));
            cs.setString(3, cita.getEstado().name());
            cs.setInt(4, cita.getMascota().getId());
            cs.setInt(5, cita.getVeterinario().getId());
            cs.setInt(6, cita.getServicio().getId());
            cs.setTimestamp(7, Timestamp.valueOf(cita.getModifiedOn()));

            if (cita.getModifiedBy() != null) {
                cs.setInt(8, cita.getModifiedBy().getId());
            } else {
                cs.setNull(8, Types.INTEGER);
            }

            resultado = cs.executeUpdate();

        } catch (Exception ex) {
            System.out.println("ERROR modificando cita: " + ex.getMessage());
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
        throw new UnsupportedOperationException("Eliminar físico de cita no aplica; usar cancelarCita().");
    }

    @Override
    public Cita buscarPorId(int id) {
        Cita cita = null;
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL buscar_cita_por_id(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();

            if (rs.next()) {
                cita = mapearCita(rs);
            }

        } catch (Exception ex) {
            System.out.println("ERROR buscando cita por id: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en buscarPorId: " + ex.getMessage());
            }
        }
        return cita;
    }

    @Override
    public List<Cita> listarTodas() {
        List<Cita> lista = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_citas()}");
            rs = cs.executeQuery();

            while (rs.next()) {
                lista.add(mapearCita(rs));
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando citas: " + ex.getMessage());
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
    public void cancelarCita(int idCita, int modifiedBy) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL cancelar_cita(?, ?)}");
            cs.setInt(1, idCita);
            cs.setInt(2, modifiedBy);
            cs.executeUpdate();

        } catch (Exception ex) {
            System.out.println("ERROR cancelando cita: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en cancelarCita: " + ex.getMessage());
            }
        }
    }

    @Override
    public void confirmarCita(int idCita, int modifiedBy) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL confirmar_cita(?, ?)}");
            cs.setInt(1, idCita);
            cs.setInt(2, modifiedBy);
            cs.executeUpdate();

        } catch (Exception ex) {
            System.out.println("ERROR confirmando cita: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en confirmarCita: " + ex.getMessage());
            }
        }
    }

    @Override
    public void marcarAtendida(int idCita, int modifiedBy) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL marcar_cita_atendida(?, ?)}");
            cs.setInt(1, idCita);
            cs.setInt(2, modifiedBy);
            cs.executeUpdate();

        } catch (Exception ex) {
            System.out.println("ERROR marcando cita atendida: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en marcarAtendida: " + ex.getMessage());
            }
        }
    }

    @Override
    public void marcarNoAsistio(int idCita, int modifiedBy) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL marcar_cita_no_asistio(?, ?)}");
            cs.setInt(1, idCita);
            cs.setInt(2, modifiedBy);
            cs.executeUpdate();

        } catch (Exception ex) {
            System.out.println("ERROR marcando cita no asistió: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en marcarNoAsistio: " + ex.getMessage());
            }
        }
    }

    @Override
    public List<Cita> listarPorVeterinarioYFecha(int idVeterinario, LocalDate fecha) {
        List<Cita> lista = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_citas_por_veterinario_fecha(?, ?)}");
            cs.setInt(1, idVeterinario);
            cs.setDate(2, java.sql.Date.valueOf(fecha));
            rs = cs.executeQuery();

            while (rs.next()) {
                lista.add(mapearCita(rs));
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando citas por veterinario y fecha: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en listarPorVeterinarioYFecha: " + ex.getMessage());
            }
        }
        return lista;
    }

    private Cita mapearCita(ResultSet rs) throws Exception {
        Cita cita = new Cita();
        cita.setId(rs.getInt("id_cita"));
        cita.setFechaHoraInicio(rs.getTimestamp("fecha_hora_inicio").toLocalDateTime());
        cita.setFechaHoraFin(rs.getTimestamp("fecha_hora_fin").toLocalDateTime());
        cita.setEstado(EstadoCita.valueOf(rs.getString("estado")));

        Mascota mascota = new Mascota();
        mascota.setId(rs.getInt("id_mascota"));
        mascota.setNombre(rs.getString("nombre_mascota"));
        cita.setMascota(mascota);

        Veterinario veterinario = new Veterinario();
        veterinario.setId(rs.getInt("id_veterinario"));
        cita.setVeterinario(veterinario);

        Servicio servicio = new Servicio();
        servicio.setId(rs.getInt("id_servicio"));
        servicio.setNombre(rs.getString("nombre_servicio"));
        cita.setServicio(servicio);

        return cita;
    }
}
