package pe.edu.pucp.vetcitas.cita.impl;

import pe.edu.pucp.vetcitas.cita.dao.IAtencionDAO;
import pe.edu.pucp.vetcitas.cita.model.Atencion;
import pe.edu.pucp.vetcitas.cita.model.Cita;
import pe.edu.pucp.vetcitas.cita.model.ServicioAtencionResumen;
import pe.edu.pucp.vetcitas.cita.model.VeterinarioAtencionResumen;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;
import pe.edu.pucp.vetcitas.common.enums.EstadoCita;
import pe.edu.pucp.vetcitas.common.enums.TipoServicio;
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


                Cita cita = new Cita();
                cita.setId(rs.getInt("id_cita"));
                cita.setEstado(EstadoCita.valueOf(rs.getString("estado_cita")));
                cita.setMotivoCancelacion(rs.getString("motivo_cancelacion"));
                Timestamp fechaCancelacion = rs.getTimestamp("fecha_cancelacion");
                if (fechaCancelacion != null) {
                    cita.setFechaCancelacion(fechaCancelacion.toLocalDateTime());
                }
                cita.setUsuarioCancelacion(mapearUsuarioCancelacion(rs));
                cita.setMotivoReprogramacion(rs.getString("motivo_reprogramacion"));
                cita.setFechaHoraInicio(rs.getTimestamp("fecha_hora_inicio").toLocalDateTime());
                cita.setFechaHoraFin(rs.getTimestamp("fecha_hora_fin").toLocalDateTime());

                Mascota mascota = new Mascota();
                mascota.setId(rs.getInt("id_mascota"));
                mascota.setNombre(rs.getString("nombre_mascota"));

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
                cita.setMotivoReprogramacion(rs.getString("motivo_reprogramacion"));

                cita.setFechaHoraInicio(rs.getTimestamp("fecha_hora_inicio").toLocalDateTime());
                cita.setFechaHoraFin(rs.getTimestamp("fecha_hora_fin").toLocalDateTime());

                Servicio servicio = new Servicio();
                servicio.setNombre(rs.getString("nombre_servicio"));
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
    @Override
    public List<Atencion> listarUltimasPorVeterinario(int idVeterinario, int limite) {
        List<Atencion> atenciones = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_ultimas_atenciones_por_veterinario(?, ?)}");

            cs.setInt(1, idVeterinario);
            cs.setInt(2, limite);

            rs = cs.executeQuery();

            while (rs.next()) {
                Atencion a = new Atencion();

                a.setId(rs.getInt("id_atencion"));
                a.setFechaHora(rs.getTimestamp("fecha_hora").toLocalDateTime());
                a.setNotaClinica(rs.getString("nota_clinica"));
                a.setDiagnostico(rs.getString("diagnostico"));
                a.setNotaPreOperatoria(rs.getString("nota_pre_operatoria"));
                a.setNotaPostOperatoria(rs.getString("nota_post_operatoria"));
                a.setRecomendacionControl(rs.getString("recomendacion_control"));
                a.setMontoReferencial(rs.getDouble("monto_referencial"));
                a.setDescuentoAplicado(rs.getDouble("descuento_aplicado"));

                Cita cita = new Cita();
                cita.setId(rs.getInt("id_cita"));
                cita.setEstado(EstadoCita.valueOf(rs.getString("estado_cita")));
                cita.setMotivoCancelacion(rs.getString("motivo_cancelacion"));

                Timestamp fechaCancelacion = rs.getTimestamp("fecha_cancelacion");
                if (fechaCancelacion != null) {
                    cita.setFechaCancelacion(fechaCancelacion.toLocalDateTime());
                }

                cita.setUsuarioCancelacion(mapearUsuarioCancelacion(rs));
                cita.setMotivoReprogramacion(rs.getString("motivo_reprogramacion"));
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
                servicio.setTipoServicio(TipoServicio.valueOf(rs.getString("tipo_servicio")));
                servicio.setDuracionMinutos(rs.getInt("duracion_minutos"));
                servicio.setPrecioReferencial(rs.getDouble("precio_referencial"));

                mascota.setCliente(cliente);
                cita.setMascota(mascota);
                cita.setVeterinario(veterinario);
                cita.setServicio(servicio);

                a.setCita(cita);
                atenciones.add(a);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando últimas atenciones por veterinario: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en listarUltimasPorVeterinario: " + ex.getMessage());
            }
        }

        return atenciones;
    }

    @Override
    public int contarPorVeterinarioEnMes(int idVeterinario, int anio, int mes) {
        int total = 0;
        Connection con = null;
        CallableStatement cs = null;

        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL contar_atenciones_por_veterinario_en_mes(?, ?, ?, ?)}");

            cs.setInt(1, idVeterinario);
            cs.setInt(2, anio);
            cs.setInt(3, mes);
            cs.registerOutParameter(4, Types.INTEGER);

            cs.execute();

            total = cs.getInt(4);

        } catch (Exception ex) {
            System.out.println("ERROR contando atenciones por veterinario en mes: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en contarPorVeterinarioEnMes: " + ex.getMessage());
            }
        }

        return total;
    }

    @Override
    public double sumarMontosNetosPorMes(int anio, int mes) {
        double total = 0.0;
        Connection con = null;
        CallableStatement cs = null;

        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL sumar_montos_netos_atenciones_por_mes(?, ?, ?)}");

            cs.setInt(1, anio);
            cs.setInt(2, mes);
            cs.registerOutParameter(3, Types.DECIMAL);

            cs.execute();

            total = cs.getDouble(3);

        } catch (Exception ex) {
            System.out.println("ERROR sumando montos netos por mes: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en sumarMontosNetosPorMes: " + ex.getMessage());
            }
        }

        return total;
    }

    @Override
    public List<ServicioAtencionResumen> topServiciosPorVeterinario(
            int idVeterinario,
            int anio,
            int mes,
            int limite
    ) {
        List<ServicioAtencionResumen> resumenes = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL top_servicios_por_veterinario(?, ?, ?, ?)}");

            cs.setInt(1, idVeterinario);
            cs.setInt(2, anio);
            cs.setInt(3, mes);
            cs.setInt(4, limite);

            rs = cs.executeQuery();

            while (rs.next()) {
                Servicio servicio = new Servicio();
                servicio.setId(rs.getInt("id_servicio"));
                servicio.setNombre(rs.getString("nombre"));
                servicio.setDescripcion(rs.getString("descripcion"));
                servicio.setTipoServicio(TipoServicio.valueOf(rs.getString("tipo_servicio")));
                servicio.setDuracionMinutos(rs.getInt("duracion_minutos"));
                servicio.setPrecioReferencial(rs.getDouble("precio_referencial"));

                ServicioAtencionResumen resumen = new ServicioAtencionResumen();
                resumen.setServicio(servicio);
                resumen.setTotalAtenciones(rs.getInt("total_atenciones"));
                resumen.setMontoNetoTotal(rs.getDouble("monto_neto_total"));

                resumenes.add(resumen);
            }

        } catch (Exception ex) {
            System.out.println("ERROR obteniendo top servicios por veterinario: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en topServiciosPorVeterinario: " + ex.getMessage());
            }
        }

        return resumenes;
    }

    @Override
    public List<VeterinarioAtencionResumen> topVeterinariosPorAtenciones(
            int anio,
            int mes,
            int limite
    ) {
        List<VeterinarioAtencionResumen> resumenes = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL top_veterinarios_por_atenciones(?, ?, ?)}");

            cs.setInt(1, anio);
            cs.setInt(2, mes);
            cs.setInt(3, limite);

            rs = cs.executeQuery();

            while (rs.next()) {
                Veterinario veterinario = new Veterinario();
                veterinario.setId(rs.getInt("id_veterinario"));
                veterinario.setUsername(rs.getString("username"));
                veterinario.setNombres(rs.getString("nombres"));
                veterinario.setApellidos(rs.getString("apellidos"));
                veterinario.setTelefono(rs.getString("telefono"));
                veterinario.setEmail(rs.getString("email"));
                veterinario.setCmpv(rs.getString("cmpv"));
                veterinario.setEspecialidad(rs.getString("especialidad"));

                VeterinarioAtencionResumen resumen = new VeterinarioAtencionResumen();
                resumen.setVeterinario(veterinario);
                resumen.setTotalAtenciones(rs.getInt("total_atenciones"));
                resumen.setMontoNetoTotal(rs.getDouble("monto_neto_total"));

                resumenes.add(resumen);
            }

        } catch (Exception ex) {
            System.out.println("ERROR obteniendo top veterinarios por atenciones: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en topVeterinariosPorAtenciones: " + ex.getMessage());
            }
        }

        return resumenes;
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