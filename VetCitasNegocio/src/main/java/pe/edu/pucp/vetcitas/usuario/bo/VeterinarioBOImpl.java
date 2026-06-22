package pe.edu.pucp.vetcitas.usuario.bo;

import pe.edu.pucp.vetcitas.usuario.boi.IVeterinarioBO;
import pe.edu.pucp.vetcitas.usuario.dao.IAdministradorDAO;
import pe.edu.pucp.vetcitas.usuario.dao.IVeterinarioDAO;
import pe.edu.pucp.vetcitas.usuario.impl.AdministradorImpl;
import pe.edu.pucp.vetcitas.usuario.impl.VeterinarioImpl;
import pe.edu.pucp.vetcitas.usuario.model.Veterinario;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.List;

public class VeterinarioBOImpl implements IVeterinarioBO {
    private IVeterinarioDAO veterinarioDAO;
    private IAdministradorDAO usuarioDAO;

    public VeterinarioBOImpl() {
        this.veterinarioDAO = new VeterinarioImpl();
        this.usuarioDAO = new AdministradorImpl();
    }

    @Override
    public int insertar(Veterinario veterinario) throws Exception {
        validar(veterinario, false);
        if (usuarioDAO.existeUsername(veterinario.getUsername(), null)) {
            throw new Exception("El username '" + veterinario.getUsername() + "' ya esta en uso.");
        }
        veterinario.setContrasenaHash(hashSiNoEstaHasheada(veterinario.getContrasenaHash()));
        return veterinarioDAO.insertar(veterinario);
    }

    @Override
    public int modificar(Veterinario veterinario) throws Exception {
        validar(veterinario, true);
        if (usuarioDAO.existeUsername(veterinario.getUsername(), veterinario.getId())) {
            throw new Exception("El username '" + veterinario.getUsername() + "' ya esta ocupado por otro usuario.");
        }
        veterinario.setContrasenaHash(hashSiNoEstaHasheada(veterinario.getContrasenaHash()));
        return veterinarioDAO.modificar(veterinario);
    }

    @Override
    public int eliminar(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del veterinario debe ser mayor que cero.");
        }
        return veterinarioDAO.eliminar(id);
    }

    @Override
    public List<Veterinario> listarTodos() throws Exception {
        return veterinarioDAO.listarTodas();
    }

    @Override
    public Veterinario buscarPorId(int id) throws Exception {
        if (id <= 0) {
            throw new Exception("El id del veterinario debe ser mayor que cero.");
        }
        Veterinario vet = veterinarioDAO.buscarPorId(id);
        if (vet == null) {
            throw new Exception("El veterinario con id " + id + " no existe.");
        }
        return vet;
    }

    @Override
    public List<Veterinario> listarDisponibles(LocalDateTime fechaHoraInicio, int idServicio) throws Exception {
        if (fechaHoraInicio == null) {
            throw new Exception("La fecha y hora son obligatorias.");
        }
        if (idServicio <= 0) {
            throw new Exception("El id del servicio debe ser mayor que cero.");
        }
        return veterinarioDAO.listarDisponibles(fechaHoraInicio, idServicio);
    }

    private void validar(Veterinario vet, boolean esModificacion) throws Exception {
        if (vet == null) {
            throw new Exception("El veterinario no puede ser nulo.");
        }
        if (esModificacion && vet.getId() <= 0) {
            throw new Exception("El id del veterinario es obligatorio para la modificación.");
        }
        if (vet.getUsername() == null || vet.getUsername().trim().isEmpty()) {
            throw new Exception("El nombre de usuario es obligatorio.");
        }
        if (vet.getContrasenaHash() == null || vet.getContrasenaHash().trim().isEmpty()) {
            throw new Exception("La contraseña es obligatoria.");
        }
        if (vet.getNombres() == null || vet.getNombres().trim().isEmpty()) {
            throw new Exception("Los nombres son obligatorios.");
        }
        if (vet.getApellidos() == null || vet.getApellidos().trim().isEmpty()) {
            throw new Exception("Los apellidos son obligatorios.");
        }
        if (vet.getCmpv() == null || vet.getCmpv().trim().isEmpty()) {
            throw new Exception("El CMPV es obligatorio.");
        }
        if (vet.getEspecialidad() == null || vet.getEspecialidad().trim().isEmpty()) {
            throw new Exception("La especialidad es obligatoria.");
        }
        validarEmailObligatorio(vet.getEmail());
        vet.setEmail(vet.getEmail().trim());
    }
    private void validarEmailObligatorio(String email) throws Exception {
        if (email == null || email.trim().isEmpty()) {
            throw new Exception("El email es obligatorio.");
        }

        email = email.trim();

        if (email.length() > 100) {
            throw new Exception("El email no puede superar los 100 caracteres.");
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
