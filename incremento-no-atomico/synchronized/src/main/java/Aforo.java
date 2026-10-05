public class Aforo {
    // la pizarra: una sola variable que apuntan los dos porteros
    private static int pizarra = 0;

    public static void main(String[] args) throws InterruptedException {
        // cuánta gente entra por cada puerta: java Aforo 10
        int porPuerta = args.length > 0
                ? Integer.parseInt(args[0]) : 100_000;

        Thread norte = new Thread(() -> abrir(porPuerta), "norte");
        Thread sur = new Thread(() -> abrir(porPuerta), "sur");
        norte.start();
        sur.start();
        norte.join();
        sur.join();

        System.out.println("Han entrado " + 2 * porPuerta
                + " y la pizarra dice " + pizarra);
    }

    private static void abrir(int personas) {
        for (int i = 0; i < personas; i++) {
            apuntar();
        }
    }

    // synchronized: un solo rotulador. Mientras un portero apunta,
    // el otro espera en la puerta del método (BLOCKED)
    private static synchronized void apuntar() {
        pizarra++;
    }
}
