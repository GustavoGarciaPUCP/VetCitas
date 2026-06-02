package pe.edu.pucp.vetcitas.configuracion.boi;

import pe.edu.pucp.vetcitas.configuracion.model.Configuracion;

public interface IConfiguracionBO {
    Configuracion obtenerConfiguracionActual() throws Exception;
    int modificar(Configuracion conf) throws Exception;
}
