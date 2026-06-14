package pe.edu.pucp.vetcitas.servicios.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

import pe.edu.pucp.vetcitas.usuario.model.Permiso;
import pe.edu.pucp.vetcitas.usuario.bo.PermisoBOImpl;
import pe.edu.pucp.vetcitas.usuario.boi.IPermisoBO;

@Path("PermisoRS")
public class PermisoRS {

    private IPermisoBO permisoBO;

    public PermisoRS() {
        this.permisoBO = new PermisoBOImpl();
    }

    @POST
    @Path("/insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int insertar(Permiso permiso) throws Exception {
        return permisoBO.insertar(permiso);
    }

    @PUT
    @Path("/modificar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int modificar(Permiso permiso) throws Exception {
        return permisoBO.modificar(permiso);
    }

    @DELETE
    @Path("/eliminar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public int eliminar(@PathParam("id") int id) throws Exception {
        return permisoBO.eliminar(id);
    }

    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Permiso> listarTodos() throws Exception {
        return permisoBO.listarTodos();
    }

    @GET
    @Path("/buscar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Permiso buscarPorId(@PathParam("id") int id) throws Exception {
        return permisoBO.buscarPorId(id);
    }
}