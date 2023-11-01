package models.entities.Contrasenias;

public class ValidadorLongitud implements Validador {
    public Boolean esValida(String _usuario, String contrasenia) {
        return contrasenia.length() >= 8;
    }
}