# Sesión 5 — ¿He acertado? Preparar y evaluar

> ⏱️ 2–3 horas · **`equals` y `hashCode` · Igualdad de objetos · Lógica de puntuación**
>
> 📌 Necesitas todas las sesiones anteriores. Esta es la sesión clave del proyecto.

---

## 1. Lo que vas a construir hoy

El corazón del juego: la máquina que decide si has acertado.

```
   ╔══════════════════════════════════════════════════════════════╗
   ║                    CASI, PERO NO DEL TODO                    ║
   ║                            ★★☆                               ║
   ╠══════════════════════════════════════════════════════════════╣
   ║   · Producto correcto: Capuchino                             ║
   ║   · Te ha pedido tamaño GRANDE y has servido MEDIANO          ║
   ║   Marta paga 1,05 €   (50% del importe)                      ║
   ╚══════════════════════════════════════════════════════════════╝
```

Y, antes de eso, la barra donde se prepara: la que gasta el género de verdad.

---

## 2. La idea de hoy: cuándo dos objetos son "el mismo"

Esta pregunta parece tonta y es una de las que más suspenden exámenes.

```java
String a = new String("café");
String b = new String("café");

a == b        // false  😱
a.equals(b)   // true
```

- **`==`** pregunta *"¿son el mismo objeto, la misma posición en memoria?"*
- **`.equals()`** pregunta *"¿valen lo mismo?"*

Con números y `boolean` (los tipos primitivos) `==` funciona como esperas. Con **objetos**,
casi nunca es lo que quieres.

Y aquí está el detalle que te va a morder: **`equals` heredado de `Object` hace exactamente
lo mismo que `==`**. Si no lo sobrescribes, dos fichas de opciones idénticas dan `false`, y
tu juego dirá que has fallado cuando has acertado.

> ⚠️ Con los `enum` **sí** se usa `==`. Es la excepción, porque cada valor de un `enum`
> existe una sola vez en memoria. `tamanio == Tamanio.GRANDE` es correcto, es lo idiomático
> y es más rápido. Además te protege de `null`.

### El contrato de `equals` y `hashCode`

Cuando sobrescribes `equals`, **tienes que sobrescribir `hashCode`**. No es una recomendación:

> Si `a.equals(b)` es `true`, entonces `a.hashCode() == b.hashCode()` **tiene que** ser `true`.

Si te lo saltas, todo parece funcionar… hasta que metes esos objetos en un `HashMap` o un
`HashSet` y empiezan a desaparecer. Ese bug es de los que te quitan una tarde entera.

---

## 3. Conceptos de Java

### 3.1 Escribir un `equals` bien

Este es el patrón. Apréndetelo, porque es siempre igual:

```java
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;                    // 1. ¿es literalmente el mismo?
    if (obj == null || getClass() != obj.getClass()) return false;  // 2. ¿es del mismo tipo?

    Medida otra = (Medida) obj;                      // 3. ahora sí, casting seguro

    return ancho == otra.ancho                       // 4. comparar campo a campo
        && alto == otra.alto
        && Objects.equals(etiqueta, otra.etiqueta);  //    ¡para objetos, Objects.equals!
}

@Override
public int hashCode() {
    return Objects.hash(ancho, alto, etiqueta);      // y ya está, con los MISMOS campos
}
```

Detalles que importan:

- **El parámetro es `Object`**, no tu clase. Si pones `equals(Medida m)` no estás
  sobrescribiendo nada: estás creando un método nuevo que no se llama nunca. Por eso hay
  que poner `@Override`: el compilador te avisa.
- **`Objects.equals(a, b)`** compara dos objetos aguantando que alguno sea `null`. Si usas
  `a.equals(b)` a pelo y `a` es `null`, te comes un `NullPointerException`.
- **`Objects.hash(...)`** te calcula el `hashCode` de golpe. Usa **los mismos campos** que
  en `equals`. Si no, rompes el contrato.

> 💡 IntelliJ genera los dos con `Alt+Insert` → *equals() and hashCode()*. **Escribe el
> primero a mano** para entenderlo y genera los demás. Y luego lee lo que te ha generado:
> vas a ver este mismo patrón.

### 3.2 Comparar y explicar por qué

Un detalle que hace mucho mejor a este juego: no basta con saber si has fallado, quieres
saber **en qué**. Así que en vez de un método que devuelve `boolean`, escribe uno que
devuelva la **lista de diferencias**:

```java
List<String> diferencias = new ArrayList<>();
if (esperado.getAncho() != real.getAncho()) {
    diferencias.add("Te pedían ancho " + esperado.getAncho() + " y has puesto " + real.getAncho());
}
...
if (diferencias.isEmpty()) { /* idénticos */ }
```

Esa lista se la pasas tal cual a la vista y el jugador ve exactamente qué falló. La
diferencia entre un juego frustrante y uno que engancha suele ser eso.

---

## 4. Especificación: qué tienes que construir

### 4.1 El resultado de un servicio

**Qué representa:** cómo ha ido atender a un cliente.

