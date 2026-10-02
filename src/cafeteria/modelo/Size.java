package cafeteria.modelo;

public enum Size {
    LITTLE("Pequeño", 1.25),
    MEDIUM("Mediano", 1.75),
    LARGE("Grande", 2.5);

    private final String sizeName;
    private final double price;

    //Constructor
    Size(String sizeName, double price) {
        this.sizeName = sizeName;
        this.price = price;
    }

    //Getters
    public String getSizeName() { return sizeName; }
public double getPrice() { return price; }

}
