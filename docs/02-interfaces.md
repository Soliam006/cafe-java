# Sesión 2 — Personalizar y servir: interfaces y polimorfismo

> ⏱️ 2–3 horas · **Interfaces · Polimorfismo · `List` · `instanceof`**
>
> 📌 Necesitas haber terminado la [sesión 1](01-productos.md).

---

## 1. Lo que vas a construir hoy

Dos cosas:

1. **Las capacidades** de tus productos: cuáles admiten extras (canela, sirope, azúcar) y
   cuáles se pueden calentar. Eso son **interfaces**.
2. **La carta** de la cafetería: la clase que guarda todos los productos y sabe buscarlos,
   filtrarlos y contarlos.

Al acabar tendrás una carta navegable y un programa que recorre productos de tipos
distintos tratándolos a todos igual. Eso último se llama **polimorfismo** y es, junto con
la herencia, la razón de existir de la programación orientada a objetos.

---

## 2. La idea de hoy: "es un" contra "puede"

En la sesión 1 usaste **herencia** y dijiste, sin decirlo:

> Un capuchino **es un** producto.

La herencia sirve para eso: para decir qué **es** una cosa. Y en Java una clase solo puede
heredar de **una** clase. Tiene sentido: un capuchino es un café, y no es a la vez un
croissant.

Pero hay otra clase de afirmación que también quieres poder hacer:

> Un capuchino **puede** llevar extras.
> Un croissant **puede** calentarse.
> Un frappé **puede** llevar extras, pero **no puede** calentarse.

Eso no son familias, son **capacidades**. Y una misma cosa puede tener varias a la vez.
Para eso están las **interfaces**: un contrato que dice "quien firme esto tiene que saber
hacer estas operaciones", sin decir nada sobre qué *es*.

```
        ┌──────────────────┐                  ┌──────────────────┐
        │  «interface»     │                  │  «interface»     │
        │  Personalizable  │                  │   Calentable     │
        │  (admite extras) │                  │ (se puede calentar)
        └────────┬─────────┘                  └─────────┬────────┘
                 │  implements                          │
        ┌────────┴────────┐                    ┌────────┴────────┐
        │  café           │                    │  repostería     │
        │  bebida fría    │                    │  salado         │
        └─────────────────┘                    └─────────────────┘
```

**La regla para decidir:** si la frase natural es *"X **es un** Y"*, usa herencia. Si es
*"X **puede** Y"* o *"X **sabe** Y"*, usa una interfaz.

---

## 3. Conceptos de Java

### 3.1 Declarar una interfaz

```java
public interface Afinable {

    // Los métodos de una interfaz son públicos y abstractos automáticamente.
    // No hace falta escribir ni "public" ni "abstract".
    void afinar(Afinacion nueva);

    Afinacion getAfinacion();

    // Un método "default" SÍ trae cuerpo. Quien implemente la interfaz lo recibe gratis,
    // y puede sobrescribirlo si le hace falta.
    default boolean estaEnAfinacionEstandar() {
        return getAfinacion() == Afinacion.ESTANDAR;
    }
}
```

Lo importante:

- Una interfaz **no tiene atributos** (bueno, puede tener constantes, pero no estado).
- Los métodos son abstractos por defecto: solo la firma, sin cuerpo.
- Los `default` son la excepción: llevan cuerpo y se heredan tal cual.

### 3.2 Implementarla

```java
public class Guitarra extends Instrumento implements Afinable, Transportable {

    private Afinacion afinacion = Afinacion.ESTANDAR;

    @Override
    public void afinar(Afinacion nueva) { this.afinacion = nueva; }

    @Override
    public Afinacion getAfinacion() { return afinacion; }

    // estaEnAfinacionEstandar() ya la tengo gratis: es default.
}
```

`extends` va antes que `implements`. Solo puedes extender **una** clase, pero implementar
**todas las interfaces que quieras**, separadas por comas.

