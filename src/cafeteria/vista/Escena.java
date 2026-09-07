package cafeteria.vista;

import java.util.List;

/**
 * Las "pantallas" del juego, ya montadas.
 *
 * CAPA DE VISTA (ya escrita, no hace falta que la toques).
 *
 * IMPORTANTE: fijate en que ningun metodo de esta clase recibe un Producto,
 * un Cliente ni un Pedido. Solo recibe textos y numeros. Eso es a proposito:
 * la vista NO conoce tu modelo. Tu logica decide QUE contar y la vista decide
 * COMO se ve. Si manana cambias el nombre de tus clases, esto sigue valiendo.
 *
 * Ejemplo de uso desde tu codigo:
 *
 *     Escena.hud(dia, caja.getSaldo(), clientes.size(), inventario.porcentajeRestante());
 *     Escena.clienteEnBarra(cliente.getNombre(), cliente.getPaciencia(), 100, "Buenos dias!");
 *     Escena.ticketPedido(cliente.getNombre(), 7, lineasDelPedido);
 */
public final class Escena {

    private Escena() { }

    // =====================================================================
    //  APERTURA Y CIERRE
    // =====================================================================

    /** Pantalla de bienvenida con el logo. */
    public static void bienvenida() {
        Consola.limpiarPantalla();
        ArteAscii.mostrarLogo();
        Marco.cajaRedondeada("como se juega", new String[]{
            "Los clientes van llegando a la barra y te cantan su pedido.",
            "Preparas lo que te piden eligiendo producto, tamano y extras.",
            "",
            Ansi.verde("  Pedido perfecto") + "  ->  cobras el 100% y el cliente se va feliz.",
            Ansi.amarillo("  Casi perfecto") + "   ->  cobras solo el 50%.",
            Ansi.rojo("  Pedido erroneo") + "  ->  no cobras nada y pierdes los ingredientes.",
            "",
            "El dia acaba cuando no quedan clientes o te quedas sin genero."
        }, Ansi.CIAN);
        Consola.salto();
    }

    /** Cartel de comienzo de jornada. */
    public static void inicioDeDia(int numeroDia, double dineroEnCaja, int clientesPrevistos) {
        Consola.limpiarPantalla();
        Consola.salto();
        Marco.cajaGruesa(null, new String[]{
            "",
            Consola.centrar(Ansi.negrita("D I A   " + numeroDia), Consola.ANCHO - 4),
            "",
            Consola.centrar("En caja: " + Ansi.verde(Consola.euros(dineroEnCaja))
                    + Ansi.gris("   |   ") + "Clientes previstos: " + Ansi.cian(String.valueOf(clientesPrevistos)),
                    Consola.ANCHO - 4),
            ""
        }, Ansi.NARANJA);
        Consola.salto();
        Consola.imprimir(Ansi.gris(Consola.centrar("· · ·  abriendo la persiana  · · ·", Consola.ANCHO)));
        Consola.salto();
    }

    /**
     * Barra de estado permanente (el "HUD" del juego).
     *
     * @param generoRestante porcentaje de genero que queda, de 0 a 100
     */
    public static void hud(int dia, double dinero, int clientesEnCola, int generoRestante) {
        String linea = " Dia " + Ansi.negrita(String.valueOf(dia))
                + Ansi.gris("  │  ") + "Caja " + Ansi.verde(Consola.euros(dinero))
                + Ansi.gris("  │  ") + "Cola " + Ansi.cian(clientesEnCola + " ")
                + Ansi.gris("  │  ") + "Genero " + ArteAscii.barra(generoRestante, 100, 10);
        Marco.cajaSimple(null, new String[]{ linea }, Ansi.GRIS);
    }

    // =====================================================================
    //  CARTA E INVENTARIO
    // =====================================================================

    /**
     * Dibuja la carta de la cafeteria.
     * Cada fila debe ser: { numero, nombre, categoria, precio }
     */
    public static void carta(List<String[]> filas) {
        Consola.seccion("CARTA DE LA CASA");
        Tabla t = new Tabla("#", "PRODUCTO", "CATEGORIA", "PRECIO");
        t.alinearDerecha(0).alinearDerecha(3).color(Ansi.MARRON);
        for (String[] f : filas) t.fila(f);
        t.imprimir();
    }

