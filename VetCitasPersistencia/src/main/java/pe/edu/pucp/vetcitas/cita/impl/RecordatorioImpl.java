package pe.edu.pucp.vetcitas.cita.impl;

import pe.edu.pucp.vetcitas.cita.dao.IRecordatorioDAO;
import pe.edu.pucp.vetcitas.cita.model.Cita;
import pe.edu.pucp.vetcitas.cita.model.Recordatorio;
import pe.edu.pucp.vetcitas.common.enums.CanalRecordatorio;
import pe.edu.pucp.vetcitas.common.enums.EstadoSeguimiento;
import pe.edu.pucp.vetcitas.config.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecordatorioImpl implements IRecordatorioDAO {

    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;

    @Override
    public int insertar(Recordatorio objeto) {
        int resultado = 0;
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
    public Recordatorio buscarPorId(int id) {
        Recordatorio recordatorio = null;
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
        List<Recordatorio> recordatorios = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_recordatorios()}");
            rs = cs.executeQuery();
            while (rs.next()) {
                if (recordatorios == null) recordatorios = new ArrayList<>();
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
}
