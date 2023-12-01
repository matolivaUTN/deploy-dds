package models.entities.converter;

import models.entities.notificaciones.CuandoSuceden;
import models.entities.notificaciones.SinApuros;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import models.entities.notificaciones.EstrategiaDeAviso;

@Converter(autoApply = true)
public class EstrategiaDeAvisoConverter implements AttributeConverter<EstrategiaDeAviso, String> {

  @Override
  public String convertToDatabaseColumn(EstrategiaDeAviso estrategiaDeAviso) {

    if (estrategiaDeAviso instanceof SinApuros) {
      return "Sin apuros";
    } else if (estrategiaDeAviso instanceof CuandoSuceden) {
      return "Cuando suceden";
    } else {
      // Manejar otros casos si es necesario
      return null;
    }
  }

  @Override
  public EstrategiaDeAviso convertToEntityAttribute(String s) {
    if ("Sin apuros".equals(s)) {
      return new SinApuros();
    }
    else if ("Cuando suceden".equals(s)) {
      return new CuandoSuceden();
    } else {
      // Manejar otros casos si es necesario
      return null;
    }
  }
}