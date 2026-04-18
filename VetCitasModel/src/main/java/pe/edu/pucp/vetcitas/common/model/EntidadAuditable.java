package pe.edu.pucp.vetcitas.common.model;
import pe.edu.pucp.vetcitas.usuario.model.Usuario;
import java.time.LocalDateTime;
public class EntidadAuditable {
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
    private Usuario modifiedBy;

    public EntidadAuditable() {
        this.createdOn = null;
        this.modifiedOn = null;
        this.modifiedBy = null;
    }

    public EntidadAuditable(LocalDateTime createdOn, LocalDateTime modifiedOn, Usuario modifiedBy) {
        this.createdOn = createdOn;
        this.modifiedOn = modifiedOn;
        this.modifiedBy = modifiedBy;
    }

    public EntidadAuditable(EntidadAuditable otra) {
        this.createdOn = otra.createdOn;
        this.modifiedOn = otra.modifiedOn;
        this.modifiedBy = (otra.modifiedBy == null) ? null : otra.modifiedBy;
    }

    public LocalDateTime getCreatedOn() {
        return this.createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getModifiedOn() {
        return this.modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Usuario getModifiedBy() {
        return this.modifiedBy;
    }

    public void setModifiedBy(Usuario modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
