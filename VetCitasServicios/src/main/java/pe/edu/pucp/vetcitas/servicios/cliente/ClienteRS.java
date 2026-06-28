package pe.edu.pucp.vetcitas.servicios.cliente;


import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import pe.edu.pucp.vetcitas.cliente.bo.ClienteBOImpl;
import pe.edu.pucp.vetcitas.cliente.boi.IClienteBO;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;

import java.util.ArrayList;
import java.util.List;

@Path("ClienteRS")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ClienteRS {

    private IClienteBO clienteBO;

    public ClienteRS() {
        clienteBO = new ClienteBOImpl();
    }

    @POST
    @Path("insertar")
    public int insertar(Cliente cliente) {
        int resultado = 0;
        try {
            resultado = clienteBO.insertar(cliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @POST
    @Path("insertarConMascotas")
    public int insertarConMascotas(Cliente cliente) {
        int resultado = 0;
        try {
            resultado = clienteBO.insertarConMascotas(cliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @PUT
    @Path("modificar")
    public int modificar(Cliente cliente) {
        int resultado = 0;
        try {
            resultado = clienteBO.modificar(cliente);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @PUT
    @Path("modificarConMascotas")
    public int modificarConMascotas(Cliente cliente) {
        int resultado = 0;
        try {
            resultado = clienteBO.modificarConMascotas(cliente);
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
            resultado = clienteBO.eliminar(id, modifiedBy);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return resultado;
    }

    @GET
    @Path("listarTodos")
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        try {
            clientes = clienteBO.listarTodos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return clientes;
    }

    @GET
    @Path("buscarPorId/{id}")
    public Cliente buscarPorId(@PathParam("id") int id) {
        Cliente cliente = null;
        try {
            cliente = clienteBO.buscarPorId(id);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return cliente;
    }

    @GET
    @Path("buscarPorTexto/{texto}")
    public List<Cliente> listarPorNombreApellidoODni(@PathParam("texto") String texto) {
        List<Cliente> clientes = new ArrayList<>();
        try {
            // Si mandamos la palabra "null" explícita en la URL, lo interpretamos como cadena vacía
            String busqueda = (texto.equals("null")) ? "" : texto;
            clientes = clienteBO.listarPorNombreApellidoODni(busqueda);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return clientes;
    }

    @GET
    @Path("contarActivos")
    public int contarActivos() {
        int cantidad = 0;
        try {
            cantidad = clienteBO.contarActivos();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return cantidad;
    }

    @GET
    @Path("contarNuevos/{anio}/{mes}")
    public int contarNuevosEnMes(
            @PathParam("anio") int anio,
            @PathParam("mes") int mes) {
        int cantidad = 0;
        try {
            cantidad = clienteBO.contarNuevosEnMes(anio, mes);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return cantidad;
    }

}
