import java.util.ArrayDeque;
import java.util.Deque;

public class Obrador {
    private static final int CABEN = 2;
    private static final int BARRAS = 5;
    // lo que tarda el horno en sacar una barra, y en venderse una
    private static final int HORNO_MS = 1000;
    private static final int VENTA_MS = 500;

    // el mostrador, vigilado a mano con su cerrojo
    private static final Deque<Integer> mostrador = new ArrayDeque<>();
    private static final Object cerrojo = new Object();
    private static int maximo = 0;
    private static int vendidas = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread panadero = new Thread(Obrador::hornear, "panadero");
        Thread dependienta = new Thread(Obrador::vender, "dependienta");
        panadero.start();
        dependienta.start();
        panadero.join();
        dependienta.join();
        System.out.println("Vendidas " + vendidas + " de " + BARRAS
                + "; en el mostrador llegó a haber " + maximo
                + " (caben " + CABEN + ")");
    }

    private static void hornear() {
        try {
            for (int barra = 1; barra <= BARRAS; barra++) {
                Thread.sleep(HORNO_MS);
                dejar(barra);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void vender() {
        try {
            Thread.sleep(750);  // abre la tienda
            for (int i = 0; i < BARRAS; i++) {
                coger();
                vendidas++;
                Thread.sleep(VENTA_MS);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // el panadero espera hueco y la dependienta espera barra; cada
    // cambio despierta a quien esté esperando (while, no if)
    private static void dejar(int barra) throws InterruptedException {
        synchronized (cerrojo) {
            while (mostrador.size() == CABEN) {
                cerrojo.wait();
            }
            mostrador.addLast(barra);
            maximo = Math.max(maximo, mostrador.size());
            cerrojo.notifyAll();
        }
    }

    private static int coger() throws InterruptedException {
        synchronized (cerrojo) {
            while (mostrador.isEmpty()) {
                cerrojo.wait();
            }
            int barra = mostrador.removeFirst();
            cerrojo.notifyAll();
            return barra;
        }
    }
}
