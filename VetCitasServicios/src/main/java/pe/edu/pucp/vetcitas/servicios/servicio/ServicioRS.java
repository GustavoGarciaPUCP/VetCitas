package pe.edu.pucp.vetcitas.servicios.servicio;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.edu.pucp.vetcitas.cita.model.ServicioAtencionResumen;
import pe.edu.pucp.vetcitas.servicio.bo.ServicioBOImpl;
import pe.edu.pucp.vetcitas.servicio.boi.IServicioBO;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Path("ServicioRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServicioRS {

    private IServicioBO servicioBO;

    public ServicioRS() {
        servicioBO = new ServicioBOImpl();
    }

    @POST
    @Path("insertar")
    public int insertar(Servicio servicio) {
        int resultado = 0;
        try {
            resultado = servicioBO.insertar(servicio);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @PUT
    @Path("modificar")
    public int modificar(Servicio servicio) {
        int resultado = 0;
        try {
            resultado = servicioBO.modificar(servicio);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @DELETE
    @Path("eliminar/{id}")
    public int eliminar(@PathParam("id") int id) {
        return eliminar(id, 0);
    }

    @DELETE
    @Path("eliminar/{id}/{modifiedBy}")
    public int eliminar(@PathParam("id") int id, @PathParam("modifiedBy") int modifiedBy) {
        int resultado = 0;
        try {
            resultado = servicioBO.eliminar(id, modifiedBy);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @GET
    @Path("listarTodos")
    public List<Servicio> listarTodos() {
        List<Servicio> servicios = new ArrayList<>();
        try {
            servicios = servicioBO.listarTodos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return servicios;
    }

    @GET
    @Path("buscarPorId/{id}")
    public Servicio buscarPorId(@PathParam("id") int id) {
        Servicio servicio = null;
        try {
            servicio = servicioBO.buscarPorId(id);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return servicio;
    }

    @PUT
    @Path("deshabilitar/{id}")
    public int deshabilitar(@PathParam("id") int id) {
        return deshabilitar(id, 0);
    }

    @PUT
    @Path("deshabilitar/{id}/{modifiedBy}")
    public int deshabilitar(@PathParam("id") int id, @PathParam("modifiedBy") int modifiedBy) {
        int resultado = 0;
        try {
            resultado = servicioBO.deshabilitar(id, modifiedBy);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @GET
    @Path("buscarPorTexto/{texto}")
    public List<Servicio> listarPorNombreOTipo(@PathParam("texto") String texto) {
        List<Servicio> servicios = new ArrayList<>();
        try {
            String busqueda = (texto.equals("null")) ? "" : texto;
            servicios = servicioBO.listarPorNombreOTipo(busqueda);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return servicios;
    }

    @GET
    @Path("listarPorEstado/{activo}")
    public List<Servicio> listarPorEstado(@PathParam("activo") boolean activo) {
        List<Servicio> servicios = new ArrayList<>();
        try {
            servicios = servicioBO.listarPorEstado(activo);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return servicios;
    }

    @GET
    @Path("topDemandados/{desde}/{hasta}/{limite}")
    public List<ServicioAtencionResumen> topNMasDemandados(
            @PathParam("desde") String desdeStr,
            @PathParam("hasta") String hastaStr,
            @PathParam("limite") int limite) {
        List<ServicioAtencionResumen> resumen = new ArrayList<>();
        try {
            LocalDateTime desde = LocalDateTime.parse(desdeStr);
            LocalDateTime hasta = LocalDateTime.parse(hastaStr);
            resumen = servicioBO.topNMasDemandados(desde, hasta, limite);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resumen;
    }

}
