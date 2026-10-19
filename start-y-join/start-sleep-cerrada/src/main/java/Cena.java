import java.util.Arrays;

public class Cena {
    // la tienda de bebidas: si hoy está abierta o cerrada
    private static final boolean BEBIDA_ABIERTA = false;

    private static final String[] RECADOS =
            {"pan", "bebida", "postre"};
    private static final int[] SEGUNDOS = {2, 3, 1};

    public static void main(String[] args)
            throws InterruptedException {
        String[] cesta = new String[RECADOS.length];
        Thread[] amigos = new Thread[RECADOS.length];
        for (int i = 0; i < RECADOS.length; i++) {
            final int n = i;
            // crear el hilo no lo arranca: está en NEW
            amigos[n] = new Thread(() -> {
                cesta[n] = comprar(n);
            }, "amigo-" + RECADOS[n]);
        }

        long inicio = System.nanoTime();

        // start(): cada amigo sale a la vez, en su propio hilo
        for (Thread amigo : amigos) {
            amigo.start();
        }

        // esperar «un rato» no es esperarlos: sleep no sabe de ellos,
        // y no garantiza que veas lo que han dejado en la cesta
        Thread.sleep(2500);

        // cuánto has tardado en sentarte: segundos, con una décima
        double segundos = (System.nanoTime() - inicio) / 100_000_000
                / 10.0;

        System.out.println("A cenar con " + Arrays.toString(cesta)
                + " a los " + segundos + " s");
        for (Thread amigo : amigos) {
            System.out.println(amigo.getName() + " está en "
                    + amigo.getState());
        }
    }

    // el recado de un amigo: ir a la tienda, comprar y volver
    private static String comprar(int n) {
        dormir(SEGUNDOS[n]);  // ir a la tienda: TIMED_WAITING
        if (RECADOS[n].equals("bebida") && !BEBIDA_ABIERTA) {
            throw new IllegalStateException(
                    "la tienda de bebidas está cerrada");
        }
        System.out.println(Thread.currentThread().getName()
                + " vuelve con " + RECADOS[n]);
        return RECADOS[n];
    }

    private static void dormir(int segundos) {
        try {
            Thread.sleep(segundos * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
