package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.entities.servicio.PrestacionDeServicio;
import models.entities.servicio.Servicio;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioServicios implements WithSimplePersistenceUnit {

    private EntityManager entityManager;
    public RepositorioServicios(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void agregar(Servicio servicio) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.persist(servicio);
        tx.commit();
    }

    public void eliminar(Servicio servicio) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.remove(servicio);
        tx.commit();
    }

    public void actualizar(Servicio servicio) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.merge(servicio);
        tx.commit();
    }

    public Servicio buscarPorId(Integer id) {
        return this.entityManager.find(Servicio.class, id);
    }

    public List<PrestacionDeServicio> buscarTodos() {
        return this.entityManager.createQuery("from " + Servicio.class.getName()).getResultList();
    }
}