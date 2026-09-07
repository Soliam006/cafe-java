# Sesión 3 — El almacén: `Map` y excepciones

> ⏱️ 2–3 horas · **`Map` · Excepciones propias · `try/catch` · `throws`**
>
> 📌 Necesitas las sesiones [1](01-productos.md) y [2](02-interfaces.md).

---

## 1. Lo que vas a construir hoy

El almacén de la cafetería: cuánto café molido queda, cuánta leche, cuántos croissants.
Y la regla que convierte esto en un juego de verdad:

> **Preparar gasta género. Aciertes o no.**

Si te equivocas de producto, no solo no cobras: además has tirado los ingredientes. Ese es
el riesgo que hace que el juego tenga tensión.

Al acabar tendrás esto pintándose por pantalla:

```
  ┌─────────────────────┬───────┬──────┬────────────────────┐
  │ INGREDIENTE         │ QUEDA │   DE │ NIVEL              │
  ├─────────────────────┼───────┼──────┼────────────────────┤
  │ Cafe molido (g)     │   320 │  500 │ [█████████░░░░░] 64%│
  │ Leche (ml)          │   900 │ 2000 │ [██████░░░░░░░░] 45%│
  │ Hielo (cubitos)     │     0 │   60 │ [░░░░░░░░░░░░░░]  0%│
  └─────────────────────┴───────┴──────┴────────────────────┘
```

---

## 2. La idea de hoy: cuando algo puede salir mal

Hasta ahora todos tus métodos siempre podían hacer su trabajo. `calcularPrecio()` siempre
devuelve un precio. Hoy aparece el primero que **puede no poder**:

> "Consume 200 ml de leche de avena." — *No hay. Solo quedan 120.*

Tienes tres formas de reaccionar, y elegir bien es la lección de hoy:

| Opción | Qué pasa | Valoración |
|---|---|---|
| Devolver `false` | Quien llama comprueba… si se acuerda | 😐 Fácil de ignorar por accidente |
| Poner la leche en negativo y seguir | El objeto queda en un estado imposible | ⛔ Nunca |
| **Lanzar una excepción** | Quien llama **está obligado** a enfrentarse al problema | ✅ |

Las excepciones no son "errores del programa". Son **una forma de devolver una respuesta
excepcional** sin poder ser ignorada. Y cuando el problema es previsible y forma parte de
las reglas del juego —como quedarse sin leche—, esa es la herramienta correcta.

---

## 3. Conceptos de Java

### 3.1 `Map`: pares clave → valor

Una `List` guarda cosas en orden y las busca por posición. Un `Map` las guarda **por clave**
y las busca por clave. Para un almacén es lo natural: la clave es el ingrediente, el valor
es cuánto queda.

```java
Map<String, Integer> stock = new HashMap<>();

stock.put("cuerdas", 40);            // añade o reemplaza
stock.put("cuerdas", 35);            // ahora vale 35, no hay duplicados

int c = stock.get("cuerdas");        // 35
Integer x = stock.get("baquetas");   // null: esa clave no existe

int seguro = stock.getOrDefault("baquetas", 0);   // 0. Mucho mejor que arriesgarse al null

stock.containsKey("cuerdas");        // true
stock.remove("cuerdas");

// Recorrerlo entero:
for (Map.Entry<String, Integer> e : stock.entrySet()) {
    System.out.println(e.getKey() + " -> " + e.getValue());
}
```

> 💡 `getOrDefault` te va a ahorrar la mitad de los `NullPointerException` de hoy. Cógele
> cariño.

**Ojo con un detalle que muerde:** un `Map<String, Integer>` guarda objetos `Integer`, no
`int`. Si haces `mapa.get(k) == 500` puede darte `false` aunque valga 500, porque estás
comparando referencias. Guarda el resultado en un `int` primero, o usa `.equals()`.

### 3.2 Crear tu propia excepción

Una excepción es una clase normal que hereda de `Exception`:

