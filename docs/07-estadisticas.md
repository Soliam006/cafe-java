# Sesión 7 — Estadísticas, récords y pulido

> ⏱️ 2–3 horas · **`Comparable` · `Comparator` · Ficheros · Cierre del proyecto**
>
> 📌 Necesitas el juego funcionando de la [sesión 6](06-la-jornada.md).

---

## 1. Lo que vas a construir hoy

El juego ya se juega. Hoy le pones lo que hace que quieras volver a jugarlo:

- El **ranking** de productos: qué es lo que más se vende en tu cafetería.
- Las **estadísticas de la partida entera**, no solo del día.
- El **récord**, guardado en un fichero para que sobreviva a cerrar el programa.

```
  ┌────┬────────────────────┬──────────┬──────────┐
  │  # │ PRODUCTO           │ VENDIDOS │ FACTURADO│
  ├────┼────────────────────┼──────────┼──────────┤
  │  1 │ Capuchino          │       23 │  48,30 € │
  │  2 │ Croissant          │       19 │  34,20 € │
  │  3 │ Cafe con leche     │       15 │  24,00 € │
  └────┴────────────────────┴──────────┴──────────┘
```

---

## 2. La idea de hoy: ordenar cosas

Ordenar números es fácil. ¿Pero cómo ordena Java una lista de productos? No puede saberlo:
¿por precio? ¿por nombre? ¿por lo que se venden?

Java resuelve esto con dos herramientas, y la diferencia entre ellas entra en todos los
exámenes:

| | `Comparable` | `Comparator` |
|---|---|---|
| Dónde vive | **Dentro** de la clase que se ordena | **Fuera**, en un objeto aparte |
| Cuántos puedes tener | Uno solo | Todos los que quieras |
| Qué significa | *"El orden natural de esta clase"* | *"Una forma concreta de ordenar, entre varias"* |
| Cuándo usarlo | Hay un orden obvio y único | Hay varios criterios posibles |

Un producto se puede ordenar por precio, por nombre, por categoría o por ventas. **Como hay
varios criterios y ninguno es "el natural", esto pide `Comparator`.**

---

## 3. Conceptos de Java

### 3.1 `Comparable`: el orden natural

```java
public class Cancion implements Comparable<Cancion> {
    private final int duracion;

    @Override
    public int compareTo(Cancion otra) {
        return Integer.compare(this.duracion, otra.duracion);
    }
}

Collections.sort(listaDeCanciones);   // usa compareTo
```

El contrato de `compareTo` es siempre el mismo:

- **negativo** si `this` va antes que el otro,
- **cero** si dan igual,
- **positivo** si va después.

> ⚠️ **No restes para comparar.** `this.duracion - otra.duracion` parece que funciona y se
> rompe con números grandes por desbordamiento. Usa siempre `Integer.compare(a, b)` o
> `Double.compare(a, b)`. Este es un clásico de las preguntas con trampa.

### 3.2 `Comparator`: criterios a la carta

La forma larga:

```java
Comparator<Cancion> porDuracion = new Comparator<Cancion>() {
    @Override
    public int compare(Cancion a, Cancion b) {
        return Integer.compare(a.getDuracion(), b.getDuracion());
    }
};
```

La forma con lambda, que es lo mismo escrito corto:

```java
Comparator<Cancion> porDuracion = (a, b) -> Integer.compare(a.getDuracion(), b.getDuracion());
```

Y la forma corta de verdad, que es la que vas a usar:

```java
lista.sort(Comparator.comparing(Cancion::getTitulo));                    // por título
lista.sort(Comparator.comparingInt(Cancion::getDuracion).reversed());    // por duración, al revés
lista.sort(Comparator.comparing(Cancion::getArtista)
                     .thenComparing(Cancion::getTitulo));                // desempatando
```

Ese `Cancion::getTitulo` es una **referencia a método**: una forma corta de decir
`c -> c.getTitulo()`. Léelo como *"la función que, dada una canción, devuelve su título"*.

### 3.3 Ficheros, con lo mínimo imprescindible

```java
import java.nio.file.*;
import java.io.IOException;
import java.util.List;

Path fichero = Path.of("record.txt");

// Escribir
try {
    Files.writeString(fichero, "42");
} catch (IOException e) {
    Consola.aviso("No se ha podido guardar el récord.");
}

// Leer
try {
    if (Files.exists(fichero)) {
        String contenido = Files.readString(fichero);
        int record = Integer.parseInt(contenido.trim());
    }
} catch (IOException | NumberFormatException e) {
    Consola.aviso("No se ha podido leer el récord. Empezamos de cero.");
}
```

Tres cosas:

- `IOException` es **checked**: el compilador te obliga a tratarla. Ya sabes de qué va eso
  desde la sesión 3.
- Ese `catch (IOException | NumberFormatException e)` captura dos tipos a la vez. Es Java
  moderno y es cómodo.
