package controllers;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import io.javalin.http.Context;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.javalin.http.HttpStatus;
import models.entities.Comunidad.Comunidad;
import models.entities.Comunidad.Miembro;
import models.entities.Incidente.EstadoPorComunidad;
import models.entities.Incidente.Incidente;
import models.entities.ServicioPublico.Entidad;
import models.entities.ServicioPublico.Establecimiento;
import models.entities.georef.entities.Municipio;
import models.entities.georef.entities.Departamento;
import models.entities.georef.entities.Provincia;
import models.repositories.*;
import java.util.Comparator;

import java.time.LocalDateTime;
import java.util.*;

public class LocalizacionController implements WithSimplePersistenceUnit {

    private RepositorioProvincias repositorioProvincias;
    private RepositorioMunicipios repositorioMunicipios;
    private RepositorioDepartamentos repositorioDepartamentos;
    private RepositorioMiembros repositorioMiembros;


    public LocalizacionController(RepositorioProvincias repositorioProvincias, RepositorioMunicipios repositorioMunicipios, RepositorioDepartamentos repositorioDepartamentos, RepositorioMiembros repositorioMiembros) {
        this.repositorioProvincias = repositorioProvincias;
        this.repositorioMunicipios = repositorioMunicipios;
        this.repositorioDepartamentos = repositorioDepartamentos;
        this.repositorioMiembros = repositorioMiembros;
    }


    public void obtenerMunicipiosProvincia(Context context) {
        String valorCampo1 = context.queryParam("valor");

        //long idMiembro = Long.parseLong(context.cookie("id_miembro"));
        //Miembro miembro = this.repositorioMiembros.buscarPorId(idMiembro);


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

        //long idMiembro = Long.parseLong(context.cookie("id_miembro"));

        //Miembro miembro = this.repositorioMiembros.buscarPorId(idMiembro);

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

}


