package pe.edu.pucp.vetcitas.common.exception;

/**
 * Se lanza cuando una cuenta esta bloqueada temporalmente por demasiados
 * intentos fallidos de inicio de sesion.
 */
public class CuentaBloqueadaException extends RuntimeException {

    public CuentaBloqueadaException(String mensaje) {
        super(mensaje);
    }
}
