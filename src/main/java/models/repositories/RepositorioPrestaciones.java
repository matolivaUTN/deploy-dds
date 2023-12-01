package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.entities.servicio.PrestacionDeServicio;

import javax.persistence.EntityManager;


public class RepositorioPrestaciones implements WithSimplePersistenceUnit {

  private EntityManager entityManager;
  public RepositorioPrestaciones(EntityManager entityManager){
    this.entityManager = entityManager;
  }

  public PrestacionDeServicio buscarPorId(Integer id) {
    return this.entityManager.find(PrestacionDeServicio.class, id);
  }

}
