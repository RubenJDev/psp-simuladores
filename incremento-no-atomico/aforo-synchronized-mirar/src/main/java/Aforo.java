public class Aforo {
    // el aforo, y la hora punta: ya hay 999 dentro
    private static final int AFORO = 1000;
    private static int dentro = AFORO - 1;
    // cuántas veces ha habido más gente dentro que el aforo
    private static int deMas = 0;

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

    // mirar y sumar, cada uno en su synchronized: entre los dos se
    // suelta el cerrojo, y el otro portero puede mirar el mismo 999
    private static boolean entrar() {
        if (cabe()) {
            sumar();
            return true;
        }
        return false;
    }

    private static synchronized boolean cabe() {
        return dentro < AFORO;
    }

    private static synchronized void sumar() {
        dentro++;
        if (dentro > AFORO) {
            deMas++;
        }
    }

    private static synchronized void salir() {
        dentro--;
    }
}
