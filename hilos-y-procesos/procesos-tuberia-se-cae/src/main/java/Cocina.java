import java.io.BufferedReader;
import java.nio.file.Path;

public class Cocina {
    // cada proceso tiene SU copia de esta variable: su propia nevera
    private static int huevos = 12;

    private static final String JAVA =
            Path.of(System.getProperty("java.home"), "bin", "java")
                    .toString();

    public static void main(String[] args) throws Exception {
        if (args.length > 0 && args[0].equals("ana")) {
            // de uno en uno… y a la mitad se cae
            for (int i = 0; i < 12; i++) {
                if (i == 6) {
                    throw new IllegalStateException("me he caído");
                }
                huevos--;
            }
            System.out.println("me he llevado los huevos");
            return;
        }

        // Ana es OTRO proceso. Sin inheritIO, lo que escribe no sale
        // en la consola: va por la tubería, y Bruno lo lee
        Process ana = new ProcessBuilder(JAVA, "-cp",
                System.getProperty("java.class.path"),
                "Cocina", "ana")
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start();
        String aviso;
        try (BufferedReader tuberia = ana.inputReader()) {
            aviso = tuberia.readLine();  // null si no ha dicho nada
        }
        int codigo = ana.waitFor();

        System.out.println("Bruno: Ana me dice: "
                + (aviso == null ? "nada" : aviso));
        System.out.println("Bruno: Ana ha acabado con el código "
                + codigo);
        System.out.println("Bruno: en mi nevera quedan " + huevos
                + " huevos");
    }
}
