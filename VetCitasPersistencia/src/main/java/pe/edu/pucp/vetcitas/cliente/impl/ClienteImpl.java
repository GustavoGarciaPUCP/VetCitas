package pe.edu.pucp.vetcitas.cliente.impl;

import pe.edu.pucp.vetcitas.cliente.dao.ClienteDAO;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;
import pe.edu.pucp.vetcitas.config.DBManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteImpl implements ClienteDAO {

    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;

    @Override
    public int insertar(Cliente cliente) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL insertar_cliente(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            cs = con.prepareCall(sql);

            cs.setString(1, cliente.getNombres());
            cs.setString(2, cliente.getApellidos());
            cs.setString(3, cliente.getTelefono());
            cs.setString(4, cliente.getObservaciones());
            cs.setBoolean(5, cliente.isActivo());


            if (cliente.getCreatedOn() != null) {
                cs.setTimestamp(6, java.sql.Timestamp.valueOf(cliente.getCreatedOn()));
            } else {
                cs.setTimestamp(6, null);
            }


            if (cliente.getModifiedOn() != null) {
                cs.setTimestamp(7, java.sql.Timestamp.valueOf(cliente.getModifiedOn()));
            } else {
                cs.setTimestamp(7, null);
            }


            if (cliente.getModifiedBy() != null) {
                cs.setInt(8, cliente.getModifiedBy().getId());
            } else {
                cs.setNull(8, java.sql.Types.INTEGER);
            }

            cs.registerOutParameter(9, java.sql.Types.INTEGER);

            resultado = cs.executeUpdate();

            int idNuevo = cs.getInt(9);
            cliente.setId(idNuevo);

        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int modificar(Cliente cliente) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL modificar_cliente(?, ?, ?, ?, ?, ?, ?, ?)}";
            cs = con.prepareCall(sql);

            cs.setInt(1, cliente.getId());
            cs.setString(2, cliente.getNombres());
            cs.setString(3, cliente.getApellidos());
            cs.setString(4, cliente.getTelefono());
            cs.setString(5, cliente.getObservaciones());
            cs.setBoolean(6, cliente.isActivo());

            if (cliente.getModifiedOn() != null) {
                cs.setTimestamp(7, java.sql.Timestamp.valueOf(cliente.getModifiedOn()));
            } else {
                cs.setTimestamp(7, null);
            }

            if (cliente.getModifiedBy() != null) {
                cs.setInt(8, cliente.getModifiedBy().getId());
            } else {
                cs.setNull(8, java.sql.Types.INTEGER);
            }

            resultado = cs.executeUpdate();

        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int eliminar(int idCliente) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL eliminar_cliente(?, ?, ?)}";
            cs = con.prepareCall(sql);

            cs.setInt(1, idCliente);
            cs.setTimestamp(2, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            cs.setNull(3, java.sql.Types.INTEGER);

            resultado = cs.executeUpdate();

        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public Cliente buscarPorId(int idCliente) {
        Cliente cliente = null;
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL buscar_cliente_por_id(?)}";
            cs = con.prepareCall(sql);

            cs.setInt(1, idCliente);
            rs = cs.executeQuery();

            if (rs.next()) {
                cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                cliente.setNombres(rs.getString("nombres"));
                cliente.setApellidos(rs.getString("apellidos"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setObservaciones(rs.getString("observaciones"));
                cliente.setActivo(rs.getBoolean("activo"));
            }

        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }
        return cliente;
    }

    @Override
    public List<Cliente> listarTodas() {
        List<Cliente> clientes = new ArrayList<>();
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL listar_clientes_activos()}";
            cs = con.prepareCall(sql);

            rs = cs.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                cliente.setNombres(rs.getString("nombres"));
                cliente.setApellidos(rs.getString("apellidos"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setObservaciones(rs.getString("observaciones"));
                cliente.setActivo(rs.getBoolean("activo"));

                clientes.add(cliente);
            }

        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR: " + ex.getMessage());
            }
        }
        return clientes;
    }
}
