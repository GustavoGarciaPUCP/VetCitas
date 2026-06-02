package pe.edu.pucp.vetcitas.configuracion.bo;

import pe.edu.pucp.vetcitas.configuracion.boi.IConfiguracionBO;
import pe.edu.pucp.vetcitas.configuracion.dao.IConfiguracionDAO;
import pe.edu.pucp.vetcitas.configuracion.impl.ConfiguracionImpl;
import pe.edu.pucp.vetcitas.configuracion.model.Configuracion;

public class ConfiguracionBOImpl implements IConfiguracionBO {
    private IConfiguracionDAO configuracionDAO;

    public ConfiguracionBOImpl() {
        this.configuracionDAO = new ConfiguracionImpl();
    }

    @Override
    public Configuracion obtenerConfiguracionActual() throws Exception {
        Configuracion conf = configuracionDAO.buscarPorId(1);
        if (conf == null) {
            throw new Exception("No existe una configuración registrada en el sistema.");
        }
        return conf;
    }

    @Override
    public int modificar(Configuracion conf) throws Exception {
        if (conf == null) {
            throw new Exception("La configuración no puede ser nula.");
        }
        if (conf.getDescuentoMaximoPermitido() < 0.0 || conf.getDescuentoMaximoPermitido() > 100.0) {
            throw new Exception("Error de Negocio: El descuento máximo debe ser un porcentaje entre 0 y 100.");
        }
        if (conf.getUmbralClienteFrecuente() <= 0) {
            throw new Exception("Error de Negocio: El umbral de cliente frecuente debe ser igual o mayor a 1.");
        }
        return configuracionDAO.modificar(conf);
    }
}
