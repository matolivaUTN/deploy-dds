package controllers;

import io.javalin.http.Context;
import models.repositories.RepositorioPrestacionesDeServicios;
import server.utils.ICrudViewsHandler;

public class PrestacionesDeServiciosController implements ICrudViewsHandler {
    private RepositorioPrestacionesDeServicios repositorioPrestacionesDeServicios;

    public PrestacionesDeServiciosController(RepositorioPrestacionesDeServicios repositorioMiembros) {
        this.repositorioPrestacionesDeServicios = repositorioMiembros;
    }

    @Override
    public void index(Context context) {

    }


    @Override
    public void show(Context context) {

    }

    @Override
    public void create(Context context) {

    }

    @Override
    public void save(Context context) {

    }

    @Override
    public void edit(Context context) {

    }

    @Override
    public void update(Context context) {

    }

    @Override
    public void delete(Context context) {

    }



}