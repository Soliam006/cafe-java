package cafeteria.modelo;

public enum Category {
    CAFE ("Café"),
    BEBIDA_FRIA ("Bebida fría"),
    REPOSTERIA ("Repostería"),
    SALADO ("Salado"),
    SNACK ("Snack"),
    OTRO ("Otro");

    private final String nameCategory;

    //Constructor
    Category(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    //Getters
    public String getNameCategory() { return nameCategory; }

}
