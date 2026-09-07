# Sesión 4 — Los clientes y sus comandas

> ⏱️ 2–3 horas · **Composición · `Queue` · `Random` · Encapsulación**
>
> 📌 Necesitas las sesiones [1](01-productos.md), [2](02-interfaces.md) y [3](03-inventario.md).

---

## 1. Lo que vas a construir hoy

La cola de la cafetería. Clientes con nombre, con prisa y con una comanda concreta:

```
        .---.
       / ^ ^ \     Marta
       |  ᵕ  |     Paciencia [████████████░░░░]
        \___/
       /|   |\     «Buenos días, tengo algo de prisa...»
        |___|
        |   |

   ┌──────────────────────────────────────────────┐
   │            *** COMANDA #1 ***                │
   │                 para Marta                   │
   ├──────────────────────────────────────────────┤
   │  1x Capuchino  (GRANDE, avena, con canela)   │
   │  2x Croissant  (calentado)                   │
   ├──────────────────────────────────────────────┤
   │            gracias por su visita             │
   └──────────────────────────────────────────────┘
```

Y un generador que los inventa solos, cada día distintos y cada día un poco más exigentes.

---

## 2. La idea de hoy: composición

En la sesión 1 usaste herencia: *"un capuchino **es un** producto"*.
En la sesión 2 usaste interfaces: *"un capuchino **puede** llevar extras"*.

Hoy toca la tercera y, con diferencia, la que más vas a usar en tu vida:

> Un cliente **tiene un** pedido.
> Un pedido **tiene varias** líneas.
> Una línea **tiene un** producto y **tiene unas** opciones.

Eso es **composición**: una clase que guarda otras clases como atributos. Sin herencia, sin
interfaces, sin nada raro. Es tan simple que cuesta creer que sea el pilar del diseño
orientado a objetos, pero lo es.

```
   Cliente ──tiene un──> Pedido ──tiene muchas──> Línea ──tiene un──> Producto
                                                        └─tiene unas─> Opciones
```

> 📌 **La regla que te va a salvar de muchos diseños malos:**
> *Prefiere composición antes que herencia.*
> La herencia es rígida: una clase solo puede tener un padre y queda atada a él para
> siempre. La composición se cambia en una línea. Cuando dudes entre "¿heredo o guardo un
> atributo?", la respuesta correcta suele ser guardar un atributo.

---

## 3. Conceptos de Java

### 3.1 `Queue`: la cola de toda la vida

Los clientes se atienden **por orden de llegada**. Java tiene una interfaz exactamente para
eso: `Queue` (FIFO, *first in, first out*).

```java
Queue<String> cola = new LinkedList<>();     // o new ArrayDeque<>()

cola.offer("Marta");        // añadir al final
cola.offer("Julio");

String siguiente = cola.peek();   // MIRAR el primero sin sacarlo -> "Marta"
String atendido  = cola.poll();   // SACAR el primero              -> "Marta"

cola.isEmpty();             // ¿queda alguien?
cola.size();                // cuántos esperan
```

`peek` y `poll` devuelven `null` si la cola está vacía, así que **comprueba `isEmpty()` antes**.

> 💡 ¿Por qué una `Queue` y no una `List`? Podrías hacerlo con una `List` y `remove(0)`.
> Pero al declararla como `Queue` estás diciéndole a quien lea tu código: *"esto se atiende
> por orden, y no voy a andar metiendo mano por el medio"*. Elegir el tipo correcto es
> documentación gratis.

### 3.2 `Random`: que cada partida sea distinta

```java
Random azar = new Random();

int n = azar.nextInt(5);          // entero de 0 a 4 (el 5 NO entra)
int m = azar.nextInt(2, 6);       // de 2 a 5
boolean b = azar.nextBoolean();
double d = azar.nextDouble();     // decimal entre 0.0 y 1.0

// Elegir un elemento al azar de un array o lista:
String[] nombres = {"Marta", "Julio", "Nuria"};
String elegido = nombres[azar.nextInt(nombres.length)];

// Que algo pase el 30% de las veces:
if (azar.nextDouble() < 0.3) { ... }
```

Para elegir un valor de un `enum` al azar: `Tamanio.values()[azar.nextInt(Tamanio.values().length)]`.