Empieza por un `enum` con las tres calidades posibles, y **mete dentro el porcentaje que se
cobra**: perfecto → 100, aceptable → 50, fallido → 0. Así el porcentaje vive en un solo
sitio; si mañana quieres que el "a medias" pague un 60 %, tocas un número y ya.

Y luego una clase que guarde el resultado completo:

| Dato | Tipo | Para qué |
|---|---|---|
| La calidad | tu enum | 100 / 50 / 0 |
| Importe cobrado | decimal | Lo que entra en caja |
| Comentarios | `List<String>` | Qué estuvo bien y qué mal |
| Coste del género gastado | decimal | Para el resumen del día |

Con métodos para consultarlo, para añadir comentarios y (útil) para saber si fue perfecto.

### 4.2 La barra: donde se prepara ⭐

Va en `cafeteria.logica`.

**Qué representa:** el puesto de trabajo. Es quien de verdad gasta el género.

**Qué guarda:** una referencia al almacén.

**Qué sabe hacer:**

> **Preparar un producto.**
> Recibe: la plantilla de producto elegida y la ficha de opciones.
> Devuelve: el producto ya preparado.
> 💥 Lanza tu excepción de la sesión 3 si no hay ingredientes.

Los pasos, en este orden exacto:

1. Construir el producto con esas opciones (el método abstracto de la sesión 4).
2. Preguntarle qué ingredientes necesita (el método abstracto de la sesión 3).
3. Consumirlos del almacén. **Si no hay, la excepción sale de aquí y el producto no se
   prepara.**
4. Devolver el producto.

**Fíjate en algo importante:** el género se gasta **aquí**, al preparar. No al evaluar. Por
eso pierdes los ingredientes aunque te equivoques de producto: ya los habías gastado. Esa
regla del juego no está escrita en ningún `if`, sale sola del orden en que hemos puesto las
cosas. **Cuando el diseño es bueno, las reglas salen solas.**

### 4.3 El evaluador ⭐⭐

La clase más importante que vas a escribir. Va en `cafeteria.logica`.

**Qué representa:** el juez. Compara lo que te pidieron con lo que serviste.

**Qué sabe hacer:**

> **Evaluar una línea de comanda.**
> Recibe: lo que pidió el cliente (plantilla + opciones) y lo que preparaste tú
> (plantilla + opciones).
> Devuelve: un resultado de los de 4.1.

**El algoritmo:**

```
1. ¿Es la misma plantilla de producto?
      NO  →  FALLIDO. Cobras 0. Comentario: "Te pidió X y le has dado Y".
      SÍ  →  sigue.

2. Compara las opciones campo por campo y ve apuntando las diferencias:
      - ¿mismo tamaño?
      - ¿mismo tipo de leche?
      - ¿mismas unidades?
      - ¿mismo calentado / mismo hielo?
      - ¿mismos extras?     ← ojo con este, mira el aviso de abajo

3. ¿La lista de diferencias está vacía?
      SÍ  →  PERFECTO.   Cobras el 100%.
      NO  →  ACEPTABLE.  Cobras el 50%, y devuelves las diferencias como comentarios.
```

**El aviso de los extras.** ¿"Con canela y azúcar" es lo mismo que "con azúcar y canela"?
Para un cliente, sí. Para una `List`, **no**: el orden cuenta.

Tienes que decidirlo tú, y las dos opciones son defendibles:
- Comparar sin orden: convierte las dos listas a `Set` (`new HashSet<>(lista)`) y compáralos.
- Comparar con orden: déjalo como `List`.

**Elige una y escribe en un comentario por qué.** Yo compararía sin orden, porque modela
mejor la realidad. Pero lo que no vale es no haberlo pensado.

**Y una decisión de diseño más, esta jugosa:** ¿debería el evaluador ser una **interfaz**,
con una implementación normal y otra "modo fácil" que sea más blanda con los fallos? Sería
una forma preciosa de meter niveles de dificultad, y una interfaz perfectamente justificada.
Piénsalo. Si te ves con ganas, hazlo.

**Evaluar un pedido entero:** un segundo método que recorra todas las líneas, evalúe cada
una y devuelva la lista de resultados o un resultado agregado. Tú decides cómo agregar:
¿la media de los porcentajes? ¿la peor de todas? La segunda es más dura y más divertida.

### 4.4 Une las piezas

Ya lo tienes todo para el ciclo completo de atender a un cliente:

```
   cliente ──> su pedido ──> por cada línea:
                                 elegir producto de la carta
                                 elegir opciones
                                 preparar en la barra   ← gasta género (puede fallar)
                                 evaluar contra lo pedido
                                 cobrar el porcentaje
```

Hoy escríbelo en tu programa de prueba, con las elecciones puestas a mano. Mañana lo
convertimos en el juego de verdad, con menús.

---

## 5. Decisiones que tomas tú

