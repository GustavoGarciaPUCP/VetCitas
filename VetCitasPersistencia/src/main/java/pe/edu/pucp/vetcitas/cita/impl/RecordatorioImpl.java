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
            cs = con.prepareCall("{call INSERTAR_RECORDATORIO(?,?,?,?,?,?,?)}");
            cs.registerOutParameter("_id_recordatorio", Types.INTEGER);
            cs.setTimestamp("_fecha_programada", Timestamp.valueOf(objeto.getFechaProgramada()));
            cs.setString("_canal", objeto.getCanal().name());
            cs.setString("_estado_seguimiento", objeto.getEstadoSeguimiento().name());
            cs.setString("_mensaje", objeto.getMensaje());
            cs.setInt("_id_cita", objeto.getCita().getId());
            if (objeto.getModifiedBy() != null) {
                cs.setInt("_modified_by", objeto.getModifiedBy().getId());
            } else {
                cs.setNull("_modified_by", Types.INTEGER);
            }
            cs.executeUpdate();
            resultado = cs.getInt("_id_recordatorio");
            objeto.setId(resultado);
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public int modificar(Recordatorio objeto) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call MODIFICAR_RECORDATORIO(?,?,?,?,?,?,?)}");
            cs.setInt("_id_recordatorio", objeto.getId());
            cs.setTimestamp("_fecha_programada", Timestamp.valueOf(objeto.getFechaProgramada()));
            cs.setString("_canal", objeto.getCanal().name());
            cs.setString("_estado_seguimiento", objeto.getEstadoSeguimiento().name());
            cs.setString("_mensaje", objeto.getMensaje());
            cs.setInt("_id_cita", objeto.getCita().getId());
            if (objeto.getModifiedBy() != null) {
                cs.setInt("_modified_by", objeto.getModifiedBy().getId());
            } else {
                cs.setNull("_modified_by", Types.INTEGER);
            }
            resultado = cs.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public int eliminar(int id) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call ELIMINAR_RECORDATORIO(?)}");
            cs.setInt("_id_recordatorio", id);
            resultado = cs.executeUpdate();
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public Recordatorio buscarPorId(int id) {
        Recordatorio recordatorio = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call BUSCAR_RECORDATORIO_POR_ID(?)}");
            cs.setInt("_id_recordatorio", id);
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
            try { rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return recordatorio;
    }

    @Override
    public List<Recordatorio> listarTodas() {
        List<Recordatorio> recordatorios = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call LISTAR_RECORDATORIOS()}");
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
            try { rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return recordatorios;
    }
}
