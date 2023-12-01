package org.example;

import models.entities.contrasenias.Validador10kContraseniasMasUsadas;
import models.entities.contrasenias.ValidadorComplejidad;
import models.entities.contrasenias.ValidadorCredencialPorDefecto;
import models.entities.contrasenias.ValidadorDeContrasenias;

import models.entities.contrasenias.ValidadorLongitud;
import java.util.Arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * Unit test for simple App.
 */
public class ValidadorContraseniasTest {

    @Test
    @DisplayName("Contrasenia que esta en las 10000 contrasenias mas usadas")
    public void testContraseniaNoEstaEn10k() {
        ValidadorDeContrasenias validador = new ValidadorDeContrasenias(Arrays.asList(new Validador10kContraseniasMasUsadas()));
        Assertions.assertFalse(validador.esValida("hola", "password")); //Contrasenia en las 10k
        Assertions.assertFalse(validador.esValida("hola", "pussy69")); //Contrasenia en las 10k
    }

    @Test
    @DisplayName("Mismo usuario y contrasenia")
    public void testCredenciales() {
        ValidadorDeContrasenias validador = new ValidadorDeContrasenias(Arrays.asList(new ValidadorCredencialPorDefecto()));

        Assertions.assertFalse(validador.esValida("aguante_la_universidad_tecnologica_nacional", "aguante_la_universidad_tecnologica_nacional")); //Para las credenciales (usuario == contrasenia)
    }

    @Test
    @DisplayName("Contrasenia contiene menos de 8 caracteres")
    public void testLongitud() {
        ValidadorDeContrasenias validador = new ValidadorDeContrasenias(Arrays.asList(new ValidadorLongitud()));

        Assertions.assertFalse(validador.esValida("hgkjl", "powlfaw")); //no son 8 caracteres
    }

    @Test
    @DisplayName("Contrasenia con caracteres seguidos")
    public void testComplejidad() {
        ValidadorDeContrasenias validador = new ValidadorDeContrasenias(Arrays.asList(new ValidadorComplejidad()));

        Assertions.assertFalse(validador.esValida("esta_tampoco_es_una_contrasenia", "holeeeesoisjefoie")); //Para los caracteres iguales seguidos
        Assertions.assertFalse(validador.esValida("no_es_una_contrasenia", "afkjwnfwfn9832fjo23nfikwfnwoif12346aflndlaks")); //Para los caracteres consecutivos
    }

    @Test
    @DisplayName("Contrasenia valida")
    public void testContresniaBuena() {
        ValidadorDeContrasenias validador = new ValidadorDeContrasenias(Arrays.asList(new Validador10kContraseniasMasUsadas(), new ValidadorCredencialPorDefecto(), new ValidadorLongitud(), new ValidadorComplejidad(), new ValidadorComplejidad()));

        Assertions.assertTrue(validador.esValida("contrasenia_valida", "holacomoestas")); //CUMPLE
    }
}