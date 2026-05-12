package pe.edu.pucp.vetcitas.configuracion.bo;

import pe.edu.pucp.vetcitas.configuracion.dao.IConfiguracionDAO;
import pe.edu.pucp.vetcitas.configuracion.impl.ConfiguracionImpl;
import pe.edu.pucp.vetcitas.configuracion.model.Configuracion;

public class ConfiguracionBO {
    private IConfiguracionDAO configuracionDAO;

    public ConfiguracionBO() {
        this.configuracionDAO = new ConfiguracionImpl();
    }

    public Configuracion obtenerConfiguracionActual() {
        return configuracionDAO.buscarPorId(1);
    }

    public int modificar(Configuracion conf) throws Exception {
        if (conf.getDescuentoMaximoPermitido() < 0.0 || conf.getDescuentoMaximoPermitido() > 100.0) {
            throw new Exception("Error de Negocio: El descuento máximo debe ser un porcentaje entre 0 y 100.");
        }

        if (conf.getUmbralClienteFrecuente() <= 0) {
            throw new Exception("Error de Negocio: El umbral de cliente frecuente debe ser igual o mayor a 1.");
        }

        return configuracionDAO.modificar(conf);
    }
}