```java
public class CuerdaRotaException extends Exception {

    private final String instrumento;

    public CuerdaRotaException(String instrumento, int cuerda) {
        super("Se ha roto la cuerda " + cuerda + " de " + instrumento);   // el mensaje
        this.instrumento = instrumento;
    }

    public String getInstrumento() { return instrumento; }
}
```

Fíjate en que le he metido un atributo. Eso es lo bueno de las excepciones propias: no solo
dicen "algo falló", **traen los datos del fallo** para que quien la reciba pueda reaccionar
con información.

### 3.3 Lanzarla y capturarla

```java
// Quien la lanza avisa en su firma con "throws":
public void tocar(int cuerda) throws CuerdaRotaException {
    if (tension[cuerda] > MAXIMO) {
        throw new CuerdaRotaException(nombre, cuerda);
    }
    ...
}

// Quien la llama está OBLIGADO a hacer una de dos cosas:
try {
    guitarra.tocar(3);
} catch (CuerdaRotaException e) {
    System.out.println(e.getMessage());
    System.out.println("Instrumento afectado: " + e.getInstrumento());
}
```

La otra opción es no capturarla y poner `throws` también en tu método, pasándole el marrón
a quien te llame a ti. Las dos son válidas: **captúrala en el sitio donde de verdad se
puede hacer algo al respecto**, no antes.

### 3.4 *Checked* y *unchecked*: la distinción que hay que entender

| | Hereda de | ¿Obliga a `try/catch`? | Para qué es |
|---|---|---|---|
| **Checked** | `Exception` | **Sí**, el compilador te obliga | Problemas previsibles del dominio: no hay stock, no existe el fichero |
| **Unchecked** | `RuntimeException` | No | Bugs de programación: un `null` donde no debía, un índice fuera de rango |

Para "no hay ingredientes suficientes" quieres una **checked**: es parte de las reglas del
juego, es previsible, y quieres que quien prepare un café **no pueda olvidarse** de que eso
puede pasar.

### 3.5 Dos cosas que no debes hacer nunca

```java
try {
    ...
} catch (Exception e) {          // ⛔ capturar "Exception" a secas: te tragas TODO,
}                                //    incluidos los bugs de verdad. Y encima vacío.
```

```java
catch (SinGeneroException e) {
    e.printStackTrace();         // ⛔ en un programa terminado esto no es manejar el error,
}                                //    es enseñarle al usuario la tripa del programa
```

Captura **el tipo concreto**, y en el `catch` **haz algo**: avisar al usuario, cancelar la
operación, cobrar 0. En este juego casi siempre será "díselo por pantalla y sigue con el
siguiente cliente".

---

## 4. Especificación: qué tienes que construir

### 4.1 Los ingredientes

**Qué representa:** cada materia prima que se gasta al preparar algo: café molido, leche,
leche de avena, azúcar, hielo, croissants, pan, jamón…

**Cómo lo modelas: te recomiendo un `enum`.** Es un conjunto cerrado (tú decides qué
ingredientes existen en tu cafetería) y te da dos ventajas grandes: no puedes escribir mal
el nombre, y compararlos es exacto.

**Qué debe guardar cada ingrediente:**
- Su etiqueta para pantalla ("Café molido").
- Su unidad de medida ("g", "ml", "ud").
- **Su precio por unidad.** Lo vas a necesitar para calcular cuánto género has gastado en
  el resumen del día y cuánto cuesta reponer el almacén.

> La alternativa es una clase normal, y también es defendible: te permitiría inventar
> ingredientes en tiempo de ejecución. Pero para este proyecto el `enum` es más limpio y
> te va a dar menos guerra. Si prefieres la clase, adelante — pero ten claro **por qué**.

### 4.2 Refactor: qué ingredientes necesita cada producto ⭐

Aquí está el retoque que te avisé en la sesión 1. Vuelve a tu clase abstracta y añádele un
**método abstracto más**:

> **"Dime qué ingredientes necesitas y cuánto de cada uno."**
> Recibe: nada. Devuelve: un `Map` de ingrediente → cantidad.

