package pe.edu.pucp.vetcitas.cliente.impl;

import pe.edu.pucp.vetcitas.cliente.dao.MascotaDAO;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;
import pe.edu.pucp.vetcitas.config.DBManager;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MascotaImpl implements MascotaDAO {
    @Override
    public int insertar(Mascota mascota) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL insertar_mascota(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

            cs = con.prepareCall(sql);

            cs.setString(1, mascota.getNombre());
            cs.setString(2, mascota.getEspecie());
            cs.setString(3, mascota.getRaza());

            if (mascota.getFechaNacimiento() != null) {
                cs.setDate(4, java.sql.Date.valueOf(mascota.getFechaNacimiento()));
            } else {
                cs.setNull(4, Types.DATE);
            }

            cs.setDouble(5, mascota.getPeso());

            cs.setBoolean(6, mascota.isEsterilizado());
            cs.setBoolean(7, mascota.isActivo());
            cs.setInt(8, mascota.getCliente().getId());

            if (mascota.getCreatedOn() != null) {
                cs.setTimestamp(9, Timestamp.valueOf(mascota.getCreatedOn()));
            } else {
                cs.setTimestamp(9, null);
            }

            if (mascota.getModifiedOn() != null) {
                cs.setTimestamp(10, Timestamp.valueOf(mascota.getModifiedOn()));
            } else {
                cs.setTimestamp(10, null);
            }

            if (mascota.getModifiedBy() != null) {
                cs.setInt(11, mascota.getModifiedBy().getId());
            } else {
                cs.setNull(11, Types.INTEGER);
            }

            cs.registerOutParameter(12, Types.INTEGER);
            cs.executeUpdate();

            int idNuevo = cs.getInt(12);
            mascota.setId(idNuevo);

            resultado = idNuevo;

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
    public int modificar(Mascota mascota) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL modificar_mascota(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            cs = con.prepareCall(sql);

            cs.setInt(1, mascota.getId());
            cs.setString(2, mascota.getNombre());
            cs.setString(3, mascota.getEspecie());
            cs.setString(4, mascota.getRaza());

            if (mascota.getFechaNacimiento() != null) {
                cs.setDate(5, java.sql.Date.valueOf(mascota.getFechaNacimiento()));
            } else {
                cs.setNull(5, Types.DATE);
            }

            cs.setDouble(6, mascota.getPeso());

            cs.setBoolean(7, mascota.isEsterilizado());
            cs.setBoolean(8, mascota.isActivo());
            cs.setInt(9, mascota.getCliente().getId());

            if (mascota.getModifiedOn() != null) {
                cs.setTimestamp(10, Timestamp.valueOf(mascota.getModifiedOn()));
            } else {
                cs.setTimestamp(10, null);
            }

            if (mascota.getModifiedBy() != null) {
                cs.setInt(11, mascota.getModifiedBy().getId());
            } else {
                cs.setNull(11, Types.INTEGER);
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
    public int eliminar(int idMascota) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL eliminar_mascota(?, ?, ?)}";
            cs = con.prepareCall(sql);

            cs.setInt(1, idMascota);
            cs.setTimestamp(2, java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()));
            cs.setNull(3, java.sql.Types.INTEGER);

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
    public Mascota buscarPorId(int idMascota) {
        Mascota mascota = null;
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL buscar_mascota_por_id(?)}";
            cs = con.prepareCall(sql);
            cs.setInt(1, idMascota);

            rs = cs.executeQuery();

            if (rs.next()) {
                mascota = new Mascota();
                mascota.setId(rs.getInt("id_mascota"));
                mascota.setNombre(rs.getString("nombre"));
                mascota.setEspecie(rs.getString("especie"));
                mascota.setRaza(rs.getString("raza"));
                mascota.setPeso(rs.getDouble("peso"));

                Date fecha = rs.getDate("fecha_nacimiento");
                if (fecha != null) {
                    mascota.setFechaNacimiento(fecha.toLocalDate());
                }

                mascota.setEsterilizado(rs.getBoolean("esterilizado"));
                mascota.setActivo(rs.getBoolean("activo"));

                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                mascota.setCliente(cliente);
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
        return mascota;
    }

    @Override
    public List<Mascota> listarTodas() {
        List<Mascota> mascotas = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL listar_mascotas_activas()}";
            cs = con.prepareCall(sql);

            rs = cs.executeQuery();

            while (rs.next()) {
                Mascota mascota = new Mascota();
                mascota.setId(rs.getInt("id_mascota"));
                mascota.setNombre(rs.getString("nombre"));
                mascota.setEspecie(rs.getString("especie"));
                mascota.setRaza(rs.getString("raza"));
                mascota.setPeso(rs.getDouble("peso"));

                Date fecha = rs.getDate("fecha_nacimiento");
                if (fecha != null) {
                    mascota.setFechaNacimiento(fecha.toLocalDate());
                }

                mascota.setEsterilizado(rs.getBoolean("esterilizado"));
                mascota.setActivo(rs.getBoolean("activo"));

                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                mascota.setCliente(cliente);

                mascotas.add(mascota);
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
        return mascotas;
    }

    @Override
    public List<Mascota> listarPorNombreODueno(String texto) {
        List<Mascota> mascotas = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL listar_mascotas_por_nombre_o_dueno(?)}";
            cs = con.prepareCall(sql);
            cs.setString(1, texto);
            rs = cs.executeQuery();

            while (rs.next()) {
                Mascota mascota = new Mascota();
                mascota.setId(rs.getInt("id_mascota"));
                mascota.setNombre(rs.getString("nombre_mascota"));
                mascota.setEspecie(rs.getString("especie"));
                mascota.setRaza(rs.getString("raza"));
                mascota.setPeso(rs.getDouble("peso"));
                mascota.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                mascota.setEsterilizado(rs.getBoolean("esterilizado"));
                mascota.setActivo(rs.getBoolean("activo"));

                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                cliente.setNombres(rs.getString("nombres_dueno"));
                cliente.setApellidos(rs.getString("apellidos_dueno"));

                mascota.setCliente(cliente);
                mascotas.add(mascota);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando mascotas por nombre o dueño: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en MascotaImpl: " + ex.getMessage());
            }
        }

        return mascotas;
    }

    @Override
    public List<Mascota> listarPorCliente(int idCliente) {
        List<Mascota> mascotas = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;

        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL listar_mascotas_por_cliente(?)}";
            cs = con.prepareCall(sql);
            cs.setInt(1, idCliente);
            rs = cs.executeQuery();

            while (rs.next()) {
                Mascota mascota = new Mascota();
                mascota.setId(rs.getInt("id_mascota"));
                mascota.setNombre(rs.getString("nombre_mascota"));
                mascota.setEspecie(rs.getString("especie"));
                mascota.setRaza(rs.getString("raza"));
                mascota.setPeso(rs.getDouble("peso"));
                mascota.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
                mascota.setEsterilizado(rs.getBoolean("esterilizado"));
                mascota.setActivo(rs.getBoolean("activo"));

                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id_cliente"));
                cliente.setNombres(rs.getString("nombres_dueno"));
                cliente.setApellidos(rs.getString("apellidos_dueno"));

                mascota.setCliente(cliente);
                mascotas.add(mascota);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando mascotas por cliente: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en MascotaImpl: " + ex.getMessage());
            }
        }

        return mascotas;
    }
}