### 3.3 Polimorfismo: la parte que importa

```java
List<Instrumento> tienda = new ArrayList<>();
tienda.add(new Guitarra("Telecaster", 900, 6));
tienda.add(new Bateria("Pearl Export", 1200, 5));

for (Instrumento i : tienda) {
    System.out.println(i.getNombre() + ": " + i.calcularPrecio());
}
```

Fíjate en lo que acaba de pasar. La lista es de `Instrumento`. El bucle no sabe si lo que
tiene entre manos es una guitarra o una batería. Llama a `calcularPrecio()` **y cada objeto
ejecuta su propia versión**.

Eso es el polimorfismo, y es lo que hace que puedas añadir un instrumento nuevo mañana sin
tocar ni una línea de este bucle.

### 3.4 Preguntar por una capacidad

A veces sí necesitas saber si un objeto tiene cierta capacidad:

```java
for (Instrumento i : tienda) {
    if (i instanceof Afinable afinable) {     // "pattern matching": comprueba Y declara
        afinable.afinar(Afinacion.DROP_D);    // aquí ya lo puedes usar como Afinable
    }
}
```

Esa forma con variable al final (`instanceof Afinable afinable`) es Java moderno y te
ahorra el *casting* a mano. Úsala.

> ⚠️ **Pero no abuses.** Si tu código está lleno de `if (x instanceof A) ... else if
> (x instanceof B) ...`, casi siempre significa que eso debería ser un método
> sobrescrito. La pregunta que te tienes que hacer es: *¿esto lo decide quien llama, o
> lo decide el propio objeto?* Si lo decide el objeto, es un método, no un `instanceof`.

---

## 4. Especificación: qué tienes que construir

### 4.1 Interfaz "admite extras"

**Qué representa:** la capacidad de añadirle cosas a un producto — canela, azúcar, sirope
de vainilla, nata, un shot extra…

**Quién la implementa:** las bebidas (calientes y frías). La repostería no.

**Qué operaciones tiene que ofrecer:**

| Operación | Qué recibe | Qué devuelve | Notas |
|---|---|---|---|
| Añadir un extra | el nombre del extra | nada | Piensa qué pasa si ya estaba puesto |
| Quitar un extra | el nombre del extra | `boolean` o nada | Devolver si estaba es útil |
| Consultar los extras puestos | nada | una lista de textos | ⚠️ lee el aviso de más abajo |
| Cuánto suman los extras | nada | decimal | Cada extra recarga el precio |

**Un método `default` de regalo:** "¿tiene algún extra?" se puede escribir a partir de la
lista de extras, así que ponlo `default` en la interfaz y no lo escribas dos veces.

> ⚠️ **Aviso importante sobre devolver listas.** Si tu método devuelve directamente la
> lista interna del objeto, cualquiera desde fuera puede hacer `producto.getExtras().clear()`
> y vaciártela. Acabas de romper el encapsulamiento sin querer. Se arregla devolviendo una
> copia (`new ArrayList<>(extras)`) o una vista de solo lectura
> (`Collections.unmodifiableList(extras)`). Esto entra en los exámenes.

**Dónde va la lista de extras:** una interfaz no puede tener atributos. Así que la `List`
vive en **cada clase que implementa la interfaz**. Sí, eso significa repetirla en dos
clases. Es el precio de las interfaces, y es un buen momento para preguntarte si preferirías
haberlo puesto en el padre. No hay respuesta única — piénsalo y decide.

### 4.2 Interfaz "se puede calentar"

**Qué representa:** la capacidad de servir algo caliente que normalmente está frío.

**Quién la implementa:** la repostería y el salado. Las bebidas no (una ya está caliente y
la otra no tendría sentido).

**Qué operaciones tiene que ofrecer:**
- Calentar el producto.
- Consultar si está calentado.
- Cuánto recarga el calentado (puede ser un `default` que devuelva una constante).