Y luego impleméntalo en cada hija:

- Un café: café molido, y leche si la lleva. Y la cantidad **debe depender del tamaño**
  (para eso pusiste el factor de consumo en el `enum` de tamaños en la sesión 1 — ahora se
  ve para qué era).
- Una bebida fría: su base, hielo si lleva, sirope si lleva.
- Un croissant: croissants, y multiplicado por las unidades pedidas.

**Esto es una refactorización de verdad** y vas a tocar cinco ficheros. Es exactamente lo
que pasa en un proyecto real cuando aparece un requisito nuevo. Fíjate en una cosa buena:
el compilador te va a ir señalando **todas** las clases que tienes que arreglar, una por
una, hasta que no quede ninguna. Esa es la razón por la que Java es un lenguaje pesado de
escribir pero difícil de romper.

### 4.3 El almacén ⭐

Va en `cafeteria.logica`.

**Qué representa:** todo el género disponible en la cafetería, con sus cantidades.

**Qué debe guardar:**
- Un `Map` de ingrediente → cantidad **actual**, `private`.
- Un `Map` de ingrediente → cantidad **inicial** del día. Lo necesitas para pintar la barra
  de "cuánto queda del total" y para saber cuánto has gastado.

**Qué debe saber hacer:**

| Operación | Qué recibe | Qué devuelve | Notas |
|---|---|---|---|
| Reponer / meter género | ingrediente y cantidad | nada | Suma a lo que ya hubiera |
| Consultar cuánto queda | un ingrediente | entero | 0 si no hay ninguno, nunca `null` |
| ¿Hay suficiente para esto? | un `Map` de necesidades | `boolean` | Comprueba **todos** antes de gastar nada |
| **Consumir** | un `Map` de necesidades | nada | 💥 **Lanza tu excepción si no hay bastante** |
| ¿Está el almacén vacío? | nada | `boolean` | Para saber cuándo acaba el día |
| Porcentaje de género restante | nada | entero 0–100 | Para la barra del HUD |
| Coste del género consumido | un `Map` de necesidades | decimal | Usando el precio por unidad |
| Datos para pintar | nada | `List<String[]>` | Cada fila: nombre, actual, inicial |

**La regla más importante de esta clase, y no es negociable:**

> ⚠️ **Consumir es todo o nada.**
> Si te piden gastar café Y leche, y hay café pero no leche, **no puedes gastar el café**.
> Comprueba primero que hay de todo, y solo entonces resta. Si te lo saltas, te quedará un
> almacén a medio consumir después de un fallo — y esos bugs son horribles de encontrar.
>
> Esta idea tiene nombre en informática: **atomicidad**. Es la 'A' de las propiedades ACID
> de las bases de datos, y te la vas a encontrar otra vez en segundo o tercero.

### 4.4 Tu excepción

**Qué representa:** que se ha intentado consumir más de lo que hay.

**De qué hereda:** de `Exception` (checked — quieres que el compilador obligue a tratarla).

**Qué debe traer dentro:** no te limites al mensaje. Mete el ingrediente que faltó, cuánto
hacía falta y cuánto había. Así, cuando la captures, podrás enseñar un mensaje útil de
verdad:

> *"Te has quedado sin leche de avena: necesitabas 200 ml y solo quedaban 120."*

Compáralo con un escueto *"error de stock"*. La diferencia entre un programa que se usa y
uno que no suele estar justo ahí.

---

## 5. Decisiones que tomas tú

- Cómo se llama tu almacén, tu ingrediente y tu excepción.
  (Las excepciones **siempre** acaban en `Exception`. Eso sí es una convención firme.)
- Qué ingredientes existen y cuánto cuesta cada unidad.
- Con cuánto género empieza el día 1.
- Las recetas: cuánto café lleva un capuchino, cuánta leche…
- Si el consumo por tamaño lo multiplicas o lo sumas.

---

