package pe.edu.pucp.vetcitas.usuario.impl;

import pe.edu.pucp.vetcitas.config.DBManager;
import pe.edu.pucp.vetcitas.usuario.dao.IRecepcionistaDAO;
import pe.edu.pucp.vetcitas.usuario.model.Recepcionista;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class RecepcionistaImpl implements IRecepcionistaDAO {
    @Override
    public int insertar(Recepcionista recepcionista) {
        int idGenerado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL insertar_recepcionista(?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            cs.setString(1, recepcionista.getUsername());
            cs.setString(2, recepcionista.getContrasenaHash());
            cs.setString(3, recepcionista.getNombres());
            cs.setString(4, recepcionista.getApellidos());
            cs.setString(5, recepcionista.getTelefono());
            cs.setString(6, recepcionista.getEmail());
            cs.setString(7, recepcionista.getArea());

            if (recepcionista.getModifiedBy() != null) {
                cs.setInt(8, recepcionista.getModifiedBy().getId());
            } else {
                cs.setNull(8, Types.INTEGER);
            }

            cs.registerOutParameter(9, Types.INTEGER);
            cs.executeUpdate();

            idGenerado = cs.getInt(9);
            recepcionista.setId(idGenerado);

        } catch (Exception ex) {
            System.out.println("ERROR insertando recepcionista: " + ex.getMessage());
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
    public int modificar(Recepcionista recepcionista) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL modificar_recepcionista(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
            cs.setInt(1, recepcionista.getId());
            cs.setString(2, recepcionista.getUsername());
            cs.setString(3, recepcionista.getContrasenaHash());
            cs.setString(4, recepcionista.getNombres());
            cs.setString(5, recepcionista.getApellidos());
            cs.setString(6, recepcionista.getTelefono());
            cs.setString(7, recepcionista.getEmail());
            cs.setBoolean(8, recepcionista.isActivo());
            cs.setString(9, recepcionista.getArea());

            if (recepcionista.getModifiedBy() != null) {
                cs.setInt(10, recepcionista.getModifiedBy().getId());
            } else {
                cs.setNull(10, Types.INTEGER);
            }

            resultado = cs.executeUpdate();

        } catch (Exception ex) {
            System.out.println("ERROR modificando recepcionista: " + ex.getMessage());
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
            System.out.println("ERROR eliminando recepcionista: " + ex.getMessage());
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
    public Recepcionista buscarPorId(int id) {
        Recepcionista recepcionista = null;
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL buscar_recepcionista_por_id(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();

            if (rs.next()) {
                recepcionista = new Recepcionista();
                recepcionista.setId(rs.getInt("id_usuario"));
                recepcionista.setUsername(rs.getString("username"));
                recepcionista.setContrasenaHash(rs.getString("contrasena_hash"));
                recepcionista.setNombres(rs.getString("nombres"));
                recepcionista.setApellidos(rs.getString("apellidos"));
                recepcionista.setTelefono(rs.getString("telefono"));
                recepcionista.setEmail(rs.getString("email"));
                recepcionista.setActivo(rs.getBoolean("activo"));
                recepcionista.setArea(rs.getString("area"));
                recepcionista.setRoles(UsuarioPersistenciaHelper.cargarRolesDeUsuario(id));
            }

        } catch (Exception ex) {
            System.out.println("ERROR buscando recepcionista por id: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en buscarPorId: " + ex.getMessage());
            }
        }
        return recepcionista;
    }

    @Override
    public List<Recepcionista> listarTodas() {
        List<Recepcionista> lista = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_recepcionistas()}");
            rs = cs.executeQuery();

            while (rs.next()) {
                Recepcionista recepcionista = new Recepcionista();
                int id = rs.getInt("id_usuario");
                recepcionista.setId(id);
                recepcionista.setUsername(rs.getString("username"));
                recepcionista.setContrasenaHash(rs.getString("contrasena_hash"));
                recepcionista.setNombres(rs.getString("nombres"));
                recepcionista.setApellidos(rs.getString("apellidos"));
                recepcionista.setTelefono(rs.getString("telefono"));
                recepcionista.setEmail(rs.getString("email"));
                recepcionista.setActivo(rs.getBoolean("activo"));
                recepcionista.setArea(rs.getString("area"));
                recepcionista.setRoles(UsuarioPersistenciaHelper.cargarRolesDeUsuario(id));
                lista.add(recepcionista);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando recepcionistas: " + ex.getMessage());
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
}
