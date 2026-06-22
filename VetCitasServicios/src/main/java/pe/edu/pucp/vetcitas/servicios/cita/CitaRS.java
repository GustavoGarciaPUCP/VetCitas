package pe.edu.pucp.vetcitas.servicios.cita;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.edu.pucp.vetcitas.cita.bo.CitaBOImpl;
import pe.edu.pucp.vetcitas.cita.boi.ICitaBO;
import pe.edu.pucp.vetcitas.cita.model.Cita;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Path("CitaRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CitaRS {

    private ICitaBO citaBO;

    public CitaRS() {
        citaBO = new CitaBOImpl();
    }

    @POST
    @Path("insertar")
    public int insertarCita(Cita cita) throws Exception {
        return citaBO.insertar(cita);
    }

    @PUT
    @Path("modificar")
    public int modificarCita(Cita cita) throws Exception {
        return citaBO.modificar(cita);
    }

    @DELETE
    @Path("eliminar/{idCita}")
    public int eliminarCita(@PathParam("idCita") int idCita) throws Exception {
        return citaBO.eliminar(idCita);
    }

    @GET
    @Path("listarTodas")
    public List<Cita> listarTodas() throws Exception {
        return citaBO.listarTodos();
    }

    @GET
    @Path("obtenerPorId/{idCita}")
    public Cita obtenerPorId(@PathParam("idCita") int idCita) throws Exception {
        return citaBO.buscarPorId(idCita);
    }

    @PUT
    @Path("reprogramar/{idCita}/{fechaInicio}/{fechaFin}/{motivo}/{modifiedBy}")
    public int reprogramar(
            @PathParam("idCita") int idCita,
            @PathParam("fechaInicio") String fechaInicioStr,
            @PathParam("fechaFin") String fechaFinStr,
            @PathParam("motivo") String motivo,
            @PathParam("modifiedBy") int modifiedBy) throws Exception {
        LocalDateTime inicio = LocalDateTime.parse(fechaInicioStr);
        LocalDateTime fin = LocalDateTime.parse(fechaFinStr);
        citaBO.reprogramar(idCita, inicio, fin, motivo, modifiedBy);
        return 1;
    }

    @PUT
    @Path("cambiarVeterinario/{idCita}/{idNuevoVeterinario}/{modifiedBy}")
    public int cambiarVeterinario(
            @PathParam("idCita") int idCita,
            @PathParam("idNuevoVeterinario") int idNuevoVeterinario,
            @PathParam("modifiedBy") int modifiedBy) throws Exception {
        citaBO.cambiarVeterinario(idCita, idNuevoVeterinario, modifiedBy);
        return 1;
    }

    @GET
    @Path("validarDisponibilidad/{idVeterinario}/{fechaInicio}/{fechaFin}")
    public boolean validarDisponibilidadSlot(
            @PathParam("idVeterinario") int idVeterinario,
            @PathParam("fechaInicio") String fechaInicioStr,
            @PathParam("fechaFin") String fechaFinStr) throws Exception {
        LocalDateTime inicio = LocalDateTime.parse(fechaInicioStr);
        LocalDateTime fin = LocalDateTime.parse(fechaFinStr);
        return citaBO.validarDisponibilidadSlot(idVeterinario, inicio, fin);
    }

    @GET
    @Path("contarPorEstado/{estado}/{desde}/{hasta}")
    public int contarPorEstadoEnRango(
            @PathParam("estado") String estado,
            @PathParam("desde") String desdeStr,
            @PathParam("hasta") String hastaStr) throws Exception {
        LocalDateTime desde = LocalDateTime.parse(desdeStr);
        LocalDateTime hasta = LocalDateTime.parse(hastaStr);
        return citaBO.contarPorEstadoEnRango(estado, desde, hasta);
    }

    @GET
    @Path("contarPorVeterinario/{idVeterinario}/{desde}/{hasta}")
    public int contarPorVeterinarioEnRango(
            @PathParam("idVeterinario") int idVeterinario,
            @PathParam("desde") String desdeStr,
            @PathParam("hasta") String hastaStr) throws Exception {
        LocalDateTime desde = LocalDateTime.parse(desdeStr);
        LocalDateTime hasta = LocalDateTime.parse(hastaStr);
        return citaBO.contarPorVeterinarioEnRango(idVeterinario, desde, hasta);
    }

    @PUT
    @Path("cancelar/{idCita}/{motivo}/{modifiedBy}")
    public int cancelarCita(
            @PathParam("idCita") int idCita,
            @PathParam("motivo") String motivoCancelacion,
            @PathParam("modifiedBy") int modifiedBy) throws Exception {
        citaBO.cancelarCita(idCita, motivoCancelacion, modifiedBy);
        return 1;
    }

    @PUT
    @Path("confirmar/{idCita}/{modifiedBy}")
    public int confirmarCita(
            @PathParam("idCita") int idCita,
            @PathParam("modifiedBy") int modifiedBy) throws Exception {
        citaBO.confirmarCita(idCita, modifiedBy);
        return 1;
    }

    @PUT
    @Path("marcarEnConsulta/{idCita}/{idUsuario}")
    public int marcarEnConsulta(
            @PathParam("idCita") int idCita,
            @PathParam("idUsuario") int idUsuario) throws Exception {
        citaBO.marcarEnConsulta(idCita, idUsuario);
        return 1;
    }

    @PUT
    @Path("marcarAtendida/{idCita}/{modifiedBy}")
    public int marcarAtendida(
            @PathParam("idCita") int idCita,
            @PathParam("modifiedBy") int modifiedBy) throws Exception {
        citaBO.marcarAtendida(idCita, modifiedBy);
        return 1;
    }

    @PUT
    @Path("marcarNoAsistio/{idCita}/{modifiedBy}")
    public int marcarNoAsistio(
            @PathParam("idCita") int idCita,
            @PathParam("modifiedBy") int modifiedBy) throws Exception {
        citaBO.marcarNoAsistio(idCita, modifiedBy);
        return 1;
    }

    @GET
    @Path("listarPorVeterinarioFecha/{idVeterinario}/{fecha}")
    public List<Cita> listarPorVeterinarioYFecha(
            @PathParam("idVeterinario") int idVeterinario,
            @PathParam("fecha") String fechaStr) throws Exception {
        LocalDate fecha = LocalDate.parse(fechaStr);
        return citaBO.listarPorVeterinarioYFecha(idVeterinario, fecha);
    }

    @GET
    @Path("filtrar/{idVeterinario}/{fechaInicio}/{fechaFin}/{estado}/{textoBusqueda}")
    public List<Cita> listarFiltradas(
            @PathParam("idVeterinario") int idVeterinario,
            @PathParam("fechaInicio") String fechaInicioStr,
            @PathParam("fechaFin") String fechaFinStr,
            @PathParam("estado") String estado,
            @PathParam("textoBusqueda") String textoBusqueda) throws Exception {
        Integer idVet = (idVeterinario == 0) ? null : idVeterinario;
        LocalDate fechaInicio = (fechaInicioStr.equals("null") || fechaInicioStr.isEmpty()) ? null : LocalDate.parse(fechaInicioStr);
        LocalDate fechaFin = (fechaFinStr.equals("null") || fechaFinStr.isEmpty()) ? null : LocalDate.parse(fechaFinStr);
        String est = (estado.equals("null") || estado.trim().isEmpty()) ? "" : estado;
        String texto = (textoBusqueda.equals("null")) ? "" : textoBusqueda;

        return citaBO.listarFiltradas(idVet, fechaInicio, fechaFin, est, texto);
    }
}