- **Que no se pueda guardar el récord no puede tumbar la partida.** Avisas y sigues. Decidir
  qué errores son fatales y cuáles no es criterio de ingeniería, y aquí está claro.

> 💡 Si necesitas leer o escribir línea a línea, `Files.readAllLines` y `Files.write` con
> una `List<String>` te lo dan hecho sin pelearte con `BufferedReader`.

---

## 4. Especificación: qué tienes que construir

### 4.1 El registro de ventas

**Qué representa:** el contador de qué se ha vendido a lo largo de la partida.

**Qué guarda:** un `Map` de nombre de producto → cuántas unidades, y otro de nombre de
producto → cuánto se ha facturado con él. (O un `Map` de nombre → un objetito con los dos
datos, que es más limpio. Tú eliges — y ya deberías tener criterio para elegir.)

**Qué sabe hacer:**
- Apuntar una venta (producto e importe).
- Devolver el ranking ordenado por unidades vendidas, de mayor a menor.
- Devolver el producto más vendido y el que menos.
- Total facturado.

**Dónde se llama:** desde donde apuntas las estadísticas en la sesión 6. Si ese sitio es
uno solo, esto es añadir una línea. Si son tres, ya sabes qué tenías que haber hecho. 😉

### 4.2 El ranking, ordenado

Aquí es donde entra lo de arriba. Necesitas ordenar el `Map` por su valor, que no es
inmediato. El camino:

1. Sacar las entradas: `mapa.entrySet()`.
2. Meterlas en una `List`: `new ArrayList<>(mapa.entrySet())`.
3. Ordenar esa lista con un `Comparator` sobre el valor de cada entrada.
4. Recorrerla y montar las filas para la tabla.

Java trae un `Comparator` ya hecho para esto: `Map.Entry.comparingByValue()`. Y `.reversed()`
lo pone de mayor a menor.

**Piensa el desempate.** Si dos productos han vendido 12 unidades cada uno, ¿cuál va antes?
Encadena un segundo criterio con `thenComparing`. Sin desempate, el orden puede cambiar
entre ejecuciones y eso queda raro.

### 4.3 Las estadísticas de la partida

**Qué representa:** el acumulado de todos los días.

Si en la sesión 6 hiciste bien la clase de estadísticas del día, esto es **sumar objetos de
esos**. Añádele a esa clase un método que sume otra a sí misma, y la partida va acumulando
día a día.

Y luego los datos que solo tienen sentido a nivel de partida:
- Días trabajados.
- El mejor día (más ingresos) y el peor.
- Porcentaje global de aciertos.
- Media de ingresos por día.

### 4.4 El récord

**Qué representa:** la mejor marca conseguida nunca en este ordenador.

**Qué guarda:** un `Path` al fichero.

**Qué sabe hacer:** leer el récord (0 si no hay fichero) y guardarlo si el nuevo es mejor.

**Qué guardas como récord:** decídelo tú. ¿El dinero total? ¿Los días aguantados? ¿El
porcentaje de perfectos? Cada uno premia un estilo de juego distinto. Lo más interesante
suele ser guardar los tres.

Formato del fichero: lo más simple posible. Una línea por dato, o valores separados por
punto y coma. **No inventes un formato complicado**; si algún día lo necesitas, existen
JSON y CSV y hay librerías para los dos.

### 4.5 La pantalla final

Enriquece el final de partida con todo esto: el ranking, las estadísticas globales, y si se
ha batido el récord, decirlo bien alto.

```java
Escena.finDePartida(buenFinal, dias, totalGanado, motivo);

Tabla ranking = new Tabla("#", "PRODUCTO", "VENDIDOS", "FACTURADO");
ranking.alinearDerecha(0).alinearDerecha(2).alinearDerecha(3);
ranking.fila("1", "Capuchino", "23", Consola.euros(48.30));
ranking.imprimir();

Marco.cartel("¡NUEVO RÉCORD!", Ansi.AMARILLO);
```

---

## 5. Buenas prácticas de hoy

**No metas la lógica de ordenación dentro de la clase de producto.** El producto no tiene
por qué saber que existe un ranking. El `Comparator` vive fuera, y por eso es un
`Comparator` y no un `Comparable`.

**Que un fallo de fichero no tumbe el juego.** Guardar el récord es un extra, no un
requisito. Si falla, se avisa y se sigue.

**Nombres de ficheros como constantes.** `"record.txt"` escrito en tres sitios distintos es
un bug esperando a que cambies uno solo.

**Ojo con `parseInt` sobre lo que hay en un fichero.** Ese fichero lo puede haber tocado
cualquiera. Todo lo que entra de fuera del programa se valida. Siempre.

---

## 6. Errores típicos

