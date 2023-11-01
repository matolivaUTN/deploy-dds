package models.Main;

import models.entities.Comunidad.Miembro;
import models.entities.Notificaciones.AdapterAngusMail;
import models.entities.Notificaciones.CuandoSuceden;
import models.entities.Notificaciones.NotificadorEmail;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityTransaction;

public class MainExample implements WithSimplePersistenceUnit {

  public static void main(String[] args) {
    new MainExample().start();
  }


  private void start() {
    this.guardarUnMiembro("prueba");
  }

  private void guardarUnMiembro(String nombreMiembro) {
    Miembro miembro = new Miembro();
    miembro.setNombre(nombreMiembro);
    miembro.setApellido("prueba");
    miembro.setEmail("prueba");
    miembro.setNotificador(new NotificadorEmail(new AdapterAngusMail()));
    miembro.setEstrategiaDeAviso(new CuandoSuceden());



    EntityTransaction tx = entityManager().getTransaction();
    tx.begin();
    entityManager().persist(miembro); //INSERT INTO ....
    tx.commit();
  }

}
