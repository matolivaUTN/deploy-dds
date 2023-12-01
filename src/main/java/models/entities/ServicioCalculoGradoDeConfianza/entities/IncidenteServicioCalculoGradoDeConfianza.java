package models.entities.ServicioCalculoGradoDeConfianza.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class IncidenteServicioCalculoGradoDeConfianza {
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private Integer idCreador;
    private Integer idCerrador;
    private Integer idPrestacion;
}
