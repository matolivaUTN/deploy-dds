package models.entities.Converter;

import models.entities.Notificaciones.CuandoSuceden;
import models.entities.Notificaciones.SinApuros;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import models.entities.Notificaciones.EstrategiaDeAviso;

@Converter(autoApply = true)
public class EstrategiaDeAvisoConverter implements AttributeConverter<EstrategiaDeAviso, String> {
  @Override
  public String convertToDatabaseColumn(EstrategiaDeAviso estrategiaDeAviso) {

    if (estrategiaDeAviso instanceof SinApuros) {
      return "sinApuros";
    } else if (estrategiaDeAviso instanceof CuandoSuceden) {
      return "cuandoSuceden";
    } else {
      // Manejar otros casos si es necesario
      return null;
    }
  }

  @Override
  public EstrategiaDeAviso convertToEntityAttribute(String s) {
    if ("sinApuros".equals(s)) {
      return new SinApuros();
    } else if ("cuandoSuceden".equals(s)) {
      return new CuandoSuceden();
    } else {
      // Manejar otros casos si es necesario
      return null;
    }
  }
}