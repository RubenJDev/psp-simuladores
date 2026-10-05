import java.util.Arrays;

public class Cena {
    private static final String[] RECADOS = {"pan", "bebida", "postre"};
    private static final int[] SEGUNDOS = {2, 3, 1};

    public static void main(String[] args) throws InterruptedException {
        String[] cesta = new String[RECADOS.length];
        Thread[] amigos = new Thread[RECADOS.length];
        for (int i = 0; i < RECADOS.length; i++) {
            final int n = i;
            // crear el hilo no lo arranca: está en NEW
            amigos[n] = new Thread(() -> {
                dormir(SEGUNDOS[n]);  // ir a la tienda: TIMED_WAITING
                cesta[n] = RECADOS[n];
            }, "amigo-" + RECADOS[n]);
        }

        long inicio = System.nanoTime();
        // run() NO arranca el hilo: ejecuta el recado aquí, en main,
        // uno detrás de otro, y los amigos se quedan en casa (NEW)
        for (Thread amigo : amigos) {
            amigo.run();
        }
        // aquí no hace falta esperar: los recados ya los has hecho tú
        long segundos = (System.nanoTime() - inicio) / 1_000_000_000L;

        System.out.println("A cenar con " + Arrays.toString(cesta)
                + " a los " + segundos + " s");
        System.out.println("El amigo del pan está en "
                + amigos[0].getState());
    }

    private static void dormir(int segundos) {
        try {
            Thread.sleep(segundos * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
