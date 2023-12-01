package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.Comparator;

import models.entities.georef.entities.Provincia;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.List;

public class RepositorioProvincias implements WithSimplePersistenceUnit {

    private EntityManager entityManager;
    public RepositorioProvincias(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void agregar(Provincia unaProvincia) {
        try {
            EntityTransaction tx = this.entityManager.getTransaction();
            tx.begin();
            this.entityManager.persist(unaProvincia);
            tx.commit();
        } catch (Exception e) {
            System.out.println("HAY UN ERROR LCDTM: " + e.getMessage());
        }
    }

    public void eliminar(Provincia unaProvincia) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.remove(unaProvincia);
        tx.commit();
    }

    public void actualizar(Provincia unaProvincia) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.merge(unaProvincia);
        tx.commit();
    }

    public Provincia buscarPorId(long id) {
        return this.entityManager.find(Provincia.class, id);
    }

    public Provincia buscarPorNombre(String nombre) {
        try {
            Provincia unaProvincia = (Provincia) this.entityManager
                    .createQuery("from " + Provincia.class.getName() + " where nombre = :nombre")
                    .setParameter("nombre", nombre)
                    .getSingleResult();

            return unaProvincia;
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<Provincia> buscarTodos() {
        List<Provincia> provincias = this.entityManager.createQuery("from " + Provincia.class.getName()).getResultList();
        return provincias.stream().sorted(Comparator.comparing(Provincia::getNombre)).toList();
    }

    /*public List<Miembro> miembrosDeComunidad(long comunidadId) {
        String jpql = "SELECT m FROM Miembro m WHERE m.comunidad = :comunidadId";
        TypedQuery<Miembro> query = this.entityManager.createQuery(jpql, Miembro.class);
        query.setParameter("comunidadId", comunidadId);
        return query.getResultList();
    }*/


}
