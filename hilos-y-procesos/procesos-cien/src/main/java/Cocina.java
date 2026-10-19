import java.nio.file.Path;

public class Cocina {
    private static final String JAVA =
            Path.of(System.getProperty("java.home"), "bin", "java")
                    .toString();

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            return;  // cada cocinero arranca y ya: se mide montarlo
        }

        long inicio = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            // una cocina entera para cada uno: otra JVM
            Process cocinero = new ProcessBuilder(JAVA, "-cp",
                    System.getProperty("java.class.path"),
                    "Cocina", "cocinero")
                    .start();
            cocinero.waitFor();
        }
        long ms = (System.nanoTime() - inicio) / 1_000_000;

        System.out.println("100 procesos: " + ms + " ms en total, "
                + ms / 100 + " ms cada uno");
    }
}
