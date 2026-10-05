public class Taquilla {
    private int entradas = 1;

    // el cerrojo en la puerta del método: mientras una está dentro,
    // la otra espera en la puerta
    public synchronized boolean vender() {
        if (entradas > 0) {      // mirar
            entradas--;          // vender: ahora nadie se cuela
            return true;
        }
        return false;
    }
}
