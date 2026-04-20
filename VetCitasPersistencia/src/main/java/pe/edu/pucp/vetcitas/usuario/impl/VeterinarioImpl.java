package pe.edu.pucp.vetcitas.usuario.impl;

import pe.edu.pucp.vetcitas.config.DBManager;
import pe.edu.pucp.vetcitas.usuario.dao.IVeterinarioDAO;
import pe.edu.pucp.vetcitas.usuario.model.Veterinario;
import pe.edu.pucp.vetcitas.common.enums.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioImpl implements IVeterinarioDAO {

    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;

    @Override
    public int insertar(Veterinario objeto) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call INSERTAR_VETERINARIO(?,?,?,?,?,?,?,?,?,?)}");
            cs.registerOutParameter("_id_usuario", Types.INTEGER);
            cs.setString("_username", objeto.getUsername());
            cs.setString("_contrasena_hash", objeto.getContrasenaHash());
            cs.setString("_nombres", objeto.getNombres());
            cs.setString("_apellidos", objeto.getApellidos());
            cs.setString("_telefono", objeto.getTelefono());
            cs.setString("_rol", objeto.getRol().name());
            cs.setString("_cmpv", objeto.getCmpv());
            cs.setString("_especialidad", objeto.getEspecialidad());
            if (objeto.getModifiedBy() != null) {
                cs.setInt("_modified_by", objeto.getModifiedBy().getId());
            } else {
                cs.setNull("_modified_by", Types.INTEGER);
            }
            cs.executeUpdate();
            resultado = cs.getInt("_id_usuario");
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
    public int modificar(Veterinario objeto) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call MODIFICAR_VETERINARIO(?,?,?,?,?,?,?,?,?)}");
            cs.setInt("_id_usuario", objeto.getId());
            cs.setString("_username", objeto.getUsername());
            cs.setString("_contrasena_hash", objeto.getContrasenaHash());
            cs.setString("_nombres", objeto.getNombres());
            cs.setString("_apellidos", objeto.getApellidos());
            cs.setString("_telefono", objeto.getTelefono());
            cs.setString("_cmpv", objeto.getCmpv());
            cs.setString("_especialidad", objeto.getEspecialidad());
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
            cs = con.prepareCall("{call ELIMINAR_VETERINARIO(?)}");
            cs.setInt("_id_usuario", id);
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
    public Veterinario buscarPorId(int id) {
        Veterinario veterinario = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call BUSCAR_VETERINARIO_POR_ID(?)}");
            cs.setInt("_id_usuario", id);
            rs = cs.executeQuery();
            if (rs.next()) {
                veterinario = new Veterinario();
                veterinario.setId(rs.getInt("id_usuario"));
                veterinario.setUsername(rs.getString("username"));
                veterinario.setContrasenaHash(rs.getString("contrasena_hash"));
                veterinario.setNombres(rs.getString("nombres"));
                veterinario.setApellidos(rs.getString("apellidos"));
                veterinario.setTelefono(rs.getString("telefono"));
                veterinario.setActivo(rs.getBoolean("activo"));
                veterinario.setRol(Rol.valueOf(rs.getString("rol")));
                veterinario.setCmpv(rs.getString("cmpv"));
                veterinario.setEspecialidad(rs.getString("especialidad"));
                veterinario.setCreatedOn(rs.getTimestamp("created_on").toLocalDateTime());
                Timestamp modifiedOn = rs.getTimestamp("modified_on");
                if (modifiedOn != null) {
                    veterinario.setModifiedOn(modifiedOn.toLocalDateTime());
                }
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return veterinario;
    }

    @Override
    public List<Veterinario> listarTodas() {
        List<Veterinario> veterinarios = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{call LISTAR_VETERINARIOS()}");
            rs = cs.executeQuery();
            while (rs.next()) {
                if (veterinarios == null) veterinarios = new ArrayList<>();
                Veterinario veterinario = new Veterinario();
                veterinario.setId(rs.getInt("id_usuario"));
                veterinario.setUsername(rs.getString("username"));
                veterinario.setContrasenaHash(rs.getString("contrasena_hash"));
                veterinario.setNombres(rs.getString("nombres"));
                veterinario.setApellidos(rs.getString("apellidos"));
                veterinario.setTelefono(rs.getString("telefono"));
                veterinario.setActivo(rs.getBoolean("activo"));
                veterinario.setRol(Rol.valueOf(rs.getString("rol")));
                veterinario.setCmpv(rs.getString("cmpv"));
                veterinario.setEspecialidad(rs.getString("especialidad"));
                veterinario.setCreatedOn(rs.getTimestamp("created_on").toLocalDateTime());
                Timestamp modifiedOn = rs.getTimestamp("modified_on");
                if (modifiedOn != null) {
                    veterinario.setModifiedOn(modifiedOn.toLocalDateTime());
                }
                veterinarios.add(veterinario);
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try { rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return veterinarios;
    }
}
