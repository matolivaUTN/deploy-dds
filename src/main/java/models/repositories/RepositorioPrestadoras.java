package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.entities.ServicioPublico.Prestadora;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import javax.persistence.NoResultException;

public class RepositorioPrestadoras implements WithSimplePersistenceUnit {

    private EntityManager entityManager;
    public RepositorioPrestadoras(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void agregar(Prestadora unaPrestadora) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.persist(unaPrestadora);
        tx.commit();
    }

    public void eliminar(Prestadora unaPrestadora) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.remove(unaPrestadora);
        tx.commit();
    }

    public void actualizar(Prestadora unaPrestadora) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.merge(unaPrestadora);
        tx.commit();
    }

    public Prestadora buscarPorId(long id) {
        return this.entityManager.find(Prestadora.class, id);
    }

    public List<Prestadora> buscarTodos() {
        return this.entityManager.createQuery("from " + Prestadora.class.getName()).getResultList();
    }

    public Prestadora buscarPorNombreYContrasenia(String nombre, String password) {
        try {
            Prestadora unaPrestadora = (Prestadora) this.entityManager
                    .createQuery("from " + Prestadora.class.getName() + " where nombre = :nombre and deleted = false ")
                    .setParameter("nombre", nombre)
                    //.setParameter("password", password) // Lo hace en el if de abajo
                    .getSingleResult();

            if(BCrypt.checkpw(password, unaPrestadora.getPassword())) {
                return unaPrestadora;
            }

        } catch (NoResultException e) {
            return null;
        }
        return null;
    }
}