package controllers;

import io.javalin.http.Context;
import models.entities.ServicioPublico.Organismo;
import models.repositories.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganismosController extends Controller {

    private RepositorioOrganismos repositorioOrganismos;


    public OrganismosController(RepositorioOrganismos repositorioOrganismos) {
        this.repositorioOrganismos = repositorioOrganismos;
    }




}
