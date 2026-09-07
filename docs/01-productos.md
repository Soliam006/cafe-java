# Sesión 1 — La carta: los productos

> ⏱️ 2–3 horas · **Clases · `enum` · Herencia · Clases abstractas · Encapsulación**

---

## 1. Lo que vas a construir hoy

Todo lo que se puede vender en la cafetería. Al acabar la sesión tendrás una carta que se
imprime sola por pantalla, así:

```
  ┌───┬────────────────────┬─────────────┬────────┐
  │ # │ PRODUCTO           │ CATEGORIA   │ PRECIO │
  ├───┼────────────────────┼─────────────┼────────┤
  │ 1 │ Cafe solo          │ CAFE        │ 1,20 € │
  │ 2 │ Capuchino          │ CAFE        │ 2,10 € │
  │ 3 │ Frappe de vainilla │ BEBIDA_FRIA │ 3,40 € │
  │ 4 │ Croissant          │ REPOSTERIA  │ 1,80 € │
  └───┴────────────────────┴─────────────┴────────┘
```

Y, lo importante, tendrás la **jerarquía de herencia** sobre la que se apoya el resto del
juego.

---

## 2. La idea de hoy: una familia de productos

Piensa en un café con leche y en un croissant.

Se parecen en cosas: los dos tienen **nombre**, los dos tienen un **precio**, los dos se
pueden **vender**. Y se diferencian en cosas: el café tiene tamaño y tipo de leche, el
croissant se puede calentar o no.

Cuando varias clases comparten una parte y se diferencian en otra, en orientación a objetos
haces esto:

```
                    ┌──────────────────┐
                    │     Producto     │   ← abstracta: lo común
                    │  (no se vende    │      nombre, precio base, categoría
                    │   "un producto"  │
                    │   a secas)       │
                    └────────┬─────────┘
                             │  extends
        ┌────────────────────┼────────────────────┐
        │                    │                    │
   ┌────┴─────┐        ┌─────┴──────┐      ┌──────┴─────┐
   │  (café)  │        │(bebida fría)│      │(repostería)│   ← concretas: lo propio
   └──────────┘        └────────────┘      └────────────┘
```

La clase de arriba es **abstracta**: representa la idea de "producto" pero no se puede
crear ninguno. No existe "un producto" a secas en una cafetería; existe un capuchino o un
croissant. Eso es exactamente lo que significa `abstract`.

---

## 3. Conceptos de Java

> Los ejemplos de sintaxis van con **otro tema** (instrumentos musicales) a propósito.
> Copiar el patrón está bien; copiar la solución no te enseña nada.

### 3.1 `enum`: un conjunto cerrado de valores

Cuando algo solo puede tomar unos pocos valores fijos, **no uses un `String`**. Un `String`
admite `"grande"`, `"Grande"`, `"GRANDE"`, `"grande"` y `"pizza"`. Un `enum` solo admite
lo que tú has escrito, y si te equivocas al escribirlo **no compila**.

```java
public enum Afinacion {
    ESTANDAR, DROP_D, MEDIO_TONO_ABAJO
}
```

Un `enum` puede llevar datos y métodos dentro, y eso es lo que lo hace potente:

```java
public enum Afinacion {
    ESTANDAR         ("Estándar",  0),
    DROP_D           ("Drop D",   -2),
    MEDIO_TONO_ABAJO ("Eb",       -1);

    private final String etiqueta;
    private final int semitonos;

    Afinacion(String etiqueta, int semitonos) {   // el constructor es privado siempre
        this.etiqueta = etiqueta;
        this.semitonos = semitonos;
    }

    public String getEtiqueta() { return etiqueta; }
    public int getSemitonos()   { return semitonos; }
}
```

Y se usa así:

```java
Afinacion a = Afinacion.DROP_D;
System.out.println(a.getEtiqueta());   // Drop D
System.out.println(a);                 // DROP_D   (el toString por defecto)

for (Afinacion cada : Afinacion.values()) { ... }   // recorrer todos los valores
```

### 3.2 Clase abstracta y método abstracto

```java
public abstract class Instrumento {

    private final String nombre;        // final: una vez puesto, no cambia
    private final double precioBase;

    protected Instrumento(String nombre, double precioBase) {
        this.nombre = nombre;
        this.precioBase = precioBase;
    }

    // Método ABSTRACTO: no tiene cuerpo. Cada hija está OBLIGADA a escribirlo.
    public abstract double calcularPrecio();

    // Método normal: lo heredan todas las hijas tal cual.
    public String getNombre() { return nombre; }

    protected double getPrecioBase() { return precioBase; }
}
```

