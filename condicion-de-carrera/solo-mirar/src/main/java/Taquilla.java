public class Taquilla {
    private int entradas = 1;

    // el cerrojo protege solo la mirada, y se suelta antes de vender:
    // la otra taquilla se cuela exactamente igual
    public boolean vender() {
        int quedan;
        synchronized (this) {    // mirar
            quedan = entradas;
        }
        if (quedan > 0) {        // aquí ya no hay cerrojo
            entradas--;          // vender
            return true;
        }
        return false;
    }
}