### 4.3 (Opcional) Interfaz "entra en la promoción del día"

Si te apetece una tercera: la capacidad de aplicar un descuento porcentual. La implementan
los productos que tú decidas. Te vendrá bien en la sesión 7 para hacer "el café del día".

### 4.4 Retoca tus clases de la sesión 1

Haz que cada hija implemente las interfaces que le correspondan, y que su cálculo de precio
tenga en cuenta lo nuevo (extras, calentado).

> 💡 Si al implementar la interfaz IntelliJ te subraya la clase en rojo, pon el cursor
> encima y pulsa `Alt+Intro` → *Implement methods*. Te genera las firmas vacías y tú
> rellenas los cuerpos.

### 4.5 La carta ⭐

Va en `cafeteria.logica` (no es una "cosa" del juego, es un servicio que organiza cosas).

**Qué representa:** el catálogo de todo lo que vende la cafetería.

**Qué debe guardar:** una `List` de productos. Del tipo del **padre**, para que quepan
todos. `private`, por supuesto.

**Qué debe saber hacer:**

| Operación | Qué recibe | Qué devuelve |
|---|---|---|
| Añadir un producto | un producto | nada |
| Cuántos productos hay | nada | entero |
| Coger el producto número N | posición | el producto |
| Buscar por nombre | texto | el producto, o `null`… ⚠️ ver abajo |
| Filtrar por categoría | la categoría | una lista de productos |
| Dar todos los productos | nada | la lista (¡copia o de solo lectura!) |
| Precio medio de la carta | nada | decimal |

**Sobre "buscar y no encontrar":** devolver `null` funciona, pero obliga a quien te llama a
acordarse de comprobarlo, y si se le olvida salta un `NullPointerException` en otro sitio y
te vuelves loca buscándolo. Java moderno prefiere `Optional<Producto>`, que **obliga** a
quien llama a plantearse el caso "no está". Míralo si te apetece; si te resulta demasiado
por ahora, usa `null` y **documéntalo con un comentario** encima del método.

**Un método más, para arrancar la partida:** algo del estilo "dame la carta de esta
cafetería, ya rellena" — un método `static` que crea una carta con 6 u 8 productos
inventados. Así no tienes que escribirlos a mano cada vez que pruebas algo.

**Y un método para pintarla:** que construya la `List<String[]>` que espera la vista y
llame a `Escena.carta(...)`.

> 🤔 **Pregunta de diseño para que le des una vuelta:** ¿ese último método debería estar
> aquí, o en una clase aparte? La carta es lógica; pintar es vista. Meter el `Escena.carta(...)`
> aquí mezcla un poco las dos cosas. Para un proyecto de este tamaño está bien y no pasa
> nada. En uno grande harías una clase aparte solo para "traducir" del modelo a la vista.
> Que sepas que la costura existe.

---

## 5. Decisiones que tomas tú

- Los nombres de tus dos (o tres) interfaces. Consejo: las interfaces de capacidad suelen
  acabar en **-able** o **-ible** (`Comparable`, `Serializable`, `Runnable`…). Sigue esa
  costumbre y quien lea tu código lo entenderá a la primera.
- Los nombres de sus métodos.
- Qué extras existen en tu cafetería y cuánto recarga cada uno.
- Qué productos concretos tiene tu carta.
- Si usas `null` u `Optional` al buscar.

Apúntalo todo en [NOMBRES.md](NOMBRES.md).

---

## 6. Buenas prácticas de hoy

**Interfaces pequeñas.**
Es mucho mejor tener dos interfaces de tres métodos que una de seis. Si una clase implementa
una interfaz y se ve obligada a dejar métodos vacíos o lanzando "esto no se puede", la
interfaz estaba mal cortada.

**Programa contra la interfaz, no contra la clase.**
Cuando declares una variable, usa el tipo más general que te valga:

