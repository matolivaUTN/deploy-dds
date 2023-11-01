package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.entities.ServicioPublico.Establecimiento;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioEstablecimientos implements WithSimplePersistenceUnit {

  private EntityManager entityManager;
  public RepositorioEstablecimientos(EntityManager entityManager){
    this.entityManager = entityManager;
  }

  public void agregar(Establecimiento unEstablecimiento) {
      EntityTransaction tx = this.entityManager.getTransaction();
      tx.begin();
      this.entityManager.persist(unEstablecimiento);
      tx.commit();
    }


    public void eliminar(Establecimiento unEstablecimiento) {
      EntityTransaction tx = this.entityManager.getTransaction();
      tx.begin();
      this.entityManager.remove(unEstablecimiento);
      tx.commit();
    }

    public void actualizar(Establecimiento unEstablecimiento) {
      EntityTransaction tx = this.entityManager.getTransaction();
      tx.begin();
      this.entityManager.merge(unEstablecimiento);
      tx.commit();
    }

    public Establecimiento buscarPorId(long id) {
      return this.entityManager.find(Establecimiento.class, id);
    }

    public List<Establecimiento> buscarTodos() {
      return this.entityManager.createQuery("from " + Establecimiento.class.getName()).getResultList();
    }

}

