package models.entities.Comunidad;

import models.entities.Servicio.PrestacionDeServicio;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Rol {

    // Tengo dudas con esta clase, capaz nos conviene hacer una lista de miembros afectados en cada prestacion
    private PrestacionDeServicio prestacionDeServicio;
    private Boolean esAfectado;

    public Rol(PrestacionDeServicio prestacionDeServicio, Boolean esAfectado) {
        this.prestacionDeServicio = prestacionDeServicio;
        this.esAfectado = esAfectado;
    }

    public void noEsAfectado() {
        this.esAfectado = false;
    }
}