> 💡 **Truco de oro para depurar:** `new Random(42)` (con una semilla) genera **siempre la
> misma secuencia**. Cuando algo te falle y no sepas por qué, ponle semilla fija y podrás
> repetir la partida exacta las veces que quieras. Quítala cuando ya funcione.

### 3.3 Un objeto solo para agrupar datos

A veces necesitas una clase cuyo único trabajo es **llevar varios datos juntos**. Sin
lógica, casi sin métodos. Es perfectamente legítimo, y tiene un nombre: objeto de valor.

Java tiene una forma corta de escribirlas, los `record`:

```java
public record Medida(int ancho, int alto) { }
```

Esa línea te da: constructor, *getters* (`m.ancho()`), `equals`, `hashCode` y `toString`.
Todo hecho, y los datos son inmutables.

> 🤔 **Para hoy te recomiendo NO usar `record`, y escribir la clase entera a mano.**
> No porque los `record` sean malos —son estupendos— sino porque escribir un `equals` y un
> `hashCode` con tus propias manos es exactamente lo que te van a pedir en el examen, y en
> la sesión 5 vas a necesitar entender qué hace ese `equals` por dentro. Cuando lo hayas
> hecho a mano una vez, usa `record` para siempre.

---

## 4. Especificación: qué tienes que construir

Todo en `cafeteria.modelo`, salvo el generador.

### 4.1 La ficha de opciones ⭐

Esta clase es la pieza que va a hacer que la sesión 5 sea fácil. Merece la pena hacerla bien.

**Qué representa:** cómo quiere el cliente su producto. Todas las decisiones que se pueden
tomar sobre un producto, juntas en un solo objeto.

**Qué debe guardar:**

| Dato | Tipo | Para qué |
|---|---|---|
| Tamaño | tu enum de tamaños | Bebidas |
| Tipo de leche | tu enum de leches | Bebidas calientes |
| Unidades | entero | Repostería ("dos croissants") |
| ¿Calentado? | `boolean` | Repostería y salado |
| ¿Con hielo? | `boolean` | Bebidas frías |
| Lista de extras | `List<String>` | Cualquiera que admita extras |

Sí, cada producto solo usa una parte. Un croissant no tiene tamaño. **Y no pasa nada**:
lleva ahí un valor por defecto que nadie mira. Es un compromiso consciente, y a cambio
tienes un solo tipo que sirve para todo, en vez de tres clases de opciones distintas.

**Qué debe saber hacer:**
- Sus *getters*.
- Un `toString()` que devuelva algo como `"GRANDE, avena, con canela"` — te va a servir
  directamente para pintar el ticket, así que hazlo bonito.
- 🔑 **Un `equals()`** que diga si dos fichas de opciones son idénticas.
  Escríbelo a mano. En la sesión 5 lo vas a usar en cada comparación.

**Constructores:** haz al menos dos. Uno completo, y otro corto con lo típico (tamaño y
leche) que rellene el resto por defecto. Que un constructor llame a otro con `this(...)`
es buena costumbre y evita duplicar código.

> 💡 **Deberían ser inmutables.** Atributos `final`, sin *setters*. Si alguien quiere unas
> opciones distintas, que construya otras. Los objetos inmutables no te dan sorpresas
> nunca: nadie puede cambiártelos por detrás mientras los comparas.

### 4.2 Refactor: crear un producto con unas opciones ⭐

Segundo (y último) retoque de tu clase abstracta. Añádele este método **abstracto**:

> **"Dame una copia de ti mismo, preparada con estas opciones."**
> Recibe: una ficha de opciones. Devuelve: un producto nuevo del mismo tipo.

Cada hija lo implementa cogiendo de las opciones **solo lo que le interesa**: el café coge
tamaño, leche y extras; el croissant coge unidades y calentado; y cada uno ignora el resto.

**Por qué esto vale la pena.** Los productos de tu carta pasan a ser **plantillas**: dicen
qué es un capuchino y cuánto cuesta de base. Cuando llega un cliente, tanto lo que él pide
como lo que tú preparas se construyen igual: `plantilla.conEstasOpciones(opciones)`.

Y entonces evaluar se convierte en dos preguntas:

1. ¿Es la misma plantilla? → si no, 0 %.
2. ¿Son las mismas opciones? → si sí 100 %, si no 50 %.

Ese es todo el corazón del juego, y sale gratis por haber diseñado bien. Mañana lo ves.

