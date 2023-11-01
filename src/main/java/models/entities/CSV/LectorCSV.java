package models.entities.CSV;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.InputStream;
import java.io.InputStreamReader;

import models.entities.ServicioPublico.Organismo;
import models.entities.ServicioPublico.Prestadora;
import models.entities.ServicioPublico.Entidad;
import models.entities.ServicioPublico.Establecimiento;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LectorCSV {
    public List<Organismo> leerCSVPrestadorasYOrganismos(InputStream inputStream) throws IOException, CsvValidationException {
      Map<String, Organismo> organismoMap = new HashMap<>();
      CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
      String[] nextLine;

      while ((nextLine = reader.readNext()) != null) {
        String organismoNombre = nextLine[0];
        Organismo organismo = organismoMap.get(organismoNombre);

        if (organismo == null) {
          organismo = new Organismo(organismoNombre);
          organismoMap.put(organismoNombre, organismo);
        }

        if (nextLine.length > 1) {
          Prestadora prestadora = new Prestadora(nextLine[1]);
          organismo.addPrestadora(prestadora);
        }
      }

      return new ArrayList<>(organismoMap.values());
    }

/*
  public List<Entidad> leerCSVEntidadesYEstablecimientos(InputStream inputStream) throws IOException, CsvValidationException {
    Map<String, Entidad> entidadMap = new HashMap<>();
    CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
    String[] nextLine;

    while ((nextLine = reader.readNext()) != null) {
      String entidadNombre = nextLine[0];
      Entidad entidad = entidadMap.get(entidadNombre);

      if (entidad == null) {
        entidad = new Entidad(entidadNombre);
        entidadMap.put(entidadNombre, entidad);
      }

      if (nextLine.length > 1) {
        Establecimiento establecimiento = new Establecimiento(nextLine[1]);
        entidad.addEstablecimiento(establecimiento);
      }
    }

    return new ArrayList<>(entidadMap.values());
  }*/

}
