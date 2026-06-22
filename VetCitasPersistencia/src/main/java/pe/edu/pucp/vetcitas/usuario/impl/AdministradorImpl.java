package pe.edu.pucp.vetcitas.usuario.impl;

import pe.edu.pucp.vetcitas.common.enums.CodigoRol;
import pe.edu.pucp.vetcitas.config.DBManager;
import pe.edu.pucp.vetcitas.usuario.dao.IAdministradorDAO;
import pe.edu.pucp.vetcitas.usuario.model.Administrador;
import pe.edu.pucp.vetcitas.usuario.model.RolSistema;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AdministradorImpl implements IAdministradorDAO {
    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;

    @Override
    public int insertar(Administrador administrador) {
        int idGenerado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL insertar_administrador(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            cs.setString(1, administrador.getUsername());
            cs.setString(2, administrador.getContrasenaHash());
            cs.setString(3, administrador.getNombres());
            cs.setString(4, administrador.getApellidos());
            cs.setString(5, administrador.getTelefono());
            cs.setString(6, administrador.getEmail());
            cs.setString(7, administrador.getArea());
            cs.setBoolean(8, administrador.isEsSuperAdmin());

            if (administrador.getModifiedBy() != null) {
                cs.setInt(9, administrador.getModifiedBy().getId());
            } else {
                cs.setNull(9, Types.INTEGER);
            }

            cs.registerOutParameter(10, Types.INTEGER);
            cs.executeUpdate();

            idGenerado = cs.getInt(10);
            administrador.setId(idGenerado);

        } catch (Exception ex) {
            throw new RuntimeException("ERROR insertando administrador: " + ex.getMessage(), ex);
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en insertar: " + ex.getMessage());
            }
        }
        return idGenerado;
    }

    @Override
    public int modificar(Administrador administrador) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL modificar_administrador(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

            cs.setInt(1, administrador.getId());
            cs.setString(2, administrador.getUsername());
            cs.setString(3, administrador.getContrasenaHash());
            cs.setString(4, administrador.getNombres());
            cs.setString(5, administrador.getApellidos());
            cs.setString(6, administrador.getTelefono());
            cs.setString(7, administrador.getEmail());
            cs.setBoolean(8, administrador.isActivo());
            cs.setString(9, administrador.getArea());

            if (administrador.getModifiedBy() != null) {
                cs.setInt(10, administrador.getModifiedBy().getId());
            } else {
                cs.setNull(10, Types.INTEGER);
            }

            resultado = cs.executeUpdate();

        } catch (Exception ex) {
            throw new RuntimeException("ERROR modificando administrador: " + ex.getMessage(), ex);
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en modificar: " + ex.getMessage());
            }
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
            cs = con.prepareCall("{CALL eliminar_administrador_logico(?, ?)}");
            cs.setInt(1, id);
            cs.setNull(2, Types.INTEGER);
            resultado = cs.executeUpdate();

        } catch (Exception ex) {
            throw new RuntimeException("ERROR eliminando administrador: " + ex.getMessage(), ex);
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en eliminar: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public Administrador buscarPorId(int id) {
        Administrador administrador = null;
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL buscar_administrador_por_id(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();

            if (rs.next()) {
                administrador = new Administrador();
                administrador.setId(rs.getInt("id_usuario"));
                administrador.setUsername(rs.getString("username"));
                administrador.setContrasenaHash(rs.getString("contrasena_hash"));
                administrador.setNombres(rs.getString("nombres"));
                administrador.setApellidos(rs.getString("apellidos"));
                administrador.setTelefono(rs.getString("telefono"));
                administrador.setEmail(rs.getString("email"));
                administrador.setActivo(rs.getBoolean("activo"));
                administrador.setArea(rs.getString("area"));
                administrador.setEsSuperAdmin(rs.getBoolean("es_super_admin"));
                administrador.setRoles(UsuarioPersistenciaHelper.cargarRolesDeUsuario(id));
            }

        } catch (Exception ex) {
            System.out.println("ERROR buscando administrador por id: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en buscarPorId: " + ex.getMessage());
            }
        }
        return administrador;
    }

    @Override
    public List<Administrador> listarTodas() {
        List<Administrador> lista = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_administradores()}");
            rs = cs.executeQuery();

            while (rs.next()) {
                Administrador administrador = new Administrador();
                int id = rs.getInt("id_usuario");
                administrador.setId(id);
                administrador.setUsername(rs.getString("username"));
                administrador.setContrasenaHash(rs.getString("contrasena_hash"));
                administrador.setNombres(rs.getString("nombres"));
                administrador.setApellidos(rs.getString("apellidos"));
                administrador.setTelefono(rs.getString("telefono"));
                administrador.setEmail(rs.getString("email"));
                administrador.setActivo(rs.getBoolean("activo"));
                administrador.setArea(rs.getString("area"));
                administrador.setEsSuperAdmin(rs.getBoolean("es_super_admin"));
                administrador.setRoles(UsuarioPersistenciaHelper.cargarRolesDeUsuario(id));
                lista.add(administrador);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando administradores: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en listarTodas: " + ex.getMessage());
            }
        }
        return lista;
    }

    @Override
    public void asignarRol(int idUsuario, String codigoRol) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL asignar_rol_a_usuario(?, ?)}");
            cs.setInt(1, idUsuario);
            cs.setString(2, codigoRol);
            cs.executeUpdate();

        } catch (Exception ex) {
            throw new RuntimeException("ERROR asignando rol a usuario: " + ex.getMessage(), ex);
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en asignarRol: " + ex.getMessage());
            }
        }
    }

    @Override
    public void revocarRol(int idUsuario, String codigoRol) {
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL revocar_rol_de_usuario(?, ?)}");
            cs.setInt(1, idUsuario);
            cs.setString(2, codigoRol);
            cs.executeUpdate();

        } catch (Exception ex) {
            throw new RuntimeException("ERROR revocando rol de usuario: " + ex.getMessage(), ex);
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en revocarRol: " + ex.getMessage());
            }
        }
    }

    @Override
    public List<RolSistema> listarRolesDeUsuario(int idUsuario) {
        return UsuarioPersistenciaHelper.cargarRolesDeUsuario(idUsuario);
    }

    @Override
    public List<String> listarPermisosDeUsuario(int idUsuario) {
        List<String> permisos = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_permisos_por_usuario(?)}");
            cs.setInt(1, idUsuario);
            rs = cs.executeQuery();

            while (rs.next()) {
                permisos.add(rs.getString("nombre"));
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando permisos de usuario: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en listarPermisosDeUsuario: " + ex.getMessage());
            }
        }
        return permisos;
    }

    @Override
    public boolean existeUsername(String username, Integer idExcluir) {
        boolean existe = false;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL verificar_username_existe(?, ?, ?)}");

            cs.setString(1, username);
            if (idExcluir != null) {
                cs.setInt(2, idExcluir);
            } else {
                cs.setNull(2, java.sql.Types.INTEGER);
            }

            cs.registerOutParameter(3, java.sql.Types.TINYINT);
            cs.execute();

            existe = cs.getBoolean(3);

        } catch (Exception ex) {
            throw new RuntimeException("ERROR verificando username: " + ex.getMessage(), ex);
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en existeUsername: " + ex.getMessage());
            }
        }
        return existe;
    }

    @Override
    public int modificarUsuarioBasico(Usuario usuario, int modifiedBy) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL modificar_usuario_basico(?, ?, ?, ?, ?, ?, ?, ?)}");
            cs.setInt(1, usuario.getId());
            cs.setString(2, usuario.getUsername());
            cs.setString(3, usuario.getNombres());
            cs.setString(4, usuario.getApellidos());
            cs.setString(5, usuario.getTelefono());
            cs.setString(6, usuario.getEmail());
            cs.setBoolean(7, usuario.isActivo());
            cs.setInt(8, modifiedBy);

            resultado = cs.executeUpdate();

        } catch (Exception ex) {
            throw new RuntimeException("ERROR modificando usuario basico: " + ex.getMessage(), ex);
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en modificarUsuarioBasico: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public List<Usuario> listarUsuariosFiltrados(String texto, String codigoRol, Boolean activo) {
        List<Usuario> usuarios = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL listar_usuarios_filtrados(?, ?, ?)}";
            cs = con.prepareCall(sql);
            cs.setString(1, texto);
            cs.setString(2, codigoRol);

            if (activo != null) cs.setBoolean(3, activo);
            else cs.setNull(3, java.sql.Types.TINYINT);

            rs = cs.executeQuery();

            Map<Integer, Usuario> mapa = new LinkedHashMap<>();

            while (rs.next()) {
                int idUsuario = rs.getInt("id_usuario");

                Usuario usuario = mapa.get(idUsuario);
                if (usuario == null) {
                    usuario = new Usuario();
                    usuario.setId(idUsuario);
                    usuario.setUsername(rs.getString("username"));
                    usuario.setNombres(rs.getString("nombres"));
                    usuario.setApellidos(rs.getString("apellidos"));
                    usuario.setTelefono(rs.getString("telefono"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setActivo(rs.getBoolean("activo"));
                    usuario.setRoles(new ArrayList<>());
                    mapa.put(idUsuario, usuario);
                }

                if (rs.getObject("id_rol") != null) {
                    RolSistema rol = new RolSistema();
                    rol.setId(rs.getInt("id_rol"));
                    rol.setCodigo(CodigoRol.valueOf(rs.getString("codigo_rol")));
                    rol.setDescripcion(rs.getString("descripcion_rol"));

                    boolean existe = false;
                    for (RolSistema r : usuario.getRoles()) {
                        if (r.getId() == rol.getId()) {
                            existe = true;
                            break;
                        }
                    }
                    if (!existe) {
                        usuario.getRoles().add(rol);
                    }
                }
            }

            usuarios.addAll(mapa.values());

        } catch (Exception ex) {
            throw new RuntimeException("ERROR listando usuarios filtrados: " + ex.getMessage(), ex);
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en AdministradorImpl: " + ex.getMessage());
            }
        }

        return usuarios;
    }
}
