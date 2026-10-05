public class Tortillas {
    private static final Object sarten = new Object();
    private static final Object espatula = new Object();

    // cuándo llega Bruno a la cocina
    private static final int BRUNO_LLEGA_MS = 4000;

    private static int tortillas = 0;

    public static void main(String[] args) throws InterruptedException {
        long inicio = System.nanoTime();
        Thread ana = new Thread(() -> cocinar(sarten, espatula), "ana");
        Thread bruno = new Thread(() -> {
            dormir(BRUNO_LLEGA_MS);
            // Bruno coge en el MISMO orden que Ana: la sartén primero
            cocinar(sarten, espatula);
        }, "bruno");
        ana.start();
        bruno.start();

        // espera a los dos, como mucho ocho segundos en total
        long limite = inicio + 8_000_000_000L;
        for (Thread cocinero : new Thread[] {ana, bruno}) {
            long quedan = (limite - System.nanoTime()) / 1_000_000;
            cocinero.join(Math.max(1, quedan));
        }

        if (ana.isAlive() || bruno.isAlive()) {
            System.out.println("Ana está en " + ana.getState()
                    + " y Bruno en " + bruno.getState()
                    + ": cada uno espera lo que tiene el otro");
        } else {
            long segundos =
                    (System.nanoTime() - inicio) / 1_000_000_000L;
            System.out.println(tortillas + " tortillas a los "
                    + segundos + " s");
        }
    }

    // coge lo primero, se da la vuelta, y después coge lo segundo
    private static void cocinar(Object primero, Object segundo) {
        synchronized (primero) {
            dormir(1000);
            synchronized (segundo) {
                dormir(2000);  // con las dos: la tortilla
                tortillas++;
            }
        }
    }

    private static void dormir(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
