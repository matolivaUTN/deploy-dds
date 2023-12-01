package models.entities.contrasenias;

import java.util.Objects;

public class ValidadorCredencialPorDefecto implements Validador {
    public Boolean esValida(String usuario, String contrasenia) {

        return !Objects.equals(usuario, contrasenia);
    }
}
