package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.entities.ServicioPublico.Entidad;
import models.entities.roles.Rol;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.List;

public class RepositorioRoles implements WithSimplePersistenceUnit {

  private EntityManager entityManager;

  public RepositorioRoles(EntityManager entityManager){
    this.entityManager = entityManager;
  }


  public Rol buscarPorId(long id) {
      return this.entityManager.find(Rol.class, id);
    }

  public Rol buscarRolPorNombre(String nombre) {
    try {
      Rol rol = (Rol) this.entityManager
              .createQuery("from " + Rol.class.getName() + " where nombre = :nombre")
              .setParameter("nombre", nombre)
              .getSingleResult();

      return rol;
    }
    catch (NoResultException e) {
      return null;
    }
  }
}
