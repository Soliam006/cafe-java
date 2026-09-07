package cafeteria.vista;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

/**
 * Entrada y salida por consola: mensajes con estilo y lectura segura de datos.
 *
 * CAPA DE VISTA (ya escrita, no hace falta que la toques).
 *
 * Todo lo que imprime el juego deberia pasar por aqui. Asi, si algun dia
 * quisierais cambiar la consola por una ventana grafica, solo habria que
 * reescribir este paquete y el resto del programa seguiria igual. Eso es
 * separar la VISTA del MODELO, y es una de las ideas mas importantes del curso.
 */
public final class Consola {

    /** Salida forzada a UTF-8 para que los acentos y los bordes se vean bien. */
    public static final PrintStream out =
            new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8);

    private static final Scanner teclado = new Scanner(System.in, StandardCharsets.UTF_8);

    /** Ancho estandar de todos los marcos y tablas del juego. */
    public static final int ANCHO = 74;

    private Consola() { }

    // =====================================================================
    //  SALIDA
    // =====================================================================

    public static void imprimir(String texto) { out.println(texto); }
    public static void escribir(String texto) { out.print(texto); }
    public static void salto()                { out.println(); }

    public static void saltos(int n) {
        for (int i = 0; i < n; i++) out.println();
    }

    /** Titulo grande, centrado y enmarcado. */
    public static void titulo(String texto) {
        Marco.cajaDoble(null, new String[]{ centrar(texto.toUpperCase(), ANCHO - 4) }, Ansi.NARANJA);
    }

    /** Encabezado de seccion, mas discreto que un titulo. */
    public static void seccion(String texto) {
        salto();
        imprimir(Ansi.negrita(Ansi.cian("  " + texto)));
        imprimir(Ansi.cian("  " + "-".repeat(Math.max(0, texto.length()))));
    }

    public static void exito(String texto)  { imprimir(Ansi.verde   ("  [OK]  " + texto)); }
    public static void error(String texto)  { imprimir(Ansi.rojo    ("  [X]   " + texto)); }
    public static void aviso(String texto)  { imprimir(Ansi.amarillo("  [!]   " + texto)); }
    public static void info(String texto)   { imprimir(Ansi.cian    ("  [i]   " + texto)); }
    public static void nota(String texto)   { imprimir(Ansi.gris    ("        " + texto)); }

    /** Frase dicha por un personaje: -- Nombre: "texto" */
    public static void dialogo(String personaje, String texto) {
        imprimir("  " + Ansi.negrita(Ansi.crema(personaje)) + ": " + Ansi.cursiva("«" + texto + "»"));
    }

    public static void separador() {
        imprimir(Ansi.gris("  " + "·".repeat(ANCHO - 2)));
    }

    public static void linea() {
        imprimir(Ansi.gris("  " + "─".repeat(ANCHO - 2)));
    }

    /** Intenta limpiar la pantalla. En algunas consolas simplemente deja hueco. */
    public static void limpiarPantalla() {
        if (Ansi.estaActivo()) {
            out.print((char) 27 + "[2J" + (char) 27 + "[H");
            out.flush();
        } else {
            saltos(3);
        }
    }

    /** Centra un texto en un ancho dado (ignorando codigos de color). */
    public static String centrar(String texto, int ancho) {
        int visible = Ansi.longitudVisible(texto);
        if (visible >= ancho) return texto;
        int izq = (ancho - visible) / 2;
        return " ".repeat(izq) + texto + " ".repeat(ancho - visible - izq);
    }

    /** Rellena por la derecha hasta 'ancho' (ignorando codigos de color). */
    public static String rellenar(String texto, int ancho) {
        int visible = Ansi.longitudVisible(texto);
        if (visible >= ancho) return recortar(texto, ancho);
        return texto + " ".repeat(ancho - visible);
    }

    /** Rellena por la izquierda hasta 'ancho' (util para numeros). */
    public static String rellenarIzquierda(String texto, int ancho) {
        int visible = Ansi.longitudVisible(texto);
        if (visible >= ancho) return recortar(texto, ancho);
        return " ".repeat(ancho - visible) + texto;
    }

    /** Recorta un texto a 'ancho' caracteres visibles, con puntos suspensivos. */
    public static String recortar(String texto, int ancho) {
        String limpio = Ansi.limpiar(texto);
        if (limpio.length() <= ancho) return texto;
        if (ancho <= 1) return limpio.substring(0, Math.max(0, ancho));
        return limpio.substring(0, ancho - 1) + "…";
    }

    /** Formatea una cantidad de dinero: 3.5 -> "3,50 EUR" */
    public static String euros(double cantidad) {
        String base = String.format(Locale.US, "%,.2f", cantidad);
        // Locale.US da "1,234.56"; lo pasamos al formato espanol "1.234,56".
        return base.replace(',', '#').replace('.', ',').replace('#', '.') + " €";
    }

    /** Pausa la ejecucion (para animaciones). */
    public static void esperar(long milisegundos) {
        try {
            Thread.sleep(milisegundos);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // =====================================================================
    //  ENTRADA
    // =====================================================================

    private static String prompt(String mensaje) {
        out.print("  " + Ansi.negrita(Ansi.amarillo("> ")) + mensaje + " ");
        out.flush();
        try {
            return teclado.nextLine().trim();
        } catch (Exception e) {
            // La entrada se ha cerrado (por ejemplo al ejecutar sin teclado).
            return "";
        }
    }

    public static String leerTexto(String mensaje) {
        String valor = prompt(mensaje);
        while (valor.isEmpty()) {
            error("No puede quedar vacio.");
            valor = prompt(mensaje);
        }
        return valor;
    }

    public static String leerTextoOpcional(String mensaje) {
        return prompt(mensaje);
    }

    /** Lee un numero entero, repitiendo la pregunta hasta que sea valido. */
    public static int leerEntero(String mensaje) {
        while (true) {
            String valor = prompt(mensaje);
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                error("Escribe un numero entero, por favor.");
            }
        }
    }

    /** Lee un entero comprendido entre min y max (ambos incluidos). */
    public static int leerEntero(String mensaje, int min, int max) {
        while (true) {
            int valor = leerEntero(mensaje + Ansi.gris(" [" + min + "-" + max + "]"));
            if (valor >= min && valor <= max) return valor;
            error("Tiene que estar entre " + min + " y " + max + ".");
        }
    }

    public static double leerDecimal(String mensaje) {
        while (true) {
            String valor = prompt(mensaje).replace(',', '.');
            try {
                return Double.parseDouble(valor);
            } catch (NumberFormatException e) {
                error("Escribe un numero, por favor (ej. 2.50).");
            }
        }
    }

    /** Pregunta de si/no. Devuelve true si contesta s, si, y, yes. */
    public static boolean confirmar(String mensaje) {
        while (true) {
            String valor = prompt(mensaje + Ansi.gris(" (s/n)")).toLowerCase();
            if (valor.equals("s") || valor.equals("si") || valor.equals("sí")
                    || valor.equals("y") || valor.equals("yes")) return true;
            if (valor.equals("n") || valor.equals("no")) return false;
            error("Contesta 's' o 'n'.");
        }
    }

    /**
     * Muestra un menu numerado y devuelve la opcion elegida (empezando en 1).
     *
     *     int op = Consola.menu("Que quieres hacer?", "Servir cliente", "Ver carta", "Cerrar");
     */
    public static int menu(String titulo, String... opciones) {
        salto();
        imprimir("  " + Ansi.negrita(Ansi.cian(titulo)));
        for (int i = 0; i < opciones.length; i++) {
            imprimir("    " + Ansi.amarillo(String.valueOf(i + 1)) + ") " + opciones[i]);
        }
        return leerEntero("Elige una opcion", 1, opciones.length);
    }

    public static void pausa() {
        salto();
        out.print("  " + Ansi.gris("Pulsa INTRO para continuar..."));
        out.flush();
        try {
            teclado.nextLine();
        } catch (Exception e) {
            // sin teclado disponible: seguimos
        }
    }
}