    /**
     * Dibuja el almacen.
     * Cada fila debe ser: { ingrediente, cantidadActual, cantidadInicial }
     * (los numeros van como texto: "120", "500"...)
     */
    public static void inventario(List<String[]> filas) {
        Consola.seccion("ALMACEN");
        Tabla t = new Tabla("INGREDIENTE", "QUEDA", "DE", "NIVEL");
        t.alinearDerecha(1).alinearDerecha(2).color(Ansi.CIAN);
        for (String[] f : filas) {
            int actual = enteroSeguro(f.length > 1 ? f[1] : "0");
            int inicial = enteroSeguro(f.length > 2 ? f[2] : "0");
            t.fila(f[0], String.valueOf(actual), String.valueOf(inicial),
                    ArteAscii.barra(actual, Math.max(inicial, 1), 14));
        }
        t.imprimir();
    }

    /** Ficha grande de un producto, con su dibujo al lado. */
    public static void fichaProducto(String nombre, String categoria, String precio, List<String> detalles) {
        String[] icono = ArteAscii.iconoDeProducto(categoria + " " + nombre);
        Consola.salto();
        for (int i = 0; i < icono.length; i++) {
            String derecha = "";
            if (i == 1) derecha = Ansi.negrita(Ansi.crema(nombre.toUpperCase()));
            else if (i == 2) derecha = Ansi.gris(categoria);
            else if (i == 3) derecha = Ansi.verde(precio);
            else if (i >= 4 && (i - 4) < detalles.size()) derecha = Ansi.gris("· " + detalles.get(i - 4));
            Consola.imprimir("    " + Ansi.marron(icono[i]) + "   " + derecha);
        }
        Consola.salto();
    }

    // =====================================================================
    //  CLIENTES Y PEDIDOS
    // =====================================================================

    /**
     * El cliente aparece en la barra: dibujo, nombre, paciencia y lo que dice.
     *
     * @param paciencia    valor actual (0..pacienciaMaxima)
     * @param frase        lo que suelta al llegar; puede ser null
     */
    public static void clienteEnBarra(String nombre, int paciencia, int pacienciaMaxima, String frase) {
        int humor = pacienciaMaxima <= 0
                ? (paciencia > 0 ? 100 : 0)
                : Math.max(0, Math.min(100, (paciencia * 100) / pacienciaMaxima));
        String[] avatar = ArteAscii.cliente(humor);
        String colorAvatar = humor >= 66 ? Ansi.VERDE : humor >= 33 ? Ansi.AMARILLO : Ansi.ROJO;

        Consola.salto();
        for (int i = 0; i < avatar.length; i++) {
            String derecha = "";
            if (i == 1) derecha = Ansi.negrita(Ansi.crema(nombre));
            else if (i == 2) derecha = "Paciencia " + ArteAscii.barra(paciencia, pacienciaMaxima, 16);
            else if (i == 4 && frase != null) derecha = Ansi.cursiva("«" + frase + "»");
            Consola.imprimir("    " + Ansi.pintar(avatar[i], colorAvatar) + "   " + derecha);
        }
        Consola.salto();
    }

    /**
     * El ticket de papel con la comanda.
     * Cada linea es un texto ya montado por ti, por ejemplo:
     *     "1x Cafe con leche (GRANDE, leche de avena)"
     */
    public static void ticketPedido(String cliente, int numeroPedido, List<String> lineas) {
        String[] contenido = new String[lineas.size() + 5];
        int i = 0;
        contenido[i++] = Consola.centrar("*** COMANDA #" + numeroPedido + " ***", 40);
        contenido[i++] = Consola.centrar("para " + cliente, 40);
        contenido[i++] = Marco.SEPARADOR;
        for (String l : lineas) contenido[i++] = " " + l;
        contenido[i++] = Marco.SEPARADOR;
        contenido[i] = Ansi.gris(Consola.centrar("gracias por su visita", 40));
        Marco.cajaEstrecha(null, contenido, Ansi.CREMA, 46);
    }

    /** Animacion de "preparando el producto". Bloquea unos segundos. */
    public static void preparando(String queSePrepara, int milisegundosTotales) {
        int pasos = 24;
        long espera = Math.max(10, milisegundosTotales / pasos);
        for (int i = 0; i <= pasos; i++) {
            Consola.escribir("\r    " + Ansi.marron("preparando " + queSePrepara + " ")
                    + ArteAscii.barra(i, pasos, 24));
            Consola.out.flush();
            Consola.esperar(espera);
        }
        Consola.salto();
    }

