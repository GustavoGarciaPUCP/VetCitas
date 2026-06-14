package pe.edu.pucp.vetcitas.servicios.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import pe.edu.pucp.vetcitas.usuario.model.Usuario;
import pe.edu.pucp.vetcitas.usuario.bo.UsuarioBOImpl;
import pe.edu.pucp.vetcitas.usuario.boi.IUsuarioBO;

@Path("UsuarioRS")
public class UsuarioRS {

    private IUsuarioBO usuarioBO;

    public UsuarioRS() {
        this.usuarioBO = new UsuarioBOImpl();
    }

    @POST
    @Path("/autenticar")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Usuario autenticar(
            @FormParam("username") String username,
            @FormParam("contrasenaPlana") String contrasenaPlana) throws Exception {

        return usuarioBO.autenticar(username, contrasenaPlana);
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