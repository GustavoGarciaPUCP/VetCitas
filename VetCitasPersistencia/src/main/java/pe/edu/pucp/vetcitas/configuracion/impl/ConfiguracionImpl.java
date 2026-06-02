package pe.edu.pucp.vetcitas.configuracion.impl;

import pe.edu.pucp.vetcitas.config.DBManager;
import pe.edu.pucp.vetcitas.configuracion.dao.IConfiguracionDAO;
import pe.edu.pucp.vetcitas.configuracion.model.Configuracion;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ConfiguracionImpl implements IConfiguracionDAO {
    @Override
    public int insertar(Configuracion configuracion) {
        throw new UnsupportedOperationException("La configuración base se carga por script SQL.");
    }

    @Override
    public int modificar(Configuracion configuracion) {
        int resultado = 0;
        Connection con = null;
        CallableStatement cs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL modificar_configuracion(?, ?, ?)}");
            cs.setInt(1, configuracion.getId());
            cs.setInt(2, configuracion.getUmbralClienteFrecuente());
            cs.setDouble(3, configuracion.getDescuentoMaximoPermitido());
            resultado = cs.executeUpdate();

        } catch (Exception ex) {
            System.out.println("ERROR modificando configuración: " + ex.getMessage());
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
        throw new UnsupportedOperationException("No aplica eliminar configuración.");
    }

    @Override
    public Configuracion buscarPorId(int id) {
        Configuracion configuracion = null;
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL buscar_configuracion_por_id(?)}");
            cs.setInt(1, id);
            rs = cs.executeQuery();

            if (rs.next()) {
                configuracion = new Configuracion();
                configuracion.setId(rs.getInt("id_configuracion"));
                configuracion.setUmbralClienteFrecuente(rs.getInt("umbral_cliente_frecuente"));
                configuracion.setDescuentoMaximoPermitido(rs.getDouble("descuento_maximo_permitido"));
            }

        } catch (Exception ex) {
            System.out.println("ERROR buscando configuración por id: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                System.out.println("ERROR cerrando recursos en buscarPorId: " + ex.getMessage());
            }
        }
        return configuracion;
    }

    @Override
    public List<Configuracion> listarTodas() {
        List<Configuracion> lista = new ArrayList<>();
        Connection con = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        try {
            con = DBManager.getInstance().getConnection();
            cs = con.prepareCall("{CALL listar_configuraciones()}");
            rs = cs.executeQuery();

            while (rs.next()) {
                Configuracion configuracion = new Configuracion();
                configuracion.setId(rs.getInt("id_configuracion"));
                configuracion.setUmbralClienteFrecuente(rs.getInt("umbral_cliente_frecuente"));
                configuracion.setDescuentoMaximoPermitido(rs.getDouble("descuento_maximo_permitido"));
                lista.add(configuracion);
            }

        } catch (Exception ex) {
            System.out.println("ERROR listando configuraciones: " + ex.getMessage());
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