### 4.3 La línea de comanda

**Qué representa:** una línea del ticket. "2x Croissant calentado".

**Qué guarda:** la plantilla de producto, la ficha de opciones y la cantidad.

**Qué sabe hacer:**
- Sus getters.
- Construir el producto real que corresponde (usando el método de 4.2).
- Calcular su importe (precio del producto × cantidad).
- Un texto de una línea listo para el ticket.

### 4.4 El pedido

**Qué representa:** todo lo que pide un cliente.

**Qué guarda:** un número identificador y una `List` de líneas.

**Qué sabe hacer:**
- Añadir una línea.
- Cuántas líneas tiene, y coger la número N.
- Calcular el importe total (suma de todas las líneas).
- Devolver `List<String>` con los textos de las líneas — así se lo pasas tal cual a
  `Escena.ticketPedido(...)`.

### 4.5 El cliente

**Qué representa:** la persona que está en la barra esperando.

**Qué guarda:** nombre, su pedido, su paciencia actual, su paciencia máxima y la frase que
suelta al llegar.

**Qué sabe hacer:**

| Operación | Notas |
|---|---|
| Sus getters | |
| Perder paciencia | Recibe cuánta. **Nunca por debajo de 0** |
| ¿Se ha hartado? | `true` cuando la paciencia llega a 0 |
| Su humor de 0 a 100 | El porcentaje de paciencia que le queda. La vista lo usa para elegir la cara del dibujo |

> ⚠️ **Sin `setter` para la paciencia.** Que solo se pueda bajar con el método de perder
> paciencia, que valida el mínimo. Si pones un `setPaciencia(int)` público, cualquier
> despiste en otra clase te la deja en -7 y el juego se rompe de formas raras. Encapsular
> no es "poner getters y setters a todo": es **decidir qué se puede tocar desde fuera y
> cómo**.

**Cómo baja la paciencia:** decídelo tú. Algunas ideas: un punto por cada producto que
preparas, más puntos si el producto tarda en prepararse, o un poco por cada cliente que
espera detrás. Elige una regla, escríbela en un comentario y sé coherente.

### 4.6 El generador de clientes

Va en `cafeteria.logica`.

**Qué representa:** la puerta de la cafetería. Fabrica clientes al azar.

**Qué guarda:** un `Random`, un array de nombres, un array de frases y la carta (necesita
saber qué se puede pedir).

**Qué sabe hacer:**

| Operación | Qué recibe | Qué devuelve |
|---|---|---|
| Generar un cliente | el número de día (para la dificultad) | un cliente completo |
| Generar la cola del día | el número de día | una `Queue` de clientes |

**Cómo debería escalar la dificultad con los días:**
- Día 1: pocos clientes, comandas de una sola línea, mucha paciencia.
- Día 5: más clientes, comandas de dos o tres líneas, menos paciencia, y opciones más
  rebuscadas (leche de avena, dobles, extras).

Piensa la fórmula tú. Algo del estilo "número de clientes = 4 + día" y "paciencia = 100 − día × 5"
ya funciona, pero prueba a jugar y ajústala hasta que te divierta. **Ajustar la dificultad
probando es diseño de videojuegos de verdad**, y es la parte divertida.

**Cómo generar una comanda:** elige un producto al azar de la carta, invéntale unas
opciones al azar coherentes con lo que ese producto acepta y monta la línea.

> 🤔 "Coherentes con lo que ese producto acepta" tiene truco: no tiene sentido pedir un
> croissant *"grande con leche de avena"*. ¿Cómo lo resuelves? Tienes al menos dos caminos:
> mirar la categoría del producto, o preguntarle al producto qué opciones admite (¡con las
> interfaces de la sesión 2!). El segundo es más elegante. Piensa por qué.

### 4.7 Guardar la cola

En algún sitio va a vivir la `Queue<Cliente>` del día. Hoy vale con tenerla en tu programa
de prueba; mañana pasado le daremos su casa definitiva en la clase de jornada.

---

## 5. Decisiones que tomas tú

- Todos los nombres de clases y métodos.
- Qué campos lleva la ficha de opciones (¿añades alguno más? ¿"para llevar"?).
- La regla de pérdida de paciencia.
- La curva de dificultad.
- Los nombres y las frases de tus clientes. **Hazlas graciosas**, que vas a leerlas mucho.

---

