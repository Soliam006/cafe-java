package cafeteria.modelo;

public interface Extras {
    void extra (String extras);
    boolean quitExtra (String extras);

    Size getSize();

    double calcularExtra();

    // Un metodo "default" SÍ trae cuerpo. Quien implemente la interfaz lo recibe gratis,
    // y puede sobrescribirlo si le hace falta.
    default boolean haveExtrasLittle(){
        return calcularExtra() == Size.LITTLE.getPrice();
    }

    default boolean haveExtrasMedium(){
        return calcularExtra() == Size.MEDIUM.getPrice();
    }

    default boolean haveExtrasLarge(){
        return calcularExtra() == Size.LARGE.getPrice();
    }

}
