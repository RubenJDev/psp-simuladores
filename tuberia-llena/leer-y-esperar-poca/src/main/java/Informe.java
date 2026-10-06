import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class Informe {
    // cuántas líneas escribe el hijo por su salida estándar
    private static final int LINEAS = 10;

    private static final String JAVA =
            Path.of(System.getProperty("java.home"), "bin", "java")
                    .toString();

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("hijo")) {
            escribirInforme();
            return;
        }

        // el hijo es este mismo programa; su salida estándar llega al
        // padre por una tubería, que tiene un tamaño limitado
        Process hijo = new ProcessBuilder(JAVA, "-cp",
                System.getProperty("java.class.path"),
                "Informe", "hijo")
                .start();

        // vacías la tubería mientras escribe, y esperas al final:
        // cuando readLine() devuelve null, el hijo ya ha cerrado
        int leidas = leer(hijo);
        int codigo = hijo.waitFor();
        System.out.println(leidas + " líneas, código " + codigo);
    }

    private static int leer(Process hijo) throws Exception {
        int leidas = 0;
        try (BufferedReader tuberia = new BufferedReader(
                new InputStreamReader(hijo.getInputStream()))) {
            while (tuberia.readLine() != null) {
                leidas++;
            }
        }
        return leidas;
    }

    private static void escribirInforme() {
        for (int i = 1; i <= LINEAS; i++) {
            System.out.println("linea " + i);
        }
    }
}
