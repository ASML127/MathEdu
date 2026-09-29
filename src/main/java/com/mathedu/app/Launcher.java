package com.mathedu.app;

/**
 * Lanzador alternativo para entornos donde JavaFX se resuelve desde el
 * classpath (JAR ejecutable, depuradores que no fijan module-path).
 *
 * <p>El lanzador de la JDK comprueba que la clase principal extienda
 * {@link javafx.application.Application} y, si lo hace, exige que JavaFX
 * este en el module-path. Como aqui las dependencias viajan en el classpath
 * del JAR sombreado, se entra por esta clase, que no extiende Application,
 * y se delega en {@link MainApp#main(String[])}.
 */
public final class Launcher {

    private Launcher() {
    }

    public static void main(String[] args) {
        MainApp.main(args);
    }
}
