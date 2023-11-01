package models.entities.georef.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ListadoDeDepartamentos {
  private int cantidad;
  private int total;
  private int inicio;
  private Parametro parametros;
  private List<Departamento> departamentos;

  private class Parametro {
    public List<String> campos;
  }
}
