import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class Informe {
    // cuántas hojas escribe el hijo por su salida estándar
    private static final int HOJAS = 10;

    private static final String JAVA =
            Path.of(System.getProperty("java.home"), "bin", "java")
                    .toString();

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("hijo")) {
            escribirInforme();
            return;
        }

        // la salida del hijo va a un fichero, no a una tubería
        File destino = new File("informe.txt");
        Process hijo = new ProcessBuilder(JAVA, "-cp",
                System.getProperty("java.class.path"),
                "Informe", "hijo")
                .redirectOutput(destino)
                .start();

        // un fichero no se llena: esperar primero ya no atasca
        int codigo = hijo.waitFor();
        int leidas = Files.readAllLines(destino.toPath()).size();
        System.out.println(leidas + " hojas, código " + codigo);
    }

    private static void escribirInforme() {
        for (int i = 1; i <= HOJAS; i++) {
            System.out.println("hoja " + i);
        }
    }
}
