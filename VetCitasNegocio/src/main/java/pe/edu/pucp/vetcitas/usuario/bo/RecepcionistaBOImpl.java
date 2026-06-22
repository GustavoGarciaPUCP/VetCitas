package pe.edu.pucp.vetcitas.usuario.bo;

import pe.edu.pucp.vetcitas.usuario.boi.IRecepcionistaBO;
import pe.edu.pucp.vetcitas.usuario.dao.IAdministradorDAO;
import pe.edu.pucp.vetcitas.usuario.dao.IRecepcionistaDAO;
import pe.edu.pucp.vetcitas.usuario.impl.AdministradorImpl;
import pe.edu.pucp.vetcitas.usuario.impl.RecepcionistaImpl;
import pe.edu.pucp.vetcitas.usuario.model.Recepcionista;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.List;

public class RecepcionistaBOImpl implements IRecepcionistaBO {
    private IRecepcionistaDAO recepcionistaDAO;
    private IAdministradorDAO usuarioDAO;

    public RecepcionistaBOImpl() {
        this.recepcionistaDAO = new RecepcionistaImpl();
        this.usuarioDAO = new AdministradorImpl();
    }

    @Override
    public int insertar(Recepcionista recepcionista) throws Exception {
        validar(recepcionista, false);
        if (usuarioDAO.existeUsername(recepcionista.getUsername(), null)) {
            throw new Exception("El username '" + recepcionista.getUsername() + "' ya esta en uso.");
        }
        recepcionista.setContrasenaHash(hashSiNoEstaHasheada(recepcionista.getContrasenaHash()));
        return recepcionistaDAO.insertar(recepcionista);
    }

    @Override
    public int modificar(Recepcionista recepcionista) throws Exception {
        validar(recepcionista, true);
        if (usuarioDAO.existeUsername(recepcionista.getUsername(), recepcionista.getId())) {
            throw new Exception("El username '" + recepcionista.getUsername() + "' ya esta ocupado por otro usuario.");
        }
        recepcionista.setContrasenaHash(hashSiNoEstaHasheada(recepcionista.getContrasenaHash()));
        return recepcionistaDAO.modificar(recepcionista);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del recepcionista debe ser mayor que cero.");
        }
        return recepcionistaDAO.eliminar(id);
    }

    @Override
    public List<Recepcionista> listarTodos() throws Exception {
        return recepcionistaDAO.listarTodas();
    }

    @Override
    public Recepcionista buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del recepcionista debe ser mayor que cero.");
        }
        Recepcionista rec = recepcionistaDAO.buscarPorId(id);
        if (rec == null) {
            throw new Exception("El recepcionista con id " + id + " no existe.");
        }
        return rec;
    }

    private void validar(Recepcionista rec, boolean esModificacion) throws Exception {
        if (rec == null) {
            throw new Exception("El recepcionista no puede ser nulo.");
        }
        if (esModificacion && rec.getId() <= 0) {
            throw new Exception("El id del recepcionista es obligatorio para la modificación.");
        }
        if (rec.getUsername() == null || rec.getUsername().trim().isEmpty()) {
            throw new Exception("El nombre de usuario es obligatorio.");
        }
        if (rec.getContrasenaHash() == null || rec.getContrasenaHash().trim().isEmpty()) {
            throw new Exception("La contraseña es obligatoria.");
        }
        if (rec.getNombres() == null || rec.getNombres().trim().isEmpty()) {
            throw new Exception("Los nombres son obligatorios.");
        }
        if (rec.getApellidos() == null || rec.getApellidos().trim().isEmpty()) {
            throw new Exception("Los apellidos son obligatorios.");
        }
        if (rec.getArea() == null || rec.getArea().trim().isEmpty()) {
            throw new Exception("El área es obligatoria.");
        }
        validarEmailObligatorio(rec.getEmail());
        rec.setEmail(rec.getEmail().trim());
    }

    private void validarEmailObligatorio(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("El email es obligatorio.");
        }

        email = email.trim();

        if (email.length() > 150) {
            throw new Exception("El email no puede superar los 150 caracteres.");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new Exception("El email no tiene un formato válido.");
        }
    }

    private String hashSiNoEstaHasheada(String contrasena) throws Exception {
        if (contrasena == null || contrasena.trim().isEmpty()) {
            throw new Exception("La contraseña es obligatoria.");
        }

        contrasena = contrasena.trim();

        if (esSha256(contrasena)) {
            return contrasena.toLowerCase();
        }

        return generarSha256(contrasena);
    }

    private boolean esSha256(String texto) {
        return texto != null && texto.matches("^[0-9a-fA-F]{64}$");
    }

    private String generarSha256(String texto) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(texto.getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder();

        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }


}
