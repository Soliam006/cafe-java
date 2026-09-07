# Chuleta de buenas prácticas

Para tener abierta mientras programas. Todo lo de aquí sale en las guías, pero junto es más
fácil de repasar antes de entregar algo.

---

## 1. Nombres

| Cosa | Cómo | Ejemplo |
|---|---|---|
| Clase | `PascalCase`, sustantivo singular | `Cliente`, `LineaDePedido` |
| Interfaz de capacidad | `PascalCase`, acaba en `-able`/`-ible` | `Personalizable`, `Calentable` |
| Método | `camelCase`, **empieza por verbo** | `calcularPrecio()`, `anadirExtra()` |
| Método que devuelve `boolean` | Empieza por `es`, `esta`, `tiene`, `hay` | `estaVacio()`, `tieneExtras()` |
| Variable | `camelCase`, descriptiva | `precioTotal`, `clientesAtendidos` |
| Constante | `MAYUSCULAS_CON_GUION` | `PACIENCIA_INICIAL` |
| Enum y sus valores | Clase `PascalCase`, valores `MAYUSCULAS` | `Tamanio.GRANDE` |
| Excepción | Acaba en `Exception`. **Siempre** | `SinGeneroException` |
| Paquete | Todo minúsculas | `cafeteria.modelo` |

**Un buen nombre ahorra un comentario.** Si tienes que explicar qué hace un método, casi
siempre es que el método está mal llamado o hace demasiadas cosas.

**Sin acentos ni eñes en el código.** En los textos que salen por pantalla, todos los que
quieras. En nombres de clases, métodos y variables, ninguno: te ahorra problemas de
codificación entre ordenadores.

---

## 2. Encapsulación

```java
private final String nombre;    // ✅ el estado, siempre privado
public String getNombre() { ... }
```

- **Atributos `private`. Sin excepciones.**
- `final` en todo lo que no cambie después del constructor. Dice mucho y lo protege el
  compilador.
- **Getter no significa que toque poner setter.** Pregúntate siempre: *¿tiene sentido que
  alguien de fuera cambie esto?* Si el saldo de la caja solo debe cambiar ingresando o
  gastando, **no pongas `setSaldo`**.
- **Copia defensiva al recibir:**
  ```java
  this.lineas = new ArrayList<>(lineas);
  ```
- **Copia defensiva (o solo lectura) al devolver:**
  ```java
  return new ArrayList<>(extras);
  // o bien
  return Collections.unmodifiableList(extras);
  ```
- **Que un objeto nunca pueda estar en un estado inválido.** Valida en el constructor y
  lanza `IllegalArgumentException` si te dan datos absurdos.

---

## 3. Herencia, interfaces y composición

**Cómo decidir cuál usar:**

| Si la frase natural es… | Usa |
|---|---|
| "X **es un** Y" | Herencia (`extends`) |
| "X **puede** Y" / "X **sabe** Y" | Interfaz (`implements`) |
| "X **tiene un** Y" | **Composición** (un atributo) |

> **Prefiere composición antes que herencia.** Es la regla que más veces acierta.

- `@Override` **siempre** que sobrescribas. Es gratis y te caza los errores de nombre.
- `super(...)` en la **primera línea** del constructor.
- Interfaces **pequeñas**. Si una clase implementa una interfaz y se ve obligada a dejar
  métodos vacíos, la interfaz estaba mal cortada.
- Declara con el tipo más general que te sirva:
  ```java
  List<Producto> carta = new ArrayList<>();   // ✅
  ```
- Si tu código se llena de `if (x instanceof A) ... else if (x instanceof B)`, casi siempre
  eso debería ser un método sobrescrito.

---

## 4. Modelo, lógica y vista

```
modelo   → las cosas: producto, cliente, pedido        (no imprimen NADA)
lógica   → las reglas: almacén, evaluador, jornada     (deciden qué contar)
vista    → cómo se ve                                  (no sabe nada del modelo)
```

- **Ninguna clase de `modelo` hace `System.out.println`.** Devuelve textos; quien la llama
  decide si los pinta.
- **Ninguna clase de `modelo` pide datos por teclado.**
- La vista recibe `String` y números, nunca tus objetos.
- Las flechas de dependencia van **hacia abajo**: la partida conoce la jornada, la jornada
  conoce la caja, y la caja no conoce a nadie.

**Cómo saber si lo has hecho bien:** si pudieras cambiar la consola por una ventana gráfica
reescribiendo solo el paquete `vista`, está bien.

---

## 5. Excepciones

| | Hereda de | ¿Obliga a `try/catch`? | Para qué |
|---|---|---|---|
| **Checked** | `Exception` | Sí | Problemas previsibles del dominio |
| **Unchecked** | `RuntimeException` | No | Bugs de programación |

