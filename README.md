# ☕ Café Java

Un proyecto de programación orientada a objetos, en Java, con forma de videojuego de gestión.

Llevas una cafetería. Los clientes entran, cantan su pedido y se te amontonan las comandas.
Tú preparas lo que te piden. Si aciertas, cobras. Si te equivocas, pierdes el género.
Cuando cierras la persiana, ves el resumen del día y decides si mañana vuelves a abrir.

---

## Cómo está organizado esto

| Carpeta | Qué hay | Quién lo escribe |
|---|---|---|
| `src/cafeteria/vista/` | Colores, marcos, tablas, arte ASCII, animaciones | **Ya está hecho** — no hace falta que lo toques |
| `src/cafeteria/modelo/` | Productos, clientes, pedidos… las "cosas" del juego | **Tú** |
| `src/cafeteria/logica/` | Inventario, evaluación, jornada, partida | **Tú** |
| `docs/` | Las guías de cada sesión | Léelas |

La idea es esta: **lo aburrido de pintar por pantalla ya está resuelto**. Tú te dedicas a lo
que de verdad se aprende — diseñar clases, heredar, usar interfaces, manejar colecciones y
excepciones — y el juego se ve bonito desde el primer día.

---

## El plan de trabajo

Ocho sesiones. Una por día, o al ritmo que te apetezca. Cada una deja algo que ya funciona.

| # | Sesión | Lo que practicas | Guía |
|---|---|---|---|
| 0 | Antes de empezar | Estructura, paquetes, ejecutar el proyecto | [00-antes-de-empezar.md](docs/00-antes-de-empezar.md) |
| 1 | La carta: productos | Clases, `enum`, **herencia**, clases **abstractas** | [01-productos.md](docs/01-productos.md) |
| 2 | Personalizar y servir | **Interfaces**, **polimorfismo**, `List` | [02-interfaces.md](docs/02-interfaces.md) |
| 3 | El almacén | `Map`, **excepciones propias**, `try/catch` | [03-inventario.md](docs/03-inventario.md) |
| 4 | Los clientes | Composición, `Queue`, `Random`, encapsulación | [04-clientes-y-pedidos.md](docs/04-clientes-y-pedidos.md) |
| 5 | ¿He acertado? | `equals`, comparación de objetos, lógica de puntuación | [05-evaluacion.md](docs/05-evaluacion.md) |
| 6 | ¡A jugar! | Bucle principal, estados, menús — **el juego ya se juega** | [06-la-jornada.md](docs/06-la-jornada.md) |
| 7 | Estadísticas y pulido | `Comparable`, `Comparator`, ficheros, ampliaciones | [07-estadisticas.md](docs/07-estadisticas.md) |

Documentos de consulta, para tener abiertos mientras trabajas:

- **[NOMBRES.md](docs/NOMBRES.md)** — tu libreta. Los nombres los eliges tú; apúntalos ahí según los decidas.
- **[API-VISTA.md](docs/API-VISTA.md)** — todo lo que la vista sabe dibujar y cómo se llama.
- **[BUENAS-PRACTICAS.md](docs/BUENAS-PRACTICAS.md)** — chuleta de estilo y de errores típicos.

---

## Descargar y abrir

**Requisitos:** IntelliJ IDEA (la Community Edition vale) y un **JDK 17 o superior**.

Con Git:

```bash
git clone https://github.com/Soliam006/cafe-java.git
```

O sin Git: botón verde **Code → Download ZIP**, y descomprimir donde quieras.

Luego, en IntelliJ: *File → Open* y elige la carpeta del proyecto. Si te avisa de que no
encuentra el SDK, ve a *File → Project Structure → Project* y selecciona tu JDK.

> ⚠️ Si te lo has bajado como ZIP, sácalo de la carpeta de Descargas antes de trabajar. Y
> si vas a usar Git, **haz commits al acabar cada sesión**: es buena costumbre y te salva
> el día que rompas algo.

---

## Arrancar

Abre el proyecto en IntelliJ y ejecuta esta clase:

```
src/cafeteria/vista/DemoVista.java
```

Si ves el logo de **CAFE JAVA** y un montón de cajas de colores, está todo listo.
Si en vez de eso ves basura tipo `[32m`, ve a [00-antes-de-empezar.md](docs/00-antes-de-empezar.md),
sección *"La consola no me pinta bien"*.

---

## Una regla, y solo una

**Las guías no traen la solución escrita.** Traen la *especificación*: qué tiene que
representar cada clase, qué debe saber hacer y por qué. El código lo escribes tú, y los
nombres los eliges tú.

Eso es exactamente lo que te van a pedir en un enunciado de prácticas de la carrera, y es
la única forma de que lo que aprendas se quede.
