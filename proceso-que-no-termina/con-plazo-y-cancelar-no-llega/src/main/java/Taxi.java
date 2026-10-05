import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class Taxi {
    // si el taxi llega, o se queda por el camino para siempre
    private static final boolean LLEGA = false;

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

        // esperas cinco segundos como mucho: waitFor con plazo
        // devuelve false si se acaba el tiempo y el hijo sigue vivo
        boolean llego = taxi.waitFor(5, TimeUnit.SECONDS);
        if (llego) {
            System.out.println("Al aeropuerto.");
        } else {
            // no ha llegado: lo terminas, y esperas a que termine
            taxi.destroyForcibly();
            taxi.waitFor();
            System.out.println("Cancelado. Pido otro.");
        }
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
