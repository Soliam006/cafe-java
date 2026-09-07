package cafeteria.vista;

/**
 * Dibujos ASCII del juego: logo, tazas, clientes, barras de progreso...
 *
 * CAPA DE VISTA (ya escrita, no hace falta que la toques).
 *
 * Todos los dibujos son String[] (un elemento por linea) para poder
 * colocarlos unos al lado de otros con {@link #unirHorizontal}.
 */
public final class ArteAscii {

    private ArteAscii() { }

    // =====================================================================
    //  LOGO
    // =====================================================================

    public static final String[] LOGO_CAFE = {
        " ██████╗ █████╗ ███████╗███████╗",
        "██╔════╝██╔══██╗██╔════╝██╔════╝",
        "██║     ███████║█████╗  █████╗  ",
        "██║     ██╔══██║██╔══╝  ██╔══╝  ",
        "╚██████╗██║  ██║██║     ███████╗",
        " ╚═════╝╚═╝  ╚═╝╚═╝     ╚══════╝"
    };

    public static final String[] LOGO_JAVA = {
        "     ██╗ █████╗ ██╗   ██╗ █████╗ ",
        "     ██║██╔══██╗██║   ██║██╔══██╗",
        "     ██║███████║██║   ██║███████║",
        "██   ██║██╔══██║╚██╗ ██╔╝██╔══██║",
        "╚█████╔╝██║  ██║ ╚████╔╝ ██║  ██║",
        " ╚════╝ ╚═╝  ╚═╝  ╚═══╝  ╚═╝  ╚═╝"
    };

    // =====================================================================
    //  PRODUCTOS
    // =====================================================================

    public static final String[] TAZA = {
        "    ( (    ",
        "     ) )   ",
        "  .______. ",
        "  |      |]",
        "  |      | ",
        "  \\______/ ",
        "  '------' "
    };

    public static final String[] VASO_FRIO = {
        "   _______ ",
        "  |o  O  o|",
        "  | O  o  |",
        "  |o   O o|",
        "   \\_____/ ",
        "     | |   ",
        "    /___\\  "
    };

    public static final String[] BOLLERIA = {
        "           ",
        "    ,---.  ",
        "  ,'     '.",
        " /  .---.  \\",
        " | (     ) |",
        "  \\ '---' / ",
        "   '-----'  "
    };

    public static final String[] SALADO = {
        "           ",
        "   ______  ",
        "  /      \\ ",
        " |~~~~~~~~|",
        " |========|",
        "  \\______/ ",
        "           "
    };

    // =====================================================================
    //  CLIENTES
    // =====================================================================

    public static final String[] CLIENTE_CONTENTO = {
        "   .---.   ",
        "  / ^ ^ \\  ",
        "  |  ᵕ  |  ",
        "   \\___/   ",
        "  /|   |\\  ",
        "   |___|   ",
        "   |   |   "
    };

    public static final String[] CLIENTE_NEUTRO = {
        "   .---.   ",
        "  / o o \\  ",
        "  |  -  |  ",
        "   \\___/   ",
        "  /|   |\\  ",
        "   |___|   ",
        "   |   |   "
    };

    public static final String[] CLIENTE_ENFADADO = {
        "   .---.   ",
        "  / \\ / \\  ",
        "  |  ∩  |  ",
        "   \\___/   ",
        "  /|   |\\  ",
        "   |___|   ",
        "   |   |   "
    };

    public static final String[] CAJA_REGISTRADORA = {
        "   ______  ",
        "  |[][][]| ",
        "  |______| ",
        "  |  €€  | ",
        "  |______| "
    };

    public static final String[] CARTEL_CERRADO = {
        "  ┌───────────────┐  ",
        "  │    CERRADO    │  ",
        "  │   hasta las   │  ",
        "  │     08:00     │  ",
        "  └───────┬───────┘  "
    };

    // =====================================================================
    //  METODOS DE AYUDA
    // =====================================================================

    /** Imprime el logo completo del juego, centrado y en color. */
    public static void mostrarLogo() {
        Consola.salto();
        for (String l : LOGO_CAFE) Consola.imprimir(Ansi.marron(Consola.centrar(l, Consola.ANCHO)));
        for (String l : LOGO_JAVA) Consola.imprimir(Ansi.naranja(Consola.centrar(l, Consola.ANCHO)));
        Consola.salto();
        Consola.imprimir(Ansi.gris(Consola.centrar("~ la cafeteria donde el codigo se sirve caliente ~", Consola.ANCHO)));
        Consola.salto();
    }

