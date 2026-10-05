import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Mudanza {
    private static final int[] KILOS = {12, 8, 15, 10, 20, 5};

    public static void main(String[] args) throws Exception {
        long inicio = System.nanoTime();
        // la cuadrilla: un número FIJO de hilos para todas las cajas
        ExecutorService cuadrilla = Executors.newFixedThreadPool(6);

        List<Future<Integer>> cajas = new ArrayList<>();
        for (int kilos : KILOS) {
            // submit() la deja en la cola: la baja el primer mozo libre
            cajas.add(cuadrilla.submit(() -> bajar(kilos)));
        }

        int total = 0;
        for (Future<Integer> caja : cajas) {
            total += caja.get();  // espera a que esa caja esté abajo
        }
        long segundos = (System.nanoTime() - inicio) / 1_000_000_000L;
        System.out.println(KILOS.length + " cajas, " + total
                + " kg, en " + segundos + " s");

        // shutdown(): cuando acaben lo que tienen, que se vayan
        cuadrilla.shutdown();
        cuadrilla.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("La cuadrilla se ha ido");
    }

    private static int bajar(int kilos) throws InterruptedException {
        Thread.sleep(1000);  // cada caja, un segundo escalera abajo
        return kilos;
    }
}
