package pe.edu.pucp.vetcitas.servicios.cita;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.edu.pucp.vetcitas.cita.bo.AtencionBOImpl;
import pe.edu.pucp.vetcitas.cita.boi.IAtencionBO;
import pe.edu.pucp.vetcitas.cita.model.Atencion;
import pe.edu.pucp.vetcitas.cita.model.ServicioAtencionResumen;
import pe.edu.pucp.vetcitas.cita.model.VeterinarioAtencionResumen;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Path("AtencionRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AtencionRS {

    private IAtencionBO atencionBO;

    public AtencionRS() {
        atencionBO = new AtencionBOImpl();
    }

    @POST
    @Path("insertar")
    public int insertar(Atencion atencion) {
        int resultado = 0;
        try {
            resultado = atencionBO.insertar(atencion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @PUT
    @Path("modificar")
    public int modificar(Atencion atencion) {
        int resultado = 0;
        try {
            resultado = atencionBO.modificar(atencion);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @DELETE
    @Path("eliminar/{id}")
    public int eliminar(@PathParam("id") int id) {
        int resultado = 0;
        try {
            resultado = atencionBO.eliminar(id);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @GET
    @Path("listarTodos")
    public List<Atencion> listarTodos() {
        List<Atencion> atenciones = new ArrayList<>();
        try {
            atenciones = atencionBO.listarTodos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return atenciones;
    }

    @GET
    @Path("buscarPorId/{id}")
    public Atencion buscarPorId(@PathParam("id") int id) {
        Atencion atencion = null;
        try {
            atencion = atencionBO.buscarPorId(id);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return atencion;
    }

    @GET
    @Path("buscarPorCita/{idCita}")
    public Atencion buscarPorCita(@PathParam("idCita") int idCita) {
        Atencion atencion = null;
        try {
            atencion = atencionBO.buscarPorCita(idCita);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return atencion;
    }

    @GET
    @Path("historialMascota/{idMascota}")
    public List<Atencion> listarHistorialPorMascota(@PathParam("idMascota") int idMascota) {
        List<Atencion> atenciones = new ArrayList<>();
        try {
            atenciones = atencionBO.listarHistorialPorMascota(idMascota);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return atenciones;
    }

    /*duda*/
    @GET
    @Path("filtrar/{idVeterinario}/{estadoCita}/{fecha}/{textoBusqueda}")
    public List<Atencion> listarFiltradas(
            @PathParam("idVeterinario") int idVeterinario,
            @PathParam("estadoCita") String estadoCita,
            @PathParam("fecha") String fechaStr,
            @PathParam("textoBusqueda") String textoBusqueda) {
        List<Atencion> atenciones = new ArrayList<>();
        try {
            Integer idVet = (idVeterinario == 0) ? null : idVeterinario;

            String est = (estadoCita.equals("null") || estadoCita.trim().isEmpty()) ? "" : estadoCita.trim();

            LocalDate fecha = (fechaStr.equals("null") || fechaStr.isEmpty()) ? null : LocalDate.parse(fechaStr);

            String texto = (textoBusqueda.equals("null")) ? "" : textoBusqueda.trim();

            atenciones = atencionBO.listarFiltradas(idVet, est, fecha, texto);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return atenciones;
    }

    /*ok*/
    @GET
    @Path("listarUltimas/{idVeterinario}/{limite}")
    public List<Atencion> listarUltimasPorVeterinario(
            @PathParam("idVeterinario") int idVeterinario,
            @PathParam("limite") int limite) {
        List<Atencion> atenciones = new ArrayList<>();
        try {
            atenciones = atencionBO.listarUltimasPorVeterinario(idVeterinario, limite);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return atenciones;
    }

    /*ok*/
    @GET
    @Path("contarPorVeterinario/{idVeterinario}/{anio}/{mes}")
    public int contarPorVeterinarioEnMes(
            @PathParam("idVeterinario") int idVeterinario,
            @PathParam("anio") int anio,
            @PathParam("mes") int mes) {
        int cantidad = 0;
        try {
            cantidad = atencionBO.contarPorVeterinarioEnMes(idVeterinario, anio, mes);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return cantidad;
    }

    /*ok*/
    @GET
    @Path("sumarMontosNetos/{anio}/{mes}")
    public double sumarMontosNetosPorMes(
            @PathParam("anio") int anio,
            @PathParam("mes") int mes) {
        double total = 0.0;
        try {
            total = atencionBO.sumarMontosNetosPorMes(anio, mes);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return total;
    }

    /*ok*/
    @GET
    @Path("topServicios/{idVeterinario}/{anio}/{mes}/{limite}")
    public List<ServicioAtencionResumen> topServiciosPorVeterinario(
            @PathParam("idVeterinario") int idVeterinario,
            @PathParam("anio") int anio,
            @PathParam("mes") int mes,
            @PathParam("limite") int limite) {
        List<ServicioAtencionResumen> resumen = new ArrayList<>();
        try {
            resumen = atencionBO.topServiciosPorVeterinario(idVeterinario, anio, mes, limite);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resumen;
    }

    /*ok*/
    @GET
    @Path("topVeterinarios/{anio}/{mes}/{limite}")
    public List<VeterinarioAtencionResumen> topVeterinariosPorAtenciones(
            @PathParam("anio") int anio,
            @PathParam("mes") int mes,
            @PathParam("limite") int limite) {
        List<VeterinarioAtencionResumen> resumen = new ArrayList<>();
        try {
            resumen = atencionBO.topVeterinariosPorAtenciones(anio, mes, limite);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resumen;
    }

}