- `abstract class` → **no se puede hacer `new Instrumento(...)`**.
- `abstract` en un método → no tiene cuerpo, y toda clase hija concreta *tiene* que escribirlo.
- El constructor es `protected` porque solo lo van a llamar las hijas.

### 3.3 Heredar

```java
public class Guitarra extends Instrumento {

    private final int numeroDeCuerdas;

    public Guitarra(String nombre, double precioBase, int numeroDeCuerdas) {
        super(nombre, precioBase);          // ¡primero llamar al padre, siempre!
        this.numeroDeCuerdas = numeroDeCuerdas;
    }

    @Override
    public double calcularPrecio() {
        return getPrecioBase() + numeroDeCuerdas * 5.0;
    }

    @Override
    public String toString() {
        return getNombre() + " de " + numeroDeCuerdas + " cuerdas";
    }
}
```

Tres cosas que se te van a olvidar y que conviene que no:

- **`super(...)` va en la primera línea del constructor.** Sin excepción.
- **`@Override` no es obligatorio, pero ponlo siempre.** Si te equivocas al escribir el
  nombre del método, el compilador te avisa. Sin `@Override`, estarías creando un método
  nuevo sin darte cuenta y no entenderías por qué no se llama nunca.
- **`private` no se hereda de forma accesible.** Si la hija necesita leer un atributo del
  padre, usa el *getter* del padre (o declara el atributo `protected`, pero prefiere el
  getter).

---

## 4. Especificación: qué tienes que construir

Todo esto va en el paquete **`cafeteria.modelo`**.

### 4.1 Un `enum` para el tamaño

**Qué representa:** el tamaño en el que se sirve una bebida.

**Valores:** tres — pequeño, mediano y grande. (En Java los valores de un enum van en
MAYÚSCULAS y sin acentos ni eñes: te ahorras problemas.)

**Qué debe guardar cada valor:**
- Una etiqueta bonita para enseñar por pantalla ("Pequeño", "Mediano", "Grande").
- Un **multiplicador de precio**: el mediano cuesta más que el pequeño y el grande más que
  el mediano. Sugerencia: 1.0 / 1.25 / 1.5, pero es tuyo, ajústalo como quieras.
- Un **factor de consumo**: cuántos ingredientes gasta respecto al pequeño. Todavía no lo
  vas a usar (llega en la sesión 3), pero ponlo ya y te ahorras volver.

**Qué debe saber hacer:** devolver esos tres datos. Nada más.

### 4.2 Un `enum` para el tipo de leche

**Qué representa:** con qué leche se prepara una bebida.

**Valores:** al menos cuatro. Entera, desnatada, alguna vegetal (avena, soja…) y "sin leche".

**Qué debe guardar cada valor:**
- Su etiqueta para pantalla.
- Un **recargo** en euros: las vegetales suelen costar un poco más. "Sin leche" recarga 0.

**Por qué esto es importante:** cuando en la sesión 5 compares lo que has servido con lo
que te habían pedido, comparar dos valores de un `enum` es trivial y seguro. Comparar dos
`String` es una fuente inagotable de bugs.

### 4.3 Un `enum` para la categoría

**Qué representa:** la familia a la que pertenece un producto — café, bebida fría,
repostería, salado.

**Para qué sirve:** para filtrar la carta, para pintar el icono correcto y para agrupar en
las estadísticas del final.

**Qué debe guardar:** una etiqueta para pantalla. Con eso vale.

> 💡 Fíjate en que la categoría es **un dato**, mientras que "café" también es **una clase
> hija**. ¿Redundante? Un poco, y es una discusión de diseño de verdad. Lo hacemos así
> porque la clase determina *cómo se calcula el precio* (comportamiento) y la categoría
> sirve para *agrupar y filtrar* (dato). Piensa si estás de acuerdo — esa duda es
> justamente lo que te van a preguntar en un examen oral.

### 4.4 La clase abstracta de producto ⭐

Es la clase más importante del proyecto. Tómate tu tiempo con ella.

**Qué representa:** cualquier cosa que se pueda vender en la cafetería. Es abstracta
porque "un producto" sin más no existe: existen cafés, croissants o tostadas.

