package models.entities.ServicioFusionComunidades.entities;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RespuestaFusionComunidades {
  private List<ComunidadServicioFusionComunidades> fusiones;

  public RespuestaFusionComunidades(){
    this.fusiones = new ArrayList<>();
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("RespuestaFusionComunidades{");

    if (fusiones != null) {
      sb.append("comunidades=[");
      for (ComunidadServicioFusionComunidades comunidad : fusiones) {
        sb.append("Comunidad: ").append(comunidad.getId()).append(", Miembros: ");
        sb.append(comunidad.getMiembros()).append(", ");
      }
      sb.append("]");
    } else {
      sb.append("fusiones=null");
    }

    sb.append("}");
    return sb.toString();
  }

}
