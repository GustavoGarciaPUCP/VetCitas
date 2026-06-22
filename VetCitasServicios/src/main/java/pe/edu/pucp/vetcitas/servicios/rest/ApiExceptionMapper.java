package pe.edu.pucp.vetcitas.servicios.rest;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ApiExceptionMapper implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        String mensaje = exception.getMessage();
        if (mensaje == null || mensaje.trim().isEmpty()) {
            mensaje = "No se pudo completar la operacion.";
        }

        mensaje = mensaje.replace("\\", "\\\\").replace("\"", "\\\"");

        return Response.status(Response.Status.BAD_REQUEST)
                .type(MediaType.APPLICATION_JSON)
                .entity("{\"mensaje\":\"" + mensaje + "\"}")
                .build();
    }
}
