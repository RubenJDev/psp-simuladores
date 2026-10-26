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

    // comprobar y sumar sin soltar el cerrojo: nadie mira mientras
    // el otro está entre los dos pasos
    private static synchronized boolean entrar() {
        if (dentro < AFORO) {
            dentro++;
            if (dentro > AFORO) {
                deMas++;
            }
            return true;
        }
        return false;
    }

    private static synchronized void salir() {
        dentro--;
    }
}
