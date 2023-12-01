package controllers;

import com.opencsv.exceptions.CsvValidationException;
import io.javalin.http.Context;
import models.entities.ServicioPublico.Organismo;
import models.entities.ServicioPublico.Prestadora;
import models.entities.comunidad.Miembro;
import models.repositories.*;

import server.utils.ICrudViewsHandler;

import java.io.IOException;
import java.util.*;

public class EmpresasController extends Controller implements ICrudViewsHandler {

    private RepositorioPrestadoras repositorioPrestadoras;
    private RepositorioMiembros repositorioMiembros;
    private RepositorioOrganismos repositorioOrganismos;

    public EmpresasController(RepositorioPrestadoras repositorioPrestadoras, RepositorioMiembros repositorioMiembros, RepositorioOrganismos repositorioOrganismos) {
        this.repositorioPrestadoras = repositorioPrestadoras;
        this.repositorioMiembros = repositorioMiembros;
        this.repositorioOrganismos = repositorioOrganismos;
    }

    @Override
    public void show(Context context) {
        // Mostramos pantalla de designacion de miembros

        List<Miembro> miembros = this.repositorioMiembros.buscarTodos();

        // Buscamos el id de la prestadora / organismo de control que quiere designar a un miembro

        long idPrestadora = Long.parseLong(context.cookie("id_prestadora") == null ? "-1" : context.cookie("id_prestadora"));
        Prestadora prestadora = repositorioPrestadoras.buscarPorId(idPrestadora);

        long idOrganismo = Long.parseLong(context.cookie("id_organismo") == null ? "-1" : context.cookie("id_organismo"));
        Organismo organismo = repositorioOrganismos.buscarPorId(idOrganismo);

        Map<String, Object> model = new HashMap<>();

        List<Miembro> miembrosSinDesignado;
        if(prestadora != null) {
            miembrosSinDesignado = miembros.stream().filter(m -> prestadora.getDesignado() == null || !Objects.equals(m.getIdMiembro(), prestadora.getDesignado().getIdMiembro())).toList();
        } else {
            miembrosSinDesignado = miembros.stream().filter(m -> organismo.getDesignado() == null || !Objects.equals(m.getIdMiembro(), organismo.getDesignado().getIdMiembro())).toList();
        }

        model.put("miembros", miembrosSinDesignado);

        cargarRolesAModel(context, model);
        context.render("designacionMiembros.hbs", model);
    }

    @Override
    public void create(Context context) {

    }

    @Override
    public void save(Context context) throws CsvValidationException, IOException {

    }

    @Override
    public void edit(Context context) {

    }

    @Override
    public void update(Context context) {

    }

    public void designar(Context context) {
        long idMiembroDesignado = Long.parseLong(context.formParam("miembro-designado"));
        Miembro miembroDesignado = repositorioMiembros.buscarPorId(idMiembroDesignado);

        if(context.cookie("id_prestadora") != null) {
            long idPrestadora = Long.parseLong(context.cookie("id_prestadora"));
            Prestadora prestadora = repositorioPrestadoras.buscarPorId(idPrestadora);

            prestadora.setDesignado(miembroDesignado);

            repositorioPrestadoras.actualizar(prestadora);
        }
        else {
            long idOrganismo = Long.parseLong((context.cookie("id_organismo")));
            Organismo organismo = repositorioOrganismos.buscarPorId(idOrganismo);

            organismo.setDesignado(miembroDesignado);

            repositorioOrganismos.actualizar(organismo);
        }

        Map<String, Object> model = new HashMap<>();

        model.put("enmpresa_designar", "enmpresa_designar");

        cargarRolesAModel(context, model);
        context.render("confirmacion.hbs", model);
    }

    @Override
    public void index(Context context) {

        List<Organismo> organismos = this.repositorioOrganismos.buscarTodos();

        Map<String, Object> model = new HashMap<>();

        model.put("organismos", organismos);
        cargarRolesAModel(context, model);
        context.render("listadoOrganismos.hbs", model);
    }

    @Override
    public void delete(Context context) {

        long organismoId = Long.parseLong(context.formParam("id"));

        Organismo organismo = this.repositorioOrganismos.buscarPorId(organismoId);

        // Tenemos que eliminar todas las prestadoras asociadas a este organismo

        List<Prestadora> prestadoras = organismo.getPrestadoras();

        prestadoras.forEach(prestadora -> prestadora.setDeleted(true));



        // Eliminamos el organismo

        organismo.setDeleted(true);

        this.repositorioOrganismos.actualizar(organismo);

        Map<String, Object> model = new HashMap<>();
        model.put("eliminacion_organismo", "eliminacion_organismo");
        model.put("organismo", organismo);

        cargarRolesAModel(context, model);
        context.render("confirmacion.hbs", model);
    }








}