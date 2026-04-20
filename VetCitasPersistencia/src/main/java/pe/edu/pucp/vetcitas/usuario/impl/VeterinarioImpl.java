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
            cs = con.prepareCall("{CALL insertar_veterinario(?,?,?,?,?,?,?,?,?,?)}");
            cs.setString(1, objeto.getUsername());
            cs.setString(2, objeto.getContrasenaHash());
            cs.setString(3, objeto.getNombres());
            cs.setString(4, objeto.getApellidos());
            cs.setString(5, objeto.getTelefono());
            cs.setString(6, objeto.getRol().name());
            cs.setString(7, objeto.getCmpv());
            cs.setString(8, objeto.getEspecialidad());
            if (objeto.getModifiedBy() != null) {
                cs.setInt(9, objeto.getModifiedBy().getId());
            } else {
                cs.setNull(9, Types.INTEGER);
            }
            cs.registerOutParameter(10, Types.INTEGER);
            cs.executeUpdate();
            resultado = cs.getInt(10);
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
    public int modificar(Veterinario objeto) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL modificar_veterinario(?,?,?,?,?,?,?,?,?)}");
            cs.setInt(1, objeto.getId());
            cs.setString(2, objeto.getUsername());
            cs.setString(3, objeto.getContrasenaHash());
            cs.setString(4, objeto.getNombres());
            cs.setString(5, objeto.getApellidos());
            cs.setString(6, objeto.getTelefono());
            cs.setString(7, objeto.getCmpv());
            cs.setString(8, objeto.getEspecialidad());
            if (objeto.getModifiedBy() != null) {
                cs.setInt(9, objeto.getModifiedBy().getId());
            } else {
                cs.setNull(9, Types.INTEGER);
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
            cs = con.prepareCall("{CALL eliminar_veterinario(?)}");
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
    public Veterinario buscarPorId(int id) {
        Veterinario veterinario = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL buscar_veterinario_por_id(?)}");
            cs.setInt(1, id);
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
            try { if (rs != null) rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return veterinario;
    }

    @Override
    public List<Veterinario> listarTodas() {
        List<Veterinario> veterinarios = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_veterinarios()}");
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
            try { if (rs != null) rs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (cs != null) cs.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
            try { if (con != null) con.close(); } catch (Exception ex) { System.out.println("ERROR: " + ex.getMessage()); }
        }
        return veterinarios;
    }
}
