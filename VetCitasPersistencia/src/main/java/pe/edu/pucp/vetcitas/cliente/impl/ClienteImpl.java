package pe.edu.pucp.vetcitas.cliente.impl;

import pe.edu.pucp.vetcitas.cliente.dao.ClienteDAO;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;
import pe.edu.pucp.vetcitas.config.DBManager;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ClienteImpl implements ClienteDAO {
    @Override
    public int insertar(Cliente cliente) {
        int resultado = 0;
        int idNuevo = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL insertar_cliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            cs = con.prepareCall(sql);

            cs.setString(1, cliente.getDni());
            cs.setString(2, cliente.getNombres());
            cs.setString(3, cliente.getApellidos());
            cs.setString(4, cliente.getTelefono());

            if (cliente.getEmail() != null && !cliente.getEmail().trim().isEmpty()) {
                cs.setString(5, cliente.getEmail().trim());
            } else {
                cs.setNull(5, java.sql.Types.VARCHAR);
            }

            cs.setString(6, cliente.getObservaciones());
            cs.setBoolean(7, cliente.isActivo());

            // El servidor genera las marcas de auditoría (no dependemos del cliente)
            java.sql.Timestamp ahora = java.sql.Timestamp.valueOf(java.time.LocalDateTime.now());
            cs.setTimestamp(8, ahora);   // created_on
            cs.setTimestamp(9, ahora);   // modified_on

            if (cliente.getModifiedBy() != null) {
                cs.setInt(10, cliente.getModifiedBy().getId());
            } else {
                cs.setNull(10, java.sql.Types.INTEGER);
            }

            cs.registerOutParameter(11, java.sql.Types.INTEGER);

            resultado = cs.executeUpdate();

            idNuevo = cs.getInt(11);
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
        // Devolvemos el id generado (no las filas afectadas) para poder
        // asociar mascotas recién creadas al nuevo cliente.
        return idNuevo;
    }

    @Override
    public int modificar(Cliente cliente) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL modificar_cliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            cs = con.prepareCall(sql);

            cs.setInt(1, cliente.getId());
            cs.setString(2, cliente.getDni());
            cs.setString(3, cliente.getNombres());
            cs.setString(4, cliente.getApellidos());
            cs.setString(5, cliente.getTelefono());

            if (cliente.getEmail() != null && !cliente.getEmail().trim().isEmpty()) {
                cs.setString(6, cliente.getEmail().trim());
            } else {
                cs.setNull(6, java.sql.Types.VARCHAR);
            }

            cs.setString(7, cliente.getObservaciones());
            cs.setBoolean(8, cliente.isActivo());

            // El servidor sella la fecha de modificación
            cs.setTimestamp(9, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));

            if (cliente.getModifiedBy() != null) {
                cs.setInt(10, cliente.getModifiedBy().getId());
            } else {
                cs.setNull(10, java.sql.Types.INTEGER);
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
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL eliminar_cliente(?, ?, ?)}";
            cs = con.prepareCall(sql);

            cs.setInt(1, idCliente);
            cs.setTimestamp(2, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            cs.setNull(3, java.sql.Types.INTEGER);

            cs.executeUpdate();
            // El procedure ejecuta varios UPDATE; si no hubo excepción, fue correcto.
            resultado = 1;

        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
            // El procedure usa SIGNAL para bloquear cuando hay una cita confirmada futura
            if (ex.getMessage() != null && ex.getMessage().contains("cita confirmada")) {
                resultado = -1;
            } else {
                resultado = 0;
            }
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
    public Cliente buscarPorId(int idCliente) {
        Cliente cliente = null;
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL buscar_cliente_por_id(?)}";
            cs = con.prepareCall(sql);

            cs.setInt(1, idCliente);
            rs = cs.executeQuery();

            if (rs.next()) {
                cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                cliente.setDni(rs.getString("dni"));
                cliente.setNombres(rs.getString("nombres"));
                cliente.setApellidos(rs.getString("apellidos"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setEmail(rs.getString("email"));
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
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL listar_clientes_todos()}";
            cs = con.prepareCall(sql);

            rs = cs.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                cliente.setDni(rs.getString("dni"));
                cliente.setNombres(rs.getString("nombres"));
                cliente.setApellidos(rs.getString("apellidos"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setEmail(rs.getString("email"));
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

    @Override
    public List<Cliente> listarPorNombreApellidoODni(String texto) {
        List<Cliente> clientes = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL listar_clientes_por_nombre_apellido_dni(?)}";
            cs = con.prepareCall(sql);
            cs.setString(1, texto);
            rs = cs.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                cliente.setDni(rs.getString("dni"));
                cliente.setNombres(rs.getString("nombres"));
                cliente.setApellidos(rs.getString("apellidos"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setEmail(rs.getString("email"));
                cliente.setObservaciones(rs.getString("observaciones"));
                cliente.setActivo(rs.getBoolean("activo"));
                clientes.add(cliente);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando clientes por nombre/apellido/dni: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en ClienteImpl: " + ex.getMessage());
            }
        }

        return clientes;
    }

    @Override
    public int contarActivos() {
        int total = 0;
        Connection con = null;
        CallableStatement cs = null;

        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL contar_clientes_activos(?)}");

            cs.registerOutParameter(1, Types.INTEGER);

            cs.execute();

            total = cs.getInt(1);

        } catch (Exception ex) {
            System.out.println("ERROR contando clientes activos: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en contarActivos cliente: " + ex.getMessage());
            }
        }

        return total;
    }

    @Override
    public int contarNuevosEnMes(int anio, int mes) {
        int total = 0;
        Connection con = null;
        CallableStatement cs = null;

        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL contar_clientes_nuevos_en_mes(?, ?, ?)}");

            cs.setInt(1, anio);
            cs.setInt(2, mes);
            cs.registerOutParameter(3, Types.INTEGER);

            cs.execute();

            total = cs.getInt(3);

        } catch (Exception ex) {
            System.out.println("ERROR contando clientes nuevos en mes: " + ex.getMessage());
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en contarNuevosEnMes cliente: " + ex.getMessage());
            }
        }

        return total;
    }


}