**Qué debe guardar (todo `private`):**

| Dato | Tipo | Notas |
|---|---|---|
| El nombre comercial | texto | "Capuchino", "Croissant"… |
| El precio base | decimal | Antes de tamaños, extras y recargos |
| La categoría | el enum de 4.3 | |

Los tres deberían ser `final`: un producto no cambia de nombre a mitad de partida.

**Qué debe saber hacer — métodos abstractos (los escriben las hijas):**

1. **Calcular su precio final.** Devuelve un decimal. Cada tipo de producto lo calcula a su
   manera: el café multiplica por el tamaño y suma el recargo de la leche, el croissant
   quizá cobra algo por calentarlo.

2. **Describirse en una línea.** Devuelve un texto listo para imprimir en la comanda, del
   estilo `"Capuchino (GRANDE, avena)"`. Cada hija sabe qué detalles suyos merecen salir.

**Qué debe saber hacer — métodos normales (los hereda todo el mundo):**

3. Los *getters* de sus tres atributos.
4. Un `toString()` decente. Consejo: que se apoye en el método de describirse, así no
   duplicas la lógica.

**Lo que NO debe hacer esta clase:**
- No imprime nada por pantalla. Ni un `System.out.println`. Devuelve textos, y quien la
  llama decide si los pinta o no.
- No sabe nada del inventario, ni de clientes, ni de la caja.

> ⚠️ **Aviso honesto:** en la sesión 3 vas a volver a esta clase para añadirle un método
> abstracto más (el que dice qué ingredientes consume). Es normal y tiene nombre:
> *refactorizar*. Ningún diseño sale perfecto a la primera, ni en clase ni en un trabajo
> de verdad.

### 4.5 Las clases hijas

Necesitas **tres como mínimo**, y te animo a hacer una cuarta.

#### Hija 1 — la bebida caliente (café y compañía)

**Qué la distingue:** se sirve en un tamaño y con un tipo de leche. Además puede llevar
**doble carga de café** (un shot extra), que sube el precio.

**Qué guarda:** un tamaño, un tipo de leche, y un booleano para el shot extra.

**Cómo calcula su precio:** precio base × multiplicador del tamaño, + recargo de la leche,
+ un fijo si lleva shot extra.

**Cómo se describe:** el nombre, y entre paréntesis el tamaño, la leche y el shot si lo lleva.

#### Hija 2 — la bebida fría

**Qué la distingue:** también tiene tamaño, pero además lleva **hielo** (o no) y puede
llevar **sirope**.

**Cómo calcula su precio:** parecido a la caliente, pero con sus propios recargos. Que no
sea idéntico — si dos hijas hacen exactamente lo mismo, es que sobra una.

#### Hija 3 — la repostería

**Qué la distingue:** no tiene tamaño ni leche. Tiene **unidades** (te pueden pedir dos
croissants) y puede ir **calentada**.

**Cómo calcula su precio:** precio base × unidades, + un pequeño recargo por calentar.

#### Hija 4 (opcional) — el salado

Tostadas, bocadillos, empanadas. Piensa tú qué la distingue: ¿ingrediente principal?
¿tipo de pan? ¿con o sin tomate?

---

## 5. Decisiones que tomas tú

Apúntalas en [NOMBRES.md](NOMBRES.md) según las decidas:

- Cómo se llama tu clase abstracta y cada una de sus hijas.
- Cómo se llaman tus tres `enum` y sus valores.
- Cómo se llaman los dos métodos abstractos.
- Qué precios y qué recargos pone tu cafetería.
- Si haces la cuarta hija y qué la distingue.

Sobre el nombre de los métodos abstractos, un consejo: **los métodos empiezan por verbo**.
`calcularPrecio()` está bien, `precio()` está regular y `elPrecio()` está mal. Y un método
que devuelve un `boolean` suele empezar por `es`, `esta` o `tiene`: `estaCalentado()`.

---

## 6. Buenas prácticas de hoy

**Atributos privados y `final` cuando puedas.**
`private final double precioBase;` dice algo muy útil a quien lee tu código: esto se pone
en el constructor y ya no cambia nunca. Si más adelante intentas cambiarlo por error, el
compilador te para.

