package pe.edu.pucp.vetcitas.cita.impl;

import pe.edu.pucp.vetcitas.cita.dao.ICitaDAO;
import pe.edu.pucp.vetcitas.cita.model.Cita;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;
import pe.edu.pucp.vetcitas.common.enums.EstadoCita;
import pe.edu.pucp.vetcitas.common.enums.Rol;
import pe.edu.pucp.vetcitas.common.enums.TipoServicio;
import pe.edu.pucp.vetcitas.config.DBManager;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;
import pe.edu.pucp.vetcitas.usuario.model.Veterinario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CitaImpl implements ICitaDAO {

    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;

    @Override
    public int insertar(Cita cita) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call insertar_cita(?,?,?,?,?,?,?,?)}");
            cs.setTimestamp("p_fecha_hora_inicio", Timestamp.valueOf(cita.getFechaHoraInicio()));
            cs.setTimestamp("p_fecha_hora_fin", Timestamp.valueOf(cita.getFechaHoraFin()));
            cs.setString("p_estado", cita.getEstado().name());
            cs.setInt("p_id_mascota", cita.getMascota().getId());
            cs.setInt("p_id_veterinario", cita.getVeterinario().getId());
            cs.setInt("p_id_servicio", cita.getServicio().getId());
            cs.setTimestamp("p_created_on", Timestamp.valueOf(LocalDateTime.now()));
            cs.registerOutParameter("p_id_generado", Types.INTEGER);
            cs.executeUpdate();
            resultado = cs.getInt("p_id_generado");
            cita.setId(resultado);
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return resultado;
    }

    @Override
    public int modificar(Cita cita) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call modificar_cita(?,?,?,?,?,?,?,?,?)}");
            cs.setInt("p_id_cita", cita.getId());
            cs.setTimestamp("p_fecha_hora_inicio", Timestamp.valueOf(cita.getFechaHoraInicio()));
            cs.setTimestamp("p_fecha_hora_fin", Timestamp.valueOf(cita.getFechaHoraFin()));
            cs.setString("p_estado", cita.getEstado().name());
            cs.setInt("p_id_mascota", cita.getMascota().getId());
            cs.setInt("p_id_veterinario", cita.getVeterinario().getId());
            cs.setInt("p_id_servicio", cita.getServicio().getId());
            cs.setTimestamp("p_modified_on", Timestamp.valueOf(LocalDateTime.now()));
            if (cita.getModifiedBy() != null) {
                cs.setInt("p_modified_by", cita.getModifiedBy().getId());
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
            cs = con.prepareCall("{call eliminar_cita(?,?,?)}");
            cs.setInt("p_id_cita", id);
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
    public Cita buscarPorId(int id) {
        Cita cita = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call buscar_cita_por_id(?)}");
            cs.setInt("p_id_cita", id);
            rs = cs.executeQuery();
            if (rs.next()) {
                cita = new Cita();
                cita.setId(rs.getInt("id_cita"));
                cita.setFechaHoraInicio(rs.getTimestamp("fecha_hora_inicio").toLocalDateTime());
                cita.setFechaHoraFin(rs.getTimestamp("fecha_hora_fin").toLocalDateTime());
                cita.setEstado(EstadoCita.valueOf(rs.getString("estado")));
                Mascota mascota = new Mascota();
                mascota.setId(rs.getInt("id_mascota"));
                mascota.setNombre(rs.getString("mascota_nombre"));
                mascota.setEspecie(rs.getString("especie"));
                mascota.setRaza(rs.getString("raza"));
                Date fechaNacimiento = rs.getDate("fecha_nacimiento");
                if (fechaNacimiento != null) {
                    mascota.setFechaNacimiento(fechaNacimiento.toLocalDate());
                }
                mascota.setEsterilizado(rs.getBoolean("esterilizado"));
                mascota.setActivo(rs.getBoolean("mascota_activo"));

                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                mascota.setCliente(cliente);
                mascota.setAtenciones(new ArrayList<>());
                cita.setMascota(mascota);

                Veterinario veterinario = new Veterinario();
                veterinario.setId(rs.getInt("id_veterinario"));
                veterinario.setUsername(rs.getString("username"));
                veterinario.setContrasenaHash(rs.getString("contrasena_hash"));
                veterinario.setNombres(rs.getString("vet_nombres"));
                veterinario.setApellidos(rs.getString("vet_apellidos"));
                veterinario.setTelefono(rs.getString("vet_telefono"));
                veterinario.setActivo(rs.getBoolean("vet_activo"));
                veterinario.setRol(Rol.valueOf(rs.getString("rol")));
                veterinario.setPermisos(new ArrayList<>());
                veterinario.setCmpv(rs.getString("cmpv"));
                veterinario.setEspecialidad(rs.getString("especialidad"));
                cita.setVeterinario(veterinario);

                Servicio servicio = new Servicio();
                servicio.setId(rs.getInt("id_servicio"));
                servicio.setNombre(rs.getString("servicio_nombre"));
                servicio.setTipoServicio(TipoServicio.valueOf(rs.getString("tipo_servicio")));
                servicio.setDuracionMinutos(rs.getInt("duracion_minutos"));
                servicio.setPrecioReferencial(rs.getDouble("precio_referencial"));
                servicio.setActivo(rs.getBoolean("servicio_activo"));
                cita.setServicio(servicio);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return cita;
    }

    @Override
    public List<Cita> listarTodas() {
        List<Cita> citas = null;
        try {
            citas = new ArrayList<>();
            con = DBManager.getInstance().getConnection();
            // Corregido: Llamado en minúsculas respetando tu convención
            cs = con.prepareCall("{call listar_citas()}");
            rs = cs.executeQuery();

            while (rs.next()) {
                Cita cita = new Cita();
                cita.setId(rs.getInt("id_cita"));
                cita.setFechaHoraInicio(rs.getTimestamp("fecha_hora_inicio").toLocalDateTime());
                cita.setFechaHoraFin(rs.getTimestamp("fecha_hora_fin").toLocalDateTime());
                cita.setEstado(EstadoCita.valueOf(rs.getString("estado")));
                Mascota mascota = new Mascota();
                mascota.setId(rs.getInt("id_mascota"));
                mascota.setNombre(rs.getString("mascota_nombre"));
                mascota.setEspecie(rs.getString("especie"));
                mascota.setRaza(rs.getString("raza"));
                Date fechaNacimiento = rs.getDate("fecha_nacimiento");
                if (fechaNacimiento != null) {
                    mascota.setFechaNacimiento(fechaNacimiento.toLocalDate());
                }
                mascota.setEsterilizado(rs.getBoolean("esterilizado"));
                mascota.setActivo(rs.getBoolean("mascota_activo"));
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                mascota.setCliente(cliente);
                mascota.setAtenciones(new ArrayList<>());
                cita.setMascota(mascota);

                Veterinario veterinario = new Veterinario();
                veterinario.setId(rs.getInt("id_veterinario"));
                veterinario.setUsername(rs.getString("username"));
                veterinario.setContrasenaHash(rs.getString("contrasena_hash"));
                veterinario.setNombres(rs.getString("vet_nombres"));
                veterinario.setApellidos(rs.getString("vet_apellidos"));
                veterinario.setTelefono(rs.getString("vet_telefono"));
                veterinario.setActivo(rs.getBoolean("vet_activo"));
                veterinario.setRol(Rol.valueOf(rs.getString("rol")));
                veterinario.setPermisos(new ArrayList<>());
                veterinario.setCmpv(rs.getString("cmpv"));
                veterinario.setEspecialidad(rs.getString("especialidad"));
                cita.setVeterinario(veterinario);

                Servicio servicio = new Servicio();
                servicio.setId(rs.getInt("id_servicio"));
                servicio.setNombre(rs.getString("servicio_nombre"));
                servicio.setTipoServicio(TipoServicio.valueOf(rs.getString("tipo_servicio")));
                servicio.setDuracionMinutos(rs.getInt("duracion_minutos"));
                servicio.setPrecioReferencial(rs.getDouble("precio_referencial"));
                servicio.setActivo(rs.getBoolean("servicio_activo"));
                cita.setServicio(servicio);
                citas.add(cita);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return citas;
    }
}
