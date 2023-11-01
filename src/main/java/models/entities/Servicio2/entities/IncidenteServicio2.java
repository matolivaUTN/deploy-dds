package models.entities.Servicio2.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class IncidenteServicio2 {
    private LocalDateTime fechaApertura;
    private LocalDateTime fechaCierre;
    private Integer idCreador;
    private Integer idCerrador;
    private Integer idPrestacion;
}
