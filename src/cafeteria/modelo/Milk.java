package cafeteria.modelo;


public enum Milk {
    ENTERA("Entera", 0),
    DESNATADA("Desnatada", 0),
    SEMIDESNATADA("Semidesnatada", 0),
    ALMENDRA("Almendra", 0.25),
    COCO("Coco", 0.25),
    AVENA("Avena", 0.25),
    SOJA("Soja", 0.25);

    private final String name;
    private final double price;

    Milk(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; };


    public double getPrice() { return price; }
}


