package pe.edu.pucp.vetcitas.cita.impl;

import pe.edu.pucp.vetcitas.cita.dao.IAtencionDAO;
import pe.edu.pucp.vetcitas.cita.model.Atencion;
import pe.edu.pucp.vetcitas.cita.model.Cita;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;
import pe.edu.pucp.vetcitas.common.enums.EstadoCita;
import pe.edu.pucp.vetcitas.config.DBManager;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;
import pe.edu.pucp.vetcitas.usuario.model.Veterinario;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AtencionImpl implements IAtencionDAO {
    @Override
    public int insertar(Atencion atencion) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();

            cs = con.prepareCall("{call insertar_atencion(?,?,?,?,?,?,?,?,?,?,?,?,?)}");

            cs.setTimestamp("p_fecha_hora", Timestamp.valueOf(atencion.getFechaHora()));
            cs.setString("p_nota_clinica", atencion.getNotaClinica());
            cs.setString("p_diagnostico", atencion.getDiagnostico());
            cs.setString("p_nota_pre_operatoria", atencion.getNotaPreOperatoria());
            cs.setString("p_nota_post_operatoria", atencion.getNotaPostOperatoria());
            cs.setString("p_recomendacion_control", atencion.getRecomendacionControl());
            cs.setDouble("p_monto_referencial", atencion.getMontoReferencial());
            cs.setDouble("p_descuento_aplicado", atencion.getDescuentoAplicado());
            cs.setInt("p_id_cita", atencion.getCita().getId());

            if (atencion.getCreatedOn() != null) {
                cs.setTimestamp("p_created_on", Timestamp.valueOf(atencion.getCreatedOn()));
            } else {
                cs.setTimestamp("p_created_on", Timestamp.valueOf(LocalDateTime.now()));
            }

            if (atencion.getModifiedOn() != null) {
                cs.setTimestamp("p_modified_on", Timestamp.valueOf(atencion.getModifiedOn()));
            } else {
                cs.setTimestamp("p_modified_on", Timestamp.valueOf(LocalDateTime.now()));
            }

            if (atencion.getModifiedBy() != null) {
                cs.setInt("p_modified_by", atencion.getModifiedBy().getId());
            } else {
                cs.setNull("p_modified_by", Types.INTEGER);
            }

            cs.registerOutParameter("p_id_generado", Types.INTEGER);

            cs.executeUpdate();

            resultado = cs.getInt("p_id_generado");
            atencion.setId(resultado);

        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public int modificar(Atencion atencion) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();

            // Son 12 parámetros, igual que el procedure modificar_atencion
            cs = con.prepareCall("{call modificar_atencion(?,?,?,?,?,?,?,?,?,?,?,?)}");

            cs.setInt("p_id_atencion", atencion.getId());
            cs.setTimestamp("p_fecha_hora", Timestamp.valueOf(atencion.getFechaHora()));
            cs.setString("p_nota_clinica", atencion.getNotaClinica());
            cs.setString("p_diagnostico", atencion.getDiagnostico());
            cs.setString("p_nota_pre_operatoria", atencion.getNotaPreOperatoria());
            cs.setString("p_nota_post_operatoria", atencion.getNotaPostOperatoria());
            cs.setString("p_recomendacion_control", atencion.getRecomendacionControl());
            cs.setDouble("p_monto_referencial", atencion.getMontoReferencial());
            cs.setDouble("p_descuento_aplicado", atencion.getDescuentoAplicado());

            // Te faltaba este parámetro
            cs.setInt("p_id_cita", atencion.getCita().getId());

            if (atencion.getModifiedOn() != null) {
                cs.setTimestamp("p_modified_on", Timestamp.valueOf(atencion.getModifiedOn()));
            } else {
                cs.setTimestamp("p_modified_on", Timestamp.valueOf(LocalDateTime.now()));
            }

            if (atencion.getModifiedBy() != null) {
                cs.setInt("p_modified_by", atencion.getModifiedBy().getId());
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
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
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
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public Atencion buscarPorId(int id) {
        Atencion atencion = null;
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
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
            try { if (rs != null) rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return atencion;
    }

    @Override
    public Atencion buscarPorCita(int idCita) {
        Atencion atencion = null;
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
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
            try { if (rs != null) rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return atencion;
    }

    @Override
    public List<Atencion> listarTodas() {
        List<Atencion> atenciones = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
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
            try { if (rs != null) rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return atenciones;
    }

    private Atencion mapearAtencionDesdeResultSet(ResultSet rs) throws SQLException {
        Atencion atencion = new Atencion();
        atencion.setId(rs.getInt("id_atencion"));
        atencion.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
        atencion.setNotaClinica(rs.getString("nota_clinica"));
        atencion.setNotaPreOperatoria(rs.getString("nota_pre_operatoria"));
        atencion.setDiagnostico(rs.getString("diagnostico"));
        atencion.setNotaPostOperatoria(rs.getString("nota_post_operatoria"));
        atencion.setRecomendacionControl(rs.getString("recomendacion_control"));
        atencion.setMontoReferencial(rs.getDouble("monto_referencial"));
        atencion.setDescuentoAplicado(rs.getDouble("descuento_aplicado"));

        Cita cita = new Cita();
        cita.setId(rs.getInt("id_cita"));
        atencion.setCita(cita);

        return atencion;
    }

    @Override
    public List<Atencion> listarFiltradas(Integer idVeterinario, String estadoCita, LocalDate fecha, String textoBusqueda) {
        List<Atencion> atenciones = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL listar_atenciones_filtradas(?, ?, ?, ?)}";
            cs = con.prepareCall(sql);

            if (idVeterinario != null) cs.setInt(1, idVeterinario);
            else cs.setNull(1, java.sql.Types.INTEGER);

            cs.setString(2, estadoCita);

            if (fecha != null) cs.setDate(3, java.sql.Date.valueOf(fecha));
            else cs.setNull(3, java.sql.Types.DATE);

            cs.setString(4, textoBusqueda);

            rs = cs.executeQuery();

            while (rs.next()) {
                Atencion a = new Atencion();
                a.setId(rs.getInt("id_atencion"));
                a.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                a.setNotaClinica(rs.getString("nota_clinica"));
                a.setNotaPreOperatoria(rs.getString("nota_pre_operatoria"));
                a.setNotaPostOperatoria(rs.getString("nota_post_operatoria"));
                a.setRecomendacionControl(rs.getString("recomendacion_control"));
                a.setMontoReferencial(rs.getDouble("monto_referencial"));
                a.setDescuentoAplicado(rs.getDouble("descuento_aplicado"));
                a.setDiagnostico(rs.getString("diagnostico"));

                Cita cita = new Cita();
                cita.setId(rs.getInt("id_cita"));
                cita.setEstado(EstadoCita.valueOf(rs.getString("estado_cita")));
                cita.setMotivoCancelacion(rs.getString("motivo_cancelacion"));
                Timestamp fechaCancelacion = rs.getTimestamp("fecha_cancelacion");
                if (fechaCancelacion != null) {
                    cita.setFechaCancelacion(fechaCancelacion.toLocalDateTime());
                }
                cita.setUsuarioCancelacion(mapearUsuarioCancelacion(rs));
                cita.setFechaHoraInicio(rs.getTimestamp("fecha_hora_inicio").toLocalDateTime());
                cita.setFechaHoraFin(rs.getTimestamp("fecha_hora_fin").toLocalDateTime());

                Mascota mascota = new Mascota();
                mascota.setId(rs.getInt("id_mascota"));
                mascota.setNombre(rs.getString("nombre_mascota"));
                mascota.setPeso(rs.getDouble("peso_mascota"));

                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                cliente.setNombres(rs.getString("nombres_cliente"));
                cliente.setApellidos(rs.getString("apellidos_cliente"));

                Veterinario veterinario = new Veterinario();
                veterinario.setId(rs.getInt("id_veterinario"));
                veterinario.setNombres(rs.getString("nombres_veterinario"));
                veterinario.setApellidos(rs.getString("apellidos_veterinario"));

                Servicio servicio = new Servicio();
                servicio.setId(rs.getInt("id_servicio"));
                servicio.setNombre(rs.getString("nombre_servicio"));
                servicio.setDescripcion(rs.getString("descripcion_servicio"));

                mascota.setCliente(cliente);
                cita.setMascota(mascota);
                cita.setVeterinario(veterinario);
                cita.setServicio(servicio);
                a.setCita(cita);

                atenciones.add(a);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando atenciones filtradas: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en AtencionImpl: " + ex.getMessage());
            }
        }

        return atenciones;
    }

    @Override
    public List<Atencion> listarHistorialPorMascota(int idMascota) {
        List<Atencion> atenciones = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL listar_historial_visitas_por_mascota(?)}";
            cs = con.prepareCall(sql);
            cs.setInt(1, idMascota);
            rs = cs.executeQuery();

            while (rs.next()) {
                Atencion a = new Atencion();

                if (rs.getObject("id_atencion") != null) {
                    a.setId(rs.getInt("id_atencion"));
                    if (rs.getTimestamp("fecha_atencion") != null) {
                        a.setFechaHora(rs.getTimestamp("fecha_atencion").toLocalDateTime());
                    }
                    a.setNotaClinica(rs.getString("nota_clinica"));
                    a.setDiagnostico(rs.getString("diagnostico"));
                    a.setRecomendacionControl(rs.getString("recomendacion_control"));
                    a.setMontoReferencial(rs.getDouble("monto_referencial"));
                    a.setDescuentoAplicado(rs.getDouble("descuento_aplicado"));
                }

                Cita cita = new Cita();
                cita.setId(rs.getInt("id_cita"));
                cita.setEstado(EstadoCita.valueOf(rs.getString("estado")));
                cita.setMotivoCancelacion(rs.getString("motivo_cancelacion"));
                Timestamp fechaCancelacion = rs.getTimestamp("fecha_cancelacion");
                if (fechaCancelacion != null) {
                    cita.setFechaCancelacion(fechaCancelacion.toLocalDateTime());
                }
                cita.setUsuarioCancelacion(mapearUsuarioCancelacion(rs));

                cita.setFechaHoraInicio(rs.getTimestamp("fecha_hora_inicio").toLocalDateTime());
                cita.setFechaHoraFin(rs.getTimestamp("fecha_hora_fin").toLocalDateTime());

                Servicio servicio = new Servicio();
                servicio.setNombre(rs.getString("nombre_servicio"));
                servicio.setDescripcion(rs.getString("descripcion_servicio"));
                cita.setServicio(servicio);

                a.setCita(cita);
                atenciones.add(a);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando historial por mascota: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en AtencionImpl: " + ex.getMessage());
            }
        }

        return atenciones;
    }

    private Usuario mapearUsuarioCancelacion(ResultSet rs) throws SQLException {
        int idUsuarioCancelacion = rs.getInt("id_usuario_cancelacion");

        if (rs.wasNull()) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setId(idUsuarioCancelacion);
        usuario.setUsername(rs.getString("username_usuario_cancelacion"));
        usuario.setNombres(rs.getString("nombres_usuario_cancelacion"));
        usuario.setApellidos(rs.getString("apellidos_usuario_cancelacion"));
        usuario.setEmail(rs.getString("email_usuario_cancelacion"));

        return usuario;
    }

}