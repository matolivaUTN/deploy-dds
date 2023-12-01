package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import models.entities.ServicioPublico.Entidad;
import models.entities.comunidad.Comunidad;

import java.util.List;

public class RepositorioEntidades  implements WithSimplePersistenceUnit {

  private EntityManager entityManager;
  public RepositorioEntidades(EntityManager entityManager){
    this.entityManager = entityManager;
  }


  public void agregar(Entidad unaEntidad) {
      EntityTransaction tx = this.entityManager.getTransaction();
      tx.begin();
      this.entityManager.persist(unaEntidad);
      tx.commit();
    }


    public void eliminar(Entidad unaEntidad) {
      EntityTransaction tx = this.entityManager.getTransaction();
      tx.begin();
      this.entityManager.remove(unaEntidad);
      tx.commit();
    }

    public void actualizar(Entidad unaEntidad) {
      EntityTransaction tx = this.entityManager.getTransaction();
      tx.begin();
      this.entityManager.merge(unaEntidad);
      tx.commit();
    }

    public Entidad buscarPorId(long id) {
      return this.entityManager.find(Entidad.class, id);
    }

  public List<Entidad> buscarTodos() {
    return this.entityManager.createQuery("from " + Entidad.class.getName() + " where deleted = false ").getResultList();
  }

  }
