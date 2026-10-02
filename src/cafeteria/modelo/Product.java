package cafeteria.modelo;

public abstract class Product {

    private final String name;
    private final double price;

    private final Category category;

    //Constructor
    public Product(String name, double price, Category category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    //Getters
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Category getCategory() {
        return category;
    }

    //CALCULAR PRECIO FINAL
    public abstract double calcularPrecioFinal();


    /*
    public abstract String describirse();
     */

    //toString()
    public String toString() {
        return "Producto: " + name + ", Precio: " +
                price + ", Categoría: " + category.getNameCategory();
    }

}
