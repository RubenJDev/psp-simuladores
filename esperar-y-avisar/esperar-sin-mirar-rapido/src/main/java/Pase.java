public class Pase {
    // lo que tarda el cocinero en sacar el plato
    private static final int COCINA_MS = 0;

    private static final Object pase = new Object();
    // el plato en el pase; volatile, para que quien se asome lo vea
    private static volatile boolean listo = false;

    public static void main(String[] args) throws InterruptedException {
        Thread camarero = new Thread(Pase::servir, "camarero");
        camarero.start();

        // main es el cocinero: saca el plato y toca la campanilla
        Thread.sleep(COCINA_MS);
        System.out.println("Cocinero: plato listo; el camarero está en "
                + camarero.getState());
        synchronized (pase) {
            listo = true;
            pase.notifyAll();
        }

        camarero.join(3000);
        if (camarero.isAlive()) {
            System.out.println("El camarero sigue en "
                    + camarero.getState() + " y el plato se enfría");
        }
    }

    private static void servir() {
        dormir(1000);  // pone la mesa
        synchronized (pase) {
            // se sienta a esperar la campanilla sin mirar el pase:
            // si ya ha sonado, no va a volver a sonar
            try {
                pase.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
        System.out.println("Camarero: a la mesa");
    }

    private static void dormir(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
