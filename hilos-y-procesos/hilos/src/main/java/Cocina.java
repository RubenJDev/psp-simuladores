public class Cocina {
    // una sola nevera: los dos hilos comparten la memoria del proceso
    private static int huevos = 12;

    public static void main(String[] args) throws InterruptedException {
        // Ana es otro hilo del MISMO proceso; Bruno es main
        Thread ana = new Thread(() -> {
            huevos = 0;  // Ana se lleva la última docena
            System.out.println("Ana: me he llevado los huevos");
        });
        ana.start();
        ana.join();  // Bruno abre la nevera cuando Ana ha terminado

        System.out.println("Bruno: quedan " + huevos + " huevos");
    }
}