    /**
     * Devuelve el dibujo que corresponde a una categoria de producto.
     * Acepta cualquier texto: "CAFE", "cafe con leche", "BEBIDA_FRIA", "reposteria"...
     */
    public static String[] iconoDeProducto(String categoriaOTexto) {
        String t = categoriaOTexto == null ? "" : categoriaOTexto.toLowerCase();
        if (t.contains("fri") || t.contains("hielo") || t.contains("granizad") || t.contains("batid")) return VASO_FRIO;
        if (t.contains("repost") || t.contains("boll") || t.contains("dulce") || t.contains("tarta")
                || t.contains("croissant") || t.contains("galleta") || t.contains("muffin")) return BOLLERIA;
        if (t.contains("salad") || t.contains("bocad") || t.contains("sandwich") || t.contains("tosta")
                || t.contains("empanada")) return SALADO;
        return TAZA;
    }

    /**
     * Devuelve un dibujo de cliente segun su humor.
     * @param humor de 0 (furioso) a 100 (encantado)
     */
    public static String[] cliente(int humor) {
        if (humor >= 66) return CLIENTE_CONTENTO;
        if (humor >= 33) return CLIENTE_NEUTRO;
        return CLIENTE_ENFADADO;
    }

    /** Coloca dos dibujos uno al lado del otro, separados por 'hueco' espacios. */
    public static String[] unirHorizontal(String[] izquierda, String[] derecha, int hueco) {
        int filas = Math.max(izquierda.length, derecha.length);
        int anchoIzq = 0;
        for (String l : izquierda) anchoIzq = Math.max(anchoIzq, Ansi.longitudVisible(l));

        String[] resultado = new String[filas];
        for (int i = 0; i < filas; i++) {
            String a = i < izquierda.length ? izquierda[i] : "";
            String b = i < derecha.length ? derecha[i] : "";
            resultado[i] = Consola.rellenar(a, anchoIzq) + " ".repeat(hueco) + b;
        }
        return resultado;
    }

    /** Imprime un dibujo con un color y una sangria. */
    public static void mostrar(String[] dibujo, String color, int sangria) {
        for (String l : dibujo) {
            Consola.imprimir(" ".repeat(sangria) + Ansi.pintar(l, color));
        }
    }

    /**
     * Barra de progreso: [████████░░░░░░░░]  50%
     * Se colorea sola: verde si va bien, amarillo a medias, rojo si queda poco.
     */
    public static String barra(int actual, int total, int ancho) {
        if (total <= 0) total = 1;
        double ratio = Math.max(0, Math.min(1.0, (double) actual / total));
        int llenos = (int) Math.round(ratio * ancho);
        String relleno = "█".repeat(llenos) + "░".repeat(ancho - llenos);
        String color = ratio > 0.6 ? Ansi.VERDE : ratio > 0.3 ? Ansi.AMARILLO : Ansi.ROJO;
        return Ansi.pintar("[" + relleno + "]", color) + " " + Consola.rellenarIzquierda((int) (ratio * 100) + "%", 4);
    }

    /** Barra de un color fijo (para inventario, paciencia, etc.). */
    public static String barra(int actual, int total, int ancho, String colorAnsi) {
        if (total <= 0) total = 1;
        double ratio = Math.max(0, Math.min(1.0, (double) actual / total));
        int llenos = (int) Math.round(ratio * ancho);
        return Ansi.pintar("[" + "█".repeat(llenos) + "░".repeat(ancho - llenos) + "]", colorAnsi);
    }

    /** Puntuacion en estrellas: ★★★☆☆ */
    public static String estrellas(int conseguidas, int maximo) {
        int c = Math.max(0, Math.min(conseguidas, maximo));
        return Ansi.amarillo("★".repeat(c)) + Ansi.gris("☆".repeat(maximo - c));
    }

    /** Rotulo grande de fin de partida. */
    public static void mostrarFinal(String texto, String colorAnsi) {
        Consola.salto();
        Marco.cajaGruesa(null, new String[]{
            "",
            Consola.centrar(texto.toUpperCase(), Consola.ANCHO - 4),
            ""
        }, colorAnsi);
        Consola.salto();
    }
}
