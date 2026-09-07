package cafeteria.vista;

/**
 * Colores y estilos de texto para la consola (secuencias ANSI).
 *
 * Esta clase forma parte de la CAPA DE VISTA: es codigo de "pintar cosas bonitas"
 * y viene ya escrito para que no pierdas tiempo con ello. Tu trabajo esta en
 * los paquetes 'modelo' y 'logica'.
 *
 * Uso tipico:
 *     System.out.println(Ansi.verde("Pedido correcto!"));
 *     System.out.println(Ansi.negrita(Ansi.amarillo("2,50 EUR")));
 *
 * Si tu terminal muestra basura del estilo "[32m", llama una sola vez a
 * Ansi.desactivar() al arrancar el programa.
 */
public final class Ansi {

    /** Caracter de escape (ESC, codigo 27). Abre toda secuencia ANSI. */
    private static final String ESC = String.valueOf((char) 27);

    // ---- Codigos crudos (por si quieres construir combinaciones a mano) ----
    public static final String RESET     = ESC + "[0m";
    public static final String NEGRITA   = ESC + "[1m";
    public static final String ATENUADO  = ESC + "[2m";
    public static final String CURSIVA   = ESC + "[3m";
    public static final String SUBRAYADO = ESC + "[4m";

    public static final String NEGRO    = ESC + "[30m";
    public static final String ROJO     = ESC + "[31m";
    public static final String VERDE    = ESC + "[32m";
    public static final String AMARILLO = ESC + "[33m";
    public static final String AZUL     = ESC + "[34m";
    public static final String MAGENTA  = ESC + "[35m";
    public static final String CIAN     = ESC + "[36m";
    public static final String BLANCO   = ESC + "[37m";
    public static final String GRIS     = ESC + "[90m";
    public static final String NARANJA  = ESC + "[38;5;208m";
    public static final String MARRON   = ESC + "[38;5;130m";
    public static final String CREMA    = ESC + "[38;5;223m";

    public static final String FONDO_ROJO     = ESC + "[41m";
    public static final String FONDO_VERDE    = ESC + "[42m";
    public static final String FONDO_AMARILLO = ESC + "[43m";
    public static final String FONDO_AZUL     = ESC + "[44m";
    public static final String FONDO_MARRON   = ESC + "[48;5;94m";

    /** Si es false, todos los metodos devuelven el texto tal cual, sin colores. */
    private static boolean activo = true;

    private Ansi() {
        // Clase de utilidades: no se instancia.
        // (Constructor privado: es una buena practica que veras en muchas librerias.)
    }

    public static void desactivar() { activo = false; }
    public static void activar()    { activo = true; }
    public static boolean estaActivo() { return activo; }

    /** Envuelve un texto con un codigo de estilo y lo cierra con RESET. */
    public static String pintar(String texto, String codigo) {
        if (!activo || texto == null) return texto;
        return codigo + texto + RESET;
    }

    public static String rojo(String t)     { return pintar(t, ROJO); }
    public static String verde(String t)    { return pintar(t, VERDE); }
    public static String amarillo(String t) { return pintar(t, AMARILLO); }
    public static String azul(String t)     { return pintar(t, AZUL); }
    public static String magenta(String t)  { return pintar(t, MAGENTA); }
    public static String cian(String t)     { return pintar(t, CIAN); }
    public static String gris(String t)     { return pintar(t, GRIS); }
    public static String naranja(String t)  { return pintar(t, NARANJA); }
    public static String marron(String t)   { return pintar(t, MARRON); }
    public static String crema(String t)    { return pintar(t, CREMA); }
    public static String negrita(String t)  { return pintar(t, NEGRITA); }
    public static String atenuado(String t) { return pintar(t, ATENUADO); }
    public static String cursiva(String t)  { return pintar(t, CURSIVA); }

    /**
     * Quita todos los codigos de color de un texto.
     * Recorre caracter a caracter y descarta lo que va desde ESC hasta la 'm'.
     */
    public static String limpiar(String texto) {
        if (texto == null) return "";
        StringBuilder sb = new StringBuilder(texto.length());
        boolean dentroDeCodigo = false;
        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (dentroDeCodigo) {
                if (c == 'm') dentroDeCodigo = false;
            } else if (c == (char) 27) {
                dentroDeCodigo = true;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * Longitud "visible" de un texto: cuenta caracteres ignorando los codigos
     * de color. Es lo que usan Tabla y Marco para alinear bien las columnas.
     */
    public static int longitudVisible(String texto) {
        return limpiar(texto).length();
    }
}
