package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.Collections;
import java.util.Comparator;

import models.entities.georef.entities.Municipio;
import models.entities.georef.entities.Provincia;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.List;

public class RepositorioMunicipios implements WithSimplePersistenceUnit {

    private EntityManager entityManager;
    public RepositorioMunicipios(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void agregar(Municipio unMunicipio) {
        try {
            EntityTransaction tx = this.entityManager.getTransaction();
            tx.begin();
            this.entityManager.persist(unMunicipio);
            tx.commit();
        } catch (Exception e) {
            System.out.println("ERROR");
        }
    }

    public void eliminar(Municipio unMunicipio) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.remove(unMunicipio);
        tx.commit();
    }

    public void actualizar(Municipio unMunicipio) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.merge(unMunicipio);
        tx.commit();
    }

    public Municipio buscarPorId(long id) {
        return this.entityManager.find(Municipio.class, id);
    }

    public Municipio buscarPorNombre(String nombre) {
        try {
            Municipio unMunicipio = (Municipio) this.entityManager
                    .createQuery("from " + Municipio.class.getName() + " where nombre = :nombre")
                    .setParameter("nombre", nombre)
                    .getSingleResult();

            return unMunicipio;
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<Municipio> buscarTodos() {
        List<Municipio> municipios = this.entityManager.createQuery("from " + Municipio.class.getName()).getResultList();
        return municipios.stream().sorted(Comparator.comparing(Municipio::getNombre)).toList();
    }

    public List<Municipio> buscarMunicipiosDeProvincia(Provincia provincia) {
        try {
            return entityManager.createQuery("from Municipio m where m.provincia = :provincia", Municipio.class)
                    .setParameter("provincia", provincia)
                    .getResultList();
        } catch (Exception e) {
            System.out.println(e);
            return Collections.emptyList();
        }
    }

}
