package controllers;

import models.repositories.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;


public class FactoryController {

    public static Object controller(String nombre, EntityManager entityManager) {
        Object controller = null;
        switch (nombre) {
            case "LogIn": controller = new LogInController(new RepositorioMiembros(entityManager)); break;
            case "Miembros": controller = new MiembrosController(new RepositorioMiembros(entityManager), new RepositorioProvincias(entityManager), new RepositorioMunicipios(entityManager), new RepositorioDepartamentos(entityManager), new RepositorioLocalizaciones(entityManager)); break;
            case "Comunidades": controller = new ComunidadesController(new RepositorioComunidades(entityManager), new RepositorioMiembros(entityManager), new RepositorioIncidentes(entityManager)); break;
            case "Incidentes": controller = new IncidentesController(new RepositorioIncidentes(entityManager), new RepositorioMiembros(entityManager), new RepositorioComunidades(entityManager), new RepositorioEstadosPorComunidad(entityManager), new RepositorioEntidades(entityManager), new RepositorioPrestacionesDeServicios(entityManager), new RepositorioEstablecimientos(entityManager)); break;
            case "PrestacionesDeServicio": controller = new PrestacionesDeServiciosController(new RepositorioPrestacionesDeServicios(entityManager)); break;
            case "Entidades": controller = new EntidadesController(new RepositorioEntidades(entityManager), new RepositorioOrganismos(entityManager), new RepositorioPrestadoras(entityManager), new RepositorioProvincias(entityManager), new RepositorioMunicipios(entityManager), new RepositorioDepartamentos(entityManager), new RepositorioLocalizaciones(entityManager)); break;
            case "Rankings": controller = new RankingsController(new RepositorioIncidentes(entityManager), new RepositorioEntidades(entityManager)); break;
            case "Establecimientos": controller = new EstablecimientosController(new RepositorioEntidades(entityManager), new RepositorioEstablecimientos(entityManager)); break;
            case "Localizacion": controller = new LocalizacionController(new RepositorioProvincias(entityManager), new RepositorioMunicipios(entityManager), new RepositorioDepartamentos(entityManager), new RepositorioMiembros(entityManager)); break;
            case "Servicios": controller = new ServiciosController(new RepositorioEstablecimientos(entityManager), new RepositorioPrestacionesDeServicios(entityManager), new RepositorioMiembros(entityManager), new RepositorioServicios(entityManager)); break;

        }
        return controller;
    }
}