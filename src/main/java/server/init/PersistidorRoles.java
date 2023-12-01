package server.init;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.entities.roles.Permiso;
import models.entities.roles.Rol;
import models.entities.roles.TipoRol;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.io.IOException;

public class PersistidorRoles implements WithSimplePersistenceUnit {

    private EntityManager entityManager;

    public PersistidorRoles(EntityManager entityManager) {
      this.entityManager = entityManager;

    }

    public void start() throws IOException {

      EntityTransaction tx = this.entityManager.getTransaction();
      tx.begin();
      persistirPermisos();
      persistirRoles();
      tx.commit();
    }

    private void persistirPermisos() {

      String[][] permisos = {
              { "Eliminar comunidad", "eliminar_comunidad" },
              { "Editar comunidad", "editar_comunidad" }
      };

      for(String[] unPermiso: permisos) {
        Permiso permiso = new Permiso();
        permiso.setNombre(unPermiso[0]);
        permiso.setNombreInterno(unPermiso[1]);
        this.entityManager.persist(permiso);
      }

    }

    public void persistirRoles() throws IOException {

      // Rol ADMINISTRADOR -> carga de datos
      Rol administrador = new Rol();
      administrador.setNombre("Administrador");
      administrador.setTipo(TipoRol.ADMINISTRADOR);
      entityManager.persist(administrador);

      // Rol USUARIO MIEMBRO -> puede hacer everything menos carga de datos ni tampoco administrar comunidades
      Rol miembro = new Rol();
      miembro.setNombre("Miembro");
      miembro.setTipo(TipoRol.MIEMBRO);
      entityManager.persist(miembro);

      Rol prestadora = new Rol();
      prestadora.setNombre("Prestadora");
      prestadora.setTipo(TipoRol.EMPRESA);
      entityManager.persist(prestadora);

      Rol organismo = new Rol();
      organismo.setNombre("Organismo");
      organismo.setTipo(TipoRol.EMPRESA);
      entityManager.persist(organismo);

    }

  public Permiso buscarPermisoPorNombre(String nombre) {
    try {
      Permiso unPermiso = (Permiso) this.entityManager
              .createQuery("from " + Permiso.class.getName() + " where nombreInterno = :nombre")
              .setParameter("nombre", nombre)
              .getSingleResult();

      return unPermiso;
    }
    catch (NoResultException e) {
      return null;
    }
  }

}


