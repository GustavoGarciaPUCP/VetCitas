package pe.edu.pucp.vetcitas.usuario.impl;

import pe.edu.pucp.vetcitas.common.enums.CodigoRol;
import pe.edu.pucp.vetcitas.config.DBManager;
import pe.edu.pucp.vetcitas.usuario.model.Permiso;
import pe.edu.pucp.vetcitas.usuario.model.RolSistema;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UsuarioPersistenciaHelper {
    private UsuarioPersistenciaHelper() {}

    public static List<RolSistema> cargarRolesDeUsuario(int idUsuario) {
        List<RolSistema> roles = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_roles_de_usuario(?)}");
            cs.setInt(1, idUsuario);
            rs = cs.executeQuery();

            while (rs.next()) {
                RolSistema rol = new RolSistema();
                rol.setId(rs.getInt("id_rol"));
                rol.setCodigo(CodigoRol.valueOf(rs.getString("codigo")));
                rol.setDescripcion(rs.getString("descripcion"));
                // Importante: Esto abrirá y cerrará su propia conexión
                rol.setPermisos(cargarPermisosPorRol(rs.getString("codigo")));
                roles.add(rol);
            }
        } catch (Exception ex) {
            System.out.println("ERROR cargando roles de usuario: " + ex.getMessage());
        } finally {
            // Separados para garantizar el cierre
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (cs != null) cs.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return roles;
    }

    public static List<Permiso> cargarPermisosPorRol(String codigoRol) {
        List<Permiso> permisos = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_permisos_por_rol(?)}");
            cs.setString(1, codigoRol);
            rs = cs.executeQuery();

            while (rs.next()) {
                Permiso permiso = new Permiso();
                permiso.setId(rs.getInt("id_permiso"));
                permiso.setNombre(rs.getString("nombre"));
                permiso.setDescripcion(rs.getString("descripcion"));
                permisos.add(permiso);
            }
        } catch (Exception ex) {
            System.out.println("ERROR cargando permisos por rol: " + ex.getMessage());
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (cs != null) cs.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return permisos;
    }
}
