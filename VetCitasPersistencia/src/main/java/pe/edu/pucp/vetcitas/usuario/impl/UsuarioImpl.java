package pe.edu.pucp.vetcitas.usuario.impl;

import pe.edu.pucp.vetcitas.config.DBManager;
import pe.edu.pucp.vetcitas.common.exception.CuentaBloqueadaException;
import pe.edu.pucp.vetcitas.common.exception.CredencialesInvalidasException;
import pe.edu.pucp.vetcitas.usuario.dao.IUsuarioDAO;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

public class UsuarioImpl implements IUsuarioDAO {
    @Override
    public boolean estaActivo(int idUsuario) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            ps = con.prepareStatement("SELECT activo FROM usuario WHERE id_usuario = ?");
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("activo");
            }
            // Si el usuario no existe, se considera no activo.
            return false;
        } catch (Exception ex) {
            throw new RuntimeException("Error consultando el estado del usuario.", ex);
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en estaActivo: " + ex.getMessage());
            }
        }
    }

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

            // El procedimiento hace UPDATEs antes del SELECT final; navegamos
            // hasta el primer ResultSet para leer el estado de autenticacion.
            boolean tieneResultados = cs.execute();
            while (!tieneResultados && cs.getUpdateCount() != -1) {
                tieneResultados = cs.getMoreResults();
            }
            rs = cs.getResultSet();

            if (rs != null && rs.next()) {
                int estado = rs.getInt("estado");

                if (estado == 2) {
                    // Cuenta bloqueada temporalmente por demasiados intentos fallidos.
                    throw new CuentaBloqueadaException(
                            "Cuenta bloqueada temporalmente por demasiados intentos. Intenta de nuevo en unos minutos.");
                }

                if (estado == 0) {
                    // Credenciales invalidas: se adjuntan los intentos restantes (puede ser null).
                    Integer restantes = (Integer) rs.getObject("intentos_restantes");
                    throw new CredencialesInvalidasException(restantes);
                }

                if (estado == 1) {
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
            }

        } catch (CuentaBloqueadaException | CredencialesInvalidasException ex) {
            // Se propagan tal cual para que el mensaje llegue al usuario.
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Error consultando credenciales de usuario.", ex);
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
