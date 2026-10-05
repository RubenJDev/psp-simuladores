import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Probadores {
    private static final int CLIENTAS = 6;
    // una ficha por probador; true: por orden de llegada
    private static final Semaphore fichas = new Semaphore(3, true);

    private static final AtomicInteger dentro = new AtomicInteger();
    private static final AtomicInteger maximo = new AtomicInteger();
    private static final AtomicInteger pasadas = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        long inicio = System.nanoTime();
        Thread[] clientas = new Thread[CLIENTAS];
        for (int i = 0; i < CLIENTAS; i++) {
            final int n = i + 1;
            clientas[i] = new Thread(() -> probarse(n), "clienta-" + n);
            clientas[i].start();
            Thread.sleep(100);  // llegan una detrás de otra
        }

        // espera a todas, como mucho doce segundos en total
        long limite = inicio + 12_000_000_000L;
        for (Thread clienta : clientas) {
            long quedan = (limite - System.nanoTime()) / 1_000_000;
            clienta.join(Math.max(1, quedan));
        }

        long segundos = (System.nanoTime() - inicio) / 1_000_000_000L;
        System.out.println("Han pasado " + pasadas.get() + " de "
                + CLIENTAS + "; a la vez, como mucho " + maximo.get());
        if (pasadas.get() < CLIENTAS) {
            System.out.println("La última sigue esperando ficha: "
                    + clientas[CLIENTAS - 1].getState());
        } else {
            System.out.println("Todas, en " + segundos + " s");
        }
    }

    private static void probarse(int n) {
        try {
            fichas.acquire();  // coge una ficha; si no quedan, espera
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
        entrar();
        if (n == 2) {
            dormir(1000);
            salir();
            return;  // se va a mitad… con la ficha en el bolsillo
        }
        dormir(2000);  // se prueba la ropa
        salir();
        fichas.release();  // solo llega aquí quien se prueba entera
    }

    private static void entrar() {
        maximo.accumulateAndGet(dentro.incrementAndGet(), Math::max);
    }

    private static void salir() {
        dentro.decrementAndGet();
        pasadas.incrementAndGet();
    }

    private static void dormir(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
