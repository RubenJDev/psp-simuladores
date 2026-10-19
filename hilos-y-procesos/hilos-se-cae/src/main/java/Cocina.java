public class Cocina {
    // una sola nevera: los dos hilos comparten la memoria del proceso
    private static int huevos = 12;

    public static void main(String[] args) throws InterruptedException {
        // Ana es otro hilo del MISMO proceso; Bruno es main
        Thread ana = new Thread(() -> {
            // de uno en uno… y a la mitad se cae
            for (int i = 0; i < 12; i++) {
                if (i == 6) {
                    throw new IllegalStateException("me he caído");
                }
                huevos--;
            }
            System.out.println("Ana: me he llevado los huevos");
        }, "ana");
        ana.start();
        ana.join();  // Bruno abre la nevera cuando Ana ha terminado

        System.out.println("Bruno: quedan " + huevos + " huevos");
    }
}
