package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.entities.incidente.Incidente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class RepositorioIncidentes implements WithSimplePersistenceUnit {

    private EntityManager entityManager;
    public RepositorioIncidentes(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void agregar(Incidente unIncidente) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.persist(unIncidente);
        tx.commit();
    }

    public void eliminar(Incidente unIncidente) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.remove(unIncidente);
        tx.commit();
    }

    public Incidente buscarPorId(long id) {
        return this.entityManager.find(Incidente.class, id);
    }

    public List<Incidente> buscarTodos() {
        return this.entityManager.createQuery("from " + Incidente.class.getName(), Incidente.class).getResultList();
    }

    public List<Incidente> obtenerHistorialDeIncidentesDeMiembro(Integer idMiembro) {
        String jpql = "SELECT i FROM Incidente i WHERE i.miembroCreador.id = :miembroId OR i.estado.miembro.id = :miembroId";
        TypedQuery<Incidente> query = this.entityManager.createQuery(jpql, Incidente.class);
        query.setParameter("miembroId", idMiembro);
        return query.getResultList();
    }


}