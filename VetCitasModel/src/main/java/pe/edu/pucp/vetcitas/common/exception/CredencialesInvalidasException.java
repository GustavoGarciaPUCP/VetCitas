package pe.edu.pucp.vetcitas.common.exception;

/**
 * Se lanza cuando las credenciales son invalidas. Lleva, cuando aplica, el
 * numero de intentos que le quedan al usuario antes de que su cuenta se bloquee
 * temporalmente. Puede ser null si no corresponde mostrar el conteo (por
 * ejemplo, cuando el usuario no existe).
 */
public class CredencialesInvalidasException extends RuntimeException {

    private final Integer intentosRestantes;

    public CredencialesInvalidasException(Integer intentosRestantes) {
        super("Usuario o contrasena incorrectos.");
        this.intentosRestantes = intentosRestantes;
    }

    public Integer getIntentosRestantes() {
        return intentosRestantes;
    }
}
