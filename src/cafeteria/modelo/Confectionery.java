package cafeteria.modelo;

public class Confectionery {
    private String name;
    private double price;
    private boolean heat;
    private boolean cold;
    private int quantity;
    private boolean natural;


    public Confectionery(String name, double price, int quantity, boolean heat, boolean cold, int cupSize, String milkType) {
        this.name = name;
        this.price = price;
        this.heat = heat;
        this.cold = cold;
        this.quantity = quantity;
        this.natural = natural;
    }

    private double calculatePrice() {
        double finalPrice = price * quantity;

        if (heat || cold) {
            finalPrice += 0.25; // Additional cost for heating
        }

        if (natural) {finalPrice += 0; }

        return finalPrice;
    }

}
