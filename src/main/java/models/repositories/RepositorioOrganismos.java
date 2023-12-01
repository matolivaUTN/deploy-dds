package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;

import models.entities.ServicioPublico.Organismo;
import models.entities.comunidad.Comunidad;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

public class RepositorioOrganismos implements WithSimplePersistenceUnit {

  private EntityManager entityManager;

  public RepositorioOrganismos(EntityManager entityManager) {
    this.entityManager = entityManager;
  }


  public void eliminar(Organismo unaPrestadora) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.remove(unaPrestadora);
    tx.commit();
  }

  public void actualizar(Organismo unOrganismo) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.merge(unOrganismo);
    tx.commit();
  }


  public void agregar(Organismo unOrganismo) {
    EntityTransaction tx = this.entityManager.getTransaction();
    tx.begin();
    this.entityManager.persist(unOrganismo);
    tx.commit();
  }

  public Organismo buscarPorId(long id) {
    return this.entityManager.find(Organismo.class, id);
  }

  public Organismo buscarPorNombreYContrasenia(String nombre, String password) {
    try {
      Organismo unOrganismo = (Organismo) this.entityManager
              .createQuery("from " + Organismo.class.getName() + " where nombre = :nombre and deleted = false ")
              .setParameter("nombre", nombre)
              //.setParameter("password", password) // Lo hace en el if de abajo
              .getSingleResult();

      if (BCrypt.checkpw(password, unOrganismo.getPassword())) {
        return unOrganismo;
      }

    } catch (NoResultException e) {
      return null;
    }
    return null;
  }

  public List<Organismo> buscarTodos() {
    return this.entityManager.createQuery("from " + Organismo.class.getName() + " where deleted = false ").getResultList();
  }


}