public class Aforo {
    // cada portero cuenta en su libreta: no comparten nada
    private static final int[] libreta = new int[2];

    public static void main(String[] args)
            throws InterruptedException {
        // cuánta gente llega a cada puerta: java Aforo 10
        int porPuerta = args.length > 0
                ? Integer.parseInt(args[0]) : 100_000;
        long inicio = System.nanoTime();

        Thread norte = new Thread(
                () -> libreta[0] = abrir(porPuerta), "norte");
        Thread sur = new Thread(
                () -> libreta[1] = abrir(porPuerta), "sur");
        norte.start();
        sur.start();
        // hasta que vuelven los dos, nadie sabe el total
        norte.join();
        sur.join();
        // después de join() se ven las dos libretas: se suman
        int pizarra = libreta[0] + libreta[1];

        long ms = (System.nanoTime() - inicio) / 1_000_000;
        System.out.println("Han entrado " + 2 * porPuerta
                + " y la pizarra dice " + pizarra
                + " (" + ms + " ms)");
    }

    // la libreta de esta puerta: una variable local, solo suya
    private static int abrir(int personas) {
        int cuenta = 0;
        for (int i = 0; i < personas; i++) {
            cuenta++;
        }
        return cuenta;
    }
}
