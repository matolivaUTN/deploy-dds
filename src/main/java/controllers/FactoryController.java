package controllers;

import models.repositories.*;

import javax.persistence.EntityManager;


public class FactoryController {

    public static Object controller(String nombre, EntityManager entityManager) {
        Object controller = null;
        switch (nombre) {
            case "LogIn": controller = new LogInController(new RepositorioMiembros(entityManager), new RepositorioPrestadoras(entityManager), new RepositorioOrganismos(entityManager)); break;
            case "Miembros": controller = new MiembrosController(new RepositorioMiembros(entityManager), new RepositorioProvincias(entityManager), new RepositorioMunicipios(entityManager), new RepositorioDepartamentos(entityManager), new RepositorioLocalizaciones(entityManager), new RepositorioRoles(entityManager)); break;
            case "Comunidades": controller = new ComunidadesController(new RepositorioComunidades(entityManager), new RepositorioMiembros(entityManager), new RepositorioIncidentes(entityManager), new RepositorioRoles(entityManager)); break;
            case "Incidentes": controller = new IncidentesController(new RepositorioIncidentes(entityManager), new RepositorioMiembros(entityManager), new RepositorioComunidades(entityManager), new RepositorioEntidades(entityManager), new RepositorioPrestacionesDeServicios(entityManager), new RepositorioEstablecimientos(entityManager), new RepositorioNotificacion(entityManager), new RepositorioNotificacionPorMiembro(entityManager)); break;
            case "PrestacionesDeServicio": controller = new PrestacionesDeServiciosController(new RepositorioPrestacionesDeServicios(entityManager)); break;
            case "Entidades": controller = new EntidadesController(new RepositorioEntidades(entityManager), new RepositorioOrganismos(entityManager), new RepositorioPrestadoras(entityManager), new RepositorioProvincias(entityManager), new RepositorioMunicipios(entityManager), new RepositorioDepartamentos(entityManager), new RepositorioLocalizaciones(entityManager), new RepositorioRoles(entityManager)); break;
            case "Rankings": controller = new RankingsController(new RepositorioIncidentes(entityManager), new RepositorioEntidades(entityManager)); break;
            case "Establecimientos": controller = new EstablecimientosController(new RepositorioEntidades(entityManager), new RepositorioEstablecimientos(entityManager), new RepositorioLocalizaciones(entityManager), new RepositorioProvincias(entityManager), new RepositorioDepartamentos(entityManager), new RepositorioMunicipios(entityManager), new RepositorioPrestadoras(entityManager)); break;
            case "Localizacion": controller = new LocalizacionController(new RepositorioProvincias(entityManager), new RepositorioMunicipios(entityManager), new RepositorioDepartamentos(entityManager), new RepositorioMiembros(entityManager), new RepositorioEntidades(entityManager)); break;
            case "Servicios": controller = new ServiciosController(new RepositorioEstablecimientos(entityManager), new RepositorioPrestacionesDeServicios(entityManager), new RepositorioMiembros(entityManager), new RepositorioServicios(entityManager)); break;
            case "Empresas": controller = new EmpresasController(new RepositorioPrestadoras(entityManager), new RepositorioMiembros(entityManager), new RepositorioOrganismos(entityManager)); break;
            case "Organismos": controller = new OrganismosController(new RepositorioOrganismos(entityManager)); break;

        }
        return controller;
    }
}