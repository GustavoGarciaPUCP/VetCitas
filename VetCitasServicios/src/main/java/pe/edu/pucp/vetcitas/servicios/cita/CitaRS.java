package pe.edu.pucp.vetcitas.servicios.cita;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.edu.pucp.vetcitas.cita.bo.CitaBOImpl;
import pe.edu.pucp.vetcitas.cita.boi.ICitaBO;
import pe.edu.pucp.vetcitas.cita.model.Cita;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    public int insertarCita(Cita cita) {
        int resultado = 0;
        try {
            resultado = citaBO.insertar(cita);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @PUT
    @Path("modificar")
    public int modificarCita(Cita cita) {
        int resultado = 0;
        try {
            resultado = citaBO.modificar(cita);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @DELETE
    @Path("eliminar/{idCita}")
    public int eliminarCita(@PathParam("idCita") int idCita) {
        int resultado = 0;
        try {
            resultado = citaBO.eliminar(idCita);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @GET
    @Path("listarTodas")
    public List<Cita> listarTodas() {
        List<Cita> citas = new ArrayList<>();
        try {
            citas = citaBO.listarTodos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return citas;
    }

    @GET
    @Path("obtenerPorId/{idCita}")
    public Cita obtenerPorId(@PathParam("idCita") int idCita) {
        Cita cita = null;
        try {
            cita = citaBO.buscarPorId(idCita);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return cita;
    }

    @PUT
    @Path("reprogramar/{idCita}/{fechaInicio}/{fechaFin}/{motivo}/{modifiedBy}")
    public int reprogramar(
            @PathParam("idCita") int idCita,
            @PathParam("fechaInicio") String fechaInicioStr,
            @PathParam("fechaFin") String fechaFinStr,
            @PathParam("motivo") String motivo,
            @PathParam("modifiedBy") int modifiedBy) {
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicioStr);
            LocalDateTime fin = LocalDateTime.parse(fechaFinStr);
            citaBO.reprogramar(idCita, inicio, fin, motivo, modifiedBy);
            return 1; // Retorna 1 si la transacción fue exitosa
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    @PUT
    @Path("cambiarVeterinario/{idCita}/{idNuevoVeterinario}/{modifiedBy}")
    public int cambiarVeterinario(
            @PathParam("idCita") int idCita,
            @PathParam("idNuevoVeterinario") int idNuevoVeterinario,
            @PathParam("modifiedBy") int modifiedBy) {
        try {
            citaBO.cambiarVeterinario(idCita, idNuevoVeterinario, modifiedBy);
            return 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    @GET
    @Path("validarDisponibilidad/{idVeterinario}/{fechaInicio}/{fechaFin}")
    public boolean validarDisponibilidadSlot(
            @PathParam("idVeterinario") int idVeterinario,
            @PathParam("fechaInicio") String fechaInicioStr,
            @PathParam("fechaFin") String fechaFinStr) {
        boolean disponible = false;
        try {
            LocalDateTime inicio = LocalDateTime.parse(fechaInicioStr);
            LocalDateTime fin = LocalDateTime.parse(fechaFinStr);
            disponible = citaBO.validarDisponibilidadSlot(idVeterinario, inicio, fin);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return disponible;
    }

    @GET
    @Path("contarPorEstado/{estado}/{desde}/{hasta}")
    public int contarPorEstadoEnRango(
            @PathParam("estado") String estado,
            @PathParam("desde") String desdeStr,
            @PathParam("hasta") String hastaStr) {
        int cantidad = 0;
        try {
            LocalDateTime desde = LocalDateTime.parse(desdeStr);
            LocalDateTime hasta = LocalDateTime.parse(hastaStr);
            cantidad = citaBO.contarPorEstadoEnRango(estado, desde, hasta);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return cantidad;
    }

    @GET
    @Path("contarPorVeterinario/{idVeterinario}/{desde}/{hasta}")
    public int contarPorVeterinarioEnRango(
            @PathParam("idVeterinario") int idVeterinario,
            @PathParam("desde") String desdeStr,
            @PathParam("hasta") String hastaStr) {
        int cantidad = 0;
        try {
            LocalDateTime desde = LocalDateTime.parse(desdeStr);
            LocalDateTime hasta = LocalDateTime.parse(hastaStr);
            cantidad = citaBO.contarPorVeterinarioEnRango(idVeterinario, desde, hasta);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return cantidad;
    }

    @PUT
    @Path("cancelar/{idCita}/{motivo}/{modifiedBy}")
    public int cancelarCita(
            @PathParam("idCita") int idCita,
            @PathParam("motivo") String motivoCancelacion,
            @PathParam("modifiedBy") int modifiedBy) {
        try {
            citaBO.cancelarCita(idCita, motivoCancelacion, modifiedBy);
            return 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    @PUT
    @Path("confirmar/{idCita}/{modifiedBy}")
    public int confirmarCita(
            @PathParam("idCita") int idCita,
            @PathParam("modifiedBy") int modifiedBy) {
        try {
            citaBO.confirmarCita(idCita, modifiedBy);
            return 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    @PUT
    @Path("marcarEnConsulta/{idCita}/{idUsuario}")
    public int marcarEnConsulta(
            @PathParam("idCita") int idCita,
            @PathParam("idUsuario") int idUsuario) {
        try {
            citaBO.marcarEnConsulta(idCita, idUsuario);
            return 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    @PUT
    @Path("marcarAtendida/{idCita}/{modifiedBy}")
    public int marcarAtendida(
            @PathParam("idCita") int idCita,
            @PathParam("modifiedBy") int modifiedBy) {
        try {
            citaBO.marcarAtendida(idCita, modifiedBy);
            return 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    @PUT
    @Path("marcarNoAsistio/{idCita}/{modifiedBy}")
    public int marcarNoAsistio(
            @PathParam("idCita") int idCita,
            @PathParam("modifiedBy") int modifiedBy) {
        try {
            citaBO.marcarNoAsistio(idCita, modifiedBy);
            return 1;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    @GET
    @Path("listarPorVeterinarioFecha/{idVeterinario}/{fecha}")
    public List<Cita> listarPorVeterinarioYFecha(
            @PathParam("idVeterinario") int idVeterinario,
            @PathParam("fecha") String fechaStr) {
        List<Cita> citas = new ArrayList<>();
        try {
            LocalDate fecha = LocalDate.parse(fechaStr);
            citas = citaBO.listarPorVeterinarioYFecha(idVeterinario, fecha);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return citas;
    }

    @GET
    @Path("filtrar/{idVeterinario}/{fechaInicio}/{fechaFin}/{estado}/{textoBusqueda}")
    public List<Cita> listarFiltradas(
            @PathParam("idVeterinario") int idVeterinario,
            @PathParam("fechaInicio") String fechaInicioStr,
            @PathParam("fechaFin") String fechaFinStr,
            @PathParam("estado") String estado,
            @PathParam("textoBusqueda") String textoBusqueda) {
        List<Cita> citas = new ArrayList<>();
        try {
            Integer idVet = (idVeterinario == 0) ? null : idVeterinario;
            LocalDate fechaInicio = (fechaInicioStr.equals("null") || fechaInicioStr.isEmpty()) ? null : LocalDate.parse(fechaInicioStr);
            LocalDate fechaFin = (fechaFinStr.equals("null") || fechaFinStr.isEmpty()) ? null : LocalDate.parse(fechaFinStr);
            String est = (estado.equals("null") || estado.trim().isEmpty()) ? "" : estado;
            String texto = (textoBusqueda.equals("null")) ? "" : textoBusqueda;

            citas = citaBO.listarFiltradas(idVet, fechaInicio, fechaFin, est, texto);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return citas;
    }

}