```java
catch (Exception e) { }              // ⛔ nunca: te tragas hasta los bugs, y encima vacío
catch (SinGeneroException e) {       // ✅ el tipo concreto
    Consola.error(e.getMessage());   //    y haz algo con ello
}
```

- Captura **donde de verdad puedas hacer algo**, no antes.
- Mensajes concretos: *"Faltan 80 ml de leche de avena (necesarios 200, disponibles 120)"*,
  no *"error"*.
- Mete datos dentro de tus excepciones, no solo un texto.
- `e.printStackTrace()` sirve para depurar, **no** para manejar un error.
- Operaciones **atómicas**: comprueba que se puede hacer todo antes de hacer nada.

---

## 6. Colecciones

| Necesitas | Usa |
|---|---|
| Orden y acceso por posición | `List` (`ArrayList`) |
| Buscar por clave | `Map` (`HashMap`) |
| Sin duplicados, sin orden | `Set` (`HashSet`) |
| Primero en entrar, primero en salir | `Queue` (`ArrayDeque` o `LinkedList`) |

- `mapa.getOrDefault(clave, 0)` en vez de arriesgarte a un `null`.
- Una lista vacía **no** es `null`. Inicialízalas al declararlas.
- No modifiques una colección mientras la recorres con `for-each` → usa `removeIf(...)`.
- Ojo al comparar `Integer` con `==`. Guárdalo en un `int` primero.

---

## 7. `equals` y `hashCode`

**Si sobrescribes uno, sobrescribe el otro. Con los mismos campos.**

```java
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    MiClase otra = (MiClase) obj;
    return campo1 == otra.campo1
        && Objects.equals(campo2, otra.campo2);
}

@Override
public int hashCode() {
    return Objects.hash(campo1, campo2);
}
```

- El parámetro es `Object`, no tu clase.
- `Objects.equals(a, b)` aguanta `null`; `a.equals(b)` no.
- Con **`enum` se usa `==`**. Es la excepción, y es lo correcto.
- `equals` nunca debe lanzar excepciones.

---

## 8. Ordenar

| | `Comparable` | `Comparator` |
|---|---|---|
| Vive | dentro de la clase | fuera, aparte |
| Cuántos | uno | los que quieras |
| Cuándo | hay un orden natural obvio | hay varios criterios |

```java
Integer.compare(a, b)                          // ✅
a - b                                          // ⛔ desbordamiento con números grandes

lista.sort(Comparator.comparing(P::getNombre));
lista.sort(Comparator.comparingDouble(P::getPrecio).reversed());
lista.sort(Comparator.comparing(P::getCategoria).thenComparing(P::getNombre));
```

Pon siempre un **criterio de desempate** si puede haber empates: sin él, el orden cambia
entre ejecuciones.

---

## 9. Métodos y estructura

- **Si un método no cabe en la pantalla, pártelo.**
- Un método hace **una** cosa. Si al describirlo usas "y", pártelo.
- Los métodos privados de apoyo son gratis: úsalos.
- Un `main` largo significa que la lógica está en el sitio equivocado.
- Nada de números mágicos sueltos: constante con nombre.
- Cada regla del juego, **en un solo sitio**. Si el 50 % aparece en tres `if` distintos,
  el día que lo cambies te vas a dejar uno.
- `switch` moderno con `->`: no necesita `break` y no se cuela al caso siguiente.

---

## 10. Antes de dar algo por terminado

- [ ] Compila sin avisos.
- [ ] Ningún atributo público.
- [ ] Ningún `System.out.println` en `modelo`.
- [ ] Todos los `@Override` puestos.
- [ ] Ningún `catch (Exception e)` vacío.
- [ ] Ningún método de más de 30 líneas.
- [ ] Los nombres se entienden sin comentarios.
- [ ] Le he metido datos absurdos por los menús y no se ha caído.
- [ ] Lo he ejecutado entero y hace lo que dice que hace.

---

## 11. Atajos de IntelliJ que te van a ahorrar horas

| Atajo | Qué hace |
|---|---|
| `Alt+Intro` | Arreglar lo que está en rojo. **El más útil con diferencia** |
| `Alt+Insert` | Generar constructor, getters, setters, `equals`/`hashCode`, `toString` |
| `Ctrl+Alt+L` | Formatear el código (indentación, espacios) |
| `Shift+F6` | Renombrar en todo el proyecto sin romper nada |
| `Ctrl+B` | Ir a la definición de lo que tengas bajo el cursor |
| `Ctrl+Alt+M` | Coger el trozo seleccionado y convertirlo en un método |
| `sout` + `Tab` | Escribe `System.out.println()` |
| `Ctrl+Shift+F10` | Ejecutar la clase en la que estás |
| `Ctrl+/` | Comentar o descomentar |
| Clic en el margen | Poner un *breakpoint* y depurar paso a paso |

**Genera los getters y el `toString`, pero escribe a mano el primer `equals` de tu vida.**
Y luego lee lo que te genera IntelliJ: verás el mismo patrón.
