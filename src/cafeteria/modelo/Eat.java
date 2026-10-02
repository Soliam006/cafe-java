package cafeteria.modelo;

public enum Eat {
    ENSALADA ("Ensalada", 4.5),
    SANDWICH ("Sándwich", 3.75),
    HAMBURGUESA ("Hamburguesa", 6.0),
    PIZZA ("Pizza", 5.6),
    PASTA ("Pasta", 5.0),
    FRUTA ("Fruta", 1.5),
    YOGURT ("Yogurt", 2.0);

    private final String nameEat;
    private final double priceEat;

    Eat(String nameEat, double priceEat) {
        this.nameEat = nameEat;
        this.priceEat = priceEat;
    }

    public String getNameEat() { return nameEat; }
    public double getPriceEat() { return priceEat; }

}
