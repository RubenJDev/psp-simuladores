import java.nio.file.Path;

public class Cocina {
    // cada proceso tiene SU copia de esta variable: su propia nevera
    private static int huevos = 12;

    private static final String JAVA =
            Path.of(System.getProperty("java.home"), "bin", "java")
                    .toString();

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("ana")) {
            // con «int» delante es OTRA huevos: local, en la pila
            // de Ana. La de la nevera ni se entera
            int huevos = 12;
            // de uno en uno… y a la mitad se cae
            for (int i = 0; i < 12; i++) {
                if (i == 6) {
                    throw new IllegalStateException("me he caído");
                }
                huevos--;
            }
            System.out.println("Ana: en mi libreta quedan " + huevos);
            return;
        }

        // Ana es OTRO proceso: este mismo programa, lanzado otra vez
        Process ana = new ProcessBuilder(JAVA, "-cp",
                System.getProperty("java.class.path"),
                "Cocina", "ana")
                .inheritIO()
                .start();
        int codigo = ana.waitFor();  // Bruno espera a que Ana acabe

        // Bruno es el proceso padre, y mira en la suya
        System.out.println("Bruno: Ana ha acabado con el código "
                + codigo);
        System.out.println("Bruno: quedan " + huevos + " huevos");
    }
}
