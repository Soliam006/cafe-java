package cafeteria.modelo;

public enum Sweet {
    BROWNIE("Brownie", 2.50),
    DONUT("Donut", 1.75),
    MUFFIN("Muffin", 2.00),
    CUPCAKE("Cupcake", 2.25),
    COOKIE("Cookie", 1.50),
    CHOCOLATE_CAKE("Chocolate Bar", 3.00),
    CINNAMON_ROLL("Cinnamon Roll", 2.25),
    RED_VELVET_CAKE("Red Velvet Cake", 3.50);

    private final String nameSweet;
    private final double priceSweet;


    Sweet(String nameSweet, double priceSweet) {
        this.nameSweet = nameSweet;
        this.priceSweet = priceSweet;
    }

    public String getNameSweet() { return nameSweet;}
    public double getPriceSweet() { return priceSweet;}

}
