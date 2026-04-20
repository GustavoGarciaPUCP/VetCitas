package pe.edu.pucp.vetcitas.servicio.impl;

import pe.edu.pucp.vetcitas.servicio.dao.IServicioDAO;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;
import pe.edu.pucp.vetcitas.common.enums.TipoServicio;
import pe.edu.pucp.vetcitas.config.DBManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ServicioImpl implements IServicioDAO {

    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;

    @Override
    public int insertar(Servicio servicio) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call insertar_servicio(?,?,?,?,?,?)}");
            cs.setString("p_nombre", servicio.getNombre());
            cs.setString("p_tipo_servicio", servicio.getTipoServicio().name());
            cs.setInt("p_duracion_minutos", servicio.getDuracionMinutos());
            cs.setDouble("p_precio_referencial", servicio.getPrecioReferencial());
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
    public int modificar(Servicio servicio) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call modificar_servicio(?,?,?,?,?,?,?)}");
            cs.setInt("p_id_servicio", servicio.getId());
            cs.setString("p_nombre", servicio.getNombre());
            cs.setString("p_tipo_servicio", servicio.getTipoServicio().name());
            cs.setInt("p_duracion_minutos", servicio.getDuracionMinutos());
            cs.setDouble("p_precio_referencial", servicio.getPrecioReferencial());
            cs.setTimestamp("p_modified_on", Timestamp.valueOf(LocalDateTime.now()));
            if (servicio.getModifiedBy() != null) {
                cs.setInt("p_modified_by", servicio.getModifiedBy().getId());
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
            cs = con.prepareCall("{call eliminar_servicio(?,?,?)}");
            cs.setInt("p_id_servicio", id);
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
    public int deshabilitar(int id) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call deshabilitar_servicio(?,?,?)}");
            cs.setInt("p_id_servicio", id);
            cs.setTimestamp("p_modified_on", Timestamp.valueOf(LocalDateTime.now()));
            cs.setNull("p_modified_by", Types.INTEGER); // O puedes recibir el usuario que deshabilita si lo ajustas

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
    public Servicio buscarPorId(int id) {
        Servicio servicio = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call buscar_servicio_por_id(?)}");
            cs.setInt("p_id_servicio", id);
            rs = cs.executeQuery();
            if (rs.next()) {
                servicio = new Servicio();
                servicio.setId(rs.getInt("id_servicio"));
                servicio.setNombre(rs.getString("nombre"));
                servicio.setTipoServicio(TipoServicio.valueOf(rs.getString("tipo_servicio")));
                servicio.setDuracionMinutos(rs.getInt("duracion_minutos"));
                servicio.setPrecioReferencial(rs.getDouble("precio_referencial"));
                servicio.setActivo(rs.getBoolean("activo"));
                // Aquí podrías setear los datos de auditoría (created_on, etc) si los necesitas
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return servicio;
    }

    @Override
    public List<Servicio> listarTodas() {
        List<Servicio> servicios = null;
        try {
            servicios = new ArrayList<>();
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call listar_servicios()}");
            rs = cs.executeQuery();

            while (rs.next()) {
                Servicio servicio = new Servicio();
                servicio.setId(rs.getInt("id_servicio"));
                servicio.setNombre(rs.getString("nombre"));
                servicio.setTipoServicio(TipoServicio.valueOf(rs.getString("tipo_servicio")));
                servicio.setDuracionMinutos(rs.getInt("duracion_minutos"));
                servicio.setPrecioReferencial(rs.getDouble("precio_referencial"));
                servicio.setActivo(rs.getBoolean("activo"));
                servicios.add(servicio);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return servicios;
    }
}