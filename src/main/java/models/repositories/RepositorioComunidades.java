package models.repositories;

import models.entities.Comunidad.Comunidad;
import models.entities.Comunidad.Miembro;
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
                    .createQuery("from " + Comunidad.class.getName() + " where nombre = :nombre")
                    .setParameter("nombre", nombre)
                    .getSingleResult();

            return unaComunidad;
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<Comunidad> buscarTodos() {
        return this.entityManager.createQuery("from " + Comunidad.class.getName()).getResultList();
    }

    /*public List<Miembro> miembrosDeComunidad(long comunidadId) {
        String jpql = "SELECT m FROM Miembro m WHERE m.comunidad = :comunidadId";
        TypedQuery<Miembro> query = this.entityManager.createQuery(jpql, Miembro.class);
        query.setParameter("comunidadId", comunidadId);
        return query.getResultList();
    }*/

    public List<Comunidad> comunidadesDeLasQueFormaParte(long miembroId) {
        // TODO: MAL!
        String jpql = "SELECT c FROM Comunidad c JOIN c.miembros mxc ON mxc.idMiembro = :miembroId";
        TypedQuery<Comunidad> query = this.entityManager.createQuery(jpql, Comunidad.class);
        query.setParameter("miembroId", miembroId);
        return query.getResultList();
    }
}
