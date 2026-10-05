import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;

public class Informe {
    // cuántas hojas escribe el hijo por su salida estándar
    private static final int HOJAS = 100_000;

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

        // primero esperas a que acabe, y solo después vacías la
        // tubería: si se llena antes, el hijo espera a que leas y
        // tú esperas a que acabe, y ninguno de los dos sale de aquí
        int codigo = hijo.waitFor();
        int leidas = leer(hijo);
        System.out.println(leidas + " hojas, código " + codigo);
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
        for (int i = 1; i <= HOJAS; i++) {
            System.out.println("hoja " + i);
        }
    }
}
