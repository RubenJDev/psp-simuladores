import java.util.concurrent.atomic.AtomicInteger;

public class Aforo {
    // un contador que lee, suma y escribe en una sola operación
    private static final AtomicInteger pizarra = new AtomicInteger();

    public static void main(String[] args)
            throws InterruptedException {
        // cuánta gente llega a cada puerta: java Aforo 10
        int porPuerta = args.length > 0
                ? Integer.parseInt(args[0]) : 100_000;
        long inicio = System.nanoTime();

        Thread norte = new Thread(() -> abrir(porPuerta), "norte");
        Thread sur = new Thread(() -> abrir(porPuerta), "sur");
        norte.start();
        sur.start();
        norte.join();
        sur.join();

        long ms = (System.nanoTime() - inicio) / 1_000_000;
        System.out.println("Han entrado " + 2 * porPuerta
                + " y la pizarra dice " + pizarra.get()
                + " (" + ms + " ms)");
    }

    private static void abrir(int personas) {
        for (int i = 0; i < personas; i++) {
            apuntar();
        }
    }

    // incrementAndGet(): leer, sumar y escribir sin que nadie se
    // cuele entre medias, y sin que el otro espere un cerrojo
    private static void apuntar() {
        pizarra.incrementAndGet();
    }
}
