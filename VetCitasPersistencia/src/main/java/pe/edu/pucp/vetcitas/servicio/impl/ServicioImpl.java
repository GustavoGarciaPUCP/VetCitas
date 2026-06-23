package pe.edu.pucp.vetcitas.servicio.impl;

import pe.edu.pucp.vetcitas.cita.model.ServicioAtencionResumen;
import pe.edu.pucp.vetcitas.servicio.dao.IServicioDAO;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;
import pe.edu.pucp.vetcitas.common.enums.TipoServicio;
import pe.edu.pucp.vetcitas.common.util.AuditClock;
import pe.edu.pucp.vetcitas.config.DBManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServicioImpl implements IServicioDAO {
    @Override
    public int insertar(Servicio servicio) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call insertar_servicio(?,?,?,?,?,?,?,?,?,?)}");
            cs.setString("p_nombre", servicio.getNombre());

            if (servicio.getDescripcion() != null && !servicio.getDescripcion().trim().isEmpty()) {
                cs.setString("p_descripcion", servicio.getDescripcion().trim());
            } else {
                cs.setNull("p_descripcion", Types.VARCHAR);
            }

            cs.setString("p_tipo_servicio", servicio.getTipoServicio().name());
            cs.setInt("p_duracion_minutos", servicio.getDuracionMinutos());
            cs.setDouble("p_precio_referencial", servicio.getPrecioReferencial());
            cs.setBoolean("p_activo", servicio.isActivo());
            Timestamp ahora = AuditClock.timestampNow();
            cs.setTimestamp("p_created_on", ahora);
            cs.setTimestamp("p_modified_on", ahora);
            if (servicio.getModifiedBy() != null) {
                cs.setInt("p_modified_by", servicio.getModifiedBy().getId());
            } else {
                cs.setNull("p_modified_by", Types.INTEGER);
            }
            cs.registerOutParameter("p_id_generado", Types.INTEGER);
            cs.executeUpdate();
            resultado = cs.getInt("p_id_generado");
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public int modificar(Servicio servicio) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call modificar_servicio(?,?,?,?,?,?,?,?,?)}");
            cs.setInt("p_id_servicio", servicio.getId());
            cs.setString("p_nombre", servicio.getNombre());

            if (servicio.getDescripcion() != null && !servicio.getDescripcion().trim().isEmpty()) {
                cs.setString("p_descripcion", servicio.getDescripcion().trim());
            } else {
                cs.setNull("p_descripcion", Types.VARCHAR);
            }

            cs.setString("p_tipo_servicio", servicio.getTipoServicio().name());
            cs.setInt("p_duracion_minutos", servicio.getDuracionMinutos());
            cs.setDouble("p_precio_referencial", servicio.getPrecioReferencial());
            cs.setBoolean("p_activo", servicio.isActivo());
            cs.setTimestamp("p_modified_on", AuditClock.timestampNow());

            if (servicio.getModifiedBy() != null) {
                cs.setInt("p_modified_by", servicio.getModifiedBy().getId());
            } else {
                cs.setNull("p_modified_by", Types.INTEGER);
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
        return eliminar(id, 0);
    }

    public int eliminar(int id, int modifiedBy) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call eliminar_servicio(?,?,?)}");
            cs.setInt("p_id_servicio", id);
            cs.setTimestamp("p_modified_on", AuditClock.timestampNow());
            if (modifiedBy > 0) {
                cs.setInt("p_modified_by", modifiedBy);
            } else {
                cs.setNull("p_modified_by", Types.INTEGER);
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
    public int deshabilitar(int id) {
        return deshabilitar(id, 0);
    }

    public int deshabilitar(int id, int modifiedBy) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call deshabilitar_servicio(?,?,?)}");
            cs.setInt("p_id_servicio", id);
            cs.setTimestamp("p_modified_on", AuditClock.timestampNow());
            if (modifiedBy > 0) {
                cs.setInt("p_modified_by", modifiedBy);
            } else {
                cs.setNull("p_modified_by", Types.INTEGER);
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
    public Servicio buscarPorId(int id) {
        Servicio servicio = null;
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call buscar_servicio_por_id(?)}");
            cs.setInt("p_id_servicio", id);
            rs = cs.executeQuery();
            if (rs.next()) {
                servicio = new Servicio();
                servicio.setId(rs.getInt("id_servicio"));
                servicio.setNombre(rs.getString("nombre"));
                servicio.setDescripcion(rs.getString("descripcion"));
                servicio.setTipoServicio(TipoServicio.valueOf(rs.getString("tipo_servicio")));
                servicio.setDuracionMinutos(rs.getInt("duracion_minutos"));
                servicio.setPrecioReferencial(rs.getDouble("precio_referencial"));
                servicio.setActivo(rs.getBoolean("activo"));
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return servicio;
    }

    @Override
    public List<Servicio> listarTodas() {
        List<Servicio> servicios = null;
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            servicios = new ArrayList<>();
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call listar_servicios()}");
            rs = cs.executeQuery();

            while (rs.next()) {
                Servicio servicio = new Servicio();
                servicio.setId(rs.getInt("id_servicio"));
                servicio.setNombre(rs.getString("nombre"));
                servicio.setDescripcion(rs.getString("descripcion"));
                servicio.setTipoServicio(TipoServicio.valueOf(rs.getString("tipo_servicio")));
                servicio.setDuracionMinutos(rs.getInt("duracion_minutos"));
                servicio.setPrecioReferencial(rs.getDouble("precio_referencial"));
                servicio.setActivo(rs.getBoolean("activo"));
                servicios.add(servicio);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return servicios;
    }

    @Override
    public List<Servicio> listarPorNombreOTipo(String texto) {
        List<Servicio> servicios = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL listar_servicios_por_nombre_o_tipo(?)}";
            cs = con.prepareCall(sql);
            cs.setString(1, texto);
            rs = cs.executeQuery();

            while (rs.next()) {
                Servicio servicio = new Servicio();
                servicio.setId(rs.getInt("id_servicio"));
                servicio.setNombre(rs.getString("nombre"));
                servicio.setDescripcion(rs.getString("descripcion"));
                servicio.setTipoServicio(TipoServicio.valueOf(rs.getString("tipo_servicio")));
                servicio.setDuracionMinutos(rs.getInt("duracion_minutos"));
                servicio.setPrecioReferencial(rs.getDouble("precio_referencial"));
                servicio.setActivo(rs.getBoolean("activo"));
                servicios.add(servicio);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando servicios por nombre o tipo: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en ServicioImpl: " + ex.getMessage());
            }
        }

        return servicios;
    }

    @Override
    public List<Servicio> listarPorEstado(boolean activo) {
        List<Servicio> servicios = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL listar_servicios_por_estado(?)}";
            cs = con.prepareCall(sql);
            cs.setBoolean(1, activo);
            rs = cs.executeQuery();

            while (rs.next()) {
                Servicio servicio = new Servicio();
                servicio.setId(rs.getInt("id_servicio"));
                servicio.setNombre(rs.getString("nombre"));
                servicio.setDescripcion(rs.getString("descripcion"));
                servicio.setTipoServicio(TipoServicio.valueOf(rs.getString("tipo_servicio")));
                servicio.setDuracionMinutos(rs.getInt("duracion_minutos"));
                servicio.setPrecioReferencial(rs.getDouble("precio_referencial"));
                servicio.setActivo(rs.getBoolean("activo"));
                servicios.add(servicio);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando servicios por estado: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en ServicioImpl: " + ex.getMessage());
            }
        }

        return servicios;
    }

    @Override
    public List<ServicioAtencionResumen> topNMasDemandados(
            LocalDateTime desde,
            LocalDateTime hasta,
            int limite
    ) {
        List<ServicioAtencionResumen> resumenes = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL top_servicios_mas_demandados(?, ?, ?)}");

            cs.setTimestamp(1, Timestamp.valueOf(desde));
            cs.setTimestamp(2, Timestamp.valueOf(hasta));
            cs.setInt(3, limite);

            rs = cs.executeQuery();

            while (rs.next()) {
                Servicio servicio = new Servicio();
                servicio.setId(rs.getInt("id_servicio"));
                servicio.setNombre(rs.getString("nombre"));
                servicio.setDescripcion(rs.getString("descripcion"));
                servicio.setTipoServicio(TipoServicio.valueOf(rs.getString("tipo_servicio")));
                servicio.setDuracionMinutos(rs.getInt("duracion_minutos"));
                servicio.setPrecioReferencial(rs.getDouble("precio_referencial"));
                servicio.setActivo(rs.getBoolean("activo"));

                ServicioAtencionResumen resumen = new ServicioAtencionResumen();
                resumen.setServicio(servicio);
                resumen.setTotalAtenciones(rs.getInt("total_atenciones"));
                resumen.setMontoNetoTotal(rs.getDouble("monto_neto_total"));

                resumenes.add(resumen);
            }

        } catch (Exception ex) {
            System.out.println("ERROR obteniendo top servicios más demandados: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en topNMasDemandados: " + ex.getMessage());
            }
        }

        return resumenes;
    }



}
