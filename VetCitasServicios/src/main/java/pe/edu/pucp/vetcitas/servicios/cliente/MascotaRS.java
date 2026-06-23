package pe.edu.pucp.vetcitas.servicios.cliente;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.edu.pucp.vetcitas.cliente.bo.MascotaBOImpl;
import pe.edu.pucp.vetcitas.cliente.boi.IMascotaBO;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;

import java.util.ArrayList;
import java.util.List;

@Path("MascotaRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MascotaRS {

    private IMascotaBO mascotaBO;

    public MascotaRS() {
        mascotaBO = new MascotaBOImpl();
    }

    @POST
    @Path("insertar")
    public int insertar(Mascota mascota) {
        int resultado = 0;
        try {
            resultado = mascotaBO.insertar(mascota);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @PUT
    @Path("modificar")
    public int modificar(Mascota mascota) {
        int resultado = 0;
        try {
            resultado = mascotaBO.modificar(mascota);
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
            resultado = mascotaBO.eliminar(id, modifiedBy);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @GET
    @Path("listarTodos")
    public List<Mascota> listarTodos() {
        List<Mascota> mascotas = new ArrayList<>();
        try {
            mascotas = mascotaBO.listarTodos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return mascotas;
    }

    @GET
    @Path("buscarPorId/{id}")
    public Mascota buscarPorId(@PathParam("id") int id) {
        Mascota mascota = null;
        try {
            mascota = mascotaBO.buscarPorId(id);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return mascota;
    }

    @GET
    @Path("buscarPorTexto/{texto}")
    public List<Mascota> listarPorNombreODueno(@PathParam("texto") String texto) {
        List<Mascota> mascotas = new ArrayList<>();
        try {
            // Manejo de cadena nula explícita desde la URL
            String busqueda = (texto.equals("null")) ? "" : texto;
            mascotas = mascotaBO.listarPorNombreODueno(busqueda);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return mascotas;
    }

    @GET
    @Path("listarPorCliente/{idCliente}")
    public List<Mascota> listarPorCliente(@PathParam("idCliente") int idCliente) {
        List<Mascota> mascotas = new ArrayList<>();
        try {
            mascotas = mascotaBO.listarPorCliente(idCliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return mascotas;
    }

    @GET
    @Path("contarActivas")
    public int contarActivas() {
        int cantidad = 0;
        try {
            cantidad = mascotaBO.contarActivas();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return cantidad;
    }

}
