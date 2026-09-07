package cafeteria.vista;

import java.util.ArrayList;
import java.util.List;

/**
 * Tablas de texto con columnas alineadas automaticamente.
 *
 * CAPA DE VISTA (ya escrita, no hace falta que la toques).
 *
 * Ejemplo:
 *     Tabla t = new Tabla("#", "Producto", "Tamano", "Precio");
 *     t.alinearDerecha(3);
 *     t.fila("1", "Cafe con leche", "Grande", Consola.euros(2.5));
 *     t.fila("2", "Croissant", "-", Consola.euros(1.8));
 *     t.imprimir();
 *
 * Aviso: no metas emojis dentro de una tabla. En consola ocupan dos espacios
 * y descuadran las columnas. Emojis solo en lineas sueltas.
 */
public class Tabla {

    private final String[] cabeceras;
    private final List<String[]> filas = new ArrayList<>();
    private final boolean[] derecha;
    private String color = Ansi.GRIS;

    public Tabla(String... cabeceras) {
        this.cabeceras = cabeceras;
        this.derecha = new boolean[cabeceras.length];
    }

    /** Alinea a la derecha la columna indicada (0 = primera). Util para numeros. */
    public Tabla alinearDerecha(int columna) {
        if (columna >= 0 && columna < derecha.length) derecha[columna] = true;
        return this;
    }

    /** Cambia el color del borde (usa las constantes de Ansi). */
    public Tabla color(String colorAnsi) {
        this.color = colorAnsi;
        return this;
    }

    /** Anade una fila. Debe tener tantas celdas como cabeceras. */
    public Tabla fila(String... celdas) {
        String[] completa = new String[cabeceras.length];
        for (int i = 0; i < cabeceras.length; i++) {
            completa[i] = i < celdas.length && celdas[i] != null ? celdas[i] : "";
        }
        filas.add(completa);
        return this;
    }

    public boolean estaVacia() {
        return filas.isEmpty();
    }

    public int numeroDeFilas() {
        return filas.size();
    }

    /** Dibuja la tabla en la consola. */
    public void imprimir() {
        int[] anchos = calcularAnchos();

        Consola.imprimir(linea(anchos, "┌", "┬", "┐"));
        Consola.imprimir(filaTexto(cabeceras, anchos, true));
        Consola.imprimir(linea(anchos, "├", "┼", "┤"));
        if (filas.isEmpty()) {
            String vacio = Consola.centrar(Ansi.gris("(vacio)"), sumar(anchos) + (anchos.length - 1) * 3);
            Consola.imprimir("  " + Ansi.pintar("│", color) + " " + vacio + " " + Ansi.pintar("│", color));
        }
        for (String[] f : filas) {
            Consola.imprimir(filaTexto(f, anchos, false));
        }
        Consola.imprimir(linea(anchos, "└", "┴", "┘"));
    }

    // ------------------------------------------------------------------
    private int[] calcularAnchos() {
        int[] anchos = new int[cabeceras.length];
        for (int i = 0; i < cabeceras.length; i++) {
            anchos[i] = Ansi.longitudVisible(cabeceras[i]);
        }
        for (String[] f : filas) {
            for (int i = 0; i < f.length; i++) {
                anchos[i] = Math.max(anchos[i], Ansi.longitudVisible(f[i]));
            }
        }
        // Si la tabla se pasa del ancho de pantalla, encogemos la columna mas ancha.
        int disponible = Consola.ANCHO - (anchos.length * 3) - 1;
        while (sumar(anchos) > disponible) {
            int mayor = 0;
            for (int i = 1; i < anchos.length; i++) {
                if (anchos[i] > anchos[mayor]) mayor = i;
            }
            if (anchos[mayor] <= 4) break;
            anchos[mayor]--;
        }
        return anchos;
    }

    private static int sumar(int[] valores) {
        int total = 0;
        for (int v : valores) total += v;
        return total;
    }

    private String linea(int[] anchos, String izq, String medio, String der) {
        StringBuilder sb = new StringBuilder("  ").append(izq);
        for (int i = 0; i < anchos.length; i++) {
            sb.append("─".repeat(anchos[i] + 2));
            sb.append(i == anchos.length - 1 ? der : medio);
        }
        return Ansi.pintar(sb.toString(), color);
    }

    private String filaTexto(String[] celdas, int[] anchos, boolean esCabecera) {
        StringBuilder sb = new StringBuilder("  ").append(Ansi.pintar("│", color));
        for (int i = 0; i < anchos.length; i++) {
            String celda = Consola.recortar(celdas[i], anchos[i]);
            if (esCabecera) celda = Ansi.negrita(Ansi.crema(celda));
            String ajustada = derecha[i]
                    ? Consola.rellenarIzquierda(celda, anchos[i])
                    : Consola.rellenar(celda, anchos[i]);
            sb.append(' ').append(ajustada).append(' ').append(Ansi.pintar("│", color));
        }
        return sb.toString();
    }

    /** Atajo: construye e imprime una tabla de una sola vez. */
    public static void mostrar(String[] cabeceras, List<String[]> filas) {
        Tabla t = new Tabla(cabeceras);
        for (String[] f : filas) t.fila(f);
        t.imprimir();
    }
}
