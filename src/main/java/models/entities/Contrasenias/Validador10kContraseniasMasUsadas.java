package models.entities.contrasenias;

import com.unicodelabs.jdp.core.DumbPassword;
import com.unicodelabs.jdp.core.exceptions.IsNullException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Validador10kContraseniasMasUsadas implements Validador {
    public Boolean esValida(String _usuario, String contrasenia) {
        return !this.estaEn10kContraseniasMasUsadas(contrasenia);
    }

    public Boolean estaEn10kContraseniasMasUsadas(String contrasenia) {
        final DumbPassword dumbPasswords = new DumbPassword();
        try {
            return dumbPasswords.checkPassword(contrasenia);
        } catch (IOException ex) {
            Logger.getLogger(ValidadorDeContrasenias.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IsNullException ex) {
            Logger.getLogger(ValidadorDeContrasenias.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true; //Por si va al catch
    }
}
