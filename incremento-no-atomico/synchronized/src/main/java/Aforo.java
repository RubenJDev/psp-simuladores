public class Aforo {
    // la pizarra: una sola variable que apuntan los dos porteros
    private static int pizarra = 0;

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
                + " y la pizarra dice " + pizarra
                + " (" + ms + " ms)");
    }

    private static void abrir(int personas) {
        for (int i = 0; i < personas; i++) {
            apuntar();
        }
    }

    // synchronized: una sola tiza. Mientras un portero apunta, el
    // otro espera en la puerta del método (BLOCKED), en fila
    private static synchronized void apuntar() {
        pizarra++;
    }
}
