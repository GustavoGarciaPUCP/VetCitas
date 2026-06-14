package pe.edu.pucp.vetcitas.servicios.cita;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.edu.pucp.vetcitas.cita.bo.RecordatorioBOImpl;
import pe.edu.pucp.vetcitas.cita.boi.IRecordatorioBO;
import pe.edu.pucp.vetcitas.cita.model.Recordatorio;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Path("RecordatorioRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecordatorioRS {

    private IRecordatorioBO recordatorioBO;

    public RecordatorioRS() {
        recordatorioBO = new RecordatorioBOImpl();
    }

    @POST
    @Path("insertar")
    public int insertar(Recordatorio recordatorio) {
        int resultado = 0;
        try {
            resultado = recordatorioBO.insertar(recordatorio);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @PUT
    @Path("modificar")
    public int modificar(Recordatorio recordatorio) {
        int resultado = 0;
        try {
            resultado = recordatorioBO.modificar(recordatorio);
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
            resultado = recordatorioBO.eliminar(id);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @GET
    @Path("listarTodos")
    public List<Recordatorio> listarTodos() {
        List<Recordatorio> recordatorios = new ArrayList<>();
        try {
            recordatorios = recordatorioBO.listarTodos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return recordatorios;
    }

    @GET
    @Path("buscarPorId/{id}")
    public Recordatorio buscarPorId(@PathParam("id") int id) {
        Recordatorio recordatorio = null;
        try {
            recordatorio = recordatorioBO.buscarPorId(id);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return recordatorio;
    }

    @GET
    @Path("listarPorMascotaCliente/{texto}")
    public List<Recordatorio> listarPorMascotaOCliente(@PathParam("texto") String texto) {
        List<Recordatorio> recordatorios = new ArrayList<>();
        try {
            String busqueda = (texto.equals("null")) ? "" : texto;
            recordatorios = recordatorioBO.listarPorMascotaOCliente(busqueda);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return recordatorios;
    }

    @GET
    @Path("listarPorEstadoFecha/{estado}/{fecha}")
    public List<Recordatorio> listarPorEstadoYFecha(
            @PathParam("estado") String estado,
            @PathParam("fecha") String fechaStr) {
        List<Recordatorio> recordatorios = new ArrayList<>();
        try {
            String est = (estado.equals("null") || estado.trim().isEmpty()) ? "" : estado;
            LocalDate fecha = (fechaStr.equals("null") || fechaStr.isEmpty()) ? null : LocalDate.parse(fechaStr);
            recordatorios = recordatorioBO.listarPorEstadoYFecha(est, fecha);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return recordatorios;
    }

    @PUT
    @Path("marcarEnviado/{idRecordatorio}/{modifiedBy}")
    public int marcarEnviado(
            @PathParam("idRecordatorio") int idRecordatorio,
            @PathParam("modifiedBy") int modifiedBy) {
        try {
            recordatorioBO.marcarEnviado(idRecordatorio, modifiedBy);
            return 1; // Retorna 1 si la operación transaccional fue exitosa
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return 0;
        }
    }

    @GET
    @Path("contarPendientes")
    public int contarPendientes() {
        int cantidad = 0;
        try {
            cantidad = recordatorioBO.contarPendientes();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return cantidad;
    }

}
