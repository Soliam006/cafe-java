# Referencia de la vista

Todo lo que el paquete `cafeteria.vista` sabe hacer. **Ya está escrito y funcionando**:
tú solo tienes que llamarlo.

Para verlo todo en marcha, ejecuta `src/cafeteria/vista/DemoVista.java`.

```java
import cafeteria.vista.*;
```

> 🔑 **La regla de oro de este paquete:** ningún método recibe un objeto tuyo. Solo textos,
> números y listas de textos. La vista no conoce tu modelo, y por eso puedes llamar a tus
> clases como te dé la gana.

---

## `Escena` — las pantallas del juego

Es lo que más vas a usar. Cada método pinta una pantalla entera.

| Método | Qué pinta |
|---|---|
| `Escena.bienvenida()` | Logo + reglas del juego |
| `Escena.inicioDeDia(int dia, double dinero, int clientesPrevistos)` | El cartel de "DÍA 3" |
| `Escena.hud(int dia, double dinero, int clientesEnCola, int generoRestante)` | La barra de estado. `generoRestante` va de 0 a 100 |
| `Escena.carta(List<String[]> filas)` | La carta. Cada fila: `{ nº, nombre, categoría, precio }` |
| `Escena.inventario(List<String[]> filas)` | El almacén. Cada fila: `{ ingrediente, cantidadActual, cantidadInicial }` (números como texto) |
| `Escena.fichaProducto(String nombre, String categoria, String precio, List<String> detalles)` | Ficha grande con dibujo al lado |
| `Escena.clienteEnBarra(String nombre, int paciencia, int pacienciaMaxima, String frase)` | El cliente. La cara cambia sola según la paciencia. `frase` puede ser `null` |
| `Escena.ticketPedido(String cliente, int numeroPedido, List<String> lineas)` | El ticket de papel |
| `Escena.preparando(String queSePrepara, int milisegundos)` | Animación de barra llenándose. Bloquea |
| `Escena.resultadoServicio(String cliente, int porcentaje, double cobrado, List<String> comentarios)` | Cómo ha ido. Se colorea solo: 100 verde, 50 amarillo, 0 rojo |
| `Escena.resumenDelDia(int dia, int atendidos, int perfectos, int aMedias, int fallados, double ingresos, double gastos, double saldoFinal)` | El cierre del día |
| `Escena.finDePartida(boolean buenFinal, int dias, double totalGanado, String motivo)` | La pantalla final |

**Ejemplo completo de atender a un cliente:**

```java
Escena.hud(dia, caja.getSaldo(), cola.size(), almacen.porcentajeRestante());
Escena.clienteEnBarra(c.getNombre(), c.getPaciencia(), c.getPacienciaMaxima(), c.getFrase());
Escena.ticketPedido(c.getNombre(), c.getPedido().getNumero(), c.getPedido().getTextos());

Escena.preparando("Capuchino", 800);
Escena.resultadoServicio(c.getNombre(), 50, 1.05, List.of("Te ha pedido GRANDE y has servido MEDIANO"));
```

**Ejemplo de montar las filas de una tabla desde tus objetos:**

```java
List<String[]> filas = new ArrayList<>();
int n = 1;
for (Producto p : carta.getProductos()) {
    filas.add(new String[]{
        String.valueOf(n++),
        p.getNombre(),
        p.getCategoria().toString(),
        Consola.euros(p.calcularPrecio())
    });
}
Escena.carta(filas);
```

---

## `Consola` — imprimir y leer

### Imprimir

| Método | Qué hace |
|---|---|
| `Consola.imprimir(String)` | Una línea |
| `Consola.escribir(String)` | Sin salto de línea |
| `Consola.salto()` / `Consola.saltos(int n)` | Líneas en blanco |
| `Consola.titulo(String)` | Título grande enmarcado |
| `Consola.seccion(String)` | Encabezado discreto y subrayado |
| `Consola.exito(String)` | `[OK]` en verde |
| `Consola.error(String)` | `[X]` en rojo |
| `Consola.aviso(String)` | `[!]` en amarillo |
| `Consola.info(String)` | `[i]` en cian |
| `Consola.nota(String)` | Texto discreto en gris |
| `Consola.dialogo(String quien, String que)` | `Marta: «buenos días»` |
| `Consola.linea()` / `Consola.separador()` | Líneas horizontales |
| `Consola.limpiarPantalla()` | Borra la pantalla |

### Leer del teclado

**Usa siempre esto en vez de `Scanner`.** Validan solos y vuelven a preguntar si te dan
basura, así que nunca te devuelven un valor inválido.

| Método | Devuelve |
|---|---|
| `Consola.leerTexto(String pregunta)` | Texto no vacío |
| `Consola.leerTextoOpcional(String pregunta)` | Texto, puede ser vacío |
| `Consola.leerEntero(String pregunta)` | Un entero cualquiera |
| `Consola.leerEntero(String pregunta, int min, int max)` | Un entero en ese rango |
| `Consola.leerDecimal(String pregunta)` | Un decimal (acepta coma y punto) |
| `Consola.confirmar(String pregunta)` | `boolean` (s/n) |
| `Consola.menu(String titulo, String... opciones)` | El número elegido, **empezando en 1** |
| `Consola.pausa()` | Espera a que pulses INTRO |

```java
int op = Consola.menu("¿Qué haces?", "Atender", "Ver carta", "Cerrar");
// op vale 1, 2 o 3.  ¡Para indexar una lista, op - 1!
```

### Formato