- Los nombres de todo, como siempre.
- Los porcentajes exactos (100/50/0 es la propuesta, pero es tu juego).
- Si los extras se comparan con orden o sin él.
- Cómo se agrega el resultado de un pedido de varias líneas.
- Si haces el evaluador interfaz o clase.
- La redacción de los comentarios. **Escríbelos como los diría un cliente**, no como un log
  de sistema. "Te he pedido un café pequeño, esto es un vaso enorme" es mucho mejor que
  "Discrepancia en el campo tamaño".

---

## 6. Buenas prácticas de hoy

**`equals` y `hashCode` siempre juntos.** Si sobrescribes uno, el otro también. Sin
excepciones.

**`equals` no debe petar nunca.** Ni con `null`, ni con un objeto de otro tipo. Debe
devolver `false` y ya. Por eso el patrón empieza con esas dos comprobaciones.

**El evaluador no debe tener estado.** Recibe dos cosas, devuelve un resultado. No guarda
contadores ni recuerda partidas anteriores. Los objetos sin estado son fáciles de probar y
no fallan nunca por sorpresa.

**Que las reglas del juego estén en un solo sitio.** El porcentaje que se cobra en cada
caso vive en el `enum`, y solo ahí. Si mañana lo quieres cambiar, cambias un número. Si lo
tienes repartido en cuatro `if`, cambias tres y te dejas uno, y te pasas una tarde
buscándolo.

**Números mágicos, fuera.** `0.5` suelto en medio del código no dice nada. Como valor del
`enum` con nombre, se explica solo.

---

## 7. Errores típicos

| Lo que pasa | Por qué | Cómo se arregla |
|---|---|---|
| Todo sale mal aunque acierte | No sobrescribiste `equals` | Escríbelo, y con `@Override` |
| Objetos que desaparecen de un `HashMap`/`HashSet` | Tienes `equals` pero no `hashCode` | Siempre los dos |
| Mi `equals` no se llama nunca | Pusiste el parámetro con tu tipo en vez de `Object` | `equals(Object obj)` |
| `ClassCastException` dentro de `equals` | Casteaste sin comprobar el tipo antes | Sigue el patrón de arriba, paso 2 |
| `NullPointerException` en `equals` | Comparaste un campo que era `null` | `Objects.equals(a, b)` |
| Dos extras iguales dan distinto | Estás comparando `List` y el orden difiere | Decide: `Set` o `List`, y sé coherente |
| Se gasta el género aunque falle la preparación | Consumes antes de comprobar | Repasa la atomicidad de la sesión 3 |
| Los `enum` no coinciden | Los comparaste con `.equals()` cuando uno era `null` | Con enums, `==` |

---

## 8. Cómo comprobar que funciona

En `Prueba05` — este es el programa de prueba más importante del proyecto, hazlo con calma:

1. **El caso perfecto.** Prepara exactamente lo que te piden. Tiene que salir 100 %.
2. **El caso a medias.** Mismo producto, tamaño distinto. 50 % y un comentario que
   mencione el tamaño.
3. **El caso fallido.** Producto completamente distinto. 0 %.
4. **El caso de los extras desordenados.** Pide "canela + azúcar", sirve "azúcar + canela".
   Comprueba que hace lo que tú has decidido que haga.
5. **El caso sin género.** Vacía el almacén, intenta preparar, captura la excepción y
   comprueba que el cliente se queda sin su producto y no se cobra nada.
6. **El ciclo completo.** Coge un cliente generado al azar, atiende su pedido entero y
   pinta todo el proceso.

Píntalo con:

```java
Escena.preparando("Capuchino", 900);
Escena.resultadoServicio(nombreCliente, porcentaje, importeCobrado, listaDeComentarios);
```

---

## ✅ Checklist de la sesión 5

- [ ] Mi ficha de opciones tiene `equals` **y** `hashCode`, con los mismos campos.
- [ ] `equals` recibe `Object` y lleva `@Override`.
- [ ] `equals` aguanta `null` y objetos de otro tipo sin petar.
- [ ] Tengo un `enum` de calidad con el porcentaje dentro.
- [ ] La barra prepara y consume género, y lanza la excepción cuando no hay.
- [ ] El género se gasta aunque el pedido salga mal.
- [ ] El evaluador distingue bien los tres casos.
- [ ] Los comentarios explican **qué** falló, no solo que falló.
- [ ] He probado los seis casos de arriba.
- [ ] He decidido lo del orden de los extras y lo he escrito en un comentario.

---

## 🎯 Reto opcional

**Fallos parciales dentro del 50 %.** Ahora mismo fallar el tamaño y fallar tres cosas a la
vez pagan igual. Haz que el porcentaje baje según cuántas diferencias haya: una diferencia
→ 70 %, dos → 50 %, tres o más → 30 %.

Piensa dónde poner esa regla para no llenar el evaluador de `if`. Pista: puede ser un
método del `enum`, o un método pequeño que reciba el número de diferencias y devuelva el
porcentaje. Lo importante es que **siga estando en un solo sitio**.

---

**Siguiente:** [Sesión 6 — ¡A jugar!](06-la-jornada.md) 👉
