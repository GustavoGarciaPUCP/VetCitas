package pe.edu.pucp.vetcitas.usuario.model;

import pe.edu.pucp.vetcitas.cita.model.Atencion;
import pe.edu.pucp.vetcitas.cita.model.Cita;
import pe.edu.pucp.vetcitas.cita.model.Recordatorio;
import pe.edu.pucp.vetcitas.cliente.model.Mascota;
import pe.edu.pucp.vetcitas.common.enums.Rol;
import pe.edu.pucp.vetcitas.servicio.model.Servicio;

import java.time.LocalDateTime;
import java.util.List;

public class Veterinario extends Usuario{
    private String cmpv;
    private String especialidad;

    public Veterinario() {
        super();
        this.cmpv = "";
        this.especialidad = "";
    }

    public Veterinario(int id, String username, String contrasenaHash, String nombres,
                       String apellidos, boolean activo, Rol rol, String telefono,
                       List<Permiso> permisos, String cmpv, String especialidad,LocalDateTime createdOn,
                       LocalDateTime modifiedOn,
                       Usuario modifiedBy) {
        super(id, username, contrasenaHash, nombres, apellidos, activo, rol, telefono,
                permisos, createdOn, modifiedOn, modifiedBy);
        this.cmpv = cmpv;
        this.especialidad = especialidad;
    }

    public Veterinario(Veterinario otro) {
        super(otro);
        this.cmpv = otro.cmpv;
        this.especialidad = otro.especialidad;
    }

    public String getCmpv() {
        return this.cmpv;
    }

    public void setCmpv(String cmpv) {
        this.cmpv = cmpv;
    }

    public String getEspecialidad() {
        return this.especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public Cita agendarCita(Mascota mascota, Servicio servicio,
                            LocalDateTime fechaHoraInicio,
                            LocalDateTime fechaHoraFin) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public Atencion registrarAtencion(Cita cita, String notaClinica) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }

    public Recordatorio programarRecordatorio(Cita cita, LocalDateTime fecha,
                                              pe.edu.pucp.vetcitas.common.enums.CanalRecordatorio canal,
                                              String mensaje) {
        throw new UnsupportedOperationException("Metodo no implementado aun.");
    }
}
