import java.nio.file.Path;

public class Taxi {
    // si el taxi llega, o se queda por el camino para siempre
    private static final boolean LLEGA = true;

    private static final String JAVA =
            Path.of(System.getProperty("java.home"), "bin", "java")
                    .toString();

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("taxi")) {
            venir();
            return;
        }

        // el taxi es este mismo programa, lanzado como proceso hijo
        Process taxi = new ProcessBuilder(JAVA, "-cp",
                System.getProperty("java.class.path"),
                "Taxi", "taxi")
                .inheritIO()
                .start();

        // esperas lo que haga falta: si no llega, no sales de aquí
        taxi.waitFor();
        System.out.println("Al aeropuerto.");
    }

    private static void venir() throws InterruptedException {
        if (LLEGA) {
            Thread.sleep(2000);
            System.out.println("Taxi: estoy en la puerta.");
        } else {
            Thread.sleep(Long.MAX_VALUE);  // no llega nunca
        }
    }
}
