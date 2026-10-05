public class Taquilla {
    private int entradas = 1;

    public boolean vender() {
        // el cerrojo que has puesto: mirar y vender son un solo tramo,
        // y mientras una taquilla está dentro, la otra espera fuera
        synchronized (this) {
            if (entradas > 0) {  // mirar
                entradas--;      // vender: ahora nadie se cuela
                return true;
            }
        }
        return false;
    }
}
