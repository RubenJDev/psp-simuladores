public class Llaves {
    private static final String[] CASA =
            {"salón", "cocina", "baño", "dormitorio", "terraza"};

    private static int miradas = 0;

    public static void main(String[] args) throws InterruptedException {
        long inicio = System.nanoTime();
        Thread hermano = new Thread(Llaves::buscar, "hermano");
        hermano.start();

        Thread.sleep(3000);  // las encuentras tú, en el bolsillo
        hermano.interrupt();  // «¡déjalo, que ya las tengo!»

        hermano.join();
        long segundos = (System.nanoTime() - inicio) / 1_000_000_000L;
        System.out.println("Deja de buscar a los " + segundos + " s");
        System.out.println("Habitaciones miradas enteras: " + miradas);
    }

    private static void buscar() {
        for (String habitacion : CASA) {
            try {
                Thread.sleep(2000);  // mira la habitación entera
                miradas++;
            } catch (InterruptedException e) {
                // al saltar, Java borra el aviso: se vuelve a poner
                Thread.currentThread().interrupt();
            }
        }
    }
}
