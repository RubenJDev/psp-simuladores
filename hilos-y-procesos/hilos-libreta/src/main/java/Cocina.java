public class Cocina {
    // una sola nevera: los dos hilos comparten la memoria del proceso
    private static int huevos = 12;

    public static void main(String[] args) throws InterruptedException {
        // Ana es otro hilo del MISMO proceso; Bruno es main
        Thread ana = new Thread(() -> {
            // con «int» delante es OTRA huevos: local, en la pila
            // de Ana. La de la nevera ni se entera
            int huevos = 12;
            huevos = 0;  // Ana se lleva la docena
            System.out.println("Ana: en mi libreta quedan " + huevos);
        }, "ana");
        ana.start();
        ana.join();  // Bruno abre la nevera cuando Ana ha terminado

        System.out.println("Bruno: quedan " + huevos + " huevos");
    }
}
