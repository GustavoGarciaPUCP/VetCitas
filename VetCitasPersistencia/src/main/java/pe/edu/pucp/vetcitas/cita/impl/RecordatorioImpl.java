package pe.edu.pucp.vetcitas.cita.impl;

import pe.edu.pucp.vetcitas.cita.dao.IRecordatorioDAO;
import pe.edu.pucp.vetcitas.cita.model.Cita;
import pe.edu.pucp.vetcitas.cita.model.Recordatorio;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;
import pe.edu.pucp.vetcitas.common.enums.CanalRecordatorio;
import pe.edu.pucp.vetcitas.common.enums.EstadoSeguimiento;
import pe.edu.pucp.vetcitas.common.enums.EstadoCita;
import pe.edu.pucp.vetcitas.config.DBManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RecordatorioImpl implements IRecordatorioDAO {
    @Override
    public int insertar(Recordatorio objeto) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL insertar_recordatorio(?,?,?,?,?,?,?)}");
            cs.setTimestamp(1, Timestamp.valueOf(objeto.getFechaProgramada()));
            cs.setString(2, objeto.getCanal().name());
            cs.setString(3, objeto.getEstadoSeguimiento().name());
            cs.setString(4, objeto.getMensaje());
            cs.setInt(5, objeto.getCita().getId());
            if (objeto.getModifiedBy() != null) {
                cs.setInt(6, objeto.getModifiedBy().getId());
            } else {
                cs.setNull(6, Types.INTEGER);
            }
            cs.registerOutParameter(7, Types.INTEGER);
            cs.executeUpdate();
            resultado = cs.getInt(7);
            objeto.setId(resultado);
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public int modificar(Recordatorio objeto) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL modificar_recordatorio(?,?,?,?,?,?,?)}");
            cs.setInt(1, objeto.getId());
            cs.setTimestamp(2, Timestamp.valueOf(objeto.getFechaProgramada()));
            cs.setString(3, objeto.getCanal().name());
            cs.setString(4, objeto.getEstadoSeguimiento().name());
            cs.setString(5, objeto.getMensaje());
            cs.setInt(6, objeto.getCita().getId());
            if (objeto.getModifiedBy() != null) {
                cs.setInt(7, objeto.getModifiedBy().getId());
            } else {
                cs.setNull(7, Types.INTEGER);
            }
            resultado = cs.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL eliminar_recordatorio(?)}");
            cs.setInt(1, id);
            resultado = cs.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public int eliminarPorCita(int idCita) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL eliminar_recordatorios_por_cita(?)}");
            cs.setInt(1, idCita);
            resultado = cs.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR eliminando recordatorios por cita: " + ex.getMessage());
        } finally {
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public Recordatorio buscarPorId(int id) {
        Recordatorio recordatorio = null;
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL buscar_recordatorio_por_id(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();
            if (rs.next()) {
                recordatorio = new Recordatorio();
                recordatorio.setId(rs.getInt("id_recordatorio"));
                recordatorio.setFechaProgramada(rs.getTimestamp("fecha_programada").toLocalDateTime());
                recordatorio.setCanal(CanalRecordatorio.valueOf(rs.getString("canal")));
                recordatorio.setEstadoSeguimiento(EstadoSeguimiento.valueOf(rs.getString("estado_seguimiento")));
                recordatorio.setMensaje(rs.getString("mensaje"));
                Cita cita = new Cita();
                cita.setId(rs.getInt("id_cita"));
                recordatorio.setCita(cita);
                recordatorio.setCreatedOn(rs.getTimestamp("created_on").toLocalDateTime());
                Timestamp modifiedOn = rs.getTimestamp("modified_on");
                if (modifiedOn != null) {
                    recordatorio.setModifiedOn(modifiedOn.toLocalDateTime());
                }
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return recordatorio;
    }

    @Override
    public List<Recordatorio> listarTodas() {
        List<Recordatorio> recordatorios = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_recordatorios()}");
            rs = cs.executeQuery();
            while (rs.next()) {
                Recordatorio recordatorio = new Recordatorio();
                recordatorio.setId(rs.getInt("id_recordatorio"));
                recordatorio.setFechaProgramada(rs.getTimestamp("fecha_programada").toLocalDateTime());
                recordatorio.setCanal(CanalRecordatorio.valueOf(rs.getString("canal")));
                recordatorio.setEstadoSeguimiento(EstadoSeguimiento.valueOf(rs.getString("estado_seguimiento")));
                recordatorio.setMensaje(rs.getString("mensaje"));
                Cita cita = new Cita();
                cita.setId(rs.getInt("id_cita"));
                recordatorio.setCita(cita);
                recordatorio.setCreatedOn(rs.getTimestamp("created_on").toLocalDateTime());
                Timestamp modifiedOn = rs.getTimestamp("modified_on");
                if (modifiedOn != null) {
                    recordatorio.setModifiedOn(modifiedOn.toLocalDateTime());
                }
                recordatorios.add(recordatorio);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return recordatorios;
    }

    @Override
    public List<Recordatorio> listarPorMascotaOCliente(String texto) {
        List<Recordatorio> recordatorios = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL listar_recordatorios_por_mascota_o_cliente(?)}";
            cs = con.prepareCall(sql);
            cs.setString(1, texto);
            rs = cs.executeQuery();

            while (rs.next()) {
                Recordatorio r = new Recordatorio();
                r.setId(rs.getInt("id_recordatorio"));
                r.setFechaProgramada(rs.getTimestamp("fecha_programada").toLocalDateTime());
                r.setMensaje(rs.getString("mensaje"));
                r.setEstadoSeguimiento(EstadoSeguimiento.valueOf(rs.getString("estado_seguimiento")));
                r.setCanal(CanalRecordatorio.valueOf(rs.getString("canal")));

                Cita cita = new Cita();
                cita.setId(rs.getInt("id_cita"));
                // Valores no-nulos requeridos por el cliente (no se muestran en la vista de recordatorios)
                cita.setEstado(EstadoCita.PENDIENTE);
                cita.setFechaHoraInicio(r.getFechaProgramada());
                cita.setFechaHoraFin(r.getFechaProgramada());

                Mascota mascota = new Mascota();
                mascota.setId(rs.getInt("id_mascota"));
                mascota.setNombre(rs.getString("nombre_mascota"));
                mascota.setPeso(rs.getDouble("peso_mascota"));

                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                cliente.setNombres(rs.getString("nombres_cliente"));
                cliente.setApellidos(rs.getString("apellidos_cliente"));

                mascota.setCliente(cliente);
                cita.setMascota(mascota);
                r.setCita(cita);

                recordatorios.add(r);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando recordatorios por mascota o cliente: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en RecordatorioImpl: " + ex.getMessage());
            }
        }

        return recordatorios;
    }

    @Override
    public List<Recordatorio> listarPorEstadoYFecha(String estado, LocalDate fecha) {
        List<Recordatorio> recordatorios = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL listar_recordatorios_por_estado_fecha(?, ?)}";
            cs = con.prepareCall(sql);
            cs.setString(1, estado);

            if (fecha != null) {
                cs.setDate(2, java.sql.Date.valueOf(fecha));
            } else {
                cs.setNull(2, java.sql.Types.DATE);
            }

            rs = cs.executeQuery();

            while (rs.next()) {
                Recordatorio r = new Recordatorio();
                r.setId(rs.getInt("id_recordatorio"));
                r.setFechaProgramada(rs.getTimestamp("fecha_programada").toLocalDateTime());
                r.setMensaje(rs.getString("mensaje"));
                r.setEstadoSeguimiento(EstadoSeguimiento.valueOf(rs.getString("estado_seguimiento")));
                r.setCanal(CanalRecordatorio.valueOf(rs.getString("canal")));

                Cita cita = new Cita();
                cita.setId(rs.getInt("id_cita"));

                Mascota mascota = new Mascota();
                mascota.setNombre(rs.getString("nombre_mascota"));
                mascota.setPeso(rs.getDouble("peso_mascota"));

                Cliente cliente = new Cliente();
                cliente.setNombres(rs.getString("nombres_cliente"));
                cliente.setApellidos(rs.getString("apellidos_cliente"));

                mascota.setCliente(cliente);
                cita.setMascota(mascota);
                r.setCita(cita);

                recordatorios.add(r);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando recordatorios por estado y fecha: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en RecordatorioImpl: " + ex.getMessage());
            }
        }

        return recordatorios;
    }
    @Override
    public void marcarEnviado(int idRecordatorio, int modifiedBy) {
        Connection con = null;
        CallableStatement cs = null;

        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL marcar_recordatorio_enviado(?, ?)}");

            cs.setInt(1, idRecordatorio);
            cs.setInt(2, modifiedBy);

            cs.executeUpdate();

        } catch (Exception ex) {
            System.out.println("ERROR marcando recordatorio como enviado: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en marcarEnviado: " + ex.getMessage());
            }
        }
    }

    @Override
    public int contarPendientes() {
        int total = 0;
        Connection con = null;
        CallableStatement cs = null;

        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL contar_recordatorios_pendientes(?)}");

            cs.registerOutParameter(1, Types.INTEGER);

            cs.execute();

            total = cs.getInt(1);

        } catch (Exception ex) {
            System.out.println("ERROR contando recordatorios pendientes: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en contarPendientes: " + ex.getMessage());
            }
        }

        return total;
    }


}
