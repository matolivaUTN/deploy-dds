package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.entities.Localizacion.Localizacion;
import models.entities.georef.entities.Municipio;
import models.entities.georef.entities.Provincia;
import org.hibernate.engine.loading.internal.LoadContexts;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.List;

public class RepositorioLocalizaciones implements WithSimplePersistenceUnit {

    private EntityManager entityManager;
    public RepositorioLocalizaciones(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void agregar(Localizacion unaLocalizacion) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.persist(unaLocalizacion);
        tx.commit();
    }

    public void eliminar(Localizacion unaLocalizacion) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.remove(unaLocalizacion);
        tx.commit();
    }

    public void actualizar(Localizacion unaLocalizacion) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.merge(unaLocalizacion);
        tx.commit();
    }

    public Localizacion buscarPorId(long id) {
        return this.entityManager.find(Localizacion.class, id);
    }

    public Localizacion buscarPorNombre(String nombre) {
        try {
            Localizacion unaLocalizacion = (Localizacion) this.entityManager
                    .createQuery("from " + Localizacion.class.getName() + " where nombre = :nombre")
                    .setParameter("nombre", nombre)
                    .getSingleResult();

            return unaLocalizacion;
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<Localizacion> buscarTodos() {
        return this.entityManager.createQuery("from " + Localizacion.class.getName()).getResultList();
    }


}
