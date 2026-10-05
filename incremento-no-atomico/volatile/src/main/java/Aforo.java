public class Aforo {
    // volatile: cada portero ve al momento lo que escribe el otro...
    // pero leer, sumar y escribir siguen siendo tres pasos
    private static volatile int pizarra = 0;

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

    // con volatile sigue habiendo un hueco entre leer y escribir:
    // si el otro escribe entre medias, lo que escribes tú lo borra
    private static void apuntar() {
        pizarra++;
    }
}
