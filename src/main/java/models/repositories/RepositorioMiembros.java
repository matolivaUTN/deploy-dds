package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import models.entities.Comunidad.Miembro;

import javax.persistence.EntityTransaction;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import models.entities.Comunidad.Miembro;
import org.mindrot.jbcrypt.BCrypt;

public class RepositorioMiembros implements WithSimplePersistenceUnit {

    private EntityManager entityManager;
    public RepositorioMiembros(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void agregar(Miembro unMiembro) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.persist(unMiembro);
        tx.commit();
    }

    public void eliminar(Miembro unMiembro) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.remove(unMiembro);
        tx.commit();
    }

    public void actualizar(Miembro unMiembro) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.merge(unMiembro);
        tx.commit();
    }

    public Miembro buscarPorId(long id) {
        return this.entityManager.find(Miembro.class, id);
    }

    public Miembro buscarPorUsuarioOMail(String username, String email) {
        try {
            Miembro unMiembro = (Miembro) this.entityManager
                    .createQuery("from " + Miembro.class.getName() + " where username = :username or email = :email")
                    .setParameter("username", username)
                    .setParameter("email", email)
                    .getSingleResult();

            return unMiembro;
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public Miembro buscarPorUsuarioYContrasenia(String username, String password) {
        try {
            Miembro unMiembro = (Miembro) this.entityManager
                    .createQuery("from " + Miembro.class.getName() + " where username = :username")
                    .setParameter("username", username)
                    //.setParameter("password", password)
                    .getSingleResult();

            if(BCrypt.checkpw(password, unMiembro.getPassword())) {
                return unMiembro;
            }

        } catch (NoResultException e) {
            return null;
        }
        return null;
    }

    public List<Miembro> buscarTodos() {
        return this.entityManager.createQuery("from " + Miembro.class.getName()).getResultList();
    }
}