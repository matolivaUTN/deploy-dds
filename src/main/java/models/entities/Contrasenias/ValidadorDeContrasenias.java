package models.entities.contrasenias;

import java.util.List;

public class    ValidadorDeContrasenias {
    List<Validador> validadores;
    public ValidadorDeContrasenias(List<Validador> validadores) {
        this.validadores = validadores;
    }

    public Boolean esValida(String usuario, String contrasenia) {
        return this.validadores.stream().allMatch(validador -> validador.esValida(usuario, contrasenia));
    }

}

