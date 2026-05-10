package pe.edu.pucp.vetcitas.usuario.model;

import pe.edu.pucp.vetcitas.common.enums.CodigoRol;
import pe.edu.pucp.vetcitas.common.model.EntidadAuditable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Usuario extends EntidadAuditable {
    private int id;
    private String username;
    private String contrasenaHash;
    private String nombres;
    private String apellidos;
    private boolean activo;
    private String telefono;
    private List<RolSistema> roles;

    public Usuario() {
        super();
        this.id = 0;
        this.username = "";
        this.contrasenaHash = "";
        this.nombres = "";
        this.apellidos = "";
        this.activo = true;
        this.telefono = "";
        this.roles = new ArrayList<>();
    }

    public Usuario(int id, String username, String contrasenaHash, String nombres,
                   String apellidos, boolean activo, String telefono,
                   List<RolSistema> roles, LocalDateTime createdOn,
                   LocalDateTime modifiedOn, Usuario modifiedBy) {
        super(createdOn, modifiedOn, modifiedBy);
        this.id = id;
        this.username = username;
        this.contrasenaHash = contrasenaHash;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.activo = activo;
        this.telefono = telefono;
        this.roles = new ArrayList<>();

        if (roles != null) {
            for (RolSistema rol : roles) {
                this.roles.add(new RolSistema(rol));
            }
        }
    }

    public Usuario(Usuario otro) {
        super(otro);
        this.id = otro.id;
        this.username = otro.username;
        this.contrasenaHash = otro.contrasenaHash;
        this.nombres = otro.nombres;
        this.apellidos = otro.apellidos;
        this.activo = otro.activo;
        this.telefono = otro.telefono;
        this.roles = new ArrayList<>();

        if (otro.roles != null) {
            for (RolSistema rol : otro.roles) {
                this.roles.add(new RolSistema(rol));
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<RolSistema> getRoles() {
        List<RolSistema> copia = new ArrayList<>();
        for (RolSistema rol : this.roles) {
            copia.add(new RolSistema(rol));
        }
        return copia;
    }

    public void setRoles(List<RolSistema> roles) {
        this.roles = new ArrayList<>();
        if (roles != null) {
            for (RolSistema rol : roles) {
                this.roles.add(new RolSistema(rol));
            }
        }
    }

    public boolean tieneRol(String codigoRol) {
        if (this.roles == null) {
            return false;
        }

        for (RolSistema rol : this.roles) {
            if (rol.getCodigo() != null && rol.getCodigo().name().equals(codigoRol)) {
                return true;
            }
        }
        return false;
    }

    public boolean tienePermiso(String nombrePermiso) {
        if (this.roles == null) {
            return false;
        }

        for (RolSistema rol : this.roles) {
            List<Permiso> permisos = rol.getPermisos();
            for (Permiso permiso : permisos) {
                if (permiso.getNombre() != null && permiso.getNombre().equals(nombrePermiso)) {
                    return true;
                }
            }
        }
        return false;
    }
}
