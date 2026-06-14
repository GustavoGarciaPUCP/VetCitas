package pe.edu.pucp.vetcitas.servicios.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.List;

import pe.edu.pucp.vetcitas.usuario.model.Veterinario;
import pe.edu.pucp.vetcitas.usuario.bo.VeterinarioBOImpl;
import pe.edu.pucp.vetcitas.usuario.boi.IVeterinarioBO;

@Path("VeterinarioRS")
public class VeterinarioRS {

    private IVeterinarioBO veterinarioBO;

    public VeterinarioRS() {
        this.veterinarioBO = new VeterinarioBOImpl();
    }

    @POST
    @Path("/insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int insertar(Veterinario veterinario) throws Exception {
        return veterinarioBO.insertar(veterinario);
    }

    @PUT
    @Path("/modificar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int modificar(Veterinario veterinario) throws Exception {
        return veterinarioBO.modificar(veterinario);
    }

    @DELETE
    @Path("/eliminar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public int eliminar(@PathParam("id") int id) throws Exception {
        return veterinarioBO.eliminar(id);
    }

    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Veterinario> listarTodos() throws Exception {
        return veterinarioBO.listarTodos();
    }

    @GET
    @Path("/buscar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Veterinario buscarPorId(@PathParam("id") int id) throws Exception {
        return veterinarioBO.buscarPorId(id);
    }

    // --- Métodos de IVeterinarioBO ---

    @GET
    @Path("/listarDisponibles")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Veterinario> listarDisponibles(
            @QueryParam("fechaHoraInicio") String fechaHoraInicioStr,
            @QueryParam("idServicio") int idServicio) throws Exception {

        // Convertimos el String a LocalDateTime (El formato esperado en el front sería algo como "2023-10-25T10:30:00")
        LocalDateTime fechaHora = LocalDateTime.parse(fechaHoraInicioStr);
        return veterinarioBO.listarDisponibles(fechaHora, idServicio);
    }
}