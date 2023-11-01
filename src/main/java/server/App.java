package server;

import models.Main.PersistidorInicial;

import java.io.IOException;

public class App {
    public static void main(String[] args) throws IOException {
        new PersistidorInicial().start();
        Server.init();
    }
}