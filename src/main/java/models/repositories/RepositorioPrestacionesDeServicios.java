package models.repositories;

import models.entities.servicio.PrestacionDeServicio;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioPrestacionesDeServicios implements WithSimplePersistenceUnit {

    private EntityManager entityManager;
    public RepositorioPrestacionesDeServicios(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void agregar(PrestacionDeServicio unaPrestacionDeServicio) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.persist(unaPrestacionDeServicio);
        tx.commit();
    }

    public void eliminar(PrestacionDeServicio unaPrestacionDeServicio) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.remove(unaPrestacionDeServicio);
        tx.commit();
    }

    public void actualizar(PrestacionDeServicio unaPrestacionDeServicio) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.merge(unaPrestacionDeServicio);
        tx.commit();
    }

    public PrestacionDeServicio buscarPorId(long id) {
        return this.entityManager.find(PrestacionDeServicio.class, id);
    }

    public List<PrestacionDeServicio> buscarTodos() {
        return this.entityManager.createQuery("from " + PrestacionDeServicio.class.getName()).getResultList();
    }
}