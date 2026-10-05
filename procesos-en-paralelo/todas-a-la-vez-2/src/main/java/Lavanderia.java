import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Lavanderia {
    // minutos de cada colada (aquí, un minuto dura un segundo)
    private static final int[] COLADAS = {3, 5};

    private static final String JAVA =
            Path.of(System.getProperty("java.home"), "bin", "java")
                    .toString();

    public static void main(String[] args) throws Exception {
        if (args.length == 2 && args[0].equals("lavadora")) {
            lavar(Integer.parseInt(args[1]));
            return;
        }
        long inicio = System.nanoTime();

        // pones TODAS en marcha...
        List<Process> lavadoras = new ArrayList<>();
        for (int minutos : COLADAS) {
            lavadoras.add(lavadora(minutos).start());
        }
        // ...y después esperas a cada una, en el orden en que las
        // pusiste: si una ya ha acabado, waitFor() vuelve enseguida
        for (Process lavadora : lavadoras) {
            lavadora.waitFor();
        }

        long segundos = (System.nanoTime() - inicio) / 1_000_000_000L;
        System.out.println("Todo lavado en " + segundos + " s");
    }

    // una lavadora es este mismo programa, lanzado como proceso hijo
    private static ProcessBuilder lavadora(int minutos) {
        return new ProcessBuilder(JAVA, "-cp",
                System.getProperty("java.class.path"),
                "Lavanderia", "lavadora", String.valueOf(minutos))
                .inheritIO();
    }

    private static void lavar(int minutos) throws InterruptedException {
        Thread.sleep(minutos * 1000L);
        System.out.println("Colada de " + minutos + " min lista");
    }
}
