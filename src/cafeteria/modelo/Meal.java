package cafeteria.modelo;

public class Meal {
    private String name;
    private double price;
    private int quantity;
    private boolean natural;
    private boolean heat;
    private boolean cold;

    public Meal(String name, double price, int quantity, boolean natural, boolean heat, boolean cold) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.natural = natural;
        this.heat = heat;
        this.cold = cold;
    }

    public double calculatePrice() {
        double finalPrice = price * quantity;

        if (heat || cold) {
            finalPrice += 0.25; // Additional cost for heating or cooling
        }



        return finalPrice;
    }
}
