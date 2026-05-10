package pe.edu.pucp.vetcitas.usuario.impl;

import pe.edu.pucp.vetcitas.config.DBManager;
import pe.edu.pucp.vetcitas.usuario.dao.IPermisoDAO;
import pe.edu.pucp.vetcitas.usuario.model.Permiso;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PermisoImpl implements IPermisoDAO {
    @Override
    public int insertar(Permiso permiso) {
        int idGenerado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL insertar_permiso(?, ?, ?)}");

            cs.setString(1, permiso.getNombre());
            cs.setString(2, permiso.getDescripcion());
            cs.registerOutParameter(3, Types.INTEGER);

            cs.executeUpdate();

            idGenerado = cs.getInt(3);
            permiso.setId(idGenerado);

        } catch (Exception ex) {
            System.out.println("ERROR insertando permiso: " + ex.getMessage());
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
    public int modificar(Permiso permiso) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL modificar_permiso(?, ?, ?, ?)}");

            cs.setInt(1, permiso.getId());
            cs.setString(2, permiso.getNombre());
            cs.setString(3, permiso.getDescripcion());
            cs.setBoolean(4, true);

            resultado = cs.executeUpdate();

        } catch (Exception ex) {
            System.out.println("ERROR modificando permiso: " + ex.getMessage());
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
            cs = con.prepareCall("{CALL eliminar_permiso_logico(?)}");
            cs.setInt(1, id);

            resultado = cs.executeUpdate();

        } catch (Exception ex) {
            System.out.println("ERROR eliminando permiso: " + ex.getMessage());
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
    public Permiso buscarPorId(int id) {
        Permiso permiso = null;
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL buscar_permiso_por_id(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();

            if (rs.next()) {
                permiso = new Permiso();
                permiso.setId(rs.getInt("id_permiso"));
                permiso.setNombre(rs.getString("nombre"));
                permiso.setDescripcion(rs.getString("descripcion"));
            }

        } catch (Exception ex) {
            System.out.println("ERROR buscando permiso por id: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en buscarPorId: " + ex.getMessage());
            }
        }
        return permiso;
    }

    @Override
    public List<Permiso> listarTodas() {
        List<Permiso> lista = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_permisos()}");
            rs = cs.executeQuery();

            while (rs.next()) {
                Permiso permiso = new Permiso();
                permiso.setId(rs.getInt("id_permiso"));
                permiso.setNombre(rs.getString("nombre"));
                permiso.setDescripcion(rs.getString("descripcion"));
                lista.add(permiso);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando permisos: " + ex.getMessage());
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