    // =====================================================================
    //  RESULTADOS
    // =====================================================================

    /**
     * Muestra como ha ido el servicio a un cliente.
     *
     * @param porcentaje 100 (perfecto), 50 (a medias) o 0 (mal)
     * @param cobrado    dinero que entra en caja
     * @param comentarios explicaciones de que estuvo bien o mal; puede ir vacia
     */
    public static void resultadoServicio(String cliente, int porcentaje, double cobrado, List<String> comentarios) {
        String color;
        String rotulo;
        if (porcentaje >= 100)      { color = Ansi.VERDE;    rotulo = "PEDIDO PERFECTO"; }
        else if (porcentaje > 0)    { color = Ansi.AMARILLO; rotulo = "CASI, PERO NO DEL TODO"; }
        else                        { color = Ansi.ROJO;     rotulo = "PEDIDO EQUIVOCADO"; }

        String[] lineas = new String[comentarios.size() + 4];
        int i = 0;
        lineas[i++] = Consola.centrar(Ansi.negrita(rotulo), Consola.ANCHO - 4);
        lineas[i++] = Consola.centrar(ArteAscii.estrellas(porcentaje >= 100 ? 3 : porcentaje > 0 ? 2 : 0, 3),
                Consola.ANCHO - 4);
        lineas[i++] = Marco.SEPARADOR;
        for (String c : comentarios) lineas[i++] = "  · " + c;
        lineas[i] = "  " + cliente + " paga " + Ansi.pintar(Consola.euros(cobrado), color)
                + Ansi.gris("   (" + porcentaje + "% del importe)");

        Consola.salto();
        Marco.cajaDoble(null, lineas, color);
        Consola.salto();
    }

    /** El resumen que sale al cerrar la persiana. */
    public static void resumenDelDia(int dia, int clientesAtendidos, int perfectos, int regulares,
                                     int fallados, double ingresos, double gastos, double saldoFinal) {
        Consola.salto();
        Marco.cajaGruesa("cierre del dia " + dia, new String[]{
            "",
            "  Clientes atendidos . . . . . " + Ansi.negrita(String.valueOf(clientesAtendidos)),
            "    " + Ansi.verde("perfectos") + " . . . . . . . . . " + perfectos + "   " + ArteAscii.barra(perfectos, Math.max(clientesAtendidos, 1), 20, Ansi.VERDE),
            "    " + Ansi.amarillo("a medias") + "  . . . . . . . . . " + regulares + "   " + ArteAscii.barra(regulares, Math.max(clientesAtendidos, 1), 20, Ansi.AMARILLO),
            "    " + Ansi.rojo("fallados") + "  . . . . . . . . . " + fallados + "   " + ArteAscii.barra(fallados, Math.max(clientesAtendidos, 1), 20, Ansi.ROJO),
            Marco.SEPARADOR,
            "  Ingresos del dia  . . . . . " + Ansi.verde("+" + Consola.euros(ingresos)),
            "  Gasto de genero . . . . . . " + Ansi.rojo("-" + Consola.euros(gastos)),
            "  " + Ansi.negrita("SALDO EN CAJA") + " . . . . . . . " + Ansi.negrita(Ansi.amarillo(Consola.euros(saldoFinal))),
            ""
        }, Ansi.NARANJA);
        Consola.salto();
    }

    /** Pantalla final de la partida. */
    public static void finDePartida(boolean buenFinal, int diasJugados, double totalGanado, String motivo) {
        Consola.salto();
        if (buenFinal) {
            ArteAscii.mostrarFinal("¡ B U E N   T R A B A J O !", Ansi.VERDE);
        } else {
            ArteAscii.mostrarFinal("F I N   D E   L A   P A R T I D A", Ansi.ROJO);
        }
        Marco.cajaRedondeada(null, new String[]{
            "  " + motivo,
            "",
            "  Dias trabajados . . . . " + Ansi.negrita(String.valueOf(diasJugados)),
            "  Total recaudado . . . . " + Ansi.negrita(Ansi.amarillo(Consola.euros(totalGanado)))
        }, buenFinal ? Ansi.VERDE : Ansi.ROJO);
        Consola.salto();
    }

    // =====================================================================
    private static int enteroSeguro(String texto) {
        try {
            return Integer.parseInt(Ansi.limpiar(texto).trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
