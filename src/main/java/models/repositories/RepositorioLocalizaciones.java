package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import models.entities.localizacion.Localizacion;
import models.entities.georef.entities.Departamento;
import models.entities.georef.entities.Municipio;
import models.entities.georef.entities.Provincia;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
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

    
    public Localizacion buscarPorCombinacion(Provincia provincia, Municipio municipio, Departamento departamento) {
        try {
            Query query =  this.entityManager
                    .createQuery("from " + Localizacion.class.getName() + " where idprovincia = :provincia " +
                            " and idmunicipio " + (municipio != null ? " = :municipio " : " is null ") +
                            " and iddepartamento " + (departamento != null ? " = :departamento " : " is null "))
                    .setParameter("provincia", provincia);
            if (municipio != null) {
                query.setParameter("municipio", municipio);
            }

            if (departamento != null) {
                query.setParameter("departamento", departamento);
            }

            Localizacion unaLocalizacion = (Localizacion) query.getSingleResult();

            return unaLocalizacion;
        }
        catch (NoResultException e) {
            Localizacion localizacion = new Localizacion(provincia, departamento, municipio);

            System.out.println("NO ENCONTRE LA LOCALIZACION, LA VOY A CREAR");
            agregar(localizacion);
            return localizacion;
        }
    }


}
