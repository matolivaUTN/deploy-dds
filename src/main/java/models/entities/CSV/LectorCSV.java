package models.entities.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.InputStream;
import java.io.InputStreamReader;

import models.entities.ServicioPublico.Organismo;
import models.entities.ServicioPublico.Prestadora;
import models.entities.roles.Rol;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.repositories.RepositorioRoles;

public class LectorCSV {
    public List<Organismo> leerCSVPrestadorasYOrganismos(InputStream inputStream, RepositorioRoles repositorioRoles) throws IOException, CsvValidationException {

      Map<String, Organismo> organismoMap = new HashMap<>();
      CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
      String[] nextLine;

      while ((nextLine = reader.readNext()) != null) {
        System.out.println("LINEA: " + nextLine.length);

        // Por si alguien se hace el vivo y mete enters de más
        if (nextLine.length < 2) {
          // Handle the case when there are not enough elements in the array
          continue; // Skip this line and move to the next one
        }

        String organismoNombre = nextLine[0];
        Organismo organismo = organismoMap.get(organismoNombre);

        if (organismo == null) {
          String contraseniaHasheadaOrganismo = BCrypt.hashpw(nextLine[1], BCrypt.gensalt());
          organismo = new Organismo(organismoNombre, contraseniaHasheadaOrganismo);

          Rol rol = repositorioRoles.buscarRolPorNombre("Organismo");
          organismo.setRol(rol);

          organismoMap.put(organismoNombre, organismo);
        }

        if (nextLine.length > 2) {

          String contraseniaHasheadaPrestadora = BCrypt.hashpw(nextLine[3], BCrypt.gensalt());
          Prestadora prestadora = new Prestadora(nextLine[2], contraseniaHasheadaPrestadora);

          Rol rol = repositorioRoles.buscarRolPorNombre("Prestadora");
          prestadora.setRol(rol);

          organismo.addPrestadora(prestadora);
        }
      }

      return new ArrayList<>(organismoMap.values());
    }
}
