public class Taquilla {
    private int entradas = 1;

    // sin cerrojo: las dos taquillas pueden entrar a la vez
    public boolean vender() {
        if (entradas > 0) {      // mirar
            // entre estas dos líneas se cuela la otra taquilla
            entradas--;          // vender
            return true;
        }
        return false;
    }
}
