package pe.edu.pucp.vetcitas.cita.impl;

import pe.edu.pucp.vetcitas.cita.dao.IAtencionDAO;
import pe.edu.pucp.vetcitas.cita.model.Atencion;
import pe.edu.pucp.vetcitas.cita.model.Cita;
import pe.edu.pucp.vetcitas.config.DBManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AtencionImpl implements IAtencionDAO {

    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;

    @Override
    public int insertar(Atencion atencion) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call insertar_atencion(?,?,?,?,?,?,?,?,?,?)}");
            cs.setTimestamp("p_fecha_hora", Timestamp.valueOf(atencion.getFechaHora()));
            cs.setString("p_nota_clinica", atencion.getNotaClinica());
            cs.setString("p_nota_pre_operatoria", atencion.getNotaPreOperatoria());
            cs.setString("p_nota_post_operatoria", atencion.getNotaPostOperatoria());
            cs.setString("p_recomendacion_control", atencion.getRecomendacionControl());
            cs.setDouble("p_monto_referencial", atencion.getMontoReferencial());
            cs.setDouble("p_descuento_aplicado", atencion.getDescuentoAplicado());
            cs.setInt("p_id_cita", atencion.getCita().getId());
            cs.setTimestamp("p_created_on", Timestamp.valueOf(LocalDateTime.now()));
            cs.registerOutParameter("p_id_generado", Types.INTEGER);
            cs.executeUpdate();
            resultado = cs.getInt("p_id_generado");
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public int modificar(Atencion atencion) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call modificar_atencion(?,?,?,?,?,?,?,?,?,?)}");
            cs.setInt("p_id_atencion", atencion.getId());
            cs.setTimestamp("p_fecha_hora", Timestamp.valueOf(atencion.getFechaHora()));
            cs.setString("p_nota_clinica", atencion.getNotaClinica());
            cs.setString("p_nota_pre_operatoria", atencion.getNotaPreOperatoria());
            cs.setString("p_nota_post_operatoria", atencion.getNotaPostOperatoria());
            cs.setString("p_recomendacion_control", atencion.getRecomendacionControl());
            cs.setDouble("p_monto_referencial", atencion.getMontoReferencial());
            cs.setDouble("p_descuento_aplicado", atencion.getDescuentoAplicado());
            cs.setTimestamp("p_modified_on", Timestamp.valueOf(LocalDateTime.now()));
            if (atencion.getModifiedBy() != null) {
                cs.setInt("p_modified_by", atencion.getModifiedBy().getId());
            } else {
                cs.setNull("p_modified_by", Types.INTEGER);
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
            cs = con.prepareCall("{call eliminar_atencion(?,?,?)}");
            cs.setInt("p_id_atencion", id);
            cs.setTimestamp("p_modified_on", Timestamp.valueOf(LocalDateTime.now()));
            cs.setNull("p_modified_by", Types.INTEGER);

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
    public Atencion buscarPorId(int id) {
        Atencion atencion = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call buscar_atencion_por_id(?)}");
            cs.setInt("p_id_atencion", id);
            rs = cs.executeQuery();
            if (rs.next()) {
                atencion = mapearAtencionDesdeResultSet(rs);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return atencion;
    }

    @Override
    public Atencion buscarPorCita(int idCita) {
        Atencion atencion = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call buscar_atencion_por_cita(?)}");
            cs.setInt("p_id_cita", idCita);
            rs = cs.executeQuery();
            if (rs.next()) {
                atencion = mapearAtencionDesdeResultSet(rs);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return atencion;
    }

    @Override
    public List<Atencion> listarTodas() {
        List<Atencion> atenciones = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call listar_atenciones()}");
            rs = cs.executeQuery();
            while (rs.next()) {
                atenciones.add(mapearAtencionDesdeResultSet(rs));
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return atenciones;
    }

    // Método auxiliar privado para no repetir código al leer el ResultSet
    private Atencion mapearAtencionDesdeResultSet(ResultSet rs) throws SQLException {
        Atencion atencion = new Atencion();
        atencion.setId(rs.getInt("id_atencion"));
        atencion.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
        atencion.setNotaClinica(rs.getString("nota_clinica"));
        atencion.setNotaPreOperatoria(rs.getString("nota_pre_operatoria"));
        atencion.setNotaPostOperatoria(rs.getString("nota_post_operatoria"));
        atencion.setRecomendacionControl(rs.getString("recomendacion_control"));
        atencion.setMontoReferencial(rs.getDouble("monto_referencial"));
        atencion.setDescuentoAplicado(rs.getDouble("descuento_aplicado"));

        Cita cita = new Cita();
        cita.setId(rs.getInt("id_cita"));
        atencion.setCita(cita);

        return atencion;
    }
}