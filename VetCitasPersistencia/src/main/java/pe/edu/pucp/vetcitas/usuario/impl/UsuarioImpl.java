package pe.edu.pucp.vetcitas.usuario.impl;

import pe.edu.pucp.vetcitas.config.DBManager;
import pe.edu.pucp.vetcitas.usuario.dao.IUsuarioDAO;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;

public class UsuarioImpl implements IUsuarioDAO {
    @Override
    public Usuario autenticar(String username, String contrasenaHash) {
        Usuario usuario = null;
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL autenticar_usuario(?, ?)}");

            cs.setString(1, username);
            cs.setString(2, contrasenaHash);

            rs = cs.executeQuery();

            if (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id_usuario"));
                usuario.setUsername(rs.getString("username"));
                usuario.setContrasenaHash(rs.getString("contrasena_hash"));
                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setTelefono(rs.getString("telefono"));
                usuario.setEmail(rs.getString("email"));
                usuario.setActivo(rs.getBoolean("activo"));
                usuario.setRoles(UsuarioPersistenciaHelper.cargarRolesDeUsuario(usuario.getId()));
            }

        } catch (Exception ex) {
            System.out.println("ERROR autenticando usuario: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en autenticar: " + ex.getMessage());
            }
        }

        return usuario;
    }

    @Override
    public boolean cambiarContrasena(int idUsuario,
                                     String contrasenaActualHash,
                                     String nuevaContrasenaHash) {
        boolean actualizado = false;
        Connection con = null;
        CallableStatement cs = null;

        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL cambiar_contrasena_usuario(?, ?, ?, ?)}");

            cs.setInt(1, idUsuario);
            cs.setString(2, contrasenaActualHash);
            cs.setString(3, nuevaContrasenaHash);
            cs.registerOutParameter(4, Types.INTEGER);

            cs.execute();

            actualizado = cs.getInt(4) > 0;

        } catch (Exception ex) {
            System.out.println("ERROR cambiando contraseña de usuario: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en cambiarContrasena: " + ex.getMessage());
            }
        }

        return actualizado;
    }

    @Override
    public boolean tieneRol(int idUsuario, String codigoRol) {
        boolean tiene = false;
        Connection con = null;
        CallableStatement cs = null;

        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL usuario_tiene_rol(?, ?, ?)}");

            cs.setInt(1, idUsuario);
            cs.setString(2, codigoRol);
            cs.registerOutParameter(3, Types.INTEGER);

            cs.execute();

            tiene = cs.getInt(3) > 0;

        } catch (Exception ex) {
            System.out.println("ERROR verificando rol de usuario: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en tieneRol: " + ex.getMessage());
            }
        }

        return tiene;
    }

    @Override
    public boolean restablecerContrasena(int idUsuarioObjetivo,
                                         String nuevaContrasenaHash,
                                         int idAdmin) {
        boolean actualizado = false;
        Connection con = null;
        CallableStatement cs = null;

        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL restablecer_contrasena_usuario(?, ?, ?, ?)}");

            cs.setInt(1, idUsuarioObjetivo);
            cs.setString(2, nuevaContrasenaHash);
            cs.setInt(3, idAdmin);
            cs.registerOutParameter(4, Types.INTEGER);

            cs.execute();

            actualizado = cs.getInt(4) > 0;

        } catch (Exception ex) {
            System.out.println("ERROR restableciendo contraseña: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en restablecerContrasena: " + ex.getMessage());
            }
        }

        return actualizado;
    }
}
