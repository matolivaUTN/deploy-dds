package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import models.entities.Comunidad.Comunidad;
import models.entities.ServicioPublico.Organismo;

public class RepositorioOrganismos implements WithSimplePersistenceUnit {

  private EntityManager entityManager;
  public RepositorioOrganismos(EntityManager entityManager){
    this.entityManager = entityManager;
  }

  public void agregar(Organismo unOrganismo) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.persist(unOrganismo);
    tx.commit();
  }
}
