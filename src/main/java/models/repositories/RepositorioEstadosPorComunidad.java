package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.entities.Comunidad.Miembro;
import models.entities.Incidente.EstadoPorComunidad;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.List;

public class RepositorioEstadosPorComunidad implements WithSimplePersistenceUnit {

    private EntityManager entityManager;
    public RepositorioEstadosPorComunidad(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void agregar(EstadoPorComunidad estadoPorComunidad) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.persist(estadoPorComunidad);
        tx.commit();
    }

    public void eliminar(EstadoPorComunidad estadoPorComunidad) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.remove(estadoPorComunidad);
        tx.commit();
    }

    public void actualizar(EstadoPorComunidad estadoPorComunidad) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.merge(estadoPorComunidad);
        tx.commit();
    }

    public EstadoPorComunidad buscarPorId(long id) {
        return this.entityManager.find(EstadoPorComunidad.class, id);
    }

    public EstadoPorComunidad buscarPorIdIncidente(long idIncidente) {

        EstadoPorComunidad unEstadoPorComunidad = (EstadoPorComunidad) this.entityManager
                .createQuery("from " + EstadoPorComunidad.class.getName() + " where idIncidente = :idIncidente")
                .setParameter("idIncidente", idIncidente)
                .getSingleResult();

        return unEstadoPorComunidad;
    }



    public List<EstadoPorComunidad> buscarTodos() {
        return this.entityManager.createQuery("from " + EstadoPorComunidad.class.getName()).getResultList();
    }
}
