package pe.edu.pucp.vetcitas.main;

import pe.edu.pucp.vetcitas.cita.dao.ICitaDAO;
import pe.edu.pucp.vetcitas.cita.impl.CitaImpl;
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
import pe.edu.pucp.vetcitas.common.enums.EstadoCita;
import pe.edu.pucp.vetcitas.common.enums.EstadoSeguimiento;
import pe.edu.pucp.vetcitas.common.enums.Rol;
import pe.edu.pucp.vetcitas.usuario.dao.IVeterinarioDAO;
import pe.edu.pucp.vetcitas.usuario.impl.VeterinarioImpl;
import pe.edu.pucp.vetcitas.usuario.model.Administrador;
import pe.edu.pucp.vetcitas.usuario.model.Veterinario;
import pe.edu.pucp.vetcitas.servicio.dao.IServicioDAO;
import pe.edu.pucp.vetcitas.servicio.impl.ServicioImpl;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;
import pe.edu.pucp.vetcitas.common.enums.TipoServicio;
import pe.edu.pucp.vetcitas.cita.dao.IAtencionDAO;
import pe.edu.pucp.vetcitas.cita.impl.AtencionImpl;
import pe.edu.pucp.vetcitas.cita.model.Atencion;

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

        // =========================================================
        // PRUEBAS: DAO SERVICIO
        // =========================================================
        System.out.println("==============================================");
        System.out.println("INICIANDO PRUEBAS DE SERVICIO");
        System.out.println("==============================================");

        IServicioDAO daoServicio = new ServicioImpl();

        // 1. Insertar Servicio
        System.out.println("\n1. Probando Inserción de Servicio...");
        Servicio nuevoServicio = new Servicio();
        nuevoServicio.setNombre("Baño y Corte de Pelo Premium");
        nuevoServicio.setTipoServicio(TipoServicio.NO_CLINICA);
        nuevoServicio.setDuracionMinutos(60);
        nuevoServicio.setPrecioReferencial(85.50);

        int resultadoServicio = daoServicio.insertar(nuevoServicio);
        if (resultadoServicio != 0) {
            System.out.println("EXITO: Servicio insertado con ID: " + resultadoServicio);
            nuevoServicio.setId(resultadoServicio); // Guardamos el ID para las siguientes pruebas
        } else {
            System.out.println("ERROR: No se pudo insertar el servicio.");
        }

        // 2. Listar todos los servicios
        System.out.println("\n2. Probando Listado de Servicios...");
        List<Servicio> listaServicios = daoServicio.listarTodas();
        for (Servicio s : listaServicios) {
            System.out.println("ID: " + s.getId() + " | Nombre: " + s.getNombre() + " | Precio: S/." + s.getPrecioReferencial() + " | Activo: " + s.isActivo());
        }

        // 3. Modificar Servicio
        System.out.println("\n3. Probando Modificación de Servicio...");
        nuevoServicio.setPrecioReferencial(95.00); // Subimos el precio
        nuevoServicio.setNombre("Baño y Corte Premium (Actualizado)");
        int modificado = daoServicio.modificar(nuevoServicio);
        System.out.println(modificado > 0 ? "EXITO: Servicio modificado." : "ERROR al modificar.");

        // 4. Deshabilitar Servicio (Borrado lógico)
        System.out.println("\n4. Probando Deshabilitar Servicio...");
        int deshabilitado = daoServicio.deshabilitar(nuevoServicio.getId());
        System.out.println(deshabilitado > 0 ? "EXITO: Servicio deshabilitado." : "ERROR al deshabilitar.");

        // 5. Buscar por ID para comprobar cambios
        System.out.println("\n5. Comprobando cambios con Buscar por ID...");
        Servicio servicioBuscado = daoServicio.buscarPorId(nuevoServicio.getId());
        if (servicioBuscado != null) {
            System.out.println("Servicio encontrado: " + servicioBuscado.getNombre() + " | Activo: " + servicioBuscado.isActivo() + " | Precio: " + servicioBuscado.getPrecioReferencial());
        }

        // =========================================================
        // PRUEBAS: CITA
        // =========================================================
        System.out.println("\n==============================================");
        System.out.println("INICIANDO PRUEBAS DE CITA [CREACION]");
        System.out.println("==============================================");

        ICitaDAO daoCita = new CitaImpl();
        Cita citaPruebaGenerada = new Cita();
        citaPruebaGenerada.setFechaHoraInicio(LocalDateTime.now().plusDays(1));
        citaPruebaGenerada.setFechaHoraFin(LocalDateTime.now().plusDays(1).plusHours(1));
        citaPruebaGenerada.setEstado(EstadoCita.PENDIENTE);

        Mascota mascotaCita = new Mascota();
        mascotaCita.setId(mascotaPrueba.getId());

        Veterinario vetCita = new Veterinario();
        vetCita.setId(idVet);

        Servicio servicioCita = new Servicio();
        servicioCita.setId(nuevoServicio.getId());

        citaPruebaGenerada.setMascota(mascotaCita);
        citaPruebaGenerada.setVeterinario(vetCita);
        citaPruebaGenerada.setServicio(servicioCita);

        int idCitaGenerada = daoCita.insertar(citaPruebaGenerada);
        if (idCitaGenerada > 0) {
            System.out.println("EXITO: Cita de prueba generada con ID: " + idCitaGenerada);
        } else {
            System.out.println("ERROR: No se pudo crear la cita.");
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


        // =========================================================
        // PRUEBAS: ATENCION
        // =========================================================
        System.out.println("\n==============================================");
        System.out.println("INICIANDO PRUEBAS DE ATENCION");
        System.out.println("==============================================");

        IAtencionDAO daoAtencion = new AtencionImpl();

        // Cambiar este número por un ID de Cita existente
        int idCitaExistente = 1;

        // 1. Insertar Atención
        System.out.println("\n1. Probando Inserción de Atención...");
        Atencion nuevaAtencion = new Atencion();
        nuevaAtencion.setFechaHora(LocalDateTime.now());
        nuevaAtencion.setNotaClinica("El paciente presenta leve irritación en la piel. Se aplicó crema.");
        nuevaAtencion.setNotaPreOperatoria("");
        nuevaAtencion.setNotaPostOperatoria("");
        nuevaAtencion.setRecomendacionControl("Evitar que se lama la herida por 3 días.");
        nuevaAtencion.setMontoReferencial(150.00);
        nuevaAtencion.setDescuentoAplicado(10.00); // S/. 10 de descuento

        Cita citaAsociada = new Cita();
        citaAsociada.setId(idCitaExistente);
        nuevaAtencion.setCita(citaAsociada);

        int resultadoAtencion = daoAtencion.insertar(nuevaAtencion);
        if (resultadoAtencion != 0) {
            System.out.println("EXITO: Atención insertada con ID: " + resultadoAtencion);
            nuevaAtencion.setId(resultadoAtencion);
        } else {
            System.out.println("ERROR: No se pudo insertar la atención (Verifica que la cita con ID " + idCitaExistente + " exista).");
        }

        // 2. Buscar Atención por ID de Cita
        System.out.println("\n2. Probando Búsqueda de Atención por Cita...");
        Atencion atencionPorCita = daoAtencion.buscarPorCita(idCitaExistente);
        if (atencionPorCita != null) {
            System.out.println("Atención encontrada para la Cita " + idCitaExistente + ": " + atencionPorCita.getNotaClinica());
        } else {
            System.out.println("No se encontró atención para esa cita.");
        }

        // 3. Modificar Atención
        System.out.println("\n3. Probando Modificación de Atención...");
        if (nuevaAtencion.getId() != 0) {
            nuevaAtencion.setNotaClinica("El paciente presenta leve irritación en la piel. Se aplicó crema. Actualización: Se recetan pastillas.");
            int atencionModificada = daoAtencion.modificar(nuevaAtencion);
            System.out.println(atencionModificada > 0 ? "EXITO: Atención modificada." : "ERROR al modificar atención.");
        }

        System.out.println("\n==============================================");
        System.out.println("FIN DE LAS PRUEBAS");
        System.out.println("==============================================");
    }
}
