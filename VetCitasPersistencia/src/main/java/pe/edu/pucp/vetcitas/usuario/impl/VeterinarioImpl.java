package pe.edu.pucp.vetcitas.usuario.impl;

import pe.edu.pucp.vetcitas.common.enums.CodigoRol;
import pe.edu.pucp.vetcitas.config.DBManager;
import pe.edu.pucp.vetcitas.usuario.dao.IVeterinarioDAO;
import pe.edu.pucp.vetcitas.usuario.model.Veterinario;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioImpl implements IVeterinarioDAO {
    @Override
    public int insertar(Veterinario veterinario) {
        int idGenerado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL insertar_veterinario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            cs.setString(1, veterinario.getUsername());
            cs.setString(2, veterinario.getContrasenaHash());
            cs.setString(3, veterinario.getNombres());
            cs.setString(4, veterinario.getApellidos());
            cs.setString(5, veterinario.getTelefono());
            cs.setString(6, veterinario.getEmail());
            cs.setString(7, veterinario.getCmpv());
            cs.setString(8, veterinario.getEspecialidad());

            if (veterinario.getModifiedBy() != null) {
                cs.setInt(9, veterinario.getModifiedBy().getId());
            } else {
                cs.setNull(9, Types.INTEGER);
            }

            cs.registerOutParameter(10, Types.INTEGER);
            cs.executeUpdate();

            idGenerado = cs.getInt(10);
            veterinario.setId(idGenerado);

        } catch (Exception ex) {
            throw new RuntimeException("ERROR insertando veterinario: " + ex.getMessage(), ex);
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
    public int modificar(Veterinario veterinario) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL modificar_veterinario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            cs.setInt(1, veterinario.getId());
            cs.setString(2, veterinario.getUsername());
            cs.setString(3, veterinario.getContrasenaHash());
            cs.setString(4, veterinario.getNombres());
            cs.setString(5, veterinario.getApellidos());
            cs.setString(6, veterinario.getTelefono());
            cs.setString(7, veterinario.getEmail());
            cs.setBoolean(8, veterinario.isActivo());
            cs.setString(9, veterinario.getCmpv());
            cs.setString(10, veterinario.getEspecialidad());

            if (veterinario.getModifiedBy() != null) {
                cs.setInt(11, veterinario.getModifiedBy().getId());
            } else {
                cs.setNull(11, Types.INTEGER);
            }

            resultado = cs.executeUpdate();

        } catch (Exception ex) {
            throw new RuntimeException("ERROR modificando veterinario: " + ex.getMessage(), ex);
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
            cs = con.prepareCall("{CALL eliminar_usuario_logico(?, ?)}");
            cs.setInt(1, id);
            cs.setNull(2, Types.INTEGER);
            resultado = cs.executeUpdate();

        } catch (Exception ex) {
            throw new RuntimeException("ERROR eliminando veterinario: " + ex.getMessage(), ex);
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
    public Veterinario buscarPorId(int id) {
        Veterinario veterinario = null;
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
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
                veterinario.setEmail(rs.getString("email"));
                veterinario.setCmpv(rs.getString("cmpv"));
                veterinario.setEspecialidad(rs.getString("especialidad"));
                veterinario.setRoles(UsuarioPersistenciaHelper.cargarRolesDeUsuario(id));
            }

        } catch (Exception ex) {
            System.out.println("ERROR buscando veterinario por id: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en buscarPorId: " + ex.getMessage());
            }
        }
        return veterinario;
    }

    @Override
    public List<Veterinario> listarTodas() {
        List<Veterinario> lista = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_veterinarios()}");
            rs = cs.executeQuery();

            while (rs.next()) {
                Veterinario veterinario = new Veterinario();
                int id = rs.getInt("id_usuario");
                veterinario.setId(id);
                veterinario.setUsername(rs.getString("username"));
                veterinario.setContrasenaHash(rs.getString("contrasena_hash"));
                veterinario.setNombres(rs.getString("nombres"));
                veterinario.setApellidos(rs.getString("apellidos"));
                veterinario.setTelefono(rs.getString("telefono"));
                veterinario.setActivo(rs.getBoolean("activo"));
                veterinario.setEmail(rs.getString("email"));
                veterinario.setCmpv(rs.getString("cmpv"));
                veterinario.setEspecialidad(rs.getString("especialidad"));
                veterinario.setRoles(UsuarioPersistenciaHelper.cargarRolesDeUsuario(id));
                lista.add(veterinario);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando veterinarios: " + ex.getMessage());
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
    public List<Veterinario> listarDisponibles(LocalDateTime fechaHoraInicio, int idServicio) {
        List<Veterinario> lista = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_veterinarios_disponibles(?, ?)}");
            cs.setTimestamp(1, Timestamp.valueOf(fechaHoraInicio));
            cs.setInt(2, idServicio);
            rs = cs.executeQuery();

            while (rs.next()) {
                Veterinario veterinario = new Veterinario();
                int id = rs.getInt("id_usuario");
                veterinario.setId(id);
                veterinario.setUsername(rs.getString("username"));
                veterinario.setNombres(rs.getString("nombres"));
                veterinario.setApellidos(rs.getString("apellidos"));
                veterinario.setTelefono(rs.getString("telefono"));
                veterinario.setEmail(rs.getString("email"));
                veterinario.setCmpv(rs.getString("cmpv"));
                veterinario.setEspecialidad(rs.getString("especialidad"));
                veterinario.setRoles(UsuarioPersistenciaHelper.cargarRolesDeUsuario(id));
                lista.add(veterinario);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando veterinarios disponibles: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en listarDisponibles: " + ex.getMessage());
            }
        }
        return lista;
    }
}
