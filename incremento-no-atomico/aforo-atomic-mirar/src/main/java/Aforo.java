import java.util.concurrent.atomic.AtomicInteger;

public class Aforo {
    // el aforo, y la hora punta: ya hay 999 dentro
    private static final int AFORO = 1000;
    private static final AtomicInteger dentro =
            new AtomicInteger(AFORO - 1);
    // cuántas veces ha habido más gente dentro que el aforo
    private static final AtomicInteger deMas = new AtomicInteger();

    public static void main(String[] args)
            throws InterruptedException {
        // cuánta gente llega a cada puerta: java Aforo 10
        int porPuerta = args.length > 0
                ? Integer.parseInt(args[0]) : 100_000;

        Thread norte = new Thread(() -> abrir(porPuerta), "norte");
        Thread sur = new Thread(() -> abrir(porPuerta), "sur");
        norte.start();
        sur.start();
        norte.join();
        sur.join();

        System.out.println("Han llegado " + 2 * porPuerta + " y "
                + deMas + " veces ha habido más de " + AFORO
                + " dentro");
    }

    // en hora punta, cada vez que entra alguien sale otro
    private static void abrir(int personas) {
        for (int i = 0; i < personas; i++) {
            if (entrar()) {
                salir();
            }
        }
    }

    // mira si cabe con get() y LUEGO suma: entre los dos pasos, el
    // otro portero puede mirar el mismo 999 y dejar pasar a otro
    private static boolean entrar() {
        if (dentro.get() < AFORO) {
            if (dentro.incrementAndGet() > AFORO) {
                deMas.incrementAndGet();
            }
            return true;
        }
        return false;
    }

    private static void salir() {
        dentro.decrementAndGet();
    }
}
