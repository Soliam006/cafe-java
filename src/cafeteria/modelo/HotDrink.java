package cafeteria.modelo;

public class HotDrink implements Extras{
    private String cupSize;
    private String milkType;
    private boolean extraShot;

    public HotDrink(String cupSize, String milkType, boolean extraShot) {
        this.cupSize = cupSize;
        this.milkType = milkType;
        this.extraShot = extraShot;
    }

    private  double calculatePrice(){
        double price = 0.5; // Base price for a hot drink

        switch (cupSize) {
            case "Pequeño":
                price += 1.25;
                break;
            case "Mediano":
                price += 1.75;
                break;
            case "Grande":
                price += 2.5;
                break;
        }

        switch (milkType) {
            case "Entera":
            case "Desnatada":
            case "Semidesnatada":
            case "Leche de vaca":
                price += 0;
                break;
            case "Almendra":
            case "Coco":
            case "Avena":
            case "Soja":
                price += 0.25;
                break;
        }

        if (extraShot) {
            price += 0.5; // Additional cost for extra shot
        }

        return price;
    }

    @Override
    public void extra(String extras) {

    }

    @Override
    public boolean quitExtra(String extras) {
        return false;
    }

    @Override
    public Size getSize() {
        return null;
    }

    @Override
    public double calcularExtra() {
        return 0;
    }
}
