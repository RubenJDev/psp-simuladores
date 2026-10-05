import java.nio.file.Path;

public class Cocina {
    // cada proceso tiene SU copia de esta variable: su propia nevera
    private static int huevos = 12;

    private static final String JAVA =
            Path.of(System.getProperty("java.home"), "bin", "java")
                    .toString();

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("ana")) {
            huevos = 0;  // Ana vacía SU nevera, no la de Bruno
            System.out.println("Ana: me he llevado los huevos");
            return;
        }

        // Ana es OTRO proceso: este mismo programa, lanzado otra vez
        Process ana = new ProcessBuilder(JAVA, "-cp",
                System.getProperty("java.class.path"),
                "Cocina", "ana")
                .inheritIO()
                .start();
        ana.waitFor();  // Bruno abre la nevera cuando Ana ha terminado

        // Bruno es el proceso padre, y mira en la suya
        System.out.println("Bruno: quedan " + huevos + " huevos");
    }
}
