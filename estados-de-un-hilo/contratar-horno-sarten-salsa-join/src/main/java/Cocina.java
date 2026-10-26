import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class Cocina {
    // Los fogones son los núcleos: no los decide el programa.
    // Con uno solo, desde cmd: start /affinity 1 /b /wait java Cocina
    // con dos, /affinity 3; con cuatro, /affinity F.

    // la prioridad del arroz, de 1 a 10 (5 es Thread.NORM_PRIORITY
    // y 10, Thread.MAX_PRIORITY): una sugerencia al planificador
    static final int PRIORIDAD_ARROZ = 5;

    // la única sartén grande: un cerrojo
    private static final Object SARTEN = new Object();
    private static final ThreadMXBean CPU =
            ManagementFactory.getThreadMXBean();
    private static long inicio;

    public static void main(String[] args)
            throws InterruptedException {
        // contratar no es dar turno: los cuatro hilos están en NEW
        Thread arroz = new Thread(() -> {
            dormir(100);  // llega a la sartén un poco después
            synchronized (SARTEN) {  // cogida: BLOCKED
                cocinar(2);
            }
            acabar();
        }, "arroz");
        Thread asado = new Thread(() -> {
            hornear(3);  // en el horno: TIMED_WAITING
            acabar();
        }, "asado");
        Thread salsa = new Thread(() -> {
            synchronized (SARTEN) {  // cogida: BLOCKED
                cocinar(2);
            }
            acabar();
        }, "salsa");
        Thread pinche = new Thread(() -> {
            pelar(4);  // pelar patatas: TIMED_WAITING
            acabar();
        }, "pinche");
        Thread[] cocineros = {arroz, asado, salsa};
        Thread[] cocina = {arroz, asado, salsa, pinche};
        arroz.setPriority(PRIORIDAD_ARROZ);

        inicio = System.nanoTime();
        parte(Thread.currentThread(), cocina);
        // sin start(): nadie tiene turno, y siguen en NEW

        // esperar a la pinche: WAITING hasta que acaba
        pinche.join();
        System.out.println("La jefa deja de esperar a la pinche"
                + " a los " + reloj() + " s");

        // esperar a cada cocinero: WAITING hasta que acaba
        for (Thread t : cocineros) {
            t.join();
        }
        System.out.println("Servicio acabado a los " + reloj()
                + " s");
        System.out.println("Al cerrar: " + estados(cocina));
    }

    // el parte: cada segundo, a la media, el estado de cada hilo
    private static void parte(Thread jefa, Thread[] cocina) {
        Thread parte = new Thread(() -> {
            for (int i = 0; ; i++) {
                esperarHasta(i * 1000L + 500);
                System.out.println(i + ".5 s: " + jefa.getName()
                        + " " + jefa.getState() + ", "
                        + estados(cocina));
            }
        }, "parte");
        parte.setDaemon(true);  // no alarga el programa
        parte.start();
    }

    private static String estados(Thread[] hilos) {
        StringBuilder s = new StringBuilder();
        for (Thread t : hilos) {
            if (s.length() > 0) {
                s.append(", ");
            }
            s.append(t.getName()).append(' ').append(t.getState());
        }
        return s.toString();
    }

    // cocinar: usar el fogón (un núcleo) hasta sumar esos segundos
    private static void cocinar(int segundos) {
        long hasta = CPU.getCurrentThreadCpuTime()
                + segundos * 1_000_000_000L;
        while (CPU.getCurrentThreadCpuTime() < hasta) {
            // remover: trabajo de verdad, sin soltar el fogón
        }
    }

    private static void hornear(int segundos) {
        dormir(segundos * 1000L);
    }

    private static void pelar(int segundos) {
        dormir(segundos * 1000L);
    }

    // al acabar: cuánto ha durado, y cuánto de eso ha sido fogón
    private static void acabar() {
        double fogon = decimas(CPU.getCurrentThreadCpuTime());
        System.out.println(Thread.currentThread().getName()
                + " acaba a los " + reloj() + " s, con " + fogon
                + " s de fogón");
    }

    // segundos desde el principio, con una décima
    private static double reloj() {
        return decimas(System.nanoTime() - inicio);
    }

    private static double decimas(long nanos) {
        return Math.round(nanos / 100_000_000.0) / 10.0;
    }

    private static void esperarHasta(long ms) {
        long falta = ms - (System.nanoTime() - inicio) / 1_000_000;
        if (falta > 0) {
            dormir(falta);
        }
    }

    private static void dormir(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
