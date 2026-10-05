import java.nio.file.Path;

public class Recado {
    // la panadería: si hoy está abierta o cerrada
    private static final boolean PANADERIA_ABIERTA = false;

    private static final String JAVA =
            Path.of(System.getProperty("java.home"), "bin", "java")
                    .toString();

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("hermano")) {
            irAPorPan();
            return;
        }

        // tu hermano es este mismo programa, lanzado otra vez como
        // proceso hijo: start() lo pone en marcha y vuelve enseguida
        Process hermano = new ProcessBuilder(JAVA, "-cp",
                System.getProperty("java.class.path"),
                "Recado", "hermano")
                .inheritIO()
                .start();

        ponerLaMesa();

        // te sientas sin esperarle: él sigue de camino, y este
        // programa acaba aunque su proceso siga vivo
        System.out.println("A comer. ¿Y el pan?");
    }

    private static void irAPorPan() throws InterruptedException {
        Thread.sleep(3000);  // ir a la panadería y volver: 3 s
        if (PANADERIA_ABIERTA) {
            System.out.println("Hermano: traigo el pan.");
            System.exit(0);
        }
        System.out.println("Hermano: estaba cerrada.");
        System.exit(1);
    }

    private static void ponerLaMesa() throws InterruptedException {
        Thread.sleep(1000);  // poner la mesa es más rápido: 1 s
        System.out.println("Mesa puesta.");
    }
}
