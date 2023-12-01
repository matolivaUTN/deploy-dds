package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.entities.notificaciones.NotificacionPorMiembro;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.util.List;

public class RepositorioNotificacionPorMiembro implements WithSimplePersistenceUnit {

    private EntityManager entityManager;
    public RepositorioNotificacionPorMiembro(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void agregar(NotificacionPorMiembro notificacionPorMiembro) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.persist(notificacionPorMiembro);
        tx.commit();
    }

    public void eliminar(NotificacionPorMiembro notificacionPorMiembro) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.remove(notificacionPorMiembro);
        tx.commit();
    }

    public void actualizar(NotificacionPorMiembro notificacionPorMiembro) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.merge(notificacionPorMiembro);
        tx.commit();
    }

    public NotificacionPorMiembro buscarPorId(long id) {
        return this.entityManager.find(NotificacionPorMiembro.class, id);
    }

    public List<NotificacionPorMiembro> buscarTodos() {
        return this.entityManager.createQuery("from " + NotificacionPorMiembro.class.getName()).getResultList();
    }

    public List<NotificacionPorMiembro> buscarSinEnviar() {
        return this.entityManager.createQuery("from " + NotificacionPorMiembro.class.getName() + " where fueenviada = false ").getResultList();
    }

    public List<NotificacionPorMiembro> buscarPorIdIncidente(long idIncidente) {
        return this.entityManager
                .createQuery("from " + NotificacionPorMiembro.class.getName() + " where idIncidente = :idIncidente")
                .setParameter("idIncidente", idIncidente)
                .getResultList();
    }

    public List<NotificacionPorMiembro> buscarPorIdIncidente2(long idIncidente) {
        TypedQuery<NotificacionPorMiembro> query = this.entityManager.createQuery(
                "from " + NotificacionPorMiembro.class.getName() + " where idIncidente = :idIncidente",
                NotificacionPorMiembro.class
        );

        return query.setParameter("idIncidente", idIncidente).getResultList();
    }

}
