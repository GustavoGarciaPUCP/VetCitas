package pe.edu.pucp.vetcitas.usuario.model;

import pe.edu.pucp.vetcitas.common.enums.CodigoRol;

import java.util.ArrayList;
import java.util.List;

public class RolSistema {
    private int id;
    private CodigoRol codigo;
    private String descripcion;
    private List<Permiso> permisos;

    public RolSistema() {
        this.id = 0;
        this.codigo = null;
        this.descripcion = "";
        this.permisos = new ArrayList<>();
    }

    public RolSistema(int id, CodigoRol codigo, String descripcion, List<Permiso> permisos) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.permisos = new ArrayList<>();

        if (permisos != null) {
            for (Permiso permiso : permisos) {
                this.permisos.add(new Permiso(permiso));
            }
        }
    }

    public RolSistema(RolSistema otro) {
        this.id = otro.id;
        this.codigo = otro.codigo;
        this.descripcion = otro.descripcion;
        this.permisos = new ArrayList<>();

        if (otro.permisos != null) {
            for (Permiso permiso : otro.permisos) {
                this.permisos.add(new Permiso(permiso));
            }
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public CodigoRol getCodigo() {
        return codigo;
    }

    public void setCodigo(CodigoRol codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Permiso> getPermisos() {
        List<Permiso> copia = new ArrayList<>();
        for (Permiso permiso : this.permisos) {
            copia.add(new Permiso(permiso));
        }
        return copia;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = new ArrayList<>();
        if (permisos != null) {
            for (Permiso permiso : permisos) {
                this.permisos.add(new Permiso(permiso));
            }
        }
    }
}