| Lo que pasa | Por qué | Cómo se arregla |
|---|---|---|
| `UnsupportedOperationException` al ordenar | Estás ordenando una lista inmutable (`List.of(...)`) | `new ArrayList<>(lista)` primero |
| El orden sale al revés | `compareTo` con los operandos cambiados | `.reversed()`, o cambia el orden en el `compare` |
| Ordena mal con números grandes | Restaste en vez de usar `Integer.compare` | `Integer.compare(a, b)` |
| `NoSuchFileException` la primera vez | El fichero aún no existe | `Files.exists(...)` antes de leer |
| `NumberFormatException` al leer el récord | El fichero está vacío o tiene basura | `try/catch` y valor por defecto |
| El ranking cambia de orden entre partidas | Empates sin criterio de desempate | `thenComparing(...)` |
| El fichero se guarda en un sitio raro | Las rutas relativas dependen de dónde se ejecuta | Comprueba con `System.out.println(fichero.toAbsolutePath())` |

---

## 7. Cómo comprobar que funciona

1. Juega una partida entera. Al final tiene que salir el ranking, y **el producto que más
   has preparado tiene que estar el primero**.
2. Provoca un empate a propósito y comprueba que el desempate es estable.
3. Bate el récord. Cierra el programa. Vuelve a abrirlo. **Tiene que seguir ahí.**
4. Abre el fichero del récord con el bloc de notas, escríbele `patata` y arranca el juego.
   **No se puede caer.**
5. Borra el fichero y arranca. Tiene que empezar de cero sin protestar.

El punto 4 es el importante. Todo programa que lee algo de fuera tiene que aguantar que eso
de fuera sea basura.

---

## ✅ Checklist de la sesión 7

- [ ] Llevo la cuenta de lo vendido por producto.
- [ ] El ranking sale ordenado con un `Comparator` y tiene desempate.
- [ ] Uso `Integer.compare` / `Double.compare`, nunca restas.
- [ ] Las estadísticas de la partida se acumulan sumando las de cada día.
- [ ] El récord se guarda en un fichero y sobrevive a cerrar el programa.
- [ ] Un fichero corrupto o inexistente no rompe nada.
- [ ] La pantalla final enseña ranking, estadísticas y récord.

---

## 🎉 Has terminado el proyecto

Mira hacia atrás un momento. En siete sesiones has usado, sin que ninguna te la regalaran:

| Concepto | Dónde |
|---|---|
| Clases, atributos, constructores, encapsulación | Todo el proyecto |
| `enum` con datos y métodos | Tamaños, leches, categorías, ingredientes, calidad |
| **Herencia** y clases **abstractas** | La familia de productos |
| Métodos abstractos y sobrescritura | Precio, descripción, ingredientes, opciones |
| **Interfaces** y métodos `default` | Personalizable, calentable |
| **Polimorfismo** | Cada recorrido de la carta |
| **Composición** | Cliente → pedido → línea → producto |
| `List`, `Map`, `Queue`, `Set` | Carta, almacén, cola, extras |
| **Excepciones propias**, checked, `try/catch` | El almacén |
| `equals` y `hashCode` | Comparar opciones |
| `Comparable` y `Comparator` | El ranking |
| Ficheros y `IOException` | El récord |
| Separación modelo / vista | Toda la arquitectura |
| Bucle principal y máquina de estados | La jornada y la partida |

Eso es, con diferencia, más de lo que se pide en una práctica de primero. Y lo tienes en un
proyecto que **funciona y se juega**, no en veinte ejercicios sueltos.

---

## 🎯 Y si te quedas con ganas

Por dificultad creciente:

1. **Modo fácil / normal / difícil.** Ya sabes dónde encaja: el evaluador como interfaz,
   con implementaciones distintas.
2. **Empleados.** Contratas a alguien que atiende clientes solo, con su probabilidad de
   fallo. Nueva jerarquía de clases con su herencia.
3. **Guardar la partida** para continuarla otro día. Serializar todo el estado a fichero.
4. **Mejoras de la cafetería.** Compras una cafetera mejor (preparación más rápida), una
   vitrina (la repostería no caduca), una segunda barra. Es un árbol de mejoras y da mucho
   juego.
5. **Fidelidad de clientes.** Los que atiendes bien vuelven y traen amigos; los que atiendes
   mal no vuelven. Necesita que los clientes persistan entre días.
6. **Tests de verdad** con JUnit, en vez de tus clases `PruebaN`. Es el siguiente paso
   natural y lo vas a dar en la carrera. Tu proyecto está bien preparado para ello
   precisamente porque el modelo no imprime nada.
7. **Interfaz gráfica** con JavaFX o Swing. Y aquí llega la recompensa de haber separado
   modelo y vista desde el día 0: **tendrías que reescribir el paquete `vista` y nada más**.
   Todo lo que has escrito tú se queda tal cual.

---

**Vuelta al índice:** [README](../README.md)
