package cafeteria.vista;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ejecuta esta clase (boton verde de IntelliJ) para ver TODO lo que la vista
 * sabe dibujar. No usa ninguna clase tuya: son solo textos y numeros inventados.
 *
 * Usala como catalogo: cuando quieras enseñar algo por pantalla, busca aqui
 * el metodo que mas se parezca y llamalo desde tu logica con tus datos.
 */
public class DemoVista {

    public static void main(String[] args) {

        // 1. Bienvenida ---------------------------------------------------
        Escena.bienvenida();

        // 2. Empieza el dia -----------------------------------------------
        Escena.inicioDeDia(1, 50.0, 8);

        // 3. Barra de estado ----------------------------------------------
        Escena.hud(1, 50.0, 8, 100);

        // 4. La carta ------------------------------------------------------
        List<String[]> carta = new ArrayList<>();
        carta.add(new String[]{"1", "Cafe solo",        "CAFE",        Consola.euros(1.20)});
        carta.add(new String[]{"2", "Cafe con leche",   "CAFE",        Consola.euros(1.60)});
        carta.add(new String[]{"3", "Capuchino",        "CAFE",        Consola.euros(2.10)});
        carta.add(new String[]{"4", "Frappe de vainilla", "BEBIDA_FRIA", Consola.euros(3.40)});
        carta.add(new String[]{"5", "Croissant",        "REPOSTERIA",  Consola.euros(1.80)});
        carta.add(new String[]{"6", "Tostada de jamon", "SALADO",      Consola.euros(2.90)});
        Escena.carta(carta);

        // 5. Ficha de un producto -------------------------------------------
        Escena.fichaProducto("Capuchino", "CAFE", Consola.euros(2.10),
                Arrays.asList("tamano MEDIANO", "leche entera", "con canela"));

        // 6. Llega un cliente ------------------------------------------------
        Escena.clienteEnBarra("Marta", 80, 100, "Buenos dias! Tengo algo de prisa...");

        // 7. Su comanda --------------------------------------------------------
        Escena.ticketPedido("Marta", 1, Arrays.asList(
                "1x Capuchino  (GRANDE, avena)",
                "1x Croissant  (calentado)"
        ));

        // 8. Preparando ----------------------------------------------------------
        Escena.preparando("Capuchino", 900);

        // 9. Resultados posibles --------------------------------------------------
        Escena.resultadoServicio("Marta", 100, 4.60, Arrays.asList(
                "Producto correcto: Capuchino",
                "Tamano correcto: GRANDE",
                "Leche correcta: avena"
        ));

        Escena.resultadoServicio("Julio", 50, 1.05, Arrays.asList(
                "Producto correcto: Cafe con leche",
                "Se ha pedido PEQUENO y has servido GRANDE"
        ));

        Escena.resultadoServicio("Nuria", 0, 0.0, Arrays.asList(
                "Ha pedido un Frappe y le has dado una Tostada",
                "Los ingredientes usados se pierden"
        ));

        // 10. El almacen -------------------------------------------------------------
        List<String[]> almacen = new ArrayList<>();
        almacen.add(new String[]{"Cafe molido (g)", "320", "500"});
        almacen.add(new String[]{"Leche (ml)",      "900", "2000"});
        almacen.add(new String[]{"Leche de avena (ml)", "120", "800"});
        almacen.add(new String[]{"Croissants (ud)", "3",   "20"});
        almacen.add(new String[]{"Hielo (cubitos)", "0",   "60"});
        Escena.inventario(almacen);

        // 11. Cierre del dia ------------------------------------------------------------
        Escena.resumenDelDia(1, 8, 5, 2, 1, 27.40, 9.15, 68.25);

        // 12. Fin de partida ---------------------------------------------------------------
        Escena.finDePartida(true, 3, 184.60, "Has cerrado la semana con la caja llena.");

        // 13. Piezas sueltas por si las necesitas -------------------------------------------
        Consola.seccion("PIEZAS SUELTAS");
        Consola.exito("Mensaje de exito");
        Consola.error("Mensaje de error");
        Consola.aviso("Mensaje de aviso");
        Consola.info("Mensaje informativo");
        Consola.nota("Nota discreta en gris");
        Consola.dialogo("Cliente", "esto lo dice un personaje");
        Consola.linea();
        Consola.imprimir("  Estrellas:   " + ArteAscii.estrellas(2, 3));
        Consola.imprimir("  Barra:       " + ArteAscii.barra(7, 10, 20));
        Consola.imprimir("  Dinero:      " + Consola.euros(1234.5));
        Consola.salto();
        ArteAscii.mostrar(ArteAscii.unirHorizontal(ArteAscii.TAZA, ArteAscii.CAJA_REGISTRADORA, 6),
                Ansi.MARRON, 6);
        Consola.salto();
        Marco.cartel("todo esto ya funciona: tu solo tienes que llamarlo", Ansi.CIAN);
        Consola.salto();
    }
}
