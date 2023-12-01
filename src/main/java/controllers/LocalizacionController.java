package controllers;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import models.entities.localizacion.Localizacion;
import models.entities.ServicioPublico.Entidad;
import models.entities.georef.entities.Municipio;
import models.entities.georef.entities.Departamento;
import models.entities.georef.entities.Provincia;
import models.repositories.*;

import java.util.*;

public class LocalizacionController extends Controller {

    private RepositorioProvincias repositorioProvincias;
    private RepositorioMunicipios repositorioMunicipios;
    private RepositorioDepartamentos repositorioDepartamentos;
    private RepositorioMiembros repositorioMiembros;
    private RepositorioEntidades repositorioEntidades;


    public LocalizacionController(RepositorioProvincias repositorioProvincias, RepositorioMunicipios repositorioMunicipios, RepositorioDepartamentos repositorioDepartamentos, RepositorioMiembros repositorioMiembros, RepositorioEntidades repositorioEntidades) {
        this.repositorioProvincias = repositorioProvincias;
        this.repositorioMunicipios = repositorioMunicipios;
        this.repositorioDepartamentos = repositorioDepartamentos;
        this.repositorioMiembros = repositorioMiembros;
        this.repositorioEntidades = repositorioEntidades;
    }

    public void obtenerMunicipiosProvincia(Context context) {
        String valorCampo1 = context.queryParam("valor");
        Provincia provincia = this.repositorioProvincias.buscarPorId(Long.parseLong(valorCampo1));

        // Crear una respuesta HTML o texto plano
        StringBuilder response = new StringBuilder();

        response.append("<option value='' disabled selected>Seleccione el municipio</option>");


        List<Municipio> municipios = this.repositorioMunicipios.buscarMunicipiosDeProvincia(provincia);


        for (Municipio municipio : municipios) {
            response.append("<option value='").append(municipio.getIdMunicipio()).append("'>").append(municipio.getNombre()).append("</option>");
        }

        // COMMIT HBS
        context.result(response.toString());
        context.contentType("text/html");
    }

    public void obtenerDepartamentosProvincia(Context context) {
        // Id entidad
        String valorCampo1 = context.queryParam("valor");

        Provincia provincia = this.repositorioProvincias.buscarPorId(Long.parseLong(valorCampo1));


        // Buscamos los establecimientos que pertenezcan a esa entidad
        List<Departamento> departamentos = this.repositorioDepartamentos.buscarDepartamentosDeProvincia(provincia);

        // Crear una respuesta HTML o texto plano
        StringBuilder response = new StringBuilder();
        response.append("<option value='' disabled selected>Seleccione el departamento</option>");


        for (Departamento departamento : departamentos) {
            response.append("<option value='").append(departamento.getIdDepartamento()).append("'>").append(departamento.getNombre()).append("</option>");
        }

        context.result(response.toString());
        context.contentType("text/html");
    }

    public void obtenerProvinciaEntidad(Context context) {

        // Id entidad
        String valorCampo1 = context.queryParam("valor");

        Entidad entidad = this.repositorioEntidades.buscarPorId(Long.parseLong(valorCampo1));

        Localizacion ubicacionEntidad = entidad.getLocalizacion();

        // Crear una respuesta HTML o texto plano
        StringBuilder response = new StringBuilder();
        response.append("<option value='").append(ubicacionEntidad.getProvincia().getId()).append("' disabled selected>").append(ubicacionEntidad.getProvincia().getNombre()).append("</option>");

        context.result(response.toString());
        context.contentType("text/html");
    }

