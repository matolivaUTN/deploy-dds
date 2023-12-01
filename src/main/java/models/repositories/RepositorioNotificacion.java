package models.repositories;

import models.entities.notificaciones.Notificacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioNotificacion implements WithSimplePersistenceUnit {

    private EntityManager entityManager;
    public RepositorioNotificacion(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void agregar(Notificacion unaNotificacion) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.persist(unaNotificacion);
        tx.commit();
    }

    public void eliminar(Notificacion unaNotificacion) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.remove(unaNotificacion);
        tx.commit();
    }

    public void actualizar(Notificacion unaNotificacion) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.merge(unaNotificacion);
        tx.commit();
    }

    public Notificacion buscarPorId(long id) {
        return this.entityManager.find(Notificacion.class, id);
    }

    public List<Notificacion> buscarTodos() {
        return this.entityManager.createQuery("from " + Notificacion.class.getName()).getResultList();
    }
}