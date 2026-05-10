package pe.edu.pucp.vetcitas.usuario.model;

import java.time.LocalDateTime;
import java.util.List;

public class Recepcionista extends Usuario{
    private String area;

    public Recepcionista() {
        super();
        this.area = "";
    }

    public Recepcionista(int id, String username, String contrasenaHash, String nombres,
                         String apellidos, boolean activo, String telefono,
                         List<RolSistema> roles, String area,
                         LocalDateTime createdOn, LocalDateTime modifiedOn,
                         Usuario modifiedBy) {
        super(id, username, contrasenaHash, nombres, apellidos, activo, telefono,
                roles, createdOn, modifiedOn, modifiedBy);
        this.area = area;
    }

    public Recepcionista(Recepcionista otro) {
        super(otro);
        this.area = otro.area;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
