package pe.edu.pucp.vetcitas.servicios.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

import pe.edu.pucp.vetcitas.usuario.model.Administrador;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;
import pe.edu.pucp.vetcitas.usuario.bo.AdministradorBOImpl;
import pe.edu.pucp.vetcitas.usuario.boi.IAdministradorBO;

@Path("AdministradorRS")
public class AdministradorRS {

    private IAdministradorBO administradorBO;

    public AdministradorRS() {
        this.administradorBO = new AdministradorBOImpl();
    }

    // --- Métodos de IBaseBO ---

    @POST
    @Path("/insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int insertar(Administrador administrador) throws Exception {
        return administradorBO.insertar(administrador);
    }

    @PUT
    @Path("/modificar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int modificar(Administrador administrador) throws Exception {
        return administradorBO.modificar(administrador);
    }

    @DELETE
    @Path("/eliminar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public int eliminar(@PathParam("id") int id) throws Exception {
        return administradorBO.eliminar(id);
    }

    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Administrador> listarTodos() throws Exception {
        return administradorBO.listarTodos();
    }

    @GET
    @Path("/buscar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Administrador buscarPorId(@PathParam("id") int id) throws Exception {
        return administradorBO.buscarPorId(id);
    }

    // --- Métodos de IAdministradorBO ---

    @POST
    @Path("/asignarRol")
    public void asignarRol(@QueryParam("idUsuario") int idUsuario, @QueryParam("codigoRol") String codigoRol) throws Exception {
        administradorBO.asignarRol(idUsuario, codigoRol);
    }

    @POST
    @Path("/revocarRol")
    public void revocarRol(@QueryParam("idUsuario") int idUsuario, @QueryParam("codigoRol") String codigoRol) throws Exception {
        administradorBO.revocarRol(idUsuario, codigoRol);
    }

    @GET
    @Path("/listarUsuariosFiltrados")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Usuario> listarUsuariosFiltrados(
            @QueryParam("texto") String texto,
            @QueryParam("codigoRol") String codigoRol,
            @QueryParam("activo") Boolean activo) throws Exception {
        return administradorBO.listarUsuariosFiltrados(texto, codigoRol, activo);
    }
}