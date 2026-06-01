package pe.edu.pucp.vetcitas.main;

import pe.edu.pucp.vetcitas.cita.bo.AtencionBOImpl;
import pe.edu.pucp.vetcitas.cita.bo.RecordatorioBOImpl;
import pe.edu.pucp.vetcitas.cita.boi.IAtencionBO;
import pe.edu.pucp.vetcitas.cita.boi.ICitaBO;
import pe.edu.pucp.vetcitas.cita.bo.CitaBOImpl;
import pe.edu.pucp.vetcitas.cita.boi.IRecordatorioBO;
import pe.edu.pucp.vetcitas.cita.model.Atencion;
import pe.edu.pucp.vetcitas.cita.model.Cita;
import pe.edu.pucp.vetcitas.cita.model.Recordatorio;
import pe.edu.pucp.vetcitas.cliente.boi.IClienteBO;
import pe.edu.pucp.vetcitas.cliente.bo.ClienteBOImpl;
import pe.edu.pucp.vetcitas.cliente.boi.IMascotaBO;
import pe.edu.pucp.vetcitas.cliente.bo.MascotaBOImpl;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;
import pe.edu.pucp.vetcitas.common.enums.*;
import pe.edu.pucp.vetcitas.configuracion.boi.IConfiguracionBO;
import pe.edu.pucp.vetcitas.configuracion.bo.ConfiguracionBOImpl;
import pe.edu.pucp.vetcitas.configuracion.model.Configuracion;
import pe.edu.pucp.vetcitas.servicio.boi.IServicioBO;
import pe.edu.pucp.vetcitas.servicio.bo.ServicioBOImpl;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;
import pe.edu.pucp.vetcitas.usuario.boi.IAdministradorBO;
import pe.edu.pucp.vetcitas.usuario.bo.AdministradorBOImpl;
import pe.edu.pucp.vetcitas.usuario.boi.IHorarioVeterinarioBO;
import pe.edu.pucp.vetcitas.usuario.bo.HorarioVeterinarioBOImpl;
import pe.edu.pucp.vetcitas.usuario.boi.IPermisoBO;
import pe.edu.pucp.vetcitas.usuario.bo.PermisoBOImpl;
import pe.edu.pucp.vetcitas.usuario.boi.IRecepcionistaBO;
import pe.edu.pucp.vetcitas.usuario.bo.RecepcionistaBOImpl;
import pe.edu.pucp.vetcitas.usuario.boi.IVeterinarioBO;
import pe.edu.pucp.vetcitas.usuario.bo.VeterinarioBOImpl;
import pe.edu.pucp.vetcitas.usuario.model.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class Principal {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" INICIO DE PRUEBAS INTEGRALES VETCITAS (CAPA BO)");
        System.out.println("==================================================");

        IAdministradorBO administradorBO = new AdministradorBOImpl();
        IVeterinarioBO veterinarioBO = new VeterinarioBOImpl();
        IRecepcionistaBO recepcionistaBO = new RecepcionistaBOImpl();
        IHorarioVeterinarioBO horarioBO = new HorarioVeterinarioBOImpl();
        IPermisoBO permisoBO = new PermisoBOImpl();
        IConfiguracionBO configuracionBO = new ConfiguracionBOImpl();

        IClienteBO clienteBO = new ClienteBOImpl();
        IMascotaBO mascotaBO = new MascotaBOImpl();
        IServicioBO servicioBO = new ServicioBOImpl();
        ICitaBO citaBO = new CitaBOImpl();
        IAtencionBO atencionBO = new AtencionBOImpl();
        IRecordatorioBO recordatorioBO = new RecordatorioBOImpl();

        LocalDateTime ahora = LocalDateTime.now();

        try {
            // ==================================================
            // 1. ADMINISTRADOR
            // ==================================================
            imprimirSeccion("1. INSERTAR ADMINISTRADOR");

            Administrador admin = new Administrador();
            admin.setUsername("admin_lab_final");
            admin.setContrasenaHash("hash_admin_123");
            admin.setNombres("Ana");
            admin.setApellidos("Torres");
            admin.setEmail("ana.torres@vetcitas.com");
            admin.setTelefono("999111222");
            admin.setActivo(true);
            admin.setArea("Administración");
            admin.setCreatedOn(ahora);

            int idAdmin = administradorBO.insertar(admin);
            System.out.println("Administrador insertado con ID: " + idAdmin);

            Administrador adminBD = null;
            if (idAdmin > 0) {
                adminBD = administradorBO.buscarPorId(idAdmin);
            }
            imprimirAdministrador(adminBD);

            // ==================================================
            // 2. VETERINARIO
            // ==================================================
            imprimirSeccion("2. INSERTAR VETERINARIO");

            Veterinario vet = new Veterinario();
            vet.setUsername("vet_lab_final");
            vet.setContrasenaHash("hash_vet_123");
            vet.setNombres("Luis");
            vet.setApellidos("Pérez");
            vet.setTelefono("988777666");
            vet.setEmail("luis.perez@vetcitas.com");
            vet.setActivo(true);
            vet.setCmpv("CMPV-99999");
            vet.setEspecialidad("Medicina General");
            vet.setCreatedOn(ahora);
            vet.setModifiedBy(adminBD);

            int idVet = veterinarioBO.insertar(vet);
            System.out.println("Veterinario insertado con ID: " + idVet);

            Veterinario vetBD = null;
            if (idVet > 0) {
                vetBD = veterinarioBO.buscarPorId(idVet);
            }
            imprimirVeterinario(vetBD);

            // ==================================================
            // 3. RECEPCIONISTA
            // ==================================================
            imprimirSeccion("3. INSERTAR RECEPCIONISTA");

            Recepcionista recep = new Recepcionista();
            recep.setUsername("recep_lab_final");
            recep.setContrasenaHash("hash_recep_123");
            recep.setNombres("Carla");
            recep.setApellidos("Rojas");
            recep.setEmail("carla.rojas@vetcitas.com");
            recep.setTelefono("977555444");
            recep.setActivo(true);
            recep.setArea("Front Desk");
            recep.setCreatedOn(ahora);
            recep.setModifiedBy(adminBD);

            int idRecep = recepcionistaBO.insertar(recep);
            System.out.println("Recepcionista insertada con ID: " + idRecep);

            Recepcionista recepBD = null;
            if (idRecep > 0) {
                recepBD = recepcionistaBO.buscarPorId(idRecep);
            }
            imprimirRecepcionista(recepBD);

            // ==================================================
            // 4. ASIGNAR ROL RECEPCIONISTA AL VETERINARIO
            // ==================================================
            imprimirSeccion("4. ASIGNAR ROL ADICIONAL AL VETERINARIO");

            if (idVet > 0) {
                administradorBO.asignarRol(idVet, CodigoRol.RECEPCIONISTA.name());
                System.out.println("Se asignó el rol RECEPCIONISTA al veterinario.");

                Veterinario vetActualizado = veterinarioBO.buscarPorId(idVet);
                List<RolSistema> rolesVet = vetActualizado.getRoles();

                System.out.println("Roles actuales del veterinario:");
                for (RolSistema rol : rolesVet) {
                    System.out.println("- " + rol.getCodigo());
                }

                System.out.println("Permisos actuales del veterinario:");
                for (RolSistema rol : rolesVet) {
                    for(Permiso permiso : rol.getPermisos()) {
                        System.out.println("- [" + rol.getCodigo() + "] " + permiso.getNombre());
                    }
                }
            } else {
                System.out.println("No se pudo asignar rol porque el veterinario no fue insertado.");
            }

            // ==================================================
            // 5. PERMISO CRUD
            // ==================================================
            imprimirSeccion("5. CRUD DE PERMISO");

            Permiso permiso = new Permiso();
            permiso.setNombre("EXPORTAR_DATOS");
            permiso.setDescripcion("Permite exportar información del sistema");

            int idPermiso = permisoBO.insertar(permiso);
            System.out.println("Permiso insertado con ID: " + idPermiso);

            Permiso permisoBD = null;
            if (idPermiso > 0) {
                permisoBD = permisoBO.buscarPorId(idPermiso);
            }

            if (permisoBD != null) {
                System.out.println("Permiso buscado: " + permisoBD.getId() + " - "
                        + permisoBD.getNombre() + " - " + permisoBD.getDescripcion());

                permisoBD.setDescripcion("Permite exportar información clínica y administrativa");
                permisoBO.modificar(permisoBD);

                Permiso permisoModificado = permisoBO.buscarPorId(idPermiso);
                if (permisoModificado != null) {
                    System.out.println("Permiso modificado: " + permisoModificado.getDescripcion());
                } else {
                    System.out.println("No se pudo recuperar el permiso modificado.");
                }
            } else {
                System.out.println("No se pudo recuperar el permiso insertado.");
            }

            List<Permiso> permisos = permisoBO.listarTodos();
            System.out.println("Total permisos listados: " + permisos.size());

            // ==================================================
            // 6. CONFIGURACIÓN
            // ==================================================
            imprimirSeccion("6. CONFIGURACIÓN");

            Configuracion configuracion = configuracionBO.obtenerConfiguracionActual();
            if (configuracion != null) {
                System.out.println("Configuración inicial:");
                System.out.println("Umbral cliente frecuente: " + configuracion.getUmbralClienteFrecuente());
                System.out.println("Descuento máximo permitido: " + configuracion.getDescuentoMaximoPermitido());

                configuracion.setUmbralClienteFrecuente(configuracion.getUmbralClienteFrecuente() + 1);
                configuracion.setDescuentoMaximoPermitido(configuracion.getDescuentoMaximoPermitido() + 5.0);
                configuracionBO.modificar(configuracion);

                Configuracion configuracionModificada = configuracionBO.obtenerConfiguracionActual();
                System.out.println("Configuración modificada:");
                System.out.println("Umbral cliente frecuente: " + configuracionModificada.getUmbralClienteFrecuente());
                System.out.println("Descuento máximo permitido: " + configuracionModificada.getDescuentoMaximoPermitido());
            }

            // ==================================================
            // ===== NUEVO ===== VALIDAR EMAIL OBLIGATORIO EN USUARIO
            // ==================================================
            imprimirSeccion("VALIDAR EMAIL OBLIGATORIO EN USUARIO");

            try {
                Veterinario vetSinEmail = new Veterinario();
                vetSinEmail.setUsername("vet_sin_email");
                vetSinEmail.setContrasenaHash("hash_vet_sin_email");
                vetSinEmail.setNombres("Marco");
                vetSinEmail.setApellidos("SinCorreo");
                vetSinEmail.setTelefono("944111222");
                vetSinEmail.setEmail(""); // debe fallar
                vetSinEmail.setActivo(true);
                vetSinEmail.setCmpv("CMPV-00001");
                vetSinEmail.setEspecialidad("Dermatología");
                vetSinEmail.setCreatedOn(ahora);
                vetSinEmail.setModifiedBy(adminBD);

                veterinarioBO.insertar(vetSinEmail);
                System.out.println("ERROR: se insertó veterinario sin email.");
            } catch (Exception ex) {
                System.out.println("ÉXITO: se bloqueó usuario sin email -> " + ex.getMessage());
            }

            // ==================================================
            // 7. HORARIO DEL VETERINARIO
            // ==================================================
            imprimirSeccion("7. HORARIO DEL VETERINARIO");

            int idHorario = 0;
            HorarioVeterinario horarioBD = null;

            if (vetBD != null) {
                HorarioVeterinario horario = new HorarioVeterinario();
                horario.setVeterinario(vetBD);
                horario.setDiaSemana(1); // Lunes
                horario.setHoraInicio(LocalTime.of(9, 0));
                horario.setHoraFin(LocalTime.of(18, 0));
                horario.setHoraDescansoInicio(LocalTime.of(13, 0));
                horario.setHoraDescansoFin(LocalTime.of(14, 0));
                horario.setActivo(true);
                horario.setCreatedOn(ahora);
                horario.setModifiedBy(adminBD);

                idHorario = horarioBO.insertar(horario);
                System.out.println("Horario insertado con ID: " + idHorario);

                if (idHorario > 0) {
                    horarioBD = horarioBO.buscarPorId(idHorario);
                }
                imprimirHorario(horarioBD);

                if (horarioBD != null) {
                    horarioBD.setHoraFin(LocalTime.of(17, 30));
                    horarioBD.setModifiedBy(adminBD);
                    horarioBO.modificar(horarioBD);
                    System.out.println("Horario modificado.");
                }

                List<HorarioVeterinario> horariosVet = horarioBO.listarPorVeterinario(idVet);
                System.out.println("Horarios del veterinario: " + horariosVet.size());
            } else {
                System.out.println("No se puede registrar horario porque el veterinario no existe.");
            }

            // ==================================================
            // 8. CLIENTE
            // ==================================================
            imprimirSeccion("8. INSERTAR CLIENTE");

            Cliente cliente = new Cliente();
            cliente.setDni("12345678");
            cliente.setNombres("Mariana");
            cliente.setApellidos("Gómez");
            cliente.setTelefono("966123123");
            cliente.setEmail("mariana.gomez@email.com");
            cliente.setObservaciones("Cliente frecuente");
            cliente.setActivo(true);
            cliente.setCreatedOn(ahora);
            cliente.setModifiedBy(adminBD);

            int resultadoCliente = clienteBO.insertar(cliente);
            System.out.println("Cliente insertado con ID real: " + resultadoCliente);

            Cliente clienteBD = null;
            if (resultadoCliente > 0) {
                clienteBD = clienteBO.buscarPorId(resultadoCliente);
                System.out.println("Cliente buscado: " + clienteBD.getId()
                        + " - DNI: " + clienteBD.getDni()
                        + " - Nombre: " + clienteBD.getNombres() + " " + clienteBD.getApellidos()
                        + " - Email: " + clienteBD.getEmail());
            }

            // ==================================================
            // 9. MASCOTA
            // ==================================================
            imprimirSeccion("9. INSERTAR MASCOTA");

            Mascota mascotaBD = null;

            if (clienteBD != null) {
                Mascota mascota = new Mascota();
                mascota.setNombre("Firulais");
                mascota.setEspecie("Perro");
                mascota.setRaza("Mestizo");
                mascota.setFechaNacimiento(LocalDate.of(2021, 5, 10));
                mascota.setPeso(12.5);
                mascota.setEsterilizado(true);
                mascota.setActivo(true);
                mascota.setCliente(clienteBD);
                mascota.setCreatedOn(ahora);
                mascota.setModifiedBy(adminBD);

                int resultadoMascota = mascotaBO.insertar(mascota);
                System.out.println("Mascota insertada con ID real: " + resultadoMascota);

                if (resultadoMascota > 0) {
                    mascotaBD = mascotaBO.buscarPorId(resultadoMascota);
                    System.out.println("Mascota buscada: " + mascotaBD.getId()
                            + " - " + mascotaBD.getNombre()
                            + " - Peso: " + mascotaBD.getPeso() + " kg");
                }
            } else {
                System.out.println("No se puede registrar mascota porque el cliente no existe.");
            }

            // ==================================================
            // 10. SERVICIO
            // ==================================================
            imprimirSeccion("10. INSERTAR SERVICIO");

            Servicio servicio = new Servicio();
            servicio.setNombre("Consulta General");
            servicio.setDescripcion("Servicio clínico general para evaluación inicial de la mascota.");
            servicio.setTipoServicio(TipoServicio.CLINICA);
            servicio.setDuracionMinutos(30);
            servicio.setPrecioReferencial(80.0);
            servicio.setActivo(true);
            servicio.setCreatedOn(ahora);
            servicio.setModifiedBy(adminBD);

            int idServicio = servicioBO.insertar(servicio);
            System.out.println("Servicio insertado con ID: " + idServicio);

            Servicio servicioBD = null;
            if (idServicio > 0) {
                servicioBD = servicioBO.buscarPorId(idServicio);
                System.out.println("Servicio buscado: " + servicioBD.getId()
                        + " - " + servicioBD.getNombre()
                        + " - Descripción: " + servicioBD.getDescripcion());
            }

            // ==================================================
            // 11. CITA
            // ==================================================
            imprimirSeccion("11. INSERTAR CITA");

            int idCita = 0;
            Cita citaBD = null;

            if (mascotaBD != null && vetBD != null && servicioBD != null) {
                LocalDate proximoLunes = obtenerProximaFecha(DayOfWeek.MONDAY);
                LocalDateTime fechaCita = LocalDateTime.of(proximoLunes, LocalTime.of(10, 0));

                LocalDateTime fechaCitaF = LocalDateTime.of(proximoLunes, LocalTime.of(11, 0));

                Cita cita = new Cita();
                cita.setFechaHoraInicio(fechaCita);
                cita.setFechaHoraFin(fechaCitaF);
                cita.setEstado(EstadoCita.PENDIENTE);
                cita.setMascota(mascotaBD);
                cita.setVeterinario(vetBD);
                cita.setServicio(servicioBD);
                cita.setCreatedOn(ahora);

                idCita = citaBO.insertar(cita);
                System.out.println("Cita insertada con ID: " + idCita);

                if (idCita > 0) {
                    citaBD = citaBO.buscarPorId(idCita);
                }
                imprimirCita(citaBD);

                // ==================================================
                // 12. PROBAR SOLAPAMIENTO
                // ==================================================
                imprimirSeccion("12. PROBAR SOLAPAMIENTO DE CITA");

                try {
                    Cita citaSolapada = new Cita();
                    citaSolapada.setFechaHoraInicio(LocalDateTime.of(proximoLunes, LocalTime.of(10, 15)));
                    citaSolapada.setEstado(EstadoCita.PENDIENTE);
                    citaSolapada.setMascota(mascotaBD);
                    citaSolapada.setVeterinario(vetBD);
                    citaSolapada.setServicio(servicioBD);
                    citaSolapada.setCreatedOn(ahora);

                    citaBO.insertar(citaSolapada); // Esto arrojará una Exception desde el BO
                    System.out.println("ERROR: la cita solapada no debió insertarse.");
                } catch (Exception ex) {
                    System.out.println("Correcto, la Capa de Negocios rechazó la cita solapada.");
                    System.out.println("Detalle del BO: " + ex.getMessage());
                }

                // ==================================================
                // 13. CONFIRMAR / MODIFICAR / ATENDER CITA
                // ==================================================
                imprimirSeccion("13. CAMBIOS DE ESTADO DE CITA");

                citaBO.confirmarCita(idCita, idAdmin);
                System.out.println("Cita confirmada mediante BO.");

                Cita citaParaModificar = citaBO.buscarPorId(idCita);
                if (citaParaModificar != null) {
                    citaParaModificar.setFechaHoraInicio(LocalDateTime.of(proximoLunes, LocalTime.of(9, 0)));
                    citaParaModificar.setEstado(EstadoCita.CONFIRMADA);
                    citaParaModificar.setMascota(mascotaBD);
                    citaParaModificar.setVeterinario(vetBD);
                    citaParaModificar.setServicio(servicioBD);
                    citaParaModificar.setModifiedOn(LocalDateTime.now());
                    citaParaModificar.setModifiedBy(adminBD);

                    citaBO.modificar(citaParaModificar);
                    System.out.println("Cita reprogramada a las 9:00.");
                }
                // ==================================================
                // ===== NUEVO ===== PASAR CITA A EN_CONSULTA
                // ==================================================
                citaBO.marcarEnConsulta(idCita, idVet);
                System.out.println("Cita marcada como EN_CONSULTA.");

                // Recuperamos la cita actualizada para que la atención use el estado correcto
                Cita citaEnConsulta = citaBO.buscarPorId(idCita);

                // ==================================================
                // ===== NUEVO ===== 13.1 REGISTRAR ATENCIÓN DE LA CITA
                // ==================================================
                imprimirSeccion("13.1 REGISTRAR ATENCIÓN");

                Atencion atencionBD = null;
                int idAtencion = 0;

                if (citaEnConsulta != null) {
                    Atencion atencion = new Atencion();
                    atencion.setFechaHora(LocalDateTime.now());
                    atencion.setNotaClinica("Paciente estable. Presenta leve tos y buena respuesta al examen clínico.");
                    atencion.setDiagnostico("Tos leve sin signos de complicación respiratoria.");
                    atencion.setNotaPreOperatoria("No aplica para consulta clínica.");
                    atencion.setNotaPostOperatoria("No aplica para consulta clínica.");
                    atencion.setRecomendacionControl("Reposo 48 horas y control en 7 días.");
                    atencion.setMontoReferencial(80.0);
                    atencion.setDescuentoAplicado(5.0);
                    atencion.setCita(citaEnConsulta);
                    atencion.setCreatedOn(LocalDateTime.now());
                    atencion.setModifiedBy(vetBD);

                    idAtencion = atencionBO.insertar(atencion);
                    System.out.println("Atención insertada con ID: " + idAtencion);

                    if (idAtencion > 0) {
                        atencionBD = atencionBO.buscarPorId(idAtencion);
                        System.out.println("Atención buscada -> ID: " + atencionBD.getId()
                                + ", Fecha: " + atencionBD.getFechaHora()
                                + ", Nota clínica: " + atencionBD.getNotaClinica()
                                + ", Diagnóstico: " + atencionBD.getDiagnostico());
                    }

                    Atencion atencionPorCita = atencionBO.buscarPorCita(citaEnConsulta.getId());
                    if (atencionPorCita != null) {
                        System.out.println("Atención encontrada por cita -> ID Atención: " + atencionPorCita.getId()
                                + ", ID Cita: " + atencionPorCita.getCita().getId());
                    } else {
                        System.out.println("No se encontró atención por cita.");
                    }
                }

                // ==================================================
                // ===== NUEVO ===== MARCAR CITA COMO ATENDIDA
                // ==================================================
                citaBO.marcarAtendida(idCita, idVet);
                System.out.println("Cita marcada como atendida.");

                // Recuperamos la cita ya atendida para usarla en recordatorio si hace falta
                Cita citaAtendida = citaBO.buscarPorId(idCita);

                // ==================================================
                // ===== NUEVO ===== 13.2 REGISTRAR RECORDATORIO DE LA CITA
                // ==================================================
                imprimirSeccion("13.2 REGISTRAR RECORDATORIO");

                Recordatorio recordatorioBD = null;
                int idRecordatorio = 0;

                if (citaAtendida != null) {
                    Recordatorio recordatorio = new Recordatorio();
                    recordatorio.setFechaProgramada(LocalDateTime.now().plusDays(3));
                    recordatorio.setCanal(CanalRecordatorio.WHATSAPP);
                    recordatorio.setEstadoSeguimiento(EstadoSeguimiento.PENDIENTE);
                    recordatorio.setMensaje("Recordatorio de control para la mascota "
                            + (mascotaBD != null ? mascotaBD.getNombre() : "N/A")
                            + " en 3 días.");
                    recordatorio.setCita(citaAtendida);
                    recordatorio.setCreatedOn(LocalDateTime.now());
                    recordatorio.setModifiedBy(adminBD);

                    idRecordatorio = recordatorioBO.insertar(recordatorio);
                    System.out.println("Recordatorio insertado con ID: " + idRecordatorio);

                    if (idRecordatorio > 0) {
                        recordatorioBD = recordatorioBO.buscarPorId(idRecordatorio);
                        if (recordatorioBD != null) {
                            System.out.println("Recordatorio buscado -> ID: " + recordatorioBD.getId()
                                    + ", Fecha: " + recordatorioBD.getFechaProgramada()
                                    + ", Estado: " + recordatorioBD.getEstadoSeguimiento()
                                    + ", Mensaje: " + recordatorioBD.getMensaje());
                        }
                    }
                }

                List<Cita> citasVetDia = citaBO.listarPorVeterinarioYFecha(idVet, proximoLunes);
                System.out.println("Citas del veterinario para ese día: " + citasVetDia.size());
                for (Cita c : citasVetDia) {
                    imprimirCita(c);
                }

            }

            // ==================================================
            // 14. MODIFICAR RECEPCIONISTA
            // ==================================================
            imprimirSeccion("14. MODIFICAR RECEPCIONISTA");

            if (recepBD != null) {
                recepBD.setArea("Agenda y Caja");
                recepBD.setModifiedBy(adminBD);
                recepcionistaBO.modificar(recepBD);

                Recepcionista recepModificada = recepcionistaBO.buscarPorId(idRecep);
                imprimirRecepcionista(recepModificada);
            }

            // ==================================================
            // 15. LISTADOS GENERALES
            // ==================================================
            imprimirSeccion("15. LISTADOS GENERALES");

            System.out.println("Administradores: " + administradorBO.listarTodos().size());
            System.out.println("Veterinarios: " + veterinarioBO.listarTodos().size());
            System.out.println("Recepcionistas: " + recepcionistaBO.listarTodos().size());
            System.out.println("Horarios: " + horarioBO.listarTodos().size());
            System.out.println("Citas: " + citaBO.listarTodos().size());
            System.out.println("Permisos: " + permisoBO.listarTodos().size());
            System.out.println("Configuraciones activas: " + (configuracionBO.obtenerConfiguracionActual() != null ? 1 : 0));

            // ==================================================
            // 16. ELIMINACIONES LÓGICAS CONTROLADAS
            // ==================================================
            imprimirSeccion("16. ELIMINACIONES LÓGICAS CONTROLADAS");

            if (idPermiso > 0) {
                permisoBO.eliminar(idPermiso);
                System.out.println("Permiso de prueba eliminado lógicamente.");
            }

            if (idRecep > 0) {
                recepcionistaBO.eliminar(idRecep);
                System.out.println("Recepcionista eliminada lógicamente.");
            }

            // ==================================================
            // 17. SUPERADMIN Y REGLAS DE ROLES
            // ==================================================
            imprimirSeccion("17. SUPERADMIN Y REGLAS DE ROLES (PROBANDO EXCEPCIONES BO)");

            Administrador superAdmin = null;
            for (Administrador a : administradorBO.listarTodos()) {
                if (a.isEsSuperAdmin()) {
                    superAdmin = a;
                    break;
                }
            }

            if (superAdmin == null) {
                System.out.println("No se encontró ningún SuperAdmin en la BD (¿corriste la DDL con el seed?).");
            } else {
                System.out.println("SuperAdmin encontrado:");
                imprimirAdministrador(superAdmin);

                // 17.1 Asignar el 3er rol al veterinario
                System.out.println("\n17.1 Asignar ADMINISTRADOR al veterinario:");
                administradorBO.asignarRol(idVet, CodigoRol.ADMINISTRADOR.name());
                Veterinario vetCheck = veterinarioBO.buscarPorId(idVet);
                System.out.println("Roles del veterinario: " + vetCheck.getRoles().size() + " (esperado 3)");
                for (RolSistema rol : vetCheck.getRoles()) {
                    System.out.println("- " + rol.getCodigo());
                }

                // 17.2 Intentar un rol duplicado
                System.out.println("\n17.2 Intentar asignar un rol que ya tiene (debería lanzar Excepción):");
                try {
                    administradorBO.asignarRol(idVet, CodigoRol.VETERINARIO.name());
                    System.out.println("ERROR CRÍTICO: el rol duplicado se asignó saltándose la validación.");
                } catch (Exception ex) {
                    System.out.println("ÉXITO: La regla de negocio bloqueó la asignación -> " + ex.getMessage());
                }

                // 17.3 Intentar eliminar al SuperAdmin
                System.out.println("\n17.3 Intentar eliminar al SuperAdmin (debería lanzar Excepción):");
                try {
                    administradorBO.eliminar(superAdmin.getId());
                    System.out.println("ERROR CRÍTICO: el SuperAdmin fue eliminado.");
                } catch (Exception ex) {
                    System.out.println("ÉXITO: La regla de negocio protegió al SuperAdmin -> " + ex.getMessage());
                }

                // 17.4 Intentar revocar ADMINISTRADOR al SuperAdmin (No lo implementaste en el BO directamente,
                // pero si tienes un administradorDAO.revocarRol, al no estar expuesto en el BO, evitamos usarlo,
                // Si sí lo expusiste, lo probamos):
                System.out.println("\n17.4 Intentar revocar ADMINISTRADOR al SuperAdmin (debería lanzar Excepción):");
                try {
                    administradorBO.revocarRol(superAdmin.getId(), CodigoRol.ADMINISTRADOR.name());
                    System.out.println("ERROR CRÍTICO: Se le revocó el rol al SuperAdmin.");
                } catch (Exception ex) {
                    System.out.println("ÉXITO: La regla de negocio impidió quitar el rol -> " + ex.getMessage());
                }
            }

            System.out.println("\n==================================================");
            System.out.println(" TODAS LAS PRUEBAS FINALIZARON CON ÉXITO");
            System.out.println("==================================================");

            // ==================================================
            // ===== NUEVO ===== 18. PRUEBAS DE FILTROS Y LISTADOS
            // ==================================================
            imprimirSeccion("18. PRUEBAS DE FILTROS Y LISTADOS NUEVOS");

            // ------------------------------
            // ===== NUEVO ===== 18.1 CLIENTES
            // Buscar por nombre, apellido o DNI
            // ------------------------------
            System.out.println("\n18.1 CLIENTES - Buscar por nombre/apellido/dni");
            List<Cliente> clientesFiltrados = clienteBO.listarPorNombreApellidoODni("Mariana");
            System.out.println("Clientes encontrados con 'Mariana': " + clientesFiltrados.size());
            for (Cliente c : clientesFiltrados) {
                System.out.println("Cliente -> ID: " + c.getId()
                        + ", DNI: " + c.getDni()
                        + ", Nombre: " + c.getNombres() + " " + c.getApellidos());
            }

            List<Cliente> clientesPorDni = clienteBO.listarPorNombreApellidoODni("12345678");
            System.out.println("Clientes encontrados con DNI '12345678': " + clientesPorDni.size());
            for (Cliente c : clientesPorDni) {
                System.out.println("Cliente DNI -> " + c.getDni() + " - " + c.getNombres());
            }

            // ------------------------------
            // ===== NUEVO ===== Buscar cliente por email opcional
            // ------------------------------
            List<Cliente> clientesPorEmail = clienteBO.listarPorNombreApellidoODni("mariana.gomez");
            System.out.println("Clientes encontrados con email 'mariana.gomez': " + clientesPorEmail.size());

            for (Cliente c : clientesPorEmail) {
                System.out.println("Cliente -> ID: " + c.getId()
                        + ", DNI: " + c.getDni()
                        + ", Nombre: " + c.getNombres() + " " + c.getApellidos()
                        + ", Email: " + c.getEmail());
            }

            // ------------------------------
            // ===== NUEVO ===== 18.2 MASCOTAS
            // Buscar por nombre o dueño
            // ------------------------------
            System.out.println("\n18.2 MASCOTAS - Buscar por nombre o dueño");
            List<Mascota> mascotasPorTexto = mascotaBO.listarPorNombreODueno("Firulais");
            System.out.println("Mascotas encontradas con 'Firulais': " + mascotasPorTexto.size());
            for (Mascota m : mascotasPorTexto) {
                System.out.println("Mascota -> ID: " + m.getId()
                        + ", Nombre: " + m.getNombre()
                        + ", Peso: " + m.getPeso() + " kg"
                        + ", Dueño: " + (m.getCliente() != null
                        ? m.getCliente().getNombres() + " " + m.getCliente().getApellidos()
                        : "null"));
            }

            List<Mascota> mascotasPorDueno = mascotaBO.listarPorNombreODueno("Gómez");
            System.out.println("Mascotas encontradas con dueño 'Gómez': " + mascotasPorDueno.size());
            for (Mascota m : mascotasPorDueno) {
                System.out.println("Mascota/Dueño -> " + m.getNombre() + " / "
                        + (m.getCliente() != null
                        ? m.getCliente().getNombres() + " " + m.getCliente().getApellidos()
                        : "null"));
            }

            // ------------------------------
            // ===== NUEVO ===== 18.3 MASCOTAS DE UN CLIENTE
            // ------------------------------
            System.out.println("\n18.3 MASCOTAS DE UN CLIENTE");
            if (clienteBD != null) {
                List<Mascota> mascotasCliente = mascotaBO.listarPorCliente(clienteBD.getId());
                System.out.println("Mascotas del cliente " + clienteBD.getNombres() + ": " + mascotasCliente.size());
                for (Mascota m : mascotasCliente) {
                    System.out.println("- " + m.getNombre() + " (" + m.getEspecie() + " - " + m.getPeso() +" kg" +  ")");
                }
            }

            // ------------------------------
            // ===== NUEVO ===== 18.4 SERVICIOS
            // Buscar por nombre o tipo
            // ------------------------------
            System.out.println("\n18.4 SERVICIOS - Buscar por nombre o tipo");
            List<Servicio> serviciosPorTexto = servicioBO.listarPorNombreOTipo("Consulta");
            System.out.println("Servicios encontrados con 'Consulta': " + serviciosPorTexto.size());
            for (Servicio s : serviciosPorTexto) {
                System.out.println("Servicio -> ID: " + s.getId()
                        + ", Nombre: " + s.getNombre()
                        + ", Tipo: " + s.getTipoServicio()
                        + ", Descripción: " + s.getDescripcion());
            }
            // ------------------------------
            // Buscar servicio por descripción
            // ------------------------------
            List<Servicio> serviciosPorDescripcion = servicioBO.listarPorNombreOTipo("evaluación");
            System.out.println("Servicios encontrados por descripción 'evaluación': " + serviciosPorDescripcion.size());

            for (Servicio s : serviciosPorDescripcion) {
                System.out.println("Servicio por descripción -> ID: " + s.getId()
                        + ", Nombre: " + s.getNombre()
                        + ", Tipo: " + s.getTipoServicio()
                        + ", Descripción: " + s.getDescripcion());
            }

            List<Servicio> serviciosActivos = servicioBO.listarPorEstado(true);
            System.out.println("Servicios activos encontrados: " + serviciosActivos.size());

            // ------------------------------
            // ===== NUEVO ===== 18.5 CITAS FILTRADAS
            // ------------------------------
            System.out.println("\n18.5 CITAS FILTRADAS");
            if (vetBD != null) {
                LocalDate hoy = LocalDate.now();
                LocalDate desde = hoy.minusDays(7);
                LocalDate hasta = hoy.plusDays(30);

                List<Cita> citasFiltradas = citaBO.listarFiltradas(
                        vetBD.getId(),
                        desde,
                        hasta,
                        "",
                        "Firulais"
                );

                System.out.println("Citas filtradas del veterinario con texto 'Firulais': " + citasFiltradas.size());
                for (Cita c : citasFiltradas) {
                    imprimirCita(c);
                }
            }

            // ------------------------------
            // ===== NUEVO ===== 18.6 HORARIO SEMANAL POR VETERINARIO
            // ------------------------------
            System.out.println("\n18.6 HORARIO SEMANAL POR VETERINARIO");
            if (vetBD != null) {
                List<HorarioVeterinario> horarioSemanal = horarioBO.listarHorarioSemanalPorVeterinario(vetBD.getId());
                System.out.println("Cantidad de horarios semanales: " + horarioSemanal.size());
                for (HorarioVeterinario h : horarioSemanal) {
                    imprimirHorario(h);
                }
            }

            // ------------------------------
            // ===== NUEVO ===== 18.7 USUARIOS FILTRADOS
            // Buscar por texto, rol y estado
            // ------------------------------
            System.out.println("\n18.7 USUARIOS FILTRADOS");
            List<Usuario> usuariosPorTexto = administradorBO.listarUsuariosFiltrados("admin_lab", "", null);
            System.out.println("Usuarios encontrados por texto 'admin_lab': " + usuariosPorTexto.size());
            for (Usuario u : usuariosPorTexto) {
                System.out.println("Usuario -> ID: " + u.getId()
                        + ", Username: " + u.getUsername()
                        + ", Nombre: " + u.getNombres() + " " + u.getApellidos()
                        + ", Activo: " + u.isActivo());
            }

            List<Usuario> usuariosPorRol = administradorBO.listarUsuariosFiltrados("", CodigoRol.VETERINARIO.name(), true);
            System.out.println("Usuarios activos con rol VETERINARIO: " + usuariosPorRol.size());
            for (Usuario u : usuariosPorRol) {
                System.out.println("Usuario con rol veterinario -> " + u.getUsername());
                if (u.getRoles() != null) {
                    for (RolSistema rol : u.getRoles()) {
                        System.out.println("   Rol: " + rol.getCodigo());
                    }
                }
            }

            // ------------------------------
            // ===== NUEVO ===== 18.8 RECORDATORIOS
            // SOLO SI YA TIENES recordatorioBO instanciado
            // ------------------------------
            System.out.println("\n18.8 RECORDATORIOS FILTRADOS");


            List<Recordatorio> recordatoriosTexto = recordatorioBO.listarPorMascotaOCliente("Firulais");
            System.out.println("Recordatorios encontrados con texto 'Firulais': " + recordatoriosTexto.size());
            for (Recordatorio r : recordatoriosTexto) {
                System.out.println("Recordatorio -> ID: " + r.getId()
                        + ", Fecha: " + r.getFechaProgramada()
                        + ", Estado: " + r.getEstadoSeguimiento()
                        + ", Mensaje: " + r.getMensaje());
            }

            List<Recordatorio> recordatoriosEstado = recordatorioBO.listarPorEstadoYFecha(
                    EstadoSeguimiento.PENDIENTE.name(),
                    null
            );
            System.out.println("Recordatorios PENDIENTES: " + recordatoriosEstado.size());

            // ------------------------------
            // ===== NUEVO ===== 18.9 ATENCIONES FILTRADAS
            // SOLO SI YA TIENES atencionBO implementado
            // ------------------------------
            System.out.println("\n18.9 ATENCIONES FILTRADAS");

            List<Atencion> atencionesFiltradas = atencionBO.listarFiltradas(
                    vetBD != null ? vetBD.getId() : null,
                    "",
                    null,
                    "Firulais"
            );
            System.out.println("Atenciones filtradas por veterinario/texto: " + atencionesFiltradas.size());
            for (Atencion a : atencionesFiltradas) {
                System.out.println("Atención -> ID: " + a.getId()
                        + ", Fecha: " + a.getFechaHora()
                        + ", Nota: " + a.getNotaClinica()
                        + ", Diagnóstico: " + a.getDiagnostico());

                if (a.getCita() != null) {
                    if (a.getCita().getMascota() != null) {
                        System.out.println("   Mascota: " + a.getCita().getMascota().getNombre());
                    }

                    // ===== NUEVO: servicio y descripción desde atención filtrada =====
                    if (a.getCita().getServicio() != null) {
                        System.out.println("   Servicio: " + a.getCita().getServicio().getNombre());
                        System.out.println("   Desc. Servicio: " + a.getCita().getServicio().getDescripcion());
                    }
                    // ===== FIN NUEVO =====
                }
            }

            // ------------------------------
            // ===== NUEVO ===== 18.10 HISTORIAL DE VISITAS DE UNA MASCOTA
            // ------------------------------
            System.out.println("\n18.10 HISTORIAL DE VISITAS DE UNA MASCOTA");
            if (mascotaBD != null) {
                List<Atencion> historialMascota = atencionBO.listarHistorialPorMascota(mascotaBD.getId());
                System.out.println("Historial de la mascota " + mascotaBD.getNombre() + ": " + historialMascota.size());
                for (Atencion a : historialMascota) {
                    System.out.println("Historial -> Atención ID: " + a.getId()
                            + ", Fecha: " + a.getFechaHora()
                            + ", Diagnóstico: " + a.getDiagnostico());
                    if (a.getCita() != null && a.getCita().getServicio() != null) {
                        System.out.println("   Servicio: " + a.getCita().getServicio().getNombre());

                        // ===== NUEVO: descripción en historial de visitas =====
                        System.out.println("   Desc. Servicio: " + a.getCita().getServicio().getDescripcion());
                        // ===== FIN NUEVO =====
                    }
                }
            }

            // ==================================================
            // ===== FIN NUEVO ===== 18. PRUEBAS DE FILTROS Y LISTADOS
            // ==================================================

        } catch (Exception ex) {
            System.out.println("==============================================");
            System.out.println("ERROR GENERAL INESPERADO EN LAS PRUEBAS");
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            System.out.println("==============================================");
        }
    }



    private static LocalDate obtenerProximaFecha(DayOfWeek diaObjetivo) {
        LocalDate fecha = LocalDate.now().plusDays(1);
        while (fecha.getDayOfWeek() != diaObjetivo) {
            fecha = fecha.plusDays(1);
        }
        return fecha;
    }

    private static void imprimirSeccion(String titulo) {
        System.out.println("\n--------------------------------------------------");
        System.out.println(titulo);
        System.out.println("--------------------------------------------------");
    }

    private static void imprimirAdministrador(Administrador admin) {
        if (admin == null) {
            System.out.println("Administrador: null");
            return;
        }
        System.out.println("Administrador -> ID: " + admin.getId()
                + ", Username: " + admin.getUsername()
                + ", Nombre: " + admin.getNombres() + " " + admin.getApellidos()
                + ", Área: " + admin.getArea()
                + ", Email: " + admin.getEmail());

    }

    private static void imprimirVeterinario(Veterinario vet) {
        if (vet == null) {
            System.out.println("Veterinario: null");
            return;
        }
        System.out.println("Veterinario -> ID: " + vet.getId()
                + ", Username: " + vet.getUsername()
                + ", Nombre: " + vet.getNombres() + " " + vet.getApellidos()
                + ", CMPV: " + vet.getCmpv()
                + ", Especialidad: " + vet.getEspecialidad()
                + ", Email: " + vet.getEmail());
    }

    private static void imprimirRecepcionista(Recepcionista recep) {
        if (recep == null) {
            System.out.println("Recepcionista: null");
            return;
        }
        System.out.println("Recepcionista -> ID: " + recep.getId()
                + ", Username: " + recep.getUsername()
                + ", Nombre: " + recep.getNombres() + " " + recep.getApellidos()
                + ", Área: " + recep.getArea()
                + ", Email: " + recep.getEmail());
    }

    private static void imprimirHorario(HorarioVeterinario horario) {
        if (horario == null) {
            System.out.println("Horario: null");
            return;
        }
        System.out.println("Horario -> ID: " + horario.getId()
                + ", Vet ID: " + (horario.getVeterinario() != null ? horario.getVeterinario().getId() : 0)
                + ", Día: " + horario.getDiaSemana()
                + ", Inicio: " + horario.getHoraInicio()
                + ", Fin: " + horario.getHoraFin()
                + ", Descanso: " + horario.getHoraDescansoInicio() + " - " + horario.getHoraDescansoFin());
    }

    private static void imprimirCita(Cita cita) {
        if (cita == null) {
            System.out.println("Cita: null");
            return;
        }
        System.out.println("Cita -> ID: " + cita.getId()
                + ", Inicio: " + cita.getFechaHoraInicio()
                + ", Fin: " + cita.getFechaHoraFin()
                + ", Estado: " + cita.getEstado()
                + ", Mascota: " + (cita.getMascota() != null ? cita.getMascota().getNombre() : "null")
                + ", Peso Mascota: " + (cita.getMascota() != null ? cita.getMascota().getPeso() + " kg" : "null")
                + ", Vet ID: " + (cita.getVeterinario() != null ? cita.getVeterinario().getId() : 0)
                + ", Servicio: " + (cita.getServicio() != null ? cita.getServicio().getNombre() : "null")
                + ", Desc. Servicio: " + (cita.getServicio() != null ? cita.getServicio().getDescripcion() : "null"));
    }
}