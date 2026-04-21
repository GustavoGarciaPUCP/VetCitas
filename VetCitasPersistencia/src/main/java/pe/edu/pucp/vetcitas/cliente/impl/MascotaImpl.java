package pe.edu.pucp.vetcitas.cliente.impl;

import pe.edu.pucp.vetcitas.cliente.dao.MascotaDAO;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;
import pe.edu.pucp.vetcitas.config.DBManager;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MascotaImpl implements MascotaDAO {
    private Connection con;
    private CallableStatement cs;
    private ResultSet rs;

    @Override
    public int insertar(Mascota mascota) {
        int resultado = 0;
        try{
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL insertar_mascota(?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)}";
            cs = con.prepareCall(sql);

            cs.setString(1,mascota.getNombre());
            cs.setString(2, mascota.getEspecie());
            cs.setString(3, mascota.getRaza());


            if (mascota.getFechaNacimiento() != null) {
                cs.setDate(4, Date.valueOf(mascota.getFechaNacimiento()));
            } else {
                cs.setDate(4, null);
            }

            cs.setBoolean(5, mascota.isEsterilizado());
            cs.setBoolean(6, mascota.isActivo());

            if (mascota.getCliente() != null) {
                cs.setInt(7, mascota.getCliente().getId());
            } else {
                cs.setNull(7, java.sql.Types.INTEGER);
            }

            if (mascota.getCreatedOn() != null) {
                cs.setTimestamp(8, java.sql.Timestamp.valueOf(mascota.getCreatedOn()));
            } else {
                cs.setTimestamp(8, null);
            }

            if (mascota.getModifiedOn() != null) {
                cs.setTimestamp(9, java.sql.Timestamp.valueOf(mascota.getModifiedOn()));
            } else {
                cs.setTimestamp(9, null);
            }

            if (mascota.getModifiedBy() != null) {
                cs.setInt(10, mascota.getModifiedBy().getId());
            } else {
                cs.setNull(10, java.sql.Types.INTEGER);
            }
            cs.registerOutParameter(11, java.sql.Types.INTEGER);
            resultado = cs.executeUpdate();
            int idNuevo = cs.getInt(11);
            mascota.setId(idNuevo);


        }catch (Exception ex){
            System.out.println("ERROR: "+ex.getMessage());
        }finally {
            try{
                if (cs != null) cs.close();
                if (con != null) con.close();
            }catch (Exception ex){
                System.out.println("ERROR: "+ex.getMessage());
            }
        }
        return resultado;
    }

    @Override
    public int modificar(Mascota mascota) {
        int resultado = 0;
        try {
            con = DBManager.getInstance().getConnection();
            String sql = "{CALL modificar_mascota(?, ?, ?, ?, ?, ?, ?, ?, ?)}";
            cs = con.prepareCall(sql);

            cs.setInt(1, mascota.getId());
            cs.setString(2, mascota.getNombre());
            cs.setString(3, mascota.getEspecie());
            cs.setString(4, mascota.getRaza());

            if (mascota.getFechaNacimiento() != null) {
                cs.setDate(5, Date.valueOf(mascota.getFechaNacimiento()));
            } else {
                cs.setDate(5, null);
            }

            cs.setBoolean(6, mascota.isEsterilizado());

            if (mascota.getCliente() != null) {
                cs.setInt(7, mascota.getCliente().getId());
            } else {
                cs.setNull(7, java.sql.Types.INTEGER);
            }

            if (mascota.getModifiedOn() != null) {
                cs.setTimestamp(8, java.sql.Timestamp.valueOf(mascota.getModifiedOn()));
            } else {
                cs.setTimestamp(8, null);
            }

            if (mascota.getModifiedBy() != null) {
                cs.setInt(9, mascota.getModifiedBy().getId());
            } else {
                cs.setNull(9, java.sql.Types.INTEGER);
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
    public Mascota buscarPorId(int idMascota) {
        Mascota mascota = null;
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
}
