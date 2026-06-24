package pe.edu.pucp.vetcitas.servicios.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import pe.edu.pucp.vetcitas.usuario.model.Usuario;
import pe.edu.pucp.vetcitas.usuario.bo.UsuarioBOImpl;
import pe.edu.pucp.vetcitas.usuario.boi.IUsuarioBO;
import pe.edu.pucp.vetcitas.common.exception.CuentaBloqueadaException;
import pe.edu.pucp.vetcitas.common.exception.CredencialesInvalidasException;

@Path("UsuarioRS")
public class UsuarioRS {

    private IUsuarioBO usuarioBO;

    public UsuarioRS() {
        this.usuarioBO = new UsuarioBOImpl();
    }

    @GET
    @Path("/estaActivo/{idUsuario}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean estaActivo(@PathParam("idUsuario") int idUsuario) throws Exception {
        return usuarioBO.estaActivo(idUsuario);
    }

    @POST
    @Path("/autenticar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response autenticar(
            @FormParam("username") String username,
            @FormParam("contrasenaPlana") String contrasenaPlana) {

        try {
            Usuario usuario = usuarioBO.autenticar(username, contrasenaPlana);
            return Response.ok(usuario).build();
        } catch (CuentaBloqueadaException ex) {
            // 429 Too Many Requests: cuenta bloqueada temporalmente.
            String respuesta = "{\"mensaje\":\"" + ex.getMessage() + "\"}";
            return Response.status(429)
                    .entity(respuesta)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (CredencialesInvalidasException ex) {
            // 401 con el numero de intentos restantes (cuando corresponde).
            String respuesta = ex.getIntentosRestantes() != null
                    ? "{\"mensaje\":\"Usuario o contrasena incorrectos.\",\"intentosRestantes\":" + ex.getIntentosRestantes() + "}"
                    : "{\"mensaje\":\"Usuario o contrasena incorrectos.\"}";
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(respuesta)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        } catch (Exception ex) {
            String mensaje = ex.getMessage() == null ? "" : ex.getMessage().toLowerCase();
            boolean errorAutenticacion = mensaje.contains("credenciales")
                    || mensaje.contains("username")
                    || mensaje.contains("contrase");

            Response.Status estado = errorAutenticacion
                    ? Response.Status.UNAUTHORIZED
                    : Response.Status.INTERNAL_SERVER_ERROR;
            String respuesta = errorAutenticacion
                    ? "{\"mensaje\":\"Usuario o contrasena incorrectos.\"}"
                    : "{\"mensaje\":\"No se pudo validar el usuario. Verifica el backend y la base de datos.\"}";

            return Response.status(estado)
                    .entity(respuesta)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    @POST
    @Path("/cambiarContrasena")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void cambiarContrasena(
            @FormParam("idUsuario") int idUsuario,
            @FormParam("contrasenaActual") String contrasenaActual,
            @FormParam("nuevaContrasena") String nuevaContrasena) throws Exception {

        usuarioBO.cambiarContrasena(idUsuario, contrasenaActual, nuevaContrasena);
    }

    @POST
    @Path("/restablecerContrasena")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public void restablecerContrasena(
            @FormParam("idUsuarioObjetivo") int idUsuarioObjetivo,
            @FormParam("nuevaContrasena") String nuevaContrasena,
            @FormParam("idAdmin") int idAdmin) throws Exception {

        usuarioBO.restablecerContrasena(idUsuarioObjetivo, nuevaContrasena, idAdmin);
    }
}