## 6. Buenas prácticas de hoy

**Objetos de valor inmutables.** Las opciones no cambian una vez creadas. Los objetos que
no cambian son los que nunca te dan sorpresas.

**Cuidado al guardar una lista que te pasan por parámetro.**

```java
public Pedido(List<Linea> lineas) {
    this.lineas = lineas;              // ⛔ quien te la pasó puede seguir modificándola
    this.lineas = new ArrayList<>(lineas);  // ✅ copia defensiva
}
```

Es el mismo problema de la sesión 2, pero al revés: allí era al devolver, aquí al recibir.
Se llama **copia defensiva** y es una de esas cosas que separan el código de clase del
código profesional.

**Que las clases no impriman.** Un cliente devuelve su nombre; no lo escribe en pantalla.
Lo repito porque es el error que más cuesta quitarse.

**Un `toString()` en cada clase.** Aunque solo sea para depurar. Te ahorra horas.

---

## 7. Errores típicos

| Lo que pasa | Por qué | Cómo se arregla |
|---|---|---|
| `NullPointerException` al sacar de la cola | `poll()` sobre una cola vacía | `if (!cola.isEmpty())` antes |
| El bucle de clientes no acaba nunca | Haces `peek()` pero nunca `poll()` | `peek` mira, `poll` saca |
| Todos los clientes salen iguales | Creas un `Random` nuevo dentro del bucle | **Un solo `Random`**, atributo de la clase |
| `nextInt` peta con `IllegalArgumentException` | Le pasas 0 o un número negativo | El límite tiene que ser ≥ 1 |
| La paciencia se queda negativa | Restas sin comprobar el mínimo | `Math.max(0, paciencia - n)` |
| Modificar un pedido cambia otro sin querer | Los dos apuntan al mismo objeto | Copia defensiva |
| El importe total sale mal | Se te olvida multiplicar por la cantidad | Repasa el importe de la línea |

---

## 8. Cómo comprobar que funciona

En `Prueba04`:

1. Crea la carta y el generador.
2. Genera un cliente del día 1 y píntalo:
   ```java
   Escena.clienteEnBarra(nombre, paciencia, pacienciaMaxima, frase);
   Escena.ticketPedido(nombre, numeroPedido, listaDeTextosDeLasLineas);
   ```
3. Genera la cola del día 1 y recórrela entera con `poll()`, pintando cada cliente. Que
   acabe sola cuando se vacíe.
4. Genera la cola del día 7 y compárala con la del 1: **tienen que notarse más difíciles**.
5. Coge un cliente, quítale paciencia en un bucle y píntalo cada vez. El dibujo tiene que
   ir pasando de cara contenta a cara enfadada, y el bucle parar cuando se harte.
6. Prueba el `equals` de tu ficha de opciones: crea dos idénticas y compáralas con
   `.equals()`. Prueba luego con `==` y **fíjate en que da `false`**. Si entiendes por qué,
   mañana lo tienes ganado.

---

## ✅ Checklist de la sesión 4

- [ ] Tengo una ficha de opciones inmutable, con `equals` y `toString` escritos a mano.
- [ ] Mi clase abstracta sabe crear una copia de sí misma con unas opciones dadas.
- [ ] Tengo línea, pedido y cliente, y la relación entre ellos es de composición.
- [ ] El cliente no tiene `setter` de paciencia y esta nunca baja de 0.
- [ ] El generador usa **un solo** `Random` y produce clientes distintos.
- [ ] La dificultad sube con el número de día.
- [ ] Uso `Queue` para la cola, no `List`.
- [ ] Hago copias defensivas al recibir y al devolver colecciones.
- [ ] La cola se pinta entera con la vista y se vacía sola.

---

## 🎯 Reto opcional

Dale **personalidad** a los clientes. Un `enum` de tipos: el que tiene prisa (mitad de
paciencia pero paga propina), el indeciso (cambia el pedido a mitad), el habitual (siempre
pide lo mismo), el grupo grande (comanda de cuatro líneas).

Piensa dónde encaja mejor: ¿un atributo `enum` en el cliente? ¿Clases hijas de cliente?
¿Una interfaz? **Argumenta tu elección en un comentario.** Aquí no hay respuesta única, y
esa es justo la gracia.

---

**Siguiente:** [Sesión 5 — ¿He acertado?](05-evaluacion.md) 👉