    public void obtenerLocalizacion(Context context) {

        // Id entidad
        String valorCampo1 = context.queryParam("valor");

        Entidad entidad = this.repositorioEntidades.buscarPorId(Long.parseLong(valorCampo1));
        Localizacion localizacionEntidad = entidad.getLocalizacion();

        StringBuilder localizacionTag = new StringBuilder();
        if(localizacionEntidad.getMunicipio() == null && localizacionEntidad.getDepartamento() == null) {
            localizacionTag.append("<label for=\"localizacion\"><b>Localizacion</b></label>\n");
            localizacionTag.append("<select id=\"localizacion\" name=\"localizacion\" class=\"custom-select\">\n");
            localizacionTag.append("    <option value=\"\" disabled selected>Seleccione la localización</option>\n");
            localizacionTag.append("    <option value=\"departamento\">Departamento</option>\n");
            localizacionTag.append("    <option value=\"municipio\">Municipio</option>\n");
            localizacionTag.append("</select>");

            localizacionTag.append("<script>");
            localizacionTag.append("document.getElementById('localizacion').addEventListener('change', function() {");
            localizacionTag.append("  const selectedOption = this.value;");
            localizacionTag.append("  const departamentoContainer = document.getElementById('departamento-container');");
            localizacionTag.append("  const municipioContainer = document.getElementById('municipio-container');");
            localizacionTag.append("  if (selectedOption === 'departamento') {");
            localizacionTag.append("    departamentoContainer.style.display = 'block';");
            localizacionTag.append("    municipioContainer.style.display = 'none';");
            localizacionTag.append("  } else if (selectedOption === 'municipio') {");
            localizacionTag.append("    municipioContainer.style.display = 'block';");
            localizacionTag.append("    departamentoContainer.style.display = 'none';");
            localizacionTag.append("  } else {");
            localizacionTag.append("    departamentoContainer.style.display = 'none';");
            localizacionTag.append("    municipioContainer.style.display = 'none';");
            localizacionTag.append("  }");
            localizacionTag.append("});");
            localizacionTag.append("</script>");
        }
        // Abajo le pongo 2 porque sino se confunde con el otro Ajax de obtenerDepartamentosProvincia / obtenerMunicipiosProvincia
        // TODO: hay tremendo bug que muestra el departamento si entro a la opción de abajo, luego a la de arriba, y luego a la de abajo también.
        else if(localizacionEntidad.getDepartamento() != null) {
            localizacionTag.append("<label for=\"departamento2\"><b>Departamento</b></label>\n");
            localizacionTag.append("<select id=\"departamento2\" name=\"departamento2\" class=\"custom-select\">\n");
            localizacionTag.append("    <option value=\"").append(localizacionEntidad.getDepartamento().getIdDepartamento()).append("\" disabled selected>").append(localizacionEntidad.getDepartamento().getNombre()).append("</option>\n");
            localizacionTag.append("</select>\n");

            localizacionTag.append("<script>");
            localizacionTag.append("  const departamentoContainer = document.getElementById('departamento-container');");
            localizacionTag.append("  const municipioContainer = document.getElementById('municipio-container');");
            localizacionTag.append("    municipioContainer.style.display = 'none';");
            localizacionTag.append("    departamentoContainer.style.display = 'none';");
            localizacionTag.append("</script>");
        }
        else if(localizacionEntidad.getMunicipio() != null) {
            localizacionTag.append("<label for=\"municipio2\"><b>Municipio</b></label>\n");
            localizacionTag.append("<select id=\"municipio2\" name=\"municipio2\" class=\"custom-select\">\n");
            localizacionTag.append("    <option value=\"").append(localizacionEntidad.getMunicipio().getIdMunicipio()).append("\" disabled selected>").append(localizacionEntidad.getMunicipio().getNombre()).append("</option>\n");
            localizacionTag.append("</select>\n");

            localizacionTag.append("<script>");
            localizacionTag.append("  const departamentoContainer = document.getElementById('departamento-container');");
            localizacionTag.append("  const municipioContainer = document.getElementById('municipio-container');");
            localizacionTag.append("    municipioContainer.style.display = 'none';");
            localizacionTag.append("    departamentoContainer.style.display = 'none';");
            localizacionTag.append("</script>");
        }

        context.result(localizacionTag.toString());
        context.contentType("text/html");
    }
}