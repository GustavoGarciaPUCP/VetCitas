package pe.edu.pucp.vetcitas.main;

import pe.edu.pucp.vetcitas.cita.dao.IRecordatorioDAO;
import pe.edu.pucp.vetcitas.cita.impl.RecordatorioImpl;
import pe.edu.pucp.vetcitas.cita.model.Cita;
import pe.edu.pucp.vetcitas.cita.model.Recordatorio;
import pe.edu.pucp.vetcitas.cliente.dao.ClienteDAO;
import pe.edu.pucp.vetcitas.cliente.dao.MascotaDAO;
import pe.edu.pucp.vetcitas.cliente.impl.ClienteImpl;
import pe.edu.pucp.vetcitas.cliente.impl.MascotaImpl;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;
import pe.edu.pucp.vetcitas.common.enums.CanalRecordatorio;
import pe.edu.pucp.vetcitas.common.enums.EstadoSeguimiento;
import pe.edu.pucp.vetcitas.common.enums.Rol;
import pe.edu.pucp.vetcitas.usuario.dao.IVeterinarioDAO;
import pe.edu.pucp.vetcitas.usuario.impl.VeterinarioImpl;
import pe.edu.pucp.vetcitas.usuario.model.Administrador;
import pe.edu.pucp.vetcitas.usuario.model.Veterinario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class Principal {
    public static void main(String[] args) {
        System.out.println("=== INICIO DE PRUEBAS DEL MODELO VETCITAS ===");

        // Sufijo unico para evitar conflictos con UNIQUE constraints al re-ejecutar
        String sufijo = String.valueOf(System.currentTimeMillis() % 100000);

        // ============================================================
        // PRUEBA CLIENTE: Insertar cliente para asociar a mascota
        // ============================================================
        System.out.println("\n=== PRUEBA CLIENTE: INSERTAR CLIENTE ===");
        ClienteDAO clienteDAO = new ClienteImpl();

        Cliente clienteNuevo = new Cliente();
        clienteNuevo.setNombres("Juan");
        clienteNuevo.setApellidos("Martinez Silva");
        clienteNuevo.setTelefono("951753852");
        clienteNuevo.setObservaciones("Cliente de prueba");
        clienteNuevo.setActivo(true);
        clienteNuevo.setCreatedOn(LocalDateTime.now());
        clienteNuevo.setModifiedOn(LocalDateTime.now());

        int idCliente = clienteDAO.insertar(clienteNuevo);
        if (idCliente > 0) {
            System.out.println("Cliente insertado con ID: " + clienteNuevo.getId());
        } else {
            System.out.println("Fallo la insercion del cliente.");
        }

        // Listar clientes
        System.out.println("\nListando clientes activos:");
        List<Cliente> listaClientes = clienteDAO.listarTodas();
        if (listaClientes != null && !listaClientes.isEmpty()) {
            for (Cliente c : listaClientes) {
                System.out.println("ID: " + c.getId() + " | " + c.getNombres()
                        + " " + c.getApellidos() + " | Tel: " + c.getTelefono());
            }
        } else {
            System.out.println("No se encontraron clientes.");
        }

        // ============================================================
        // PRUEBA MASCOTA: Ahora usando el cliente recien creado
        // ============================================================
        System.out.println("\n=== PRUEBA MASCOTA: INSERTAR MASCOTA ===");
        MascotaDAO mascotaDAO = new MascotaImpl();

        Mascota mascotaPrueba = new Mascota();
        mascotaPrueba.setNombre("Oreo");
        mascotaPrueba.setEspecie("Gato");
        mascotaPrueba.setRaza("Carey");
        mascotaPrueba.setFechaNacimiento(LocalDate.of(2023, 2, 14));
        mascotaPrueba.setEsterilizado(true);
        mascotaPrueba.setActivo(true);
        mascotaPrueba.setCliente(clienteNuevo); // Asociado al cliente recien insertado
        mascotaPrueba.setCreatedOn(LocalDateTime.now());
        mascotaPrueba.setModifiedOn(LocalDateTime.now());

        int resultadoInsertar = mascotaDAO.insertar(mascotaPrueba);
        if (resultadoInsertar > 0) {
            System.out.println("Mascota insertada. ID generado: " + mascotaPrueba.getId());
        } else {
            System.out.println("Fallo la insercion de la mascota.");
        }

        System.out.println("\nListando mascotas desde la BD:");
        List<Mascota> listaBD = mascotaDAO.listarTodas();
        if (listaBD != null && !listaBD.isEmpty()) {
            for (Mascota m : listaBD) {
                System.out.println("ID BD: " + m.getId() + " | Nombre: " + m.getNombre() + " | Especie: " + m.getEspecie());
            }
        } else {
            System.out.println("La lista esta vacia.");
        }

        // ============================================================
        // PRUEBA 1: DAO VETERINARIO
        // ============================================================
        System.out.println("\n=== PRUEBA 1: INSERTAR VETERINARIO ===");
        IVeterinarioDAO vetDAO = new VeterinarioImpl();

        Veterinario vet = new Veterinario();
        vet.setUsername("drperez" + sufijo);
        vet.setContrasenaHash("hash123");
        vet.setNombres("Carlos");
        vet.setApellidos("Perez Lopez");
        vet.setTelefono("987654321");
        vet.setRol(Rol.VETERINARIO);
        vet.setCmpv("CMPV-" + sufijo);
        vet.setEspecialidad("Cirugia");

        int idVet = vetDAO.insertar(vet);
        if (idVet > 0) {
            System.out.println("Veterinario insertado con ID: " + idVet);
        } else {
            System.out.println("Fallo la insercion del veterinario.");
        }

        // Insertar un segundo veterinario para tener mas datos
        Veterinario vet2 = new Veterinario();
        vet2.setUsername("dragomez" + sufijo);
        vet2.setContrasenaHash("hash456");
        vet2.setNombres("Ana");
        vet2.setApellidos("Gomez Ruiz");
        vet2.setTelefono("912345678");
        vet2.setRol(Rol.VETERINARIO);
        vet2.setCmpv("CMPV2-" + sufijo);
        vet2.setEspecialidad("Dermatologia");

        int idVet2 = vetDAO.insertar(vet2);
        System.out.println("Segundo veterinario insertado con ID: " + idVet2);

        // Listar veterinarios
        System.out.println("\n=== PRUEBA 2: LISTAR VETERINARIOS ===");
        List<Veterinario> listaVets = vetDAO.listarTodas();
        if (listaVets != null) {
            for (Veterinario v : listaVets) {
                System.out.println("ID: " + v.getId() + " | " + v.getNombres()
                        + " " + v.getApellidos() + " | CMPV: " + v.getCmpv()
                        + " | Esp: " + v.getEspecialidad());
            }
        } else {
            System.out.println("No se encontraron veterinarios.");
        }

        // Buscar por ID
        System.out.println("\n=== PRUEBA 3: BUSCAR VETERINARIO POR ID ===");
        Veterinario vetBuscado = vetDAO.buscarPorId(idVet);
        if (vetBuscado != null) {
            System.out.println("Encontrado: " + vetBuscado.getNombres() + " "
                    + vetBuscado.getApellidos() + " | Username: "
                    + vetBuscado.getUsername() + " | CMPV: " + vetBuscado.getCmpv());
        } else {
            System.out.println("No se encontro el veterinario con ID: " + idVet);
        }

        // Modificar veterinario
        System.out.println("\n=== PRUEBA 4: MODIFICAR VETERINARIO ===");
        vet.setEspecialidad("Traumatologia");
        vet.setTelefono("999888777");
        int resModificar = vetDAO.modificar(vet);
        System.out.println("Resultado modificar: " + resModificar);
        Veterinario vetModificado = vetDAO.buscarPorId(idVet);
        if (vetModificado != null) {
            System.out.println("Especialidad actualizada: " + vetModificado.getEspecialidad()
                    + " | Telefono: " + vetModificado.getTelefono());
        }

        // Eliminar veterinario (soft delete)
        System.out.println("\n=== PRUEBA 5: ELIMINAR VETERINARIO (SOFT DELETE) ===");
        int resEliminar = vetDAO.eliminar(idVet2);
        System.out.println("Resultado eliminar vet2: " + resEliminar);
        Veterinario vetEliminado = vetDAO.buscarPorId(idVet2);
        if (vetEliminado != null) {
            System.out.println("Vet2 activo? " + vetEliminado.isActivo());
        }

        // ============================================================
        // PRUEBA 6: DAO RECORDATORIO
        // Para recordatorio se necesita una cita existente en la BD.
        // Asegurate de tener al menos una cita insertada con id_cita = 1
        // ============================================================
        System.out.println("\n=== PRUEBA 6: INSERTAR RECORDATORIO ===");
        IRecordatorioDAO recDAO = new RecordatorioImpl();

        Cita citaPrueba = new Cita();
        citaPrueba.setId(1); // ID de una cita existente en la BD

        Recordatorio rec = new Recordatorio();
        rec.setFechaProgramada(LocalDateTime.now().plusDays(1));
        rec.setCanal(CanalRecordatorio.WHATSAPP);
        rec.setEstadoSeguimiento(EstadoSeguimiento.PENDIENTE);
        rec.setMensaje("Recordatorio: su cita es manana a las 10am");
        rec.setCita(citaPrueba);

        int idRec = recDAO.insertar(rec);
        if (idRec > 0) {
            System.out.println("Recordatorio insertado con ID: " + idRec);
        } else {
            System.out.println("Fallo la insercion del recordatorio. Verifica que exista la cita con ID 1.");
        }

        // Listar recordatorios
        System.out.println("\n=== PRUEBA 7: LISTAR RECORDATORIOS ===");
        List<Recordatorio> listaRecs = recDAO.listarTodas();
        if (listaRecs != null) {
            for (Recordatorio r : listaRecs) {
                System.out.println("ID: " + r.getId() + " | Fecha: " + r.getFechaProgramada()
                        + " | Canal: " + r.getCanal() + " | Estado: " + r.getEstadoSeguimiento()
                        + " | Cita ID: " + r.getCita().getId());
            }
        } else {
            System.out.println("No se encontraron recordatorios.");
        }

        // Buscar por ID
        System.out.println("\n=== PRUEBA 8: BUSCAR RECORDATORIO POR ID ===");
        Recordatorio recBuscado = recDAO.buscarPorId(idRec);
        if (recBuscado != null) {
            System.out.println("Encontrado: ID " + recBuscado.getId()
                    + " | Mensaje: " + recBuscado.getMensaje()
                    + " | Estado: " + recBuscado.getEstadoSeguimiento());
        } else {
            System.out.println("No se encontro el recordatorio con ID: " + idRec);
        }

        // Modificar recordatorio
        System.out.println("\n=== PRUEBA 9: MODIFICAR RECORDATORIO ===");
        rec.setEstadoSeguimiento(EstadoSeguimiento.ENVIADO);
        rec.setMensaje("Recordatorio actualizado: cita confirmada para manana");
        int resModRec = recDAO.modificar(rec);
        System.out.println("Resultado modificar recordatorio: " + resModRec);
        Recordatorio recModificado = recDAO.buscarPorId(idRec);
        if (recModificado != null) {
            System.out.println("Estado actualizado: " + recModificado.getEstadoSeguimiento()
                    + " | Mensaje: " + recModificado.getMensaje());
        }

        // Eliminar recordatorio (delete fisico)
        System.out.println("\n=== PRUEBA 10: ELIMINAR RECORDATORIO ===");
        int resElimRec = recDAO.eliminar(idRec);
        System.out.println("Resultado eliminar recordatorio: " + resElimRec);
        Recordatorio recEliminado = recDAO.buscarPorId(idRec);
        System.out.println("Buscar despues de eliminar: " + (recEliminado == null ? "No encontrado (OK)" : "Aun existe"));

        System.out.println("\n=== FIN DE PRUEBAS ===");
    }
}
