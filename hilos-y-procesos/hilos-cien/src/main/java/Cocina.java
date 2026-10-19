public class Cocina {
    public static void main(String[] args) throws InterruptedException {
        long inicio = System.nanoTime();
        for (int i = 0; i < 100; i++) {
            // otro cocinero en la misma cocina: un hilo
            Thread cocinero = new Thread(() -> { });
            cocinero.start();
            cocinero.join();
        }
        long us = (System.nanoTime() - inicio) / 1_000;

        System.out.println("100 hilos: " + us / 1000.0
                + " ms en total, " + us / 100
                + " microsegundos cada uno");
    }
}
