package pe.edu.pucp.vetcitas.main;

import pe.edu.pucp.vetcitas.cita.model.Atencion;
import pe.edu.pucp.vetcitas.cita.model.Cita;
import pe.edu.pucp.vetcitas.cita.model.Recordatorio;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;
import pe.edu.pucp.vetcitas.common.enums.CanalRecordatorio;
import pe.edu.pucp.vetcitas.common.enums.EstadoCita;
import pe.edu.pucp.vetcitas.common.enums.EstadoSeguimiento;
import pe.edu.pucp.vetcitas.common.enums.Rol;
import pe.edu.pucp.vetcitas.common.enums.TipoReporte;
import pe.edu.pucp.vetcitas.common.enums.TipoServicio;
import pe.edu.pucp.vetcitas.configuracion.model.Configuracion;
import pe.edu.pucp.vetcitas.reporte.model.Reporte;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;
import pe.edu.pucp.vetcitas.usuario.model.Administrador;
import pe.edu.pucp.vetcitas.usuario.model.Permiso;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;
import pe.edu.pucp.vetcitas.usuario.model.Veterinario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Principal {
    public static void main(String[] args) {
        System.out.println("=== INICIO DE PRUEBAS DEL MODELO VETCITAS ===");

        // 1. Crear permisos
        Permiso permiso1 = new Permiso(1, "GESTIONAR_USUARIOS", "Permite gestionar usuarios del sistema");
        Permiso permiso2 = new Permiso(2, "GESTIONAR_SERVICIOS", "Permite gestionar servicios");
        Permiso permiso3 = new Permiso(3, "GESTIONAR_CITAS", "Permite gestionar citas");

        List<Permiso> permisosAdmin = new ArrayList<>();
        permisosAdmin.add(permiso1);
        permisosAdmin.add(permiso2);

        List<Permiso> permisosVet = new ArrayList<>();
        permisosVet.add(permiso3);

        // 2. Crear usuarios
        Administrador admin = new Administrador(
                1,
                "admin01",
                "hash_admin",
                "Ana",
                "Torres",
                true,
                Rol.ADMINISTRADOR,
                "999111222",
                permisosAdmin,
                "Administración"
        );

        Veterinario vet = new Veterinario(
                2,
                "vet01",
                "hash_vet",
                "Luis",
                "Pérez",
                true,
                Rol.VETERINARIO,
                "988777666",
                permisosVet,
                "CMPV-12345",
                "Cirugía"
        );

        System.out.println("Administrador creado: " + admin.getNombres() + " " + admin.getApellidos());
        System.out.println("Rol administrador: " + admin.getRol());
        System.out.println("Veterinario creado: " + vet.getNombres() + " " + vet.getApellidos());
        System.out.println("Especialidad veterinario: " + vet.getEspecialidad());


        // 3. Crear configuración
        Configuracion configuracion = new Configuracion(1, 5, 20.0);
        System.out.println("Umbral cliente frecuente: " + configuracion.getUmbralClienteFrecuente());
        System.out.println("Descuento máximo permitido: " + configuracion.getDescuentoMaximoPermitido());

        // 4. Crear cliente
        Cliente cliente = new Cliente();
        cliente.setId(1);
        cliente.setNombres("Carlos");
        cliente.setApellidos("Ramírez");
        cliente.setTelefono("987654321");
        cliente.setObservaciones("Cliente prefiere atención por las mañanas");
        cliente.setCreatedOn(LocalDateTime.now());
        cliente.setModifiedOn(LocalDateTime.now());
        cliente.setModifiedBy(admin);

        System.out.println("Cliente creado: " + cliente.getNombres() + " " + cliente.getApellidos());

        // 5. Crear servicio
        Servicio servicio = new Servicio(
                1,
                "Esterilización",
                TipoServicio.CLINICA,
                90,
                150.0,
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                admin
        );

        System.out.println("Servicio creado: " + servicio.getNombre());
        System.out.println("Tipo de servicio: " + servicio.getTipoServicio());
        System.out.println("Duración del servicio: " + servicio.getDuracionMinutos() + " min");

        // 6. Crear mascota
        Mascota mascota = new Mascota();
        mascota.setId(1);
        mascota.setNombre("Firulais");
        mascota.setEspecie("Perro");
        mascota.setRaza("Mestizo");
        mascota.setFechaNacimiento(LocalDate.of(2021, 5, 10));
        mascota.setCliente(cliente);
        mascota.setCreatedOn(LocalDateTime.now());
        mascota.setModifiedOn(LocalDateTime.now());
        mascota.setModifiedBy(vet);

        System.out.println("Mascota creada: " + mascota.getNombre());
        System.out.println("Especie: " + mascota.getEspecie());
        System.out.println("Raza: " + mascota.getRaza());

        // 7. Crear cita
        LocalDateTime inicio = LocalDateTime.of(2026, 5, 20, 10, 0);
        LocalDateTime fin = LocalDateTime.of(2026, 5, 20, 11, 30);

        Cita cita = new Cita(
                1,
                inicio,
                fin,
                EstadoCita.PENDIENTE,
                mascota,
                vet,
                servicio,
                LocalDateTime.now(),
                LocalDateTime.now(),
                admin
        );

        System.out.println("Cita creada con ID: " + cita.getId());
        System.out.println("Estado de la cita: " + cita.getEstado());
        System.out.println("Inicio cita: " + cita.getFechaHoraInicio());
        System.out.println("Fin cita: " + cita.getFechaHoraFin());

        // 8. Crear atención
        Atencion atencion = new Atencion(
                1,
                LocalDateTime.now(),
                "Paciente estable, apto para procedimiento",
                "Ayuno de 8 horas",
                "Reposo por 48 horas",
                "Control en 7 días",
                150.0,
                10.0,
                cita,
                LocalDateTime.now(),
                LocalDateTime.now(),
                vet
        );

        System.out.println("Atención creada con ID: " + atencion.getId());
        System.out.println("Nota clínica: " + atencion.getNotaClinica());
        System.out.println("Monto referencial: " + atencion.getMontoReferencial());
        System.out.println("Descuento aplicado: " + atencion.getDescuentoAplicado());

        // 9. Crear recordatorio
        Recordatorio recordatorio = new Recordatorio(
                1,
                LocalDateTime.of(2026, 5, 27, 9, 0),
                CanalRecordatorio.WHATSAPP,
                EstadoSeguimiento.PENDIENTE,
                "Recordatorio de control postoperatorio",
                cita,
                LocalDateTime.now(),
                LocalDateTime.now(),
                vet
        );

        System.out.println("Recordatorio creado con ID: " + recordatorio.getId());
        System.out.println("Canal del recordatorio: " + recordatorio.getCanal());
        System.out.println("Estado del recordatorio: " + recordatorio.getEstadoSeguimiento());

        // 10. Crear reporte
        Reporte reporte = new Reporte(
                1,
                TipoReporte.CITAS,
                LocalDateTime.of(2026, 5, 1, 0, 0),
                LocalDateTime.of(2026, 5, 31, 23, 59),
                LocalDateTime.now(),
                "Reporte mensual de citas",
                "PDF"
        );

        System.out.println("Reporte creado con ID: " + reporte.getId());
        System.out.println("Tipo de reporte: " + reporte.getTipo());
        System.out.println("Formato del reporte: " + reporte.getFormato());

        // 11. Probar constructor copia
        Servicio copiaServicio = new Servicio(servicio);
        Cliente copiaCliente = new Cliente(cliente);
        Mascota copiaMascota = new Mascota(mascota);
        Cita copiaCita = new Cita(cita);
        Atencion copiaAtencion = new Atencion(atencion);
        Recordatorio copiaRecordatorio = new Recordatorio(recordatorio);
        Reporte copiaReporte = new Reporte(reporte);
        Administrador copiaAdmin = new Administrador(admin);
        Veterinario copiaVet = new Veterinario(vet);

        System.out.println("\n=== PRUEBA DE CONSTRUCTORES COPIA ===");
        System.out.println("Copia servicio: " + copiaServicio.getNombre());
        System.out.println("Copia cliente: " + copiaCliente.getNombres());
        System.out.println("Copia mascota: " + copiaMascota.getNombre());
        System.out.println("Copia cita ID: " + copiaCita.getId());
        System.out.println("Copia atención ID: " + copiaAtencion.getId());
        System.out.println("Copia recordatorio ID: " + copiaRecordatorio.getId());
        System.out.println("Copia reporte formato: " + copiaReporte.getFormato());
        System.out.println("Copia administrador usuario: " + copiaAdmin.getUsername());
        System.out.println("Copia veterinario usuario: " + copiaVet.getUsername());

        System.out.println("\n=== FIN DE PRUEBAS DEL MODELO VETCITAS ===");
    }
}