## 6. Buenas prácticas de hoy

**Que el objeto nunca quede en un estado imposible.** Cantidades negativas, no. Ni
"temporalmente mientras arreglo algo". Nunca.

**El almacén no habla con el usuario.** No imprime "no hay leche": **lanza** la excepción y
que quien la capture decida si eso se dice por pantalla, se apunta en un registro o se
ignora. El modelo no sabe que existe una pantalla.

**Mensajes de excepción concretos.** "No hay stock" es inútil. "Faltan 80 ml de leche de
avena (necesarios 200, disponibles 120)" es oro. Escribe los mensajes pensando en ti misma
a las tres de la mañana buscando por qué falla algo.

**Constantes con nombre.** Si el día empieza con 500 g de café, no escribas `500` suelto
en medio del código. `private static final int CAFE_INICIAL = 500;` arriba de la clase. En
tres semanas no te acordarás de qué era ese 500.

---

## 7. Errores típicos

| Lo que pasa | Por qué | Cómo se arregla |
|---|---|---|
| `unreported exception X; must be caught or declared` | Llamas a un método que lanza una checked | Rodéalo con `try/catch` o añade `throws` a tu método |
| `NullPointerException` al leer del `Map` | La clave no existía y `get` devolvió `null` | `getOrDefault(clave, 0)` |
| El stock se queda a medias tras un fallo | Restaste antes de comprobarlo todo | Comprueba primero, resta después |
| La cantidad no cambia | Modificaste una copia del valor en vez del `Map` | Los `int` se copian: hay que hacer `put` del nuevo valor |
| Al comparar cantidades pasan cosas raras con números grandes | Comparaste `Integer` con `==` | Guárdalo en un `int` o usa `.equals()` |
| El programa se cae al quedarse sin género | Nadie captura tu excepción | Captúrala donde se prepara el producto |

---

## 8. Cómo comprobar que funciona

En `Prueba03`:

1. Crea el almacén con el género inicial y píntalo con `Escena.inventario(...)`.
2. Coge un producto de la carta, pídele sus ingredientes e imprímelos.
3. Consume esos ingredientes y vuelve a pintar el almacén. Las barras tienen que bajar.
4. Consume en bucle **hasta que salte la excepción**. Captúrala e imprime su mensaje con
   `Consola.error(e.getMessage())`.
5. Comprueba lo de la atomicidad: prepara una situación donde haya de un ingrediente pero
   no de otro, intenta consumir los dos, captura la excepción y **verifica que el primero
   sigue intacto**.
6. Imprime el porcentaje restante y compruébalo con `ArteAscii.barra(porcentaje, 100, 20)`.

El punto 5 es el importante. Es el tipo de prueba que separa a quien programa de quien
copia código.

---

## ✅ Checklist de la sesión 3

- [ ] Tengo los ingredientes modelados con su unidad y su precio.
- [ ] Mi clase abstracta tiene el método de "qué ingredientes necesito" y **todas** las
      hijas lo implementan.
- [ ] El consumo depende del tamaño y de las unidades.
- [ ] Tengo mi excepción, hereda de `Exception` y trae datos útiles dentro.
- [ ] Consumir es atómico: o se gasta todo o no se gasta nada.
- [ ] Ningún método del almacén imprime nada.
- [ ] Sé decir qué porcentaje de género queda.
- [ ] El almacén se pinta y las barras bajan al consumir.

---

## 🎯 Reto opcional

Añade al almacén el concepto de **caducidad**: la leche y la repostería que sobran al
acabar el día se tiran, pero el café molido y el azúcar se guardan.

Piensa cómo modelarlo. ¿Un dato más en el `enum` de ingredientes? ¿Una interfaz
"perecedero"? Las dos valen. Elige una **y escribe en un comentario por qué la has
elegido** — argumentar decisiones de diseño es la mitad de la nota en cualquier práctica.

---

**Siguiente:** [Sesión 4 — Los clientes](04-clientes-y-pedidos.md) 👉
