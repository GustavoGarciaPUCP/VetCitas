package pe.edu.pucp.vetcitas.cliente.bo;

import pe.edu.pucp.vetcitas.cliente.boi.IClienteBO;
import pe.edu.pucp.vetcitas.cliente.dao.ClienteDAO;
import pe.edu.pucp.vetcitas.cliente.impl.ClienteImpl;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;

import java.util.List;

public class ClienteBOImpl implements IClienteBO {
    private ClienteDAO clienteDAO;

    public ClienteBOImpl() {
        this.clienteDAO = new ClienteImpl();
    }

    @Override
    public int insertar(Cliente cliente) throws Exception {
        validar(cliente, false);
        return clienteDAO.insertar(cliente);
    }

    @Override
    public int modificar(Cliente cliente) throws Exception {
        validar(cliente, true);
        return clienteDAO.modificar(cliente);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del cliente debe ser mayor que cero.");
        }
        return clienteDAO.eliminar(id);
    }

    @Override
    public List<Cliente> listarTodos() throws Exception {
        return clienteDAO.listarTodas();
    }

    @Override
    public Cliente buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del cliente debe ser mayor que cero.");
        }
        Cliente cliente = clienteDAO.buscarPorId(id);
        if (cliente == null) {
            throw new Exception("El cliente con id " + id + " no existe.");
        }
        return cliente;
    }

    @Override
    public List<Cliente> listarPorNombreApellidoODni(String texto) throws Exception {
        if (texto == null) texto = "";
        texto = texto.trim();
        return clienteDAO.listarPorNombreApellidoODni(texto);
    }


    private void validar(Cliente cliente, boolean esModificacion) throws Exception {
        if (cliente == null) {
            throw new Exception("El cliente no puede ser nulo.");
        }
        if (esModificacion && cliente.getId() <= 0) {
            throw new Exception("El id del cliente es obligatorio para la modificación.");
        }
        if (cliente.getDni() == null || cliente.getDni().trim().isEmpty()) {
            throw new Exception("El DNI es obligatorio.");
        }
        if (!cliente.getDni().matches("\\d{8}")) {
            throw new Exception("El DNI debe tener exactamente 8 dígitos.");
        }
        if (cliente.getNombres() == null || cliente.getNombres().trim().isEmpty()) {
            throw new Exception("Los nombres son obligatorios.");
        }
        if (cliente.getApellidos() == null || cliente.getApellidos().trim().isEmpty()) {
            throw new Exception("Los apellidos son obligatorios.");
        }
        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {
            throw new Exception("El teléfono es obligatorio.");
        }
        if (!cliente.getTelefono().matches("\\d{6,15}")) {
            throw new Exception("El teléfono debe contener solo dígitos (entre 6 y 15).");
        }
        if (cliente.getEmail() != null && !cliente.getEmail().trim().isEmpty()) {
            String email = cliente.getEmail().trim();

            if (email.length() > 70) {
                throw new Exception("El email no puede superar los 70 caracteres.");
            }

            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                throw new Exception("El email no tiene un formato válido.");
            }

            cliente.setEmail(email);
        }
    }
}
