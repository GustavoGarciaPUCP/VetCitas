package pe.edu.pucp.vetcitas.servicios.usuario;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

import pe.edu.pucp.vetcitas.usuario.model.HorarioVeterinario;
import pe.edu.pucp.vetcitas.usuario.bo.HorarioVeterinarioBOImpl;
import pe.edu.pucp.vetcitas.usuario.boi.IHorarioVeterinarioBO;

@Path("HorarioVeterinarioRS")
public class HorarioVeterinarioRS {

    private IHorarioVeterinarioBO horarioVeterinarioBO;

    public HorarioVeterinarioRS() {
        this.horarioVeterinarioBO = new HorarioVeterinarioBOImpl();
    }

    @POST
    @Path("/insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int insertar(HorarioVeterinario horario) throws Exception {
        return horarioVeterinarioBO.insertar(horario);
    }

    @PUT
    @Path("/modificar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int modificar(HorarioVeterinario horario) throws Exception {
        return horarioVeterinarioBO.modificar(horario);
    }

    @DELETE
    @Path("/eliminar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public int eliminar(@PathParam("id") int id) throws Exception {
        return horarioVeterinarioBO.eliminar(id);
    }

    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public List<HorarioVeterinario> listarTodos() throws Exception {
        return horarioVeterinarioBO.listarTodos();
    }

    @GET
    @Path("/buscar/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public HorarioVeterinario buscarPorId(@PathParam("id") int id) throws Exception {
        return horarioVeterinarioBO.buscarPorId(id);
    }

    // --- Métodos de IHorarioVeterinarioBO ---

    @GET
    @Path("/listarPorVeterinario/{idVeterinario}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<HorarioVeterinario> listarPorVeterinario(@PathParam("idVeterinario") int idVeterinario) throws Exception {
        return horarioVeterinarioBO.listarPorVeterinario(idVeterinario);
    }

    @GET
    @Path("/listarHorarioSemanalPorVeterinario/{idVeterinario}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<HorarioVeterinario> listarHorarioSemanalPorVeterinario(@PathParam("idVeterinario") int idVeterinario) throws Exception {
        return horarioVeterinarioBO.listarHorarioSemanalPorVeterinario(idVeterinario);
    }
}