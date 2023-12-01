package models.entities.contrasenias;

public class ValidadorComplejidad implements Validador {
    public Boolean esValida(String _usuario, String contrasenia) {
        return !contrasenia.matches(".*(.)\\1\\1\\1.*") && this.tieneCaracteresConsecutivos(_usuario, contrasenia);
    }

    public Boolean tieneCaracteresConsecutivos(String _usuario, String contrasenia) {
        int count = 1;
        for (int i = 0; i < contrasenia.length() - 1; i++) {
            if (Character.isLetterOrDigit(contrasenia.charAt(i)) && Character.isLetterOrDigit(contrasenia.charAt(i + 1)) &&
                    contrasenia.charAt(i + 1) - contrasenia.charAt(i) == 1) {
                count++;
                if (count == 4) {
                    return false;
                }
            } else {
                count = 1;
            }
        }
        return true;
    }
}