**Valida en el constructor.**
Un producto con precio negativo no tiene sentido y no debería poder existir. Si te pasan
uno, lanza `new IllegalArgumentException("El precio no puede ser negativo")`. Es una
excepción que ya trae Java y está pensada exactamente para esto. Que un objeto **nunca
pueda estar en un estado inválido** es una de las mejores costumbres que puedes coger.

**No repitas código entre las hijas.**
Si las tres calculan `precioBase * algo`, quizá ese trocito común pueda vivir en el padre
como método `protected` y las hijas lo llamen. Ojo: no fuerces esto. A veces dos cosas se
parecen por casualidad y juntarlas es peor que repetirlas.

**Un fichero por clase.**
Aunque Java te deje meter varias clases en un mismo `.java`, no lo hagas. Los `enum`
también van en su propio fichero.

---

## 7. Errores típicos

| Lo que pasa | Por qué | Cómo se arregla |
|---|---|---|
| `Instrumento is abstract; cannot be instantiated` | Estás haciendo `new` de la clase abstracta | Haz `new` de una hija |
| `constructor X in class Y cannot be applied to given types` | Se te ha olvidado el `super(...)` o le faltan argumentos | El `super` va primero y con todos los datos que pide el padre |
| `nombre has private access in X` | La hija intenta tocar directamente un atributo privado del padre | Usa el getter del padre |
| El precio sale `0.0` siempre | Se te ha olvidado el `this.` en el constructor: `precio = precio;` no hace nada | `this.precio = precio;` |
| Las comparaciones de tamaño fallan | Estás guardando el tamaño como `String` | Para eso están los `enum` |
| El precio sale con 15 decimales | Los `double` son así | No lo arregles en el modelo: `Consola.euros(...)` ya lo formatea al imprimir |

---

## 8. Cómo comprobar que funciona

Crea una clase de pruebas con `main` — por ejemplo `Prueba01` en el paquete `cafeteria`.
Es código de usar y tirar, pero es la forma más rápida de ver si vas bien.

Tiene que hacer estas cinco cosas:

1. Crear un producto de cada tipo, con datos distintos.
2. Imprimir la descripción y el precio de cada uno, sueltos.
3. Meterlos todos en una `List` **del tipo del padre** y recorrerla imprimiendo el precio.
   *(Esto ya es polimorfismo, y es la comprobación importante: si compila y da precios
   distintos para cada uno, tu jerarquía está bien.)*
4. Intentar crear un producto con precio negativo, dentro de un `try/catch`, y comprobar
   que salta la excepción.
5. Pintar la carta usando la vista.

Para lo último, la vista te da esto:

```java
List<String[]> filas = new ArrayList<>();
// por cada producto, una fila de 4 textos: número, nombre, categoría, precio
filas.add(new String[]{ "1", "Capuchino", "CAFE", Consola.euros(2.10) });
Escena.carta(filas);
```

Y para la ficha grande de un producto:

```java
Escena.fichaProducto("Capuchino", "CAFE", Consola.euros(2.10),
        List.of("tamaño MEDIANO", "leche entera"));
```

No olvides los `import cafeteria.vista.*;` (IntelliJ te los pone con `Alt+Intro`).

---

## ✅ Checklist de la sesión 1

- [ ] Tengo tres `enum` con sus datos dentro y sus getters.
- [ ] Tengo una clase abstracta con al menos dos métodos abstractos.
- [ ] Tengo tres (o cuatro) clases hijas y **todas compilan**.
- [ ] Todos los atributos son `private`.
- [ ] Todos los métodos sobrescritos llevan `@Override`.
- [ ] Ninguna clase del modelo hace `System.out.println`.
- [ ] El constructor rechaza datos absurdos.
- [ ] Mi programa de prueba mete todos los productos en una `List` del tipo del padre y
      cada uno calcula su precio a su manera.
- [ ] La carta se pinta por pantalla.
- [ ] He apuntado mis nombres en [NOMBRES.md](NOMBRES.md).

---

## 🎯 Reto opcional

Añade a la clase abstracta un método **no abstracto** que devuelva el precio con IVA
(21 %). Como no es abstracto, lo heredan todas las hijas gratis y no hay que escribirlo
cuatro veces.

Ahora la pregunta interesante: **¿por qué ese método sí puede estar en el padre, y el de
calcular el precio no?** Si sabes contestarla, has entendido para qué sirve `abstract`.

---

**Siguiente:** [Sesión 2 — Personalizar y servir](02-interfaces.md) 👉
