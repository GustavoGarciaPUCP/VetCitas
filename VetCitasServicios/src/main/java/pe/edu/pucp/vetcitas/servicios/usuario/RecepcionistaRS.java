package pe.edu.pucp.vetcitas.servicios.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

import pe.edu.pucp.vetcitas.usuario.model.Recepcionista;
import pe.edu.pucp.vetcitas.usuario.bo.RecepcionistaBOImpl;
import pe.edu.pucp.vetcitas.usuario.boi.IRecepcionistaBO;

@Path("RecepcionistaRS")
public class RecepcionistaRS {

    private IRecepcionistaBO recepcionistaBO;

    public RecepcionistaRS() {
        this.recepcionistaBO = new RecepcionistaBOImpl();
    }

    @POST
    @Path("/insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int insertar(Recepcionista recepcionista) throws Exception {
        return recepcionistaBO.insertar(recepcionista);
    }

    @PUT
    @Path("/modificar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int modificar(Recepcionista recepcionista) throws Exception {
        return recepcionistaBO.modificar(recepcionista);
    }

    @DELETE
    @Path("/eliminar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public int eliminar(@PathParam("id") int id) throws Exception {
        return recepcionistaBO.eliminar(id);
    }

    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Recepcionista> listarTodos() throws Exception {
        return recepcionistaBO.listarTodos();
    }

    @GET
    @Path("/buscar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Recepcionista buscarPorId(@PathParam("id") int id) throws Exception {
        return recepcionistaBO.buscarPorId(id);
    }
}