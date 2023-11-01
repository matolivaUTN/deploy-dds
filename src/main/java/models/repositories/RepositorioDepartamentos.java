package models.repositories;

import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import java.util.Comparator;
import models.entities.Comunidad.Comunidad;
import models.entities.georef.entities.Departamento;
import models.entities.georef.entities.Municipio;
import models.entities.georef.entities.Provincia;

import javax.persistence.*;
import java.util.List;

public class RepositorioDepartamentos implements WithSimplePersistenceUnit {

    private EntityManager entityManager;
    public RepositorioDepartamentos(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    public void agregar(Departamento unDepartamento) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.persist(unDepartamento);
        tx.commit();
    }

    public void eliminar(Departamento unDepartamento) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.remove(unDepartamento);
        tx.commit();
    }

    public void actualizar(Departamento unDepartamento) {
        EntityTransaction tx = this.entityManager.getTransaction();
        tx.begin();
        this.entityManager.merge(unDepartamento);
        tx.commit();
    }


    public Departamento buscarPorId(long id) {
        return this.entityManager.find(Departamento.class, id);
    }

    public Departamento buscarPorNombre(String nombre) {
        try {
            Departamento unDepartamento = (Departamento) this.entityManager
                    .createQuery("from " + Departamento.class.getName() + " where nombre = :nombre")
                    .setParameter("nombre", nombre)
                    .getSingleResult();

            return unDepartamento;
        }
        catch (NoResultException e) {
            return null;
        }
    }

    public List<Departamento> buscarTodos() {
        List<Departamento> departamentos = this.entityManager.createQuery("from " + Departamento.class.getName()).getResultList();
        return departamentos.stream().sorted(Comparator.comparing(Departamento::getNombre)).toList();
    }


    public List<Departamento> buscarDepartamentosDeProvincia(Provincia provincia) {
        List<Departamento> departamentos = this.entityManager
                .createQuery("from " + Departamento.class.getName() + " where provincia = :provincia ")
                .setParameter("provincia", provincia)
                .getResultList();
        return departamentos.stream().sorted(Comparator.comparing(Departamento::getNombre)).toList();
    }
}