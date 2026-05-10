package pe.edu.pucp.vetcitas.main;

import pe.edu.pucp.vetcitas.cita.dao.ICitaDAO;
import pe.edu.pucp.vetcitas.cita.impl.CitaImpl;
import pe.edu.pucp.vetcitas.cita.model.Cita;
import pe.edu.pucp.vetcitas.cliente.dao.ClienteDAO;
import pe.edu.pucp.vetcitas.cliente.dao.MascotaDAO;
import pe.edu.pucp.vetcitas.cliente.impl.ClienteImpl;
import pe.edu.pucp.vetcitas.cliente.impl.MascotaImpl;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;
import pe.edu.pucp.vetcitas.common.enums.CodigoRol;
import pe.edu.pucp.vetcitas.common.enums.EstadoCita;
import pe.edu.pucp.vetcitas.common.enums.TipoServicio;
import pe.edu.pucp.vetcitas.configuracion.dao.IConfiguracionDAO;
import pe.edu.pucp.vetcitas.configuracion.impl.ConfiguracionImpl;
import pe.edu.pucp.vetcitas.configuracion.model.Configuracion;
import pe.edu.pucp.vetcitas.servicio.dao.IServicioDAO;
import pe.edu.pucp.vetcitas.servicio.impl.ServicioImpl;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;
import pe.edu.pucp.vetcitas.usuario.dao.IAdministradorDAO;
import pe.edu.pucp.vetcitas.usuario.dao.IHorarioVeterinarioDAO;
import pe.edu.pucp.vetcitas.usuario.dao.IPermisoDAO;
import pe.edu.pucp.vetcitas.usuario.dao.IRecepcionistaDAO;
import pe.edu.pucp.vetcitas.usuario.dao.IVeterinarioDAO;
import pe.edu.pucp.vetcitas.usuario.impl.AdministradorImpl;
import pe.edu.pucp.vetcitas.usuario.impl.HorarioVeterinarioImpl;
import pe.edu.pucp.vetcitas.usuario.impl.PermisoImpl;
import pe.edu.pucp.vetcitas.usuario.impl.RecepcionistaImpl;
import pe.edu.pucp.vetcitas.usuario.impl.VeterinarioImpl;
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
        System.out.println(" INICIO DE PRUEBAS INTEGRALES VETCITAS");
        System.out.println("==================================================");

        IAdministradorDAO administradorDAO = new AdministradorImpl();
        IVeterinarioDAO veterinarioDAO = new VeterinarioImpl();
        IRecepcionistaDAO recepcionistaDAO = new RecepcionistaImpl();
        IHorarioVeterinarioDAO horarioDAO = new HorarioVeterinarioImpl();
        IPermisoDAO permisoDAO = new PermisoImpl();
        IConfiguracionDAO configuracionDAO = new ConfiguracionImpl();

        ClienteDAO clienteDAO = new ClienteImpl();
        MascotaDAO mascotaDAO = new MascotaImpl();
        IServicioDAO servicioDAO = new ServicioImpl();
        ICitaDAO citaDAO = new CitaImpl();

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

            int idAdmin = administradorDAO.insertar(admin);
            System.out.println("Administrador insertado con ID: " + idAdmin);

            Administrador adminBD = null;
            if (idAdmin > 0) {
                adminBD = administradorDAO.buscarPorId(idAdmin);
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

            int idVet = veterinarioDAO.insertar(vet);
            System.out.println("Veterinario insertado con ID: " + idVet);

            Veterinario vetBD = null;
            if (idVet > 0) {
                vetBD = veterinarioDAO.buscarPorId(idVet);
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

            int idRecep = recepcionistaDAO.insertar(recep);
            System.out.println("Recepcionista insertada con ID: " + idRecep);

            Recepcionista recepBD = null;
            if (idRecep > 0) {
                recepBD = recepcionistaDAO.buscarPorId(idRecep);
            }
            imprimirRecepcionista(recepBD);

            // ==================================================
            // 4. ASIGNAR ROL RECEPCIONISTA AL VETERINARIO
            // ==================================================
            imprimirSeccion("4. ASIGNAR ROL ADICIONAL AL VETERINARIO");

            if (idVet > 0) {
                administradorDAO.asignarRol(idVet, CodigoRol.RECEPCIONISTA.name());
                System.out.println("Se asignó el rol RECEPCIONISTA al veterinario.");

                List<RolSistema> rolesVet = administradorDAO.listarRolesDeUsuario(idVet);
                List<String> permisosVet = administradorDAO.listarPermisosDeUsuario(idVet);

                System.out.println("Roles actuales del veterinario:");
                for (RolSistema rol : rolesVet) {
                    System.out.println("- " + rol.getCodigo());
                }

                System.out.println("Permisos actuales del veterinario:");
                for (String permiso : permisosVet) {
                    System.out.println("- " + permiso);
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

            int idPermiso = permisoDAO.insertar(permiso);
            System.out.println("Permiso insertado con ID: " + idPermiso);

            Permiso permisoBD = null;
            if (idPermiso > 0) {
                permisoBD = permisoDAO.buscarPorId(idPermiso);
            }

            if (permisoBD != null) {
                System.out.println("Permiso buscado: " + permisoBD.getId() + " - "
                        + permisoBD.getNombre() + " - " + permisoBD.getDescripcion());

                permisoBD.setDescripcion("Permite exportar información clínica y administrativa");
                permisoDAO.modificar(permisoBD);

                Permiso permisoModificado = permisoDAO.buscarPorId(idPermiso);
                if (permisoModificado != null) {
                    System.out.println("Permiso modificado: " + permisoModificado.getDescripcion());
                } else {
                    System.out.println("No se pudo recuperar el permiso modificado.");
                }
            } else {
                System.out.println("No se pudo recuperar el permiso insertado.");
            }

            List<Permiso> permisos = permisoDAO.listarTodas();
            System.out.println("Total permisos listados: " + permisos.size());

            // ==================================================
            // 6. CONFIGURACIÓN
            // ==================================================
            imprimirSeccion("6. CONFIGURACIÓN");

            Configuracion configuracion = configuracionDAO.buscarPorId(1);
            if (configuracion != null) {
                System.out.println("Configuración inicial:");
                System.out.println("Umbral cliente frecuente: " + configuracion.getUmbralClienteFrecuente());
                System.out.println("Descuento máximo permitido: " + configuracion.getDescuentoMaximoPermitido());

                configuracion.setUmbralClienteFrecuente(configuracion.getUmbralClienteFrecuente() + 1);
                configuracion.setDescuentoMaximoPermitido(configuracion.getDescuentoMaximoPermitido() + 5.0);
                configuracionDAO.modificar(configuracion);

                Configuracion configuracionModificada = configuracionDAO.buscarPorId(1);
                if (configuracionModificada != null) {
                    System.out.println("Configuración modificada:");
                    System.out.println("Umbral cliente frecuente: " + configuracionModificada.getUmbralClienteFrecuente());
                    System.out.println("Descuento máximo permitido: " + configuracionModificada.getDescuentoMaximoPermitido());
                } else {
                    System.out.println("No se pudo recuperar la configuración modificada.");
                }
            } else {
                System.out.println("No se encontró la configuración con ID 1.");
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

                idHorario = horarioDAO.insertar(horario);
                System.out.println("Horario insertado con ID: " + idHorario);

                if (idHorario > 0) {
                    horarioBD = horarioDAO.buscarPorId(idHorario);
                }
                imprimirHorario(horarioBD);

                if (horarioBD != null) {
                    horarioBD.setHoraFin(LocalTime.of(17, 30));
                    horarioBD.setModifiedBy(adminBD);
                    horarioDAO.modificar(horarioBD);
                    System.out.println("Horario modificado.");
                } else {
                    System.out.println("No se pudo recuperar el horario insertado.");
                }

                List<HorarioVeterinario> horariosVet = horarioDAO.listarPorVeterinario(idVet);
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

            int resultadoCliente = clienteDAO.insertar(cliente);
            System.out.println("Resultado insertar cliente: " + resultadoCliente);
            System.out.println("Cliente insertado con ID real: " + cliente.getId());

            Cliente clienteBD = null;
            if (cliente.getId() > 0) {
                clienteBD = clienteDAO.buscarPorId(cliente.getId());
            }

            if (clienteBD != null) {
                System.out.println("Cliente buscado: " + clienteBD.getId() + " - "
                        + clienteBD.getNombres() + " " + clienteBD.getApellidos());
            } else {
                System.out.println("No se pudo recuperar el cliente insertado.");
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

                int resultadoMascota = mascotaDAO.insertar(mascota);
                System.out.println("Resultado insertar mascota: " + resultadoMascota);
                System.out.println("Mascota insertada con ID real: " + mascota.getId());

                if (mascota.getId() > 0) {
                    mascotaBD = mascotaDAO.buscarPorId(mascota.getId());
                }

                if (mascotaBD != null) {
                    System.out.println("Mascota buscada: " + mascotaBD.getId() + " - " + mascotaBD.getNombre());
                } else {
                    System.out.println("No se pudo recuperar la mascota insertada.");
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

            int idServicio = servicioDAO.insertar(servicio);
            System.out.println("Servicio insertado con ID: " + idServicio);

            Servicio servicioBD = null;
            if (idServicio > 0) {
                servicioBD = servicioDAO.buscarPorId(idServicio);
            }

            if (servicioBD != null) {
                System.out.println("Servicio buscado: " + servicioBD.getId() + " - " + servicioBD.getNombre());
            } else {
                System.out.println("No se pudo recuperar el servicio insertado.");
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

                Cita cita = new Cita();
                cita.setFechaHoraInicio(fechaCita);
                cita.setEstado(EstadoCita.PENDIENTE);
                cita.setMascota(mascotaBD);
                cita.setVeterinario(vetBD);
                cita.setServicio(servicioBD);
                cita.setCreatedOn(ahora);

                idCita = citaDAO.insertar(cita);
                System.out.println("Cita insertada con ID: " + idCita);

                if (idCita > 0) {
                    citaBD = citaDAO.buscarPorId(idCita);
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

                    int idSolapada = citaDAO.insertar(citaSolapada);
                    if (idSolapada > 0) {
                        System.out.println("ERROR: la cita solapada no debió insertarse.");
                    } else {
                        System.out.println("La inserción de cita solapada no se completó, como se esperaba.");
                    }
                } catch (Exception ex) {
                    System.out.println("Correcto, se rechazó la cita solapada.");
                    System.out.println("Detalle: " + ex.getMessage());
                }

                // ==================================================
                // 13. CONFIRMAR / MODIFICAR / ATENDER CITA
                // ==================================================
                imprimirSeccion("13. CAMBIOS DE ESTADO DE CITA");

                citaDAO.confirmarCita(idCita, idAdmin);
                System.out.println("Cita confirmada.");

                Cita citaParaModificar = citaDAO.buscarPorId(idCita);
                if (citaParaModificar != null) {
                    citaParaModificar.setFechaHoraInicio(LocalDateTime.of(proximoLunes, LocalTime.of(11, 0)));
                    citaParaModificar.setEstado(EstadoCita.CONFIRMADA);
                    citaParaModificar.setMascota(mascotaBD);
                    citaParaModificar.setVeterinario(vetBD);
                    citaParaModificar.setServicio(servicioBD);
                    citaParaModificar.setModifiedOn(LocalDateTime.now());
                    citaParaModificar.setModifiedBy(adminBD);

                    citaDAO.modificar(citaParaModificar);
                    System.out.println("Cita reprogramada a las 11:00.");
                } else {
                    System.out.println("No se pudo recuperar la cita para modificar.");
                }

                citaDAO.marcarAtendida(idCita, idVet);
                System.out.println("Cita marcada como atendida.");

                List<Cita> citasVetDia = citaDAO.listarPorVeterinarioYFecha(idVet, proximoLunes);
                System.out.println("Citas del veterinario para ese día: " + citasVetDia.size());
                for (Cita c : citasVetDia) {
                    imprimirCita(c);
                }
            } else {
                System.out.println("No se puede registrar cita porque faltan mascota, veterinario o servicio.");
            }

            // ==================================================
            // 14. MODIFICAR RECEPCIONISTA
            // ==================================================
            imprimirSeccion("14. MODIFICAR RECEPCIONISTA");

            if (recepBD != null) {
                recepBD.setArea("Agenda y Caja");
                recepBD.setModifiedBy(adminBD);
                recepcionistaDAO.modificar(recepBD);

                Recepcionista recepModificada = recepcionistaDAO.buscarPorId(idRecep);
                imprimirRecepcionista(recepModificada);
            } else {
                System.out.println("No se puede modificar recepcionista porque no fue recuperada.");
            }

            // ==================================================
            // 15. LISTADOS GENERALES
            // ==================================================
            imprimirSeccion("15. LISTADOS GENERALES");

            System.out.println("Administradores: " + administradorDAO.listarTodas().size());
            System.out.println("Veterinarios: " + veterinarioDAO.listarTodas().size());
            System.out.println("Recepcionistas: " + recepcionistaDAO.listarTodas().size());
            System.out.println("Horarios: " + horarioDAO.listarTodas().size());
            System.out.println("Citas: " + citaDAO.listarTodas().size());
            System.out.println("Permisos: " + permisoDAO.listarTodas().size());
            System.out.println("Configuraciones: " + configuracionDAO.listarTodas().size());

            // ==================================================
            // 16. ELIMINACIONES LÓGICAS CONTROLADAS
            // ==================================================
            imprimirSeccion("16. ELIMINACIONES LÓGICAS CONTROLADAS");

            if (idPermiso > 0) {
                permisoDAO.eliminar(idPermiso);
                System.out.println("Permiso de prueba eliminado lógicamente.");
            } else {
                System.out.println("No se eliminó permiso porque no se insertó correctamente.");
            }

            if (idRecep > 0) {
                recepcionistaDAO.eliminar(idRecep);
                System.out.println("Recepcionista eliminada lógicamente.");
            } else {
                System.out.println("No se eliminó recepcionista porque no se insertó correctamente.");
            }

            System.out.println("\n==================================================");
            System.out.println(" TODAS LAS PRUEBAS FINALIZARON");
            System.out.println("==================================================");

        } catch (Exception ex) {
            System.out.println("==============================================");
            System.out.println("ERROR GENERAL EN LAS PRUEBAS");
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


