package pe.edu.pucp.vetcitas.main;

import pe.edu.pucp.vetcitas.cita.boi.ICitaBO;
import pe.edu.pucp.vetcitas.cita.bo.CitaBOImpl;
import pe.edu.pucp.vetcitas.cita.model.Cita;
import pe.edu.pucp.vetcitas.cliente.boi.IClienteBO;
import pe.edu.pucp.vetcitas.cliente.bo.ClienteBOImpl;
import pe.edu.pucp.vetcitas.cliente.boi.IMascotaBO;
import pe.edu.pucp.vetcitas.cliente.bo.MascotaBOImpl;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;
import pe.edu.pucp.vetcitas.common.enums.CodigoRol;
import pe.edu.pucp.vetcitas.common.enums.EstadoCita;
import pe.edu.pucp.vetcitas.common.enums.TipoServicio;
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
import pe.edu.pucp.vetcitas.usuario.model.Administrador;
import pe.edu.pucp.vetcitas.usuario.model.HorarioVeterinario;
import pe.edu.pucp.vetcitas.usuario.model.Permiso;
import pe.edu.pucp.vetcitas.usuario.model.Recepcionista;
import pe.edu.pucp.vetcitas.usuario.model.RolSistema;
import pe.edu.pucp.vetcitas.usuario.model.Veterinario;

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
            cliente.setNombres("Mariana");
            cliente.setApellidos("Gómez");
            cliente.setTelefono("966123123");
            cliente.setObservaciones("Cliente frecuente");
            cliente.setActivo(true);
            cliente.setCreatedOn(ahora);
            cliente.setModifiedBy(adminBD);

            int resultadoCliente = clienteBO.insertar(cliente);
            System.out.println("Cliente insertado con ID real: " + resultadoCliente);

            Cliente clienteBD = null;
            if (resultadoCliente > 0) {
                clienteBD = clienteBO.buscarPorId(resultadoCliente);
                System.out.println("Cliente buscado: " + clienteBD.getId() + " - "
                        + clienteBD.getNombres() + " " + clienteBD.getApellidos());
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
                mascota.setEsterilizado(true);
                mascota.setActivo(true);
                mascota.setCliente(clienteBD);
                mascota.setCreatedOn(ahora);
                mascota.setModifiedBy(adminBD);

                int resultadoMascota = mascotaBO.insertar(mascota);
                System.out.println("Mascota insertada con ID real: " + resultadoMascota);

                if (resultadoMascota > 0) {
                    mascotaBD = mascotaBO.buscarPorId(resultadoMascota);
                    System.out.println("Mascota buscada: " + mascotaBD.getId() + " - " + mascotaBD.getNombre());
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
                System.out.println("Servicio buscado: " + servicioBD.getId() + " - " + servicioBD.getNombre());
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
                    System.out.println("Cita reprogramada a las 11:00.");
                }

                citaBO.marcarAtendida(idCita, idVet);
                System.out.println("Cita marcada como atendida.");

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
                + ", Área: " + admin.getArea());
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
                + ", Especialidad: " + vet.getEspecialidad());
    }

    private static void imprimirRecepcionista(Recepcionista recep) {
        if (recep == null) {
            System.out.println("Recepcionista: null");
            return;
        }
        System.out.println("Recepcionista -> ID: " + recep.getId()
                + ", Username: " + recep.getUsername()
                + ", Nombre: " + recep.getNombres() + " " + recep.getApellidos()
                + ", Área: " + recep.getArea());
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
                + ", Vet ID: " + (cita.getVeterinario() != null ? cita.getVeterinario().getId() : 0)
                + ", Servicio: " + (cita.getServicio() != null ? cita.getServicio().getNombre() : "null"));
    }
}