| Método | Ejemplo |
|---|---|
| `Consola.euros(double)` | `2.5` → `"2,50 €"` |
| `Consola.centrar(String, int ancho)` | |
| `Consola.rellenar(String, int ancho)` | Alinea a la izquierda |
| `Consola.rellenarIzquierda(String, int ancho)` | Alinea a la derecha (números) |
| `Consola.recortar(String, int ancho)` | Corta con `…` si no cabe |
| `Consola.esperar(long ms)` | Pausa la ejecución |
| `Consola.ANCHO` | El ancho estándar: 74 |

---

## `Tabla` — tablas con columnas alineadas

```java
Tabla t = new Tabla("#", "PRODUCTO", "VENDIDOS", "FACTURADO");
t.alinearDerecha(0).alinearDerecha(2).alinearDerecha(3);
t.color(Ansi.CIAN);
t.fila("1", "Capuchino", "23", Consola.euros(48.30));
t.fila("2", "Croissant", "19", Consola.euros(34.20));
t.imprimir();
```

Los anchos se calculan solos. Si la tabla no cabe, encoge la columna más ancha.

| Método | |
|---|---|
| `new Tabla(String... cabeceras)` | |
| `.fila(String... celdas)` | Devuelve la tabla, así que se pueden encadenar |
| `.alinearDerecha(int columna)` | 0 = la primera |
| `.color(String colorAnsi)` | Color del borde |
| `.imprimir()` | |
| `.estaVacia()` / `.numeroDeFilas()` | |

> ⚠️ **No metas emojis dentro de una tabla.** En consola ocupan dos espacios y te
> descuadran las columnas. Los emojis, en líneas sueltas.

---

## `Marco` — cajas

```java
Marco.cajaSimple("MI TÍTULO", new String[]{ "primera línea", "segunda línea" }, Ansi.CIAN);
```

| Método | Estilo del borde |
|---|---|
| `Marco.cajaSimple(titulo, lineas, color)` | `┌─┐` fino |
| `Marco.cajaSimple(titulo, lineas)` | Igual, en gris |
| `Marco.cajaDoble(titulo, lineas, color)` | `╔═╗` doble |
| `Marco.cajaGruesa(titulo, lineas, color)` | `┏━┓` gruesa |
| `Marco.cajaRedondeada(titulo, lineas, color)` | `╭─╮` redondeada |
| `Marco.cajaEstrecha(titulo, lineas, color, ancho)` | A la anchura que le digas |
| `Marco.cartel(texto, color)` | Una línea centrada en caja gruesa |

El `titulo` puede ser `null` si no quieres título.

**Truco:** pon `Marco.SEPARADOR` como una de las líneas y dibuja un divisor horizontal
dentro de la caja.

```java
Marco.cajaDoble("CAJA", new String[]{
    "Saldo: " + Consola.euros(120),
    Marco.SEPARADOR,
    "Ingresos: " + Consola.euros(45)
}, Ansi.VERDE);
```

---

## `ArteAscii` — dibujos y barras

| Método | Qué devuelve |
|---|---|
| `ArteAscii.mostrarLogo()` | Pinta el logo del juego |
| `ArteAscii.barra(int actual, int total, int ancho)` | `[████░░░░] 50%`, se colorea sola |
| `ArteAscii.barra(int actual, int total, int ancho, String color)` | Sin porcentaje, color fijo |
| `ArteAscii.estrellas(int conseguidas, int maximo)` | `★★☆` |
| `ArteAscii.iconoDeProducto(String textoOCategoria)` | El dibujo que toque, según lo que diga el texto |
| `ArteAscii.cliente(int humor)` | Cara según el humor (0–100) |
| `ArteAscii.mostrar(String[] dibujo, String color, int sangria)` | Pinta un dibujo |
| `ArteAscii.unirHorizontal(String[] a, String[] b, int hueco)` | Pone dos dibujos lado a lado |
| `ArteAscii.mostrarFinal(String texto, String color)` | Rótulo grande enmarcado |

Dibujos disponibles como constantes: `TAZA`, `VASO_FRIO`, `BOLLERIA`, `SALADO`,
`CLIENTE_CONTENTO`, `CLIENTE_NEUTRO`, `CLIENTE_ENFADADO`, `CAJA_REGISTRADORA`,
`CARTEL_CERRADO`, `LOGO_CAFE`, `LOGO_JAVA`.

---

## `Ansi` — colores

```java
Consola.imprimir(Ansi.verde("¡bien!"));
Consola.imprimir(Ansi.negrita(Ansi.amarillo("2,50 €")));
```

Colores: `rojo`, `verde`, `amarillo`, `azul`, `magenta`, `cian`, `gris`, `naranja`,
`marron`, `crema`.
Estilos: `negrita`, `cursiva`, `atenuado`.

También como constantes (`Ansi.VERDE`, `Ansi.MARRON`…) para pasárselas a `Tabla` y `Marco`.

| Método | |
|---|---|
| `Ansi.desactivar()` | Quita todos los colores. Si tu terminal saca basura tipo `[32m`, llama a esto en la primera línea del `main` |
| `Ansi.limpiar(String)` | Quita los códigos de color de un texto |
| `Ansi.longitudVisible(String)` | Cuenta caracteres ignorando los códigos |

---

## Y si quieres tocar la vista

Puedes, es tu proyecto. Pero **antes de añadir algo aquí, pregúntate si no debería estar en
tu lógica**. La vista solo debería saber pintar. En cuanto le metas un `if` sobre reglas del
juego, has roto la separación que te va a permitir cambiar la consola por una ventana sin
tocar nada más.
