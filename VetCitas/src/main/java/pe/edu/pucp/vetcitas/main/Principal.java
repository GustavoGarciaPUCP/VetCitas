package pe.edu.pucp.vetcitas.main;

import pe.edu.pucp.vetcitas.cita.model.Atencion;
import pe.edu.pucp.vetcitas.cita.model.Cita;
import pe.edu.pucp.vetcitas.cita.model.Recordatorio;
import pe.edu.pucp.vetcitas.cliente.dao.MascotaDAO;
import pe.edu.pucp.vetcitas.cliente.impl.MascotaImpl;
import pe.edu.pucp.vetcitas.cliente.model.Cliente;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;
import pe.edu.pucp.vetcitas.common.enums.CanalRecordatorio;
import pe.edu.pucp.vetcitas.common.enums.EstadoCita;
import pe.edu.pucp.vetcitas.common.enums.EstadoSeguimiento;
import pe.edu.pucp.vetcitas.common.enums.Rol;
import pe.edu.pucp.vetcitas.common.enums.TipoServicio;
import pe.edu.pucp.vetcitas.configuracion.model.Configuracion;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;
import pe.edu.pucp.vetcitas.usuario.model.Administrador;
import pe.edu.pucp.vetcitas.usuario.model.Permiso;
import pe.edu.pucp.vetcitas.usuario.model.Veterinario;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Principal {
    public static void main(String[] args) {
        System.out.println("=== INICIO DE PRUEBAS DEL MODELO VETCITAS ===");

        System.out.println("\n=== INICIANDO PRUEBA CERO: DAO MASCOTA ===");

        //Estos datos los puse manuales pq no había Cliente aún
        //cuando crees cliente el id es autimatico por la base de datos
        // 1. Preparamos los "Padres" falsos (Solo nos importa que tengan el ID 1)
        Cliente clientePrueba = new Cliente();
        clientePrueba.setId(1); // Este es el que metimos por SQL

        Administrador adminPrueba = new Administrador();
        adminPrueba.setId(1); // Este es el que metimos por SQL

        // 2. Preparamos tu Mascota
        Mascota mascotaPrueba = new Mascota();
        // NO asignamos ID a la mascota, MySQL lo generara
        mascotaPrueba.setNombre("Oreo");
        mascotaPrueba.setEspecie("Gato");
        mascotaPrueba.setRaza("Carey");
        mascotaPrueba.setFechaNacimiento(LocalDate.of(2023, 2, 14));
        mascotaPrueba.setEsterilizado(true);
        mascotaPrueba.setActivo(true);

        // Enlazamos los padres
        mascotaPrueba.setCliente(clientePrueba);
        mascotaPrueba.setCreatedOn(LocalDateTime.now());
        mascotaPrueba.setModifiedOn(LocalDateTime.now());
        mascotaPrueba.setModifiedBy(adminPrueba);

        // 3. Ejecutamos tu DAO
        MascotaDAO mascotaDAO = new MascotaImpl();

        System.out.println("Intentando insertar mascota 'Oreo' en la base de datos AWS...");
        int resultadoInsertar = mascotaDAO.insertar(mascotaPrueba);

        if (resultadoInsertar > 0) {
            System.out.println("Exito: Mascota insertada. Tu DAO funciona perfectamente.");
        } else {
            System.out.println("Fallo la insercion. Revisa el texto de error en la consola.");
        }

        // 4. Comprobamos leyendo la base de datos
        System.out.println("\nListando mascotas desde la BD:");
        List<Mascota> listaBD = mascotaDAO.listarTodas();

        if (listaBD != null && !listaBD.isEmpty()) {
            for (Mascota m : listaBD) {
                System.out.println("ID BD: " + m.getId() + " | Nombre: " + m.getNombre() + " | Especie: " + m.getEspecie());
            }
        } else {
            System.out.println("La lista esta vacia.");
        }
        //Probando si el id generado en SQL fue asignado en mi mascota
        System.out.println(mascotaPrueba.getId());
    }
}
