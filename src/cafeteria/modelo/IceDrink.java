package cafeteria.modelo;

public class IceDrink implements Extras{
    private String cupSize;
    private String milkType;
    private boolean extraShot;
    private boolean withIce;
    private boolean withSyrup;

    public IceDrink(String cupSize, String milkType, boolean extraShot, boolean withIce, boolean withSyrup) {
        this.cupSize = cupSize;
        this.milkType = milkType;
        this.extraShot = extraShot;
        this.withIce = withIce;
        this.withSyrup = withSyrup;
    }

    private double calculatePrice() {
        double price = 0.5; // Base price for an ice drink

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

        if (withIce) {
            price += 0.5; // Additional cost for extra ice
        }

        if (extraShot) {
            price += 0.5; // Additional cost for extra shot
        }

        if (withSyrup) {
            price += 0.75; // Additional cost for syrup
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