```java
List<Producto> carta = new ArrayList<>();      // ✅ bien
ArrayList<Producto> carta = new ArrayList<>(); // ⛔ funciona, pero te ata a ArrayList
```

Así, si mañana cambias a `LinkedList`, solo tocas esa línea.

**No devuelvas tus colecciones internas tal cual.** (Ya te lo he dicho arriba, pero es que
es el error de encapsulación más común que existe.)

**Un `@Override` en cada método de interfaz.** También cuenta como sobrescritura.

---

## 7. Errores típicos

| Lo que pasa | Por qué | Cómo se arregla |
|---|---|---|
| `X is not abstract and does not override abstract method Y` | Has puesto `implements` pero falta un método | `Alt+Intro` → *Implement methods* |
| `interface expected here` | Has puesto `extends` donde iba `implements` | `extends` para clases, `implements` para interfaces |
| `variable extras might not have been initialized` | Declaraste la `List` pero no hiciste `new ArrayList<>()` | Inicialízala al declararla o en el constructor |
| `NullPointerException` al recorrer los extras | La lista es `null` porque nunca la inicializaste | Igual que arriba. Una `List` vacía **no** es `null` |
| `ConcurrentModificationException` | Estás quitando elementos de una lista mientras la recorres con `for-each` | Usa `removeIf(...)`, o un `Iterator`, o recorre una copia |
| El precio no cuenta los extras | Sobrescribiste el cálculo pero no sumas los extras | Repasa el método de precio de esa clase |

---

## 8. Cómo comprobar que funciona

Escribe un `Prueba02` con `main` que haga esto:

1. Crear la carta con su método de fábrica y pintarla con `Escena.carta(...)`.
2. Recorrer **todos** los productos en un bucle y, para cada uno:
   - imprimir su descripción y su precio (polimorfismo puro);
   - si admite extras, añadirle uno y volver a imprimir el precio para ver que ha subido;
   - si se puede calentar, calentarlo y comprobar lo mismo.

   Usa `instanceof` con variable para las dos comprobaciones.
3. Filtrar la carta por una categoría y pintar solo esa parte.
4. Buscar un producto que existe y otro que no, y comprobar que los dos casos se comportan
   bien.
5. Comprobar el encapsulamiento: coge la lista de extras de un producto, intenta hacerle
   `clear()` y mira si eso afecta al producto. **Si le afecta, tienes un problema** — y ya
   sabes cómo arreglarlo.

Para el paso 2, la vista te da la ficha grande:

```java
Escena.fichaProducto(nombre, categoria, Consola.euros(precio), listaDeDetalles);
```

---

## ✅ Checklist de la sesión 2

- [ ] Tengo al menos dos interfaces, cada una con un propósito claro.
- [ ] Al menos una tiene un método `default`.
- [ ] Cada clase hija implementa las que le corresponden — y **no todas implementan todas**.
- [ ] El cálculo de precio tiene en cuenta extras y calentado.
- [ ] Tengo una clase carta con su lista `private` y sus métodos de búsqueda y filtrado.
- [ ] Los métodos que devuelven colecciones **no** devuelven la lista interna.
- [ ] Un bucle sobre `List<Producto>` calcula precios distintos según el tipo real.
- [ ] La carta se pinta bonita.

---

## 🎯 Reto opcional

Ordena la carta por precio antes de pintarla.

Con `List` hay dos caminos: `Collections.sort(...)` con un `Comparator`, o el método
`sort(...)` de la propia lista. Prueba con una expresión lambda:

```java
lista.sort((a, b) -> Double.compare(a.calcularPrecio(), b.calcularPrecio()));
```

Si eso te ha parecido magia negra, tranquila: en la [sesión 7](07-estadisticas.md) lo
vemos despacio y con `Comparable`. Pero si lo pruebas ahora ya sabrás para qué sirve
cuando llegue.

---

**Siguiente:** [Sesión 3 — El almacén](03-inventario.md) 👉
