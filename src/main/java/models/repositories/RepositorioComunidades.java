package models.repositories;

import models.entities.comunidad.Comunidad;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.*;

import java.util.List;

public class RepositorioComunidades implements WithSimplePersistenceUnit {
    private EntityManager entityManager;
    public RepositorioComunidades(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void agregar(Comunidad unaComunidad) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.persist(unaComunidad);
        tx.commit();
    }

    public void eliminar(Comunidad unaComunidad) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.remove(unaComunidad);
        tx.commit();
    }

    public void actualizar(Comunidad unaComunidad) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.merge(unaComunidad);
        tx.commit();
    }

    public void deshabilitarComunidadPorId(Integer id) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();

        Comunidad comunidad = this.entityManager.find(Comunidad.class, id);
        if (comunidad != null) {
            comunidad.setEstaHabilitada(0);
        }

        tx.commit();
    }

    public Comunidad buscarPorId(long id) {
        return this.entityManager.find(Comunidad.class, id);
    }

    public Comunidad buscarPorNombre(String nombre) {
        try {
            Comunidad unaComunidad = (Comunidad) this.entityManager
                    .createQuery("from " + Comunidad.class.getName() + " where nombre = :nombre and estaHabilitada = 1 and deleted = false ")
                    .setParameter("nombre", nombre)
                    .getSingleResult();

            return unaComunidad;
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<Comunidad> buscarTodos() {
        return this.entityManager.createQuery("from " + Comunidad.class.getName() + " where deleted = false ").getResultList();
    }
